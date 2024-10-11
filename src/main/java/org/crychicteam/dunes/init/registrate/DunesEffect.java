package org.crychicteam.dunes.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.ForgeRegistries;
import org.crychicteam.dunes.content.effect.CactusAffinity;

import static org.crychicteam.dunes.Dunes.REGISTRATE;

public class DunesEffect {
    public static final RegistryEntry<CactusAffinity> CACTUS_AFFINITY;
    static {
        CACTUS_AFFINITY = REGISTRATE
                .generic("cactus_affinity", ForgeRegistries.MOB_EFFECTS.getRegistryKey(), () ->
                        new CactusAffinity(MobEffectCategory.NEUTRAL, 0xB6FFA1))
                .register();
    }

    public static void register() {}
}
