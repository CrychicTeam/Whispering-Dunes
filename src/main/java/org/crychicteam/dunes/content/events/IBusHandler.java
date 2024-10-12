package org.crychicteam.dunes.content.events;

import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.crychicteam.dunes.init.registrate.DunesMisc;

public class IBusHandler {

    @SubscribeEvent
    public void attributeRegisterEvent(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(type ->{
            if (!event.has(type, DunesMisc.CACTUS_AFFINITY.get())) {
                event.add(type, DunesMisc.CACTUS_AFFINITY.get());
            }
        });
    }
}
