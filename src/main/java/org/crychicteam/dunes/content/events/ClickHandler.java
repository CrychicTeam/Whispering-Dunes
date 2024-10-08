package org.crychicteam.dunes.content.events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.crychicteam.dunes.Dunes;
import org.crychicteam.dunes.content.block.cactus.AbstractDunesCactus;
import org.crychicteam.dunes.content.block.cactus.DunesDwarfCactus;
import org.crychicteam.dunes.content.block.cactus.GiantCactus;

@Mod.EventBusSubscriber(modid = Dunes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClickHandler {

    @SubscribeEvent
    public static void OnPlayerPunch(PlayerInteractEvent.LeftClickBlock event) {
        if (!event.getLevel().isClientSide()) {
            Level level = event.getLevel();
            BlockPos pos = event.getPos();
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() instanceof AbstractDunesCactus) {
                if (state.getBlock() instanceof DunesDwarfCactus) {
                    InteractionResult result = state.getBlock().use(state, level, pos, event.getEntity(), InteractionHand.MAIN_HAND, new BlockHitResult(new Vec3(pos.getX(), pos.getY(), pos.getZ()), Direction.UP, pos, false));
                } else {
                    BlockPos topPos = GiantCactus.findCactusTop(level, pos);
                    InteractionResult result = state.getBlock().use(state, level, topPos, event.getEntity(), InteractionHand.MAIN_HAND, new BlockHitResult(new Vec3(pos.getX(), pos.getY(), pos.getZ()), Direction.UP, topPos, false));
                }
            }
        }
    }
}
