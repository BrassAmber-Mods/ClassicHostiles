package com.brassamber.classichostiles.entity.client.renderer.hostile;

import com.brassamber.classichostiles.entity.client.model.hostile.CrocodileModel;
import com.brassamber.classichostiles.entity.client.model.hostile.CrocodileModel;
import com.brassamber.classichostiles.entity.client.renderer.AbstractAnimalRenderer;
import com.brassamber.classichostiles.entity.hostile.CrocodileEntity;

import com.brassamber.classichostiles.entity.hostile.CrocodileEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class CrocodileRenderer extends AbstractAnimalRenderer<CrocodileEntity> {

    public CrocodileRenderer(Context renderManager) {
        super(renderManager, new CrocodileModel(), 1.3f, 1.0f);
    }
}
