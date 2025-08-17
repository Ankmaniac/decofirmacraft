package com.ankmaniac.decofirmacraft.common.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.dries007.tfc.common.recipes.RecipeSerializerImpl;
import net.dries007.tfc.common.recipes.SimpleBlockRecipe;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.util.JsonHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class AsbestosRecipe extends SimpleBlockRecipe {

    private final Item itemInput;

    public AsbestosRecipe(ResourceLocation id, BlockIngredient blockInput, BlockState blockOutput, boolean copyInputState, Item itemInput) {
        super(id, blockInput, blockOutput, copyInputState);
        this.itemInput = itemInput;
    }

    public InteractionResult onRightClick(Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockState clickedState = level.getBlockState(pos);
        ItemStack heldItem = player.getItemInHand(hand);

        if (!matches(clickedState) || heldItem.isEmpty() || heldItem.getItem() != itemInput) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide) {
            heldItem.shrink(1);

            BlockState resultState = getBlockCraftingResult(clickedState);
            level.setBlock(pos, resultState, 3);

            level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);

            if (level instanceof ServerLevel serverLevel) {
                for (int i = 0; i < 20; i++) {
                    double offsetX = pos.getX() + level.random.nextDouble();
                    double offsetY = pos.getY() + level.random.nextDouble();
                    double offsetZ = pos.getZ() + level.random.nextDouble();
                    serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, offsetX, offsetY, offsetZ, 1, 0, 0, 0, 0.1);
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return DFCRecipeSerializers.ASBESTOS.get();
    }

    @Override
    public RecipeType<?> getType() {
        return DFCRecipeTypes.ASBESTOS.get();
    }

    public static class Serializer extends RecipeSerializerImpl<AsbestosRecipe> {

        @Override
        public AsbestosRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            // Parse block input
            if (!json.has("block_input")) {
                throw new JsonSyntaxException("Missing property 'block_input'");
            }
            BlockIngredient blockInput = BlockIngredient.fromJson(json.get("block_input"));
            boolean copyInputState = GsonHelper.getAsBoolean(json, "copy_input", false);

            // Parse block output
            BlockState blockOutput;
            if (!copyInputState) {
                if (!json.has("block_output")) {
                    throw new JsonSyntaxException("Missing property 'block_output'");
                }
                blockOutput = JsonHelpers.getBlockState(GsonHelper.getAsString(json, "block_output"));
            } else {
                blockOutput = Blocks.AIR.defaultBlockState(); // Placeholder, as the input state will be copied
            }

            // Parse item input
            if (!json.has("item_input")) {
                throw new JsonSyntaxException("Missing property 'item_input'");
            }
            String itemId = GsonHelper.getAsString(json, "item_input");
            Item itemInput = BuiltInRegistries.ITEM.get(new ResourceLocation(itemId));
            if (itemInput == null) {
                throw new IllegalArgumentException("Invalid item ID: " + itemId);
            }

            return new AsbestosRecipe(recipeId, blockInput, blockOutput, copyInputState, itemInput);
        }

        @Override
        public @Nullable AsbestosRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            BlockIngredient blockInput = BlockIngredient.fromNetwork(buffer);
            boolean copyInputState = buffer.readBoolean();
            BlockState blockOutput = copyInputState ? Blocks.AIR.defaultBlockState() : BuiltInRegistries.BLOCK.byId(buffer.readVarInt()).defaultBlockState();
            Item itemInput = BuiltInRegistries.ITEM.byId(buffer.readVarInt());
            return new AsbestosRecipe(recipeId, blockInput, blockOutput, copyInputState, itemInput);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, AsbestosRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeBoolean(recipe.copyInputState);
            if (!recipe.copyInputState) {
                buffer.writeVarInt(BuiltInRegistries.BLOCK.getId(recipe.outputState.getBlock()));
            }
            buffer.writeVarInt(BuiltInRegistries.ITEM.getId(recipe.itemInput));
        }
    }
}