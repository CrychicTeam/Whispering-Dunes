package org.crychicteam.dunes.content.events;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.crychicteam.dunes.Dunes;
import org.crychicteam.dunes.content.block.DunesDwarfCactus;
import org.crychicteam.dunes.content.block.GiantCactus;

@Mod.EventBusSubscriber(modid = Dunes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClickHandler {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getLevel().isClientSide()) {
            if (event.getItemStack().getItem() instanceof BlockItem) {
                BlockState state = event.getLevel().getBlockState(event.getPos());
                Player player = event.getEntity();
                BlockItem blockItem = (BlockItem) player.getMainHandItem().getItem();
                if (state.getBlock() instanceof GiantCactus && blockItem.getBlock() instanceof GiantCactus) {
                    if (state.getValue(GiantCactus.STATE) == DunesDwarfCactus.State.FRUITS) {
                        GiantCactus.dropResources(state, event.getLevel(), event.getPos());
                        event.getLevel().setBlockAndUpdate(event.getPos(), state.setValue(GiantCactus.STATE, DunesDwarfCactus.State.NONE));
                    }
                    event.setCanceled(true);
                }
            }
        }
    }
}
