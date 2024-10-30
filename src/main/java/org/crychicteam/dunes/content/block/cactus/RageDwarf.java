package org.crychicteam.dunes.content.block.cactus;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class RageDwarf extends AbstractDunesCactus {
    public static final EnumProperty<RageState> RAGE_STATE = EnumProperty.create("rage", RageState.class);
    public static final BooleanProperty IS_ANIMATION_PLAYING = BooleanProperty.create("is_animation_playing");
    public static final IntegerProperty ANIMATION_TICK = IntegerProperty.create("animation_tick", 0, 20);
    public static final IntegerProperty TICK_COUNTER = IntegerProperty.create("tick_counter", 0, 5);

    public enum RageState implements StringRepresentable {
        CALM,
        ALERT,
        ANGRY,
        EXHAUSTED;

        @Override
        public @NotNull String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    protected RageDwarf(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(RAGE_STATE, RageState.CALM)
                .setValue(IS_ANIMATION_PLAYING, false)
                .setValue(ANIMATION_TICK, 0)
                .setValue(TICK_COUNTER, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(RAGE_STATE, IS_ANIMATION_PLAYING, ANIMATION_TICK, TICK_COUNTER);
    }

    @Override
    protected float getDamageAmount() {
        return 5;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return null;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return null;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
        if (level.isClientSide()) return;

        if (state.getValue(IS_ANIMATION_PLAYING)) {
            int newTick = state.getValue(ANIMATION_TICK) + 1;
            if (newTick >= 20) {
                level.setBlock(pos, state
                        .setValue(IS_ANIMATION_PLAYING, false)
                        .setValue(ANIMATION_TICK, 0)
                        .setValue(RAGE_STATE, RageState.ALERT), 3);
            } else {
                level.setBlock(pos, state.setValue(ANIMATION_TICK, newTick), 3);
            }
            return;
        }

        int newCounter = state.getValue(TICK_COUNTER) + 1;
        if (newCounter >= 5) {
            boolean hasNearbyEntity = !level.getEntitiesOfClass(
                    LivingEntity.class,
                    new AABB(pos).inflate(5),
                    entity -> true
            ).isEmpty();

            if (!hasNearbyEntity) {
                level.setBlock(pos, state
                        .setValue(RAGE_STATE, RageState.CALM)
                        .setValue(TICK_COUNTER, 0), 3);
                return;
            }

            BlockState newState = state.setValue(TICK_COUNTER, 0);
            var rage = state.getValue(RAGE_STATE);
            if (rage.equals(RageState.CALM)) {
                level.setBlock(pos, newState.setValue(RAGE_STATE, RageState.ALERT), 3);
            } else if (random.nextFloat() >= 0.9) {
                level.setBlock(pos, newState
                        .setValue(RAGE_STATE, RageState.EXHAUSTED)
                        .setValue(IS_ANIMATION_PLAYING, true), 3);
            } else if (random.nextFloat() >= 0.3) {
                level.setBlock(pos, newState
                        .setValue(RAGE_STATE, RageState.ANGRY)
                        .setValue(IS_ANIMATION_PLAYING, true), 3);
            }
        } else {
            level.setBlock(pos, state.setValue(TICK_COUNTER, newCounter), 3);
        }
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        entity.hurt(level.damageSources().cactus(), getDamageAmount());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (!state.canSurvive(level, currentPos)) {
            level.scheduleTick(currentPos, this, 1);
        }
        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockState neighborState = level.getBlockState(pos.relative(direction));
            if (neighborState.isSolid() || level.getFluidState(pos.relative(direction)).is(FluidTags.LAVA)) {
                return false;
            }
        }
        BlockState belowState = level.getBlockState(pos.below());
        return belowState.canSustainPlant(level, pos.below(), Direction.UP, this)
                && !level.getBlockState(pos.above()).liquid();
    }
}