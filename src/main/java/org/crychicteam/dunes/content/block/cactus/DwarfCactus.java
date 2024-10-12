package org.crychicteam.dunes.content.block.cactus;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import org.crychicteam.dunes.init.registrate.DunesItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 */
public class DwarfCactus extends AbstractDunesCactus {
    private static final VoxelShape SEED_SHAPE = Shapes.box(7/16.0, 0/16.0, 7/16.0, 9/16.0, 2/16.0 ,9/16.0 );
    private static final VoxelShape GROWING_SHAPE = Shapes.box(5/16.0, 0/16.0, 5/16.0, 11/16.0, 6/16.0, 11/16.0 );
    private static final VoxelShape DONE_SHAPE = Shapes.box(3/16.0, 0, 3/16.0, 13/16.0, 10/16.0, 13/16.0);
    private static final VoxelShape FRUIT_SHAPE = Shapes.or(
            DONE_SHAPE,
            Shapes.box(5/16.0, 10/16.0, 5/16.0, 11/16.0, 13/16.0, 11/16.0)
    );

    public DwarfCactus(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FRUIT_STATE, FruitState.SEEDS));
    }

    @Override
    protected float getDamageAmount() {
        return 1.0F;
    }

    public Item getDrop() {
        return DunesItem.DWARF_CACTUS_FRUIT.get();
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
        var shapeState = state.getValue(FRUIT_STATE);
        return switch (shapeState) {
            case SEEDS -> SEED_SHAPE;
            case GROWING -> GROWING_SHAPE;
            case FRUITS -> FRUIT_SHAPE;
            default -> DONE_SHAPE;
        };
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(FRUIT_STATE) == FruitState.FRUITS) {
            if (level instanceof ServerLevel serverLevel) {
                dropFruit(state, serverLevel, pos);
            }
            return InteractionResult.SUCCESS;
        }
        if (!player.getMainHandItem().is(ItemTags.TOOLS) && !player.getAbilities().instabuild) {
                player.getMainHandItem().hurtAndBreak((int) getDamageAmount(), player, e -> e.broadcastBreakEvent(hand));
                player.hurt(level.damageSources().cactus(), getDamageAmount());
        }
        return InteractionResult.PASS;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (ForgeHooks.onCropsGrowPre(level, pos, state, true)) {
            grow(state, level, pos, random);
            ForgeHooks.onCropsGrowPost(level, pos, state);
        }
    }

    private void grow(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(FRUIT_STATE) == FruitState.SEEDS && random.nextFloat() < 0.3f) {
            level.setBlock(pos, state.setValue(FRUIT_STATE, FruitState.GROWING), 2);
        } else if (state.getValue(FRUIT_STATE) == FruitState.GROWING && random.nextFloat() < 0.2f) {
            level.setBlock(pos, state.setValue(FRUIT_STATE, FruitState.DONE), 2);
        } else if (state.getValue(FRUIT_STATE) == FruitState.DONE && random.nextFloat() < 0.1f) {
            level.setBlock(pos, state.setValue(FRUIT_STATE, FruitState.FRUITS), 2);
        }
    }

    protected void dropFruit(BlockState state, ServerLevel level, BlockPos pos) {
        Block.popResource(level, pos, getDrops().get(0));
        level.setBlock(pos, state.setValue(FRUIT_STATE, FruitState.DONE), 2);
    }

    protected List<ItemStack> getDrops() {
        List<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(getDrop(), getRandom()));
        return drops;
    }
}