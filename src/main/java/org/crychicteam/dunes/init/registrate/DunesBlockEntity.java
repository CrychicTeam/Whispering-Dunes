package org.crychicteam.dunes.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import org.crychicteam.dunes.Dunes;
import org.crychicteam.dunes.client.render.JuicerGeoBlockRenderer;
import org.crychicteam.dunes.content.blockentity.JuicerBlock;
import org.crychicteam.dunes.content.blockentity.JuicerBlockEntity;

public class DunesBlockEntity {
    public static final BlockEntry<JuicerBlock> JUICER;
    public static final BlockEntityEntry<JuicerBlockEntity> JUICER_BE;

    static {
        JUICER = Dunes.REGISTRATE.block("juicer", JuicerBlock::new).simpleItem().register();
        JUICER_BE = Dunes.REGISTRATE.blockEntity("juicer", JuicerBlockEntity::new)
                .renderer(() -> JuicerGeoBlockRenderer::new)
                .validBlock(JUICER)
                .register();
    }

    public static void register() {}
}
