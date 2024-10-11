package org.crychicteam.dunes.content.armorset;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.crychicteam.cibrary.content.armorset.ArmorSet;
import org.crychicteam.cibrary.content.armorset.SetEffect;
import org.crychicteam.dunes.init.registrate.DunesEffect;

import java.util.Objects;

public class CactusArmorSet extends ArmorSet {
    public CactusArmorSet(String identifier, SetEffect effect) {
        super(identifier);
    }

//    @Override
//    public void onHurt(AttackCache cache, ItemStack weapon) {
//        LivingEntity entity = cache.getAttackTarget();
//        if (cache.getAttackTarget() instanceof  Player player
//                && Objects.requireNonNull(cache.getLivingHurtEvent()).getSource().is(DamageTypes.CACTUS)
//         &&!Objects.requireNonNull(cache.getLivingHurtEvent().getSource().getDirectEntity()).is(entity)) {
//                cache.getLivingHurtEvent().setCanceled(true);
//
//                if (!player.hasEffect(DunesEffect.CACTUS_AFFINITY.get())){
//                    player.addEffect(new MobEffectInstance(DunesEffect.CACTUS_AFFINITY.get(), 30, (int) cache.getDamageDealt()));
//                } else {
//                    int newAmplifier = Objects.requireNonNull(player.getEffect(DunesEffect.CACTUS_AFFINITY.get())).getAmplifier() + 1;
//                    player.removeEffect(Objects.requireNonNull(player.getEffect(DunesEffect.CACTUS_AFFINITY.get())).getEffect());
//                    player.addEffect(new MobEffectInstance(DunesEffect.CACTUS_AFFINITY.get(), 30, newAmplifier));
//                }
//
//        }
//    }
}
