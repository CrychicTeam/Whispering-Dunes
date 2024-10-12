package org.crychicteam.dunes.content.events;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.crychicteam.dunes.content.item.cactus.CactusFruits;

public class EntityHandler {

    @SubscribeEvent
    public void preventFruitsDye(EntityJoinLevelEvent event) {
        if (event.getLevel() instanceof ServerLevel && event.getEntity() instanceof ItemEntity item) {
            if (item.getItem().getItem() instanceof CactusFruits) {
                item.setInvulnerable(true);
            }
        }
    }
}
