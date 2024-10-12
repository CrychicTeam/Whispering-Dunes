package org.crychicteam.dunes.content.item.cactus;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.crychicteam.dunes.content.block.cactus.AbstractDunesCactus;
import org.crychicteam.dunes.init.registrate.DunesBlock;
import org.jetbrains.annotations.NotNull;

public class DwarfCactusSeedItem extends Item {
    public DwarfCactusSeedItem() {
        super(new Item.Properties());
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos().above();
        BlockState state = DunesBlock.DWARF_CACTUS.getDefaultState()
                .setValue(AbstractDunesCactus.FRUIT_STATE, AbstractDunesCactus.FruitState.SEEDS);
        if (state.canSurvive(level, pos) && level.isEmptyBlock(pos)) {
            if (!level.isClientSide()) {
                level.setBlock(pos, state, 3);
                pContext.getItemInHand().shrink(1);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.FAIL;
    }
}