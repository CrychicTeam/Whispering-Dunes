package org.crychicteam.dunes.content.effect;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.ItemStack;
import org.crychicteam.dunes.init.registrate.DunesMisc;

public class CactusAffinity extends MobEffect {
    public CactusAffinity(MobEffectCategory pCategory, int pColor) {
        super(MobEffectCategory.NEUTRAL, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        int damage = (int) (amplifier * 5 / livingEntity.getAttributeValue(DunesMisc.CACTUS_AFFINITY.get()));
        Holder<DamageType> cactusDamageType = livingEntity.level().registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(DamageTypes.CACTUS);
        DamageSource cactusDamageFromSelf = new DamageSource(cactusDamageType) {
            @Override
            public net.minecraft.world.entity.Entity getEntity() {
                return livingEntity;
            }
        };
        livingEntity.hurt(cactusDamageFromSelf, (float) damage);
        livingEntity.hurt(livingEntity.level().damageSources().cactus(), (float) damage);
        if (livingEntity.isUsingItem() && livingEntity.getMainHandItem().getItem() instanceof InstrumentItem item) {
            // if the player has the Armor set

        }
    }

    private void armorSetEffect(LivingEntity livingEntity, int damage){
        for (EquipmentSlot slot : EquipmentSlot.values()){
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack item = livingEntity.getItemBySlot(slot);
                item.hurtAndBreak(damage ,livingEntity, entity -> {});
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
