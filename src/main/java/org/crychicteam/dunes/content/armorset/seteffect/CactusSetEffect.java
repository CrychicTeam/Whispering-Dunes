package org.crychicteam.dunes.content.armorset.seteffect;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.ItemStackedOnOtherEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import org.crychicteam.cibrary.Cibrary;
import org.crychicteam.cibrary.content.armorset.ArmorSet;
import org.crychicteam.cibrary.content.armorset.SetEffect;
import org.crychicteam.cibrary.content.event.ItemHurtEffectResult;
import org.crychicteam.cibrary.content.event.StandOnFluidEvent;
import org.crychicteam.dunes.content.item.cactus.CactusFruits;
import org.crychicteam.dunes.init.registrate.DunesEffect;
import org.crychicteam.dunes.init.registrate.DunesMisc;

import java.util.Objects;

public class CactusSetEffect implements SetEffect {

    @Override
    public void applyEffect(LivingEntity livingEntity) {
        var armorSet = Cibrary.ARMOR_SET_MANAGER.getActiveArmorSet((Player) livingEntity);
        for (ItemStack item : livingEntity.getArmorSlots()) {
            if (!item.getOrCreateTag().getBoolean("active")) {
                armorSet.setState(ArmorSet.State.NORMAL);
                return;
            }
        }
        armorSet.setState(ArmorSet.State.ACTIVE);
    }

    @Override
    public void crouchingEffect(LivingEntity entity) {
        if (entity instanceof Player player && entity.getMainHandItem().is(Items.APPLE)) {
            SwordEffect(player);
        }
    }

    private void SwordEffect(Player player) {
        if (player.tickCount % 4 == 0 && player.hasEffect(DunesEffect.CACTUS_AFFINITY.get())) {
            player.heal((float) Objects.requireNonNull(player.getAttribute(DunesMisc.CACTUS_AFFINITY.get())).getValue());
            for (ItemStack item : player.getArmorSlots()) {
                item.setDamageValue(Math.min(  Math.max( item.getDamageValue() - (int) Objects.requireNonNull(player.getAttribute(DunesMisc.CACTUS_AFFINITY.get())).getValue() , 0 ), item.getMaxDamage() )  );
            }
        }
    }

    @Override
    public void onStandOnFluidEffect(LivingEntity entity, StandOnFluidEvent event) {
        if (entity instanceof Player player && entity.getMainHandItem().is(Items.APPLE) && entity.isCrouching()) {
            event.setCanceled(true);
            SwordEffect(player);
        }
    }

    @Override
    public void stackedOnOther(LivingEntity entity, ItemStackedOnOtherEvent event) {
        if (entity instanceof Player player) {
            var armor = event.getCarriedItem();
            var material = event.getStackedOnItem();
            if (armor.isEmpty() || material.isEmpty()) return;
            var slot = event.getSlot().getSlotIndex();
            if (isArmorSlot(event.getSlot().getSlotIndex()) && (material.getItem() instanceof CactusFruits)) {
                int repairAmount = Math.min(material.getCount(), armor.getDamageValue());
                armor.setDamageValue(Math.max(armor.getDamageValue() - repairAmount, 0));
                int materialCount = armor.getOrCreateTag().getInt("material_count") + repairAmount;
                armor.getOrCreateTag().putInt("material_count", materialCount);
                if (materialCount >= 20) {
                    armor.getOrCreateTag().putBoolean("active", true);
                    applyEffect(entity);
                }
                if (!player.getAbilities().instabuild) {
                    material.shrink(repairAmount);
                }
                event.setCanceled(true);
            }
        }
    }

    private boolean isArmorSlot(int slotIndex) {
        return slotIndex == 36 ||
                slotIndex == 37 ||
                slotIndex == 38 ||
                slotIndex == 39;
    }

    @Override
    public void onTargetedEffect(LivingEntity entity, LivingChangeTargetEvent changer) {
        if (entity.getMainHandItem().is(Items.APPLE) && changer.getEntity() instanceof NeutralMob ) changer.setCanceled(true);
    }

    @Override
    public ItemHurtEffectResult itemHurtEffect(LivingEntity entity, ItemStack item, int originalDamage) {
        if (item.getMaxDamage() <= originalDamage + item.getDamageValue()) {
            item.setDamageValue(item.getMaxDamage()-1);
            return ItemHurtEffectResult.cancel();
        }
        return ItemHurtEffectResult.unmodified();
    }

    @Override
    public void removeEffect(LivingEntity livingEntity) {}

    @Override
    public String getIdentifier() {
        return "cactus";
    }
}
