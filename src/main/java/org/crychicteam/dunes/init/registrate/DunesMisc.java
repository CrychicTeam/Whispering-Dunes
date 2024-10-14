package org.crychicteam.dunes.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2damagetracker.contents.attributes.WrappedAttribute;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.ForgeRegistries;
import org.crychicteam.dunes.Dunes;

import javax.annotation.Nullable;

public class DunesMisc {
    public static final @Nullable RegistryEntry<RangedAttribute> CACTUS_AFFINITY;

    static {
        CACTUS_AFFINITY = Dunes.REGISTRATE
            .generic("cactus_affinity", ForgeRegistries.ATTRIBUTES.getRegistryKey(), () ->
                    (RangedAttribute) new RangedAttribute("attribute." + Dunes.MOD_ID + ".cactus_affinity", 1, 1, 1000)
                            .setSyncable(true))
            .register();
    }

    public static void register () {}
}
