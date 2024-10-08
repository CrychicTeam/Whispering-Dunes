package org.crychicteam.dunes.content.block.cactus;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public abstract class AbstractDunesCactus extends Block implements IPlantable {
    public static final EnumProperty<FruitState> FRUIT_STATE = EnumProperty.create("state", FruitState.class);

    public enum FruitState implements StringRepresentable {
        NONE, FRUITS;

        @Override
        public @NotNull String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    protected AbstractDunesCactus(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FRUIT_STATE, FruitState.NONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FRUIT_STATE);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
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

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        entity.hurt(level.damageSources().cactus(), getDamageAmount());
    }

    protected abstract float getDamageAmount();

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public PlantType getPlantType(BlockGetter world, BlockPos pos) {
        return PlantType.DESERT;
    }

    @Override
    public BlockState getPlant(BlockGetter world, BlockPos pos) {
        return this.defaultBlockState();
    }

    public abstract VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context);

    public abstract VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context);

    protected void grow(BlockState state, ServerLevel level, BlockPos pos, int height) {}
}