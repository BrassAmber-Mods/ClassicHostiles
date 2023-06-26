package com.brassamber.classichostiles.entity.client.renderer.hostile;

import com.brassamber.classichostiles.entity.client.model.hostile.BigCatModel;
import com.brassamber.classichostiles.entity.client.renderer.AbstractAnimalRenderer;
import com.brassamber.classichostiles.entity.hostile.BigCatEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.15
 */
@OnlyIn(Dist.CLIENT)
public class BigCatRenderer extends AbstractAnimalRenderer<BigCatEntity> {

    public BigCatRenderer(Context renderManager) {
        super(renderManager, new BigCatModel(), 0.7f, 1.0f);
    }
}
