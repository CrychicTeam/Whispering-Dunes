package org.crychicteam.dunes.content.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.crychicteam.dunes.init.registrate.DunesBlockEntity;
import org.jetbrains.annotations.Nullable;

public class JuicerBlock extends BaseEntityBlock implements EntityBlock {

    public JuicerBlock(Properties pProperties) {
        super(pProperties.noOcclusion());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return DunesBlockEntity.JUICER_BE.create(blockPos, blockState);
    }
}
