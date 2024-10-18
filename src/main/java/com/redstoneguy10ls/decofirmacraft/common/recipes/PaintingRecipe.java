package com.redstoneguy10ls.decofirmacraft.common.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Either;
import com.redstoneguy10ls.decofirmacraft.common.items.DFCItems;
import com.redstoneguy10ls.decofirmacraft.util.DFCHelpers;
import net.dries007.tfc.common.recipes.RecipeSerializerImpl;
import net.dries007.tfc.common.recipes.SimpleBlockRecipe;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.JsonHelpers;
import net.dries007.tfc.util.collections.IndirectHashCollection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.Nullable;

public class PaintingRecipe extends SimpleBlockRecipe {
    public static Either<BlockState, InteractionResult> computeResult(Player player, BlockState state, BlockHitResult hit, boolean informWhy) {
        final ItemStack held = player.getMainHandItem();

        if (Helpers.isItem(held, DFCItems.PAINTBRUSH.get()) && DFCHelpers.isHoldingFluidContainerInOffhand(player)) {
            final BlockPos pos = hit.getBlockPos();
            FluidStack fluidInHand = DFCHelpers.getFluidFromItem(player.getOffhandItem());
            final PaintingRecipe recipe = PaintingRecipe.getRecipe(state, held, fluidInHand);

            if (recipe == null) {
                if (informWhy) complain(player, "no_recipe");
                return Either.right(InteractionResult.PASS);
            } else {
                @Nullable BlockState painted;

                if (recipe.copyInputState) {
                    // Copy the blockstate from the input state
                    painted = recipe.copyBlockState(state);
                } else {
                    // Use the predefined output block state
                    painted = recipe.getBlockCraftingResult(state);
                }

                if (painted == null) {
                    if (informWhy) complain(player, "cannot_place");
                    return Either.right(InteractionResult.FAIL);
                }

                // Pass the fluid ingredient from the recipe for proper validation
                if (consumeFluidIfPresent(player, recipe.getFluidAmount(), recipe.getFluidIngredient())) {
                    return Either.left(painted);
                } else {
                    if (informWhy) complain(player, "not_enough_fluid");
                    return Either.right(InteractionResult.FAIL);
                }
            }
        }
        return Either.right(InteractionResult.PASS);
    }

    @Nullable
    public BlockState copyBlockState(BlockState inputState) {
        BlockState newState = outputState;

        // Iterate through all the properties of the input block state
        for (Property<?> property : inputState.getProperties()) {
            // Use capture helper to get the correct value type for the property
            newState = copyProperty(inputState, newState, property);
        }

        return newState;
    }

    @SuppressWarnings("unchecked")
    private <T extends Comparable<T>> BlockState copyProperty(BlockState inputState, BlockState newState, Property<T> property) {
        T value = inputState.getValue(property);
        return newState.setValue(property, value);
    }

    private static void complain(Player player, String message)
    {
        player.displayClientMessage(Component.translatable("dfc.painting." + message), true);
    }

    public static boolean consumeFluidIfPresent(Player player, int amount, @Nullable FluidStack fluidIngredient) {
        ItemStack offHandStack = player.getOffhandItem();

        // Check if the off-hand item contains a fluid handler (bucket or other fluid container)
        LazyOptional<IFluidHandlerItem> fluidHandlerCap = offHandStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);

