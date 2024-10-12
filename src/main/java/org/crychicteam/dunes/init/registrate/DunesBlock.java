package org.crychicteam.dunes.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.fml.common.Mod;
import org.crychicteam.dunes.Dunes;
import org.crychicteam.dunes.content.block.cactus.AbstractDunesCactus;
import org.crychicteam.dunes.content.block.cactus.DwarfCactus;
import org.crychicteam.dunes.content.block.cactus.GiantCactus;

import static org.crychicteam.dunes.Dunes.REGISTRATE;

@Mod.EventBusSubscriber(modid = Dunes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DunesBlock {
    public static final BlockEntry<DwarfCactus> DWARF_CACTUS;
    public static final BlockEntry<GiantCactus> GIANT_PILLAR_CACTUS;


    static {
        DWARF_CACTUS = REGISTRATE
                .block("dwarf_cactus", DwarfCactus::new)
                .initialProperties(() -> Blocks.CACTUS)
                .properties(BlockBehaviour.Properties::dynamicShape)
                .properties(BlockBehaviour.Properties::noOcclusion)
                .blockstate((ctx, pvd) -> {
                    String basePath = "block/cactus/" + ctx.getName();
                    ModelFile seedModel = pvd.models().getExistingFile(pvd.modLoc(basePath + "_seed"));
                    ModelFile growingModel = pvd.models().getExistingFile(pvd.modLoc(basePath + "_growing"));
                    ModelFile baseModel = pvd.models().getExistingFile(pvd.modLoc(basePath));
                    ModelFile fruitModel = pvd.models().getExistingFile(pvd.modLoc(basePath + "_fruit"));
                    pvd.getVariantBuilder(ctx.getEntry())
                            .forAllStates(state -> {
                                AbstractDunesCactus.FruitState states = state.getValue(AbstractDunesCactus.FRUIT_STATE);
                                return ConfiguredModel.builder()
                                        .modelFile(switch (states) {
                                            case SEEDS -> seedModel;
                                            case GROWING -> growingModel;
                                            case DONE -> baseModel;
                                            case FRUITS -> fruitModel;
                                        })
                                        .build();
                            });
                })
                .loot((pvd, ctx) -> {
                    pvd.add(ctx, LootTable.lootTable()
                            .withPool(LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1))
                                    .add(LootItem.lootTableItem(ctx)
                                            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ctx)
                                                    .setProperties(StatePropertiesPredicate.Builder.properties()
                                                            .hasProperty(DwarfCactus.FRUIT_STATE, AbstractDunesCactus.FruitState.DONE))))
                                    .add(LootItem.lootTableItem(ctx)
                                            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ctx)
                                                    .setProperties(StatePropertiesPredicate.Builder.properties()
                                                            .hasProperty(DwarfCactus.FRUIT_STATE, AbstractDunesCactus.FruitState.FRUITS))))
                            )
                    );
                })
                .item()
                .model((ctx, pvd) -> pvd.withExistingParent(ctx.getName(), pvd.modLoc("block/cactus/" + ctx.getName())))
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
                .model((ctx, pvd) -> pvd.withExistingParent(ctx.getName(), pvd.modLoc("block/cactus/" + ctx.getName())))
                .tab(CreativeModeTabs.NATURAL_BLOCKS)
                .build()
                .register();
    }

    public static void register() {}
}
