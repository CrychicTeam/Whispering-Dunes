package org.crychicteam.dunes.client.model;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.crychicteam.dunes.Dunes;
import org.crychicteam.dunes.content.blockentity.JuicerBlockEntity;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class JuicerGeoModel extends DefaultedBlockGeoModel<JuicerBlockEntity> {

    public JuicerGeoModel() {
        super(new ResourceLocation(Dunes.MOD_ID, "juicer"));
    }

    @Override
    public RenderType getRenderType(JuicerBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