        if (fluidHandlerCap.isPresent()) {
            IFluidHandlerItem fluidHandler = fluidHandlerCap.orElseThrow(IllegalStateException::new);
            FluidStack fluidInItem = fluidHandler.getFluidInTank(0);

            // Verify that the fluid in the container matches the required fluid ingredient
            if (fluidIngredient != null && !fluidIngredient.isEmpty()) {
                if (fluidInItem.getFluid() == fluidIngredient.getFluid() && fluidInItem.getAmount() >= amount) {
                    fluidHandler.drain(amount, IFluidHandler.FluidAction.EXECUTE);
                    return true;
                }
            }
        }
        return false;
    }

    public static final IndirectHashCollection<Block, PaintingRecipe> CACHE = IndirectHashCollection.createForRecipe(recipe -> recipe.getBlockIngredient().blocks(), DFCRecipeTypes.PAINTING);

    @Nullable
    public static PaintingRecipe getRecipe(BlockState state, ItemStack held, @Nullable FluidStack fluidInHand) {
        PaintingRecipe bestMatch = null;

        // Iterate through all recipes for the block tag
        for (PaintingRecipe recipe : CACHE.getAll(state.getBlock())) {
            // Check if the block state matches, and then test the item and fluid ingredients
            if (recipe.matches(state, held, fluidInHand)) {
                if (bestMatch == null || isMoreSpecificMatch(recipe, bestMatch)) {
                    bestMatch = recipe;  // Keep track of the best (most specific) match
                }
            }
        }

        return bestMatch;
    }

    @Nullable
    private final Ingredient itemIngredient;
    @Nullable
    private final FluidStack fluidIngredient;
    private final int fluidAmount;
    private final boolean copyInputState;

    public PaintingRecipe(ResourceLocation id, BlockIngredient ingredient, BlockState outputState, boolean copyInputState, @Nullable Ingredient itemIngredient, @Nullable FluidStack fluidIngredient, int fluidAmount)
    {
        super(id, ingredient, outputState, false);
        this.itemIngredient = itemIngredient;
        this.fluidIngredient = fluidIngredient;
        this.fluidAmount = fluidAmount;
        this.copyInputState = copyInputState;
    }
    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return DFCRecipeSerializers.PAINTING.get();
    }

    @Override
    public RecipeType<?> getType()
    {
        return DFCRecipeTypes.PAINTING.get();
    }

    public boolean matches(BlockState state, ItemStack stack, @Nullable FluidStack fluidInHand) {
        // First, check if the block state matches
        if (!matches(state)) {
            return false;
        }

        // Check if the item ingredient matches
        if (itemIngredient != null && !itemIngredient.test(stack)) {
            return false;
        }

        // Check if the fluid ingredient matches (if a fluid ingredient is defined in the recipe)
        if (fluidIngredient != null && !fluidIngredient.isEmpty()) {
            if (fluidInHand == null || fluidInHand.isEmpty()) {
                return false; // No fluid in hand, but recipe requires fluid
            }

            // Check if the fluid in hand matches the fluid ingredient in the recipe
            if (!fluidInHand.isFluidEqual(fluidIngredient) || fluidInHand.getAmount() < fluidAmount) {
                return false;
            }
        }

        return true;  // All conditions are met
    }

    private static boolean isMoreSpecificMatch(PaintingRecipe candidate, PaintingRecipe currentBest) {
        int candidateSpecificity = 0;
        int currentSpecificity = 0;

        if (candidate.getItemIngredient() != null) {
            candidateSpecificity++;
        }
        if (candidate.getFluidIngredient() != null && !candidate.getFluidIngredient().isEmpty()) {
            candidateSpecificity++;
        }

        if (currentBest.getItemIngredient() != null) {
            currentSpecificity++;
        }
        if (currentBest.getFluidIngredient() != null && !currentBest.getFluidIngredient().isEmpty()) {
            currentSpecificity++;
        }

        return candidateSpecificity > currentSpecificity;
    }

    @Nullable
    public Ingredient getItemIngredient()
    {
        return itemIngredient;
    }

    @Nullable
    public FluidStack getFluidIngredient() {
        return fluidIngredient;
    }

    public int getFluidAmount() {
        return this.fluidAmount;
    }

    public static class Serializer extends RecipeSerializerImpl<PaintingRecipe>
    {
        @Override
        public PaintingRecipe fromJson(ResourceLocation recipeId, JsonObject json)
        {
            if (!json.has("ingredient"))
            {
                throw new JsonSyntaxException("Missing 'ingredient' key in JSON");
            }
            BlockIngredient ingredient = BlockIngredient.fromJson(JsonHelpers.get(json, "ingredient"));
            boolean copyInputState = GsonHelper.getAsBoolean(json, "copy_input", false);
            if (!json.has("result"))
            {
                throw new JsonSyntaxException("Missing 'result' key in JSON");
            }
            BlockState state = JsonHelpers.getBlockState(GsonHelper.getAsString(json, "result"));
            Ingredient itemIngredient = null;
            if (json.has("item_ingredient"))
            {
                try
                {
                    itemIngredient = Ingredient.fromJson(json.get("item_ingredient"));
                }
                catch (JsonSyntaxException e)
                {
                    throw new JsonSyntaxException("Invalid 'item_ingredient' in JSON", e);
                }
            }
            FluidStack fluidIngredient = FluidStack.EMPTY;
            if (json.has("fluid_ingredient"))
            {
                try
                {
                    fluidIngredient = DFCHelpers.getFluidStack(json.getAsJsonObject("fluid_ingredient"));
                }
                catch (JsonSyntaxException e)
                {
                    throw new JsonSyntaxException("Invalid 'fluid_ingredient' in JSON", e);
                }
            }
            int fluidAmount = 0;
            if (json.has("fluid_amount"))
            {
                fluidAmount = GsonHelper.getAsInt(json, "fluid_amount");
                if (fluidAmount < 0)
                {
                    throw new JsonSyntaxException("'fluid_amount' cannot be negative");
                }
            }
            return new PaintingRecipe(recipeId, ingredient, state, copyInputState, itemIngredient, fluidIngredient, fluidAmount);
        }

        @Nullable
        @Override
        public PaintingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
        {
            BlockIngredient ingredient = BlockIngredient.fromNetwork(buffer);
            boolean copyInputState = buffer.readBoolean();
            BlockState state = BuiltInRegistries.BLOCK.byId(buffer.readVarInt()).defaultBlockState();
            Ingredient itemIngredient = Helpers.decodeNullable(buffer, Ingredient::fromNetwork);
            FluidStack fluidIngredient = FluidStack.readFromPacket(buffer);
            int fluidAmount = buffer.readInt();
            return new PaintingRecipe(recipeId, ingredient, state, copyInputState, itemIngredient, fluidIngredient, fluidAmount);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, PaintingRecipe recipe)
        {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeBoolean(recipe.copyInputState);
            buffer.writeVarInt(BuiltInRegistries.BLOCK.getId(recipe.outputState.getBlock()));
            Helpers.encodeNullable(recipe.itemIngredient, buffer, Ingredient::toNetwork);
            recipe.fluidIngredient.writeToPacket(buffer);
            buffer.writeInt(recipe.fluidAmount);
        }
    }
}
