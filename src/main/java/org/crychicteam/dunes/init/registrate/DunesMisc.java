package org.crychicteam.dunes.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2damagetracker.contents.attributes.WrappedAttribute;
import net.minecraftforge.registries.ForgeRegistries;
import org.crychicteam.dunes.Dunes;

public class DunesMisc {
    public static final RegistryEntry<WrappedAttribute> CACTUS_AFFINITY;

    static {
        CACTUS_AFFINITY = Dunes.REGISTRATE
            .generic("cactus_affinity", ForgeRegistries.ATTRIBUTES.getRegistryKey(), () ->
                    new WrappedAttribute("attribute." + Dunes.MOD_ID + ".cactus_affinity", 1, 1, 2)
                            .setSyncable(true))
            .register();
    }

    public static void register () {}
}
