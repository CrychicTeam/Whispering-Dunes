package org.crychicteam.dunes.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import org.crychicteam.dunes.content.block.cactus.AbstractDunesCactus;
import org.crychicteam.dunes.content.block.cactus.DunesDwarfCactus;
import org.crychicteam.dunes.content.block.cactus.GiantCactus;

import static org.crychicteam.dunes.Dunes.REGISTRATE;

public class DunesBlock {
    public static final BlockEntry<DunesDwarfCactus> DWARF_CACTUS;
    public static final BlockEntry<GiantCactus> GIANT_PILLAR_CACTUS;


    static {
        DWARF_CACTUS = REGISTRATE
                .block("dwarf_cactus", DunesDwarfCactus::new)
                .initialProperties(() -> Blocks.CACTUS)
                .properties(BlockBehaviour.Properties::dynamicShape)
                .properties(BlockBehaviour.Properties::noOcclusion)
                .blockstate((ctx, pvd) -> {
                    ModelFile baseModel = pvd.models().getExistingFile(pvd.modLoc("block/dwarf_cactus"));
                    ModelFile fruitModel = pvd.models().getExistingFile(pvd.modLoc("block/dwarf_cactus_fruit"));
                    pvd.getVariantBuilder(ctx.getEntry())
                            .forAllStates(state -> {
                                boolean hasFruit = state.getValue(DunesDwarfCactus.FRUIT_STATE) == AbstractDunesCactus.FruitState.FRUITS;
                                return ConfiguredModel.builder()
                                        .modelFile(hasFruit ? fruitModel : baseModel)
                                        .build();
                            });
                })
                .simpleItem()
                .register();

        GIANT_PILLAR_CACTUS = REGISTRATE
                .block("giant_cactus", GiantCactus::new)
                .initialProperties(() -> Blocks.CACTUS)
                .properties(BlockBehaviour.Properties::dynamicShape)
                .properties(BlockBehaviour.Properties::noOcclusion)
                .blockstate((ctx, pvd) -> {
                    ModelFile baseModel = pvd.models().getExistingFile(pvd.modLoc("block/giant_cactus"));
                    ModelFile fruitModel = pvd.models().getExistingFile(pvd.modLoc("block/giant_cactus_fruit"));
                    pvd.getVariantBuilder(ctx.getEntry())
                            .forAllStates(state -> {
                                GiantCactus.PillarState pillarState = state.getValue(GiantCactus.PILLAR_STATE);
                                boolean hasFruit = state.getValue(GiantCactus.FRUIT_STATE) == AbstractDunesCactus.FruitState.FRUITS;
                                ModelFile model = switch (pillarState) {
                                    case BODY, HEAD -> baseModel;
                                    case DONE -> hasFruit ? fruitModel : baseModel;
                                };
                                return ConfiguredModel.builder()
                                        .modelFile(model)
                                        .build();
                            });
                })
                .simpleItem()
                .register();
    }

    public static void register() {}
}
