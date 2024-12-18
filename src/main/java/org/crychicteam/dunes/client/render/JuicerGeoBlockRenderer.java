package org.crychicteam.dunes.client.render;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.crychicteam.dunes.client.model.JuicerGeoModel;
import org.crychicteam.dunes.content.blockentity.JuicerBlockEntity;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class JuicerGeoBlockRenderer extends GeoBlockRenderer<JuicerBlockEntity> {

    public JuicerGeoBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(new JuicerGeoModel());
    }
}
