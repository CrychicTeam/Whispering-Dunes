package org.crychicteam.dunes.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
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
                    String basePath = "block/cactus/" + ctx.getName();
                    ModelFile plantModel = pvd.models().getExistingFile(pvd.modLoc(basePath)); //  + "_plant"
                    ModelFile baseModel = pvd.models().getExistingFile(pvd.modLoc(basePath));
                    ModelFile fruitModel = pvd.models().getExistingFile(pvd.modLoc(basePath + "_fruit"));
                    pvd.getVariantBuilder(ctx.getEntry())
                            .forAllStates(state -> {
                                AbstractDunesCactus.FruitState states = state.getValue(AbstractDunesCactus.FRUIT_STATE);
                                return ConfiguredModel.builder()
                                        .modelFile(switch (states) {
                                            case PLANTS -> plantModel;
                                            case DONE -> baseModel;
                                            case FRUITS -> fruitModel;
                                        })
                                        .build();
                            });
                })
                .item()
                .model((ctx, pvd) -> pvd.withExistingParent("item/cactus/" + ctx.getName(), pvd.modLoc("block/cactus/" + ctx.getName())))
                .tab(CreativeModeTabs.NATURAL_BLOCKS)
                .build()
                .register();

        GIANT_PILLAR_CACTUS = REGISTRATE
                .block("giant_cactus", GiantCactus::new)
                .initialProperties(() -> Blocks.CACTUS)
                .properties(BlockBehaviour.Properties::dynamicShape)
                .properties(BlockBehaviour.Properties::noOcclusion)
                .blockstate((ctx, pvd) -> {
                    String basePath = "block/cactus/" + ctx.getName();
                    ModelFile baseModel = pvd.models().getExistingFile(pvd.modLoc(basePath));
                    ModelFile fruitModel = pvd.models().getExistingFile(pvd.modLoc(basePath + "_fruit"));
                    pvd.getVariantBuilder(ctx.getEntry())
                            .forAllStates(state -> {
                                GiantCactus.PillarState pillarState = state.getValue(GiantCactus.PILLAR_STATE);
                                boolean hasFruit = state.getValue(GiantCactus.FRUIT_STATE) == AbstractDunesCactus.FruitState.FRUITS;
                                return ConfiguredModel.builder()
                                        .modelFile(switch (pillarState) {
                                            case BODY, HEAD -> baseModel;
                                            case DONE -> hasFruit ? fruitModel : baseModel;
                                        })
                                        .build();
                            });
                })
                .item()
                .model((ctx, pvd) -> pvd.withExistingParent("item/cactus/" + ctx.getName(), pvd.modLoc("block/cactus/" + ctx.getName())))
                .tab(CreativeModeTabs.NATURAL_BLOCKS)
                .build()
                .register();
    }

    public static void register() {}
}
