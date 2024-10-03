package org.crychicteam.dunes;

import com.tterrag.registrate.Registrate;
import net.minecraft.resources.ResourceLocation;
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
}
