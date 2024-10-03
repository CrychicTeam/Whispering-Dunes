package org.crychicteam.dunes.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import org.crychicteam.dunes.Dunes;
import org.crychicteam.dunes.content.block.DunesDwarfCactus;

public class DunesBlock {
    public static final BlockEntry<DunesDwarfCactus> DWARF_CACTUS = Dunes.REGISTRATE
            .block("dwarf_cactus", DunesDwarfCactus::new)
            .initialProperties(() -> Blocks.CACTUS)
            .properties(BlockBehaviour.Properties::dynamicShape)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate((ctx, pvd) -> {
                ResourceLocation baseTexture = pvd.modLoc("block/dwarf_cactus");
                ResourceLocation fruitTexture = pvd.modLoc("block/dwarf_cactus_fruit");

                ModelFile baseModel = pvd.models().getExistingFile(baseTexture);
                ModelFile fruitModel = pvd.models().getExistingFile(fruitTexture);

                pvd.getVariantBuilder(ctx.getEntry())
                        .forAllStates(state -> {
                            int age = state.getValue(DunesDwarfCactus.AGE);
                            boolean hasFruit = state.getValue(DunesDwarfCactus.STATE) == DunesDwarfCactus.State.FRUITS;

                            return ConfiguredModel.builder()
                                    .modelFile(hasFruit ? fruitModel : baseModel)
                                    .rotationY(age % 4 * 90)
                                    .build();
                        });
            })
            .simpleItem()
            .register();

    public static void register() {}
}
