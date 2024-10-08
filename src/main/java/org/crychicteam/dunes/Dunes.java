package org.crychicteam.dunes;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.common.Mod;
import org.crychicteam.dunes.init.registrate.DunesBlock;
import org.crychicteam.dunes.init.registrate.DunesItem;

@Mod(Dunes.MOD_ID)
public class Dunes
{
	public static final String MOD_ID = "dunes";
	public static ResourceLocation source(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}
	public static Registrate REGISTRATE = Registrate.create(MOD_ID);

	public Dunes()
	{
		DunesBlock.register();
		DunesItem.register();
	}

	public static void gatherData (GatherDataEvent event)
	{

	}

	public static final RegistryEntry<CreativeModeTab> TAB = REGISTRATE
			.defaultCreativeTab("dunes", builder -> {
				builder.title(Component.translatable("itemGroup.dunes"))
						.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
						.icon(() -> new ItemStack(DunesBlock.DWARF_CACTUS.get()));
			})
			.register();
}
