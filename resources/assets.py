import itertools

from mcresources import ResourceManager, ItemContext, BlockContext, block_states
from mcresources import utils, loot_tables
from mcresources.type_definitions import JsonObject, Json

from constants import *


def generate(rm: ResourceManager):

    for rock, data in TFC_ROCKS.items():
        for type in ('road', 'tile'):
            block = rm.blockstate(('rock',type, rock), 'dfc:block/rock/%s/%s' % (type, rock))
            block.with_block_model({
                'all': 'dfc:block/rock/%s/%s' % (type, rock),
                'particle': 'tfc:block/rock/raw/%s' % rock
            }
            )
            block.with_item_model().with_lang(lang('%s %s',rock, type)).with_block_loot('dfc:rock/%s/%s' % (type, rock)).with_tag('minecraft:mineable/pickaxe')
            block.make_slab()
            block.make_wall()
            block.make_stairs()
            rm.block_loot('dfc:rock/%s/%s_stairs' % (type,rock), 'dfc:rock/%s/%s_stairs' % (type,rock))
            rm.block_loot('dfc:rock/%s/%s_wall' % (type,rock), 'dfc:rock/%s/%s_wall' % (type,rock))
            slab_loot(rm, 'dfc:rock/%s/%s_slab' % (type,rock))

            for varients in ('slab', 'stairs', 'wall'):
                rm.lang('block.dfc.rock.'+type+'.'+rock+'_'+varients, lang('%s %s %s' % (rock, type, varients)))

        block = rm.blockstate(('rock','pillar',rock), variants={
            'axis=y': {'model': 'dfc:block/rock/pillar/%s' % rock},
            'axis=z': {'model': 'dfc:block/rock/pillar/%s' % rock, 'x': 90},
            'axis=x': {'model': 'dfc:block/rock/pillar/%s' % rock, 'x': 90, 'y': 90}
        }, use_default_model=False)

        rm.item_model(('rock', 'pillar', rock), parent='dfc:block/rock/pillar/%s' % rock)

        block.with_block_loot('dfc:rock/pillar/%s' % rock)

        end = 'dfc:block/rock/pillar/%stop' % rock
        side = 'dfc:block/rock/pillar/%s' % rock
        block.with_block_model({'end': end, 'side': side}, parent='block/cube_column')
        block.with_lang(lang('%s Pillar', rock))


    for rock, data in DFC_ROCKS.items():
        # Aqueducts
        block = rm.blockstate_multipart(('rock', 'aqueduct', rock), *[
            {'model': 'dfc:block/rock/aqueduct/%s/base' % rock},
            ({'north': 'false'}, {'model': 'dfc:block/rock/aqueduct/%s/north' % rock}),
            ({'east': 'false'}, {'model': 'dfc:block/rock/aqueduct/%s/east' % rock}),
            ({'south': 'false'}, {'model': 'dfc:block/rock/aqueduct/%s/south' % rock}),
            ({'west': 'false'}, {'model': 'dfc:block/rock/aqueduct/%s/west' % rock}),
        ])

        block.with_lang(lang('%s aqueduct', rock))
        block.with_block_loot('dfc:rock/aqueduct/%s' % rock)

        rm.item_model(('rock', 'aqueduct', rock), parent='dfc:block/rock/aqueduct/%s/base' % rock, no_textures=True)

        textures = {'texture': 'dfc:block/rock/bricks/%s' % rock, 'particle': 'dfc:block/rock/bricks/%s' % rock}
        rm.block_model('rock/aqueduct/%s/base' % rock, textures, parent='tfc:block/aqueduct/base')
        rm.block_model('rock/aqueduct/%s/north' % rock, textures, parent='tfc:block/aqueduct/north')
        rm.block_model('rock/aqueduct/%s/east' % rock, textures, parent='tfc:block/aqueduct/east')
        rm.block_model('rock/aqueduct/%s/south' % rock, textures, parent='tfc:block/aqueduct/south')
        rm.block_model('rock/aqueduct/%s/west' % rock, textures, parent='tfc:block/aqueduct/west')

        # Spikes
        block = rm.blockstate(('rock', 'spike', rock), variants=dict(('part=%s' % part, {'model': 'dfc:block/rock/spike/%s_%s' % (rock, part)}) for part in ROCK_SPIKE_PARTS))
        block.with_lang(lang('%s spike', rock))
        block.with_block_loot('1-2 dfc:rock/loose/%s' % rock)

        # Individual models
        rm.item_model(('rock', 'spike', rock), 'dfc:block/rock/raw/%s' % rock, parent='dfc:block/rock/spike/%s_base' % rock)
        for part in ROCK_SPIKE_PARTS:
            rm.block_model(('rock', 'spike', '%s_%s' % (rock, part)), {
                'texture': 'dfc:block/rock/raw/%s' % rock,
                'particle': 'dfc:block/rock/raw/%s' % rock
            }, parent='tfc:block/rock/spike_%s' % part)



def slab_loot(rm: ResourceManager, loot: str):
    return rm.block_loot(loot, {
        'name': loot,
        'functions': [{
            'function': 'minecraft:set_count',
            'conditions': [loot_tables.block_state_property(loot + '[type=double]')],
            'count': 2,
            'add': False
        }]
    })

def lang(key: str, *args) -> str:
    return ((key % args) if len(args) > 0 else key).replace('_', ' ').replace('/', ' ').title()