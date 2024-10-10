package org.crychicteam.dunes.content.block.cactus;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import org.crychicteam.dunes.init.registrate.DunesItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class GiantCactus extends AbstractDunesCactus {
    public static final int MAX_HEIGHT = 7;
    public static final int MIN_FULL_HEIGHT = 5;

    private static final VoxelShape BASE_SHAPE = Block.box(1, 0, 1, 15, 16, 15);
    private static final VoxelShape FRUIT_SHAPE = Shapes.or(
            BASE_SHAPE,
            Block.box(4, 16, 4, 12, 21, 12),
            Block.box(3, 16, 3, 5, 18, 5),
            Block.box(11, 16, 3, 13, 18, 5),
            Block.box(3, 16, 11, 5, 18, 13),
            Block.box(11, 16, 11, 13, 18, 13)
    );

    public static final EnumProperty<PillarState> PILLAR_STATE = EnumProperty.create("pillar_state", PillarState.class);
    public static final IntegerProperty HEIGHT = IntegerProperty.create("height", 1, MAX_HEIGHT);

    public enum PillarState implements StringRepresentable {
        HEAD, BODY, DONE;

        @Override
        public @NotNull String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    public GiantCactus(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(PILLAR_STATE, PillarState.HEAD)
                .setValue(HEIGHT, 1)
                .setValue(FRUIT_STATE, FruitState.PLANTS));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PILLAR_STATE, HEIGHT);
    }

    @Override
    protected float getDamageAmount() {
        return 1.0F;
    }

    public Item getDrop() {
        return DunesItem.GIANT_CACTUS_FRUIT.get();
    }

    public int getRandom(){
        Random random = new Random();
        int num = 1 + random.nextInt(3);
        return num;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(FRUIT_STATE) == FruitState.FRUITS ? FRUIT_SHAPE : BASE_SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(FRUIT_STATE) == FruitState.PLANTS && !player.getMainHandItem().is(ItemTags.TOOLS) && !player.getAbilities().instabuild) {
                player.getMainHandItem().hurtAndBreak((int) getDamageAmount(), player, e -> e.broadcastBreakEvent(hand));
                player.hurt(level.damageSources().cactus(), getDamageAmount());
        }
        BlockPos topPos = findCactusTop(level, pos);
        BlockState topState = level.getBlockState(topPos);
        if (topState.getValue(PILLAR_STATE) == PillarState.DONE && topState.getValue(FRUIT_STATE) == FruitState.FRUITS) {
            if (level instanceof ServerLevel serverLevel) {
                dropFruit(topState, serverLevel, topPos);
            }
            return InteractionResult.SUCCESS;
        }
        if (!player.getMainHandItem().is(ItemTags.TOOLS)) player.hurt(level.damageSources().cactus(), getDamageAmount());
        return InteractionResult.PASS;
    }

    public static BlockPos findCactusTop(Level level, BlockPos startPos) {
        BlockPos.MutableBlockPos mutablePos = startPos.mutable();
        while (level.getBlockState(mutablePos.above()).getBlock() instanceof GiantCactus) {
            mutablePos.move(Direction.UP);
        }
        return mutablePos.immutable();
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(PILLAR_STATE) == PillarState.HEAD) {
            int height = state.getValue(HEIGHT);
            if (height <= MAX_HEIGHT && ForgeHooks.onCropsGrowPre(level, pos, state, true)) {
                if (level.isEmptyBlock(pos.above())) {
                    float growthChance;
                    if (height < MIN_FULL_HEIGHT) {
                        growthChance = 0.3f;
                    } else if (height == MIN_FULL_HEIGHT) {
                        growthChance = 0.2f;
                    } else {
                        growthChance = 0.2f;
                    }

                    if (random.nextFloat() < growthChance) {
                        if (height >= MIN_FULL_HEIGHT) {
                            float doneChance = (height - MIN_FULL_HEIGHT + 1) * 0.3f;
                            if (random.nextFloat() < doneChance) {
                                level.setBlock(pos, state.setValue(PILLAR_STATE, PillarState.DONE), 2);
                            } else {
                                grow(state, level, pos, height);
                            }
                        } else {
                            grow(state, level, pos, height);
                        }
                    }
                }
                ForgeHooks.onCropsGrowPost(level, pos, state);
            }
        } else if (state.getValue(PILLAR_STATE) == PillarState.DONE) {
            if (ForgeHooks.onCropsGrowPre(level, pos, state, true)) {
                if (state.getValue(FRUIT_STATE) == FruitState.PLANTS && random.nextFloat() < 0.2f) {
                    level.setBlock(pos, state.setValue(FRUIT_STATE, FruitState.FRUITS), 2);
                }
                ForgeHooks.onCropsGrowPost(level, pos, state);
            }
        }
    }

    public void grow(BlockState state, ServerLevel level, BlockPos pos, int height) {
        int newHeight = height + 1;
        if (newHeight > MAX_HEIGHT) { newHeight = MAX_HEIGHT; }
        if (level.getBlockState(pos.above()).isSolid()) {
            if (new Random().nextFloat() < 0.05) level.setBlock(pos, state.setValue(PILLAR_STATE, PillarState.DONE),2);
            return;
        }
        level.setBlock(pos, state.setValue(PILLAR_STATE, PillarState.BODY), 2);
        level.setBlock(pos.above(), defaultBlockState()
                .setValue(PILLAR_STATE, PillarState.HEAD)
                .setValue(HEIGHT, newHeight), 2);
    }

    /**
     * This is for general tall cactus survive.
     * extends this class when creating a new tall cactus.
     * @param pState
     * @param pLevel
     * @param pPos
     * @return
     */
    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockState belowState = pLevel.getBlockState(pPos.below());
        BlockState aboveState = pLevel.getBlockState(pPos.above());
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (!pLevel.getBlockState(pPos.relative(direction)).isAir()
                    && !pLevel.getBlockState(pPos.relative(direction)).is(Blocks.GRASS)
                    && !pLevel.getBlockState(pPos.relative(direction)).is(Blocks.TALL_GRASS)
                    && !pLevel.getBlockState(pPos.relative(direction)).is(BlockTags.FLOWERS)) {
                return false;
            }
        }
        if (belowState.getBlock() instanceof GiantCactus) {
            return belowState.getValue(PILLAR_STATE) != PillarState.HEAD;
        }
        return belowState.canSustainPlant(pLevel, pPos, Direction.UP, this) &&
                !aboveState.liquid();
    }
    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        updateRemainingCactus(level, pos);
    }

    protected void updateRemainingCactus(Level level, BlockPos pos) {
        for (int i = 1; i <= MAX_HEIGHT; i++) {
            BlockPos currentPos = pos.below(i);
            BlockState currentState = level.getBlockState(currentPos);
            if (currentState.is(this)) {
                if (currentState.getValue(PILLAR_STATE) != PillarState.HEAD) {
                    int newHeight = getHeight(level, currentPos);
                    level.setBlock(currentPos, currentState.setValue(PILLAR_STATE, PillarState.HEAD).setValue(HEIGHT, newHeight), 3);
                }
                break;
            }
        }
    }

    protected int getHeight(LevelReader level, BlockPos pos) {
        int height = 1;
        BlockPos.MutableBlockPos mutablePos = pos.mutable().move(Direction.DOWN);
        while (level.getBlockState(mutablePos).is(this) && height < MAX_HEIGHT) {
            height++;
            mutablePos.move(Direction.DOWN);
        }
        return height;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (!state.canSurvive(level, currentPos)) {
            return Blocks.AIR.defaultBlockState();
        }

        if (state.getValue(PILLAR_STATE) == PillarState.BODY && direction == Direction.UP && !neighborState.is(this)) {
            return state.setValue(PILLAR_STATE, PillarState.HEAD);
        }

        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    protected void dropFruit(BlockState state, ServerLevel level, BlockPos pos) {
        Block.popResource(level, pos, getDrops().get(0));
        level.setBlock(pos, state.setValue(FRUIT_STATE, FruitState.PLANTS), 2);
    }

    protected List<ItemStack> getDrops() {
        List<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(getDrop(), getRandom()));
        return drops;
    }
}