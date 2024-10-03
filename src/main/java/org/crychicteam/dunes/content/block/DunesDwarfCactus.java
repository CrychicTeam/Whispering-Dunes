package org.crychicteam.dunes.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;

import java.util.Locale;

public class DunesDwarfCactus extends CactusBlock implements IPlantable {
    public enum State implements StringRepresentable {
        NONE, FRUITS;

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    public static final EnumProperty<State> STATE = EnumProperty.create("state", State.class);

    public DunesDwarfCactus(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(STATE, State.NONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(STATE);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return doClick(level, pos, state);
    }

    public InteractionResult doClick(Level level, BlockPos pos, BlockState state) {
        if (state.getValue(STATE) == State.FRUITS) {
            if (level instanceof ServerLevel sl) {
                dropFruit(state, sl, pos);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    protected void dropFruit(BlockState state, ServerLevel level, BlockPos pos) {
        dropResources(state, level, pos);
        level.setBlockAndUpdate(pos, state.setValue(STATE, State.NONE));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(STATE) == State.NONE) {
            boolean grow = random.nextDouble() < 0.2f;
            if (ForgeHooks.onCropsGrowPre(level, pos, state, grow)) {
                level.setBlockAndUpdate(pos, state.setValue(STATE, State.FRUITS));
                ForgeHooks.onCropsGrowPost(level, pos, state);
            }
        }
        BlockPos blockpos = pos.above();
        if (level.isEmptyBlock(blockpos)) {
            int i;
            for (i = 1; level.getBlockState(pos.below(i)).is(this); ++i) {
            }

            if (i < 3) {
                int j = state.getValue(AGE);
                if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(level, blockpos, state, true)) {
                    if (j == 15) {
                        level.setBlockAndUpdate(blockpos, this.defaultBlockState());
                        BlockState blockstate = state.setValue(AGE, 0);
                        level.setBlock(pos, blockstate, 4);
                        blockstate.neighborChanged(level, blockpos, this, pos, false);
                    } else {
                        level.setBlock(pos, state.setValue(AGE, j + 1), 4);
                    }
                    net.minecraftforge.common.ForgeHooks.onCropsGrowPost(level, pos, state);
                }
            }
        }
    }
}