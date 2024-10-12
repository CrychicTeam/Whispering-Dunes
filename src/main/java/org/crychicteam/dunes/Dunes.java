package org.crychicteam.dunes;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.crychicteam.dunes.content.events.EntityHandler;
import org.crychicteam.dunes.content.events.IBusHandler;
import org.crychicteam.dunes.init.registrate.DunesBlock;
import org.crychicteam.dunes.init.registrate.DunesEffect;
import org.crychicteam.dunes.init.registrate.DunesItem;
import org.crychicteam.dunes.init.registrate.DunesMisc;

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
		FMLJavaModLoadingContext ctx = FMLJavaModLoadingContext.get();
		IEventBus bus = ctx.getModEventBus();
		bus.register(new IBusHandler());

		DunesItem.register();
		DunesBlock.register();
		DunesEffect.register();
		DunesMisc.register();

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new EntityHandler());
	}

	public static void gatherData (GatherDataEvent event)
	{

	}

	public static final RegistryEntry<CreativeModeTab> TAB = REGISTRATE
			.defaultCreativeTab("main", builder -> {
				var item = DunesBlock.DWARF_CACTUS.get();
				builder.title(Component.translatable("itemGroup.dunes.main").withStyle(Style.EMPTY.withColor(0xC68300)))
						.withLabelColor(0xFFAA00)
						.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
						.icon(() -> new ItemStack(DunesBlock.DWARF_CACTUS.get())); // new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("dunes:dwarf_cactus"))))
			})
			.register();
}
