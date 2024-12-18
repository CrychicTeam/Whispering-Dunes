package org.crychicteam.dunes.init.registrate;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.RegistryObject;
import org.crychicteam.cibrary.api.registry.ArmorSetRegistry;
import org.crychicteam.cibrary.content.armorset.ArmorSet;
import org.crychicteam.dunes.content.armorset.CactusArmorSet;
import org.crychicteam.dunes.content.armorset.seteffect.CactusSetEffect;

public class DunesArmorSet {
    public static final RegistryObject<ArmorSet> CACTUS_ARMORSET;
    static {
        CACTUS_ARMORSET = ArmorSetRegistry.ARMOR_SETS.register(
                "cactus_armor_set",
                () -> ArmorSetRegistry.Builder.of(()-> new CactusArmorSet(new CactusSetEffect()))
                        .addEquipment(EquipmentSlot.HEAD, Items.DIAMOND_HELMET)
                        .addEquipment(EquipmentSlot.CHEST, Items.DIAMOND_CHESTPLATE)
                        .addEquipment(EquipmentSlot.LEGS, Items.DIAMOND_LEGGINGS)
                        .addEquipment(EquipmentSlot.FEET, Items.DIAMOND_BOOTS)
                        .addAttribute(DunesMisc.CACTUS_AFFINITY.get(), "cactus_armor_set_cactus_affinity", 5, AttributeModifier.Operation.ADDITION)
                        .addEffect(MobEffects.WATER_BREATHING, 0)
                        .setState(ArmorSet.State.NORMAL)
                        .setSkillState("none")
                        .build()
        );
    }

    public static void register() {}
}
