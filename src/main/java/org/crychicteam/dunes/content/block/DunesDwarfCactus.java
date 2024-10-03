package org.crychicteam.dunes.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class DunesDwarfCactus extends CactusBlock implements IPlantable {
    private static final VoxelShape BASE_SHAPE = Shapes.box(3/16.0, 0, 3/16.0, 13/16.0, 10/16.0, 13/16.0);
    private static final VoxelShape FRUIT_SHAPE = Shapes.or(
            BASE_SHAPE,
            Shapes.box(5/16.0, 10/16.0, 5/16.0, 11/16.0, 13/16.0, 11/16.0)
    );

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
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(STATE) == State.FRUITS ? FRUIT_SHAPE : BASE_SHAPE;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return getShape(state, world, pos, context);
    }

    @Override
    public @NotNull VoxelShape getInteractionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return getShape(state, world, pos, CollisionContext.empty());
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
    public @NotNull List<ItemStack> getDrops(BlockState pState, LootParams.Builder pParams) {
        return super.getDrops(pState, pParams);
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
    }
}