package com.brassamber.classichostiles.entity.client.renderer;

import com.brassamber.classichostiles.entity.client.model.BoarModel;
import com.brassamber.classichostiles.entity.hostile.BoarEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.0
 */
@OnlyIn(Dist.CLIENT)
public class BoarRenderer extends AbstractAnimalRenderer<BoarEntity> {

	public BoarRenderer(Context renderManager) {
		super(renderManager, new BoarModel(), 0.7f);
	}
}
