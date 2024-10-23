package org.crychicteam.dunes.content.block.cactus;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class RageDwarf extends AbstractDunesCactus {
    public static final EnumProperty<RageState> RAGE_STATE = EnumProperty.create("rage", RageState.class);
    private boolean isAnimationPlaying = false;
    private int animationTick = 0;
    private int tickCounter = 0;

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
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isClientSide()) return;

        if (isAnimationPlaying) {
            animationTick++;
            if (animationTick >= 20) {
                isAnimationPlaying = false;
                animationTick = 0;
                state.setValue(RAGE_STATE, RageState.ALERT);
            }
            return;
        }

        tickCounter++;
        if (tickCounter >= 5) {
            tickCounter = 0;
            boolean hasNearbyEntity = !level.getEntitiesOfClass(
                    LivingEntity.class,
                    new AABB(pos).inflate(5),
                    entity -> true
            ).isEmpty();

            if (!hasNearbyEntity) {
                state.setValue(RAGE_STATE, RageState.CALM);
                return;
            }

            var rage = state.getValue(RAGE_STATE);
            if (rage.equals(RageState.CALM)) {
                state.setValue(RAGE_STATE, RageState.ALERT);
            } else if (random.nextFloat() >= 0.9){
                state.setValue(RAGE_STATE, RageState.EXHAUSTED);
                isAnimationPlaying = true;
            } else if (random.nextFloat() >= 0.3) {
                state.setValue(RAGE_STATE, RageState.ANGRY);
                isAnimationPlaying = true;
            }
        }
    }
}