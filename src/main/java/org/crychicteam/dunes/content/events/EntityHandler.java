package org.crychicteam.dunes.content.events;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.crychicteam.dunes.content.item.cactus.CactusFruits;
import org.crychicteam.dunes.init.registrate.DunesBlock;

import java.util.Objects;

public class EntityHandler {

    @SubscribeEvent
    public void preventFruitsDye(EntityJoinLevelEvent event) {
        if (event.getLevel() instanceof ServerLevel && event.getEntity() instanceof ItemEntity item) {
            if (item.getItem().getItem() instanceof CactusFruits) {
                item.setInvulnerable(true);
            } else if (item.getItem().getItem() instanceof BlockItem blockItem) {
                if (DunesBlock.DWARF_CACTUS.is(blockItem.getBlock()) || DunesBlock.GIANT_PILLAR_CACTUS.is(blockItem.getBlock())) {
                    item.setInvulnerable(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void AntiExplosionEffect(ExplosionEvent.Detonate event) {
        var damage = new DamageSource(Objects.requireNonNull(event.getExplosion().getExploder()).level().registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(DamageTypes.CACTUS), Objects.requireNonNull(event.getExplosion().getExploder()), Objects.requireNonNull(event.getExplosion().getExploder()));
        if (event.getExplosion().getDamageSource().type() == damage.type()) {
            var affectedEntities = event.getAffectedEntities();
            affectedEntities.removeIf(entity -> entity instanceof ItemEntity);
        }
    }
}
