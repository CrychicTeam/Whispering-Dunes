package org.crychicteam.dunes.init.registrate;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import org.crychicteam.cibrary.api.registry.armorset.ArmorSetBuilder;
import org.crychicteam.cibrary.content.armorset.ArmorSet;
import org.crychicteam.dunes.content.armorset.CactusArmorSet;
import org.crychicteam.dunes.content.armorset.seteffect.CactusSetEffect;

public class DunesArmorSet {
    public static final ArmorSet CACTUS_ARMORSET;

    static {
        CACTUS_ARMORSET = ArmorSetBuilder.create(new CactusArmorSet("cactus_armor_set", new CactusSetEffect()))
                .addEquipment(EquipmentSlot.HEAD, Items.DIAMOND_HELMET)
                .addEquipment(EquipmentSlot.CHEST, Items.DIAMOND_CHESTPLATE)
                .addEquipment(EquipmentSlot.LEGS, Items.DIAMOND_LEGGINGS)
                .addEquipment(EquipmentSlot.FEET, Items.DIAMOND_BOOTS)
                .addAttribute(DunesMisc.CACTUS_AFFINITY.get(),"cactus_armor_set",2, AttributeModifier.Operation.MULTIPLY_TOTAL)
                .addAttribute(Attributes.ARMOR, "cactus_armor_set", -2, AttributeModifier.Operation.ADDITION)
                .addEffect(MobEffects.WATER_BREATHING, 2)
                .build();
    }

    public static void register() {}
}
