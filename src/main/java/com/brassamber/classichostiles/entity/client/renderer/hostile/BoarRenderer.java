package com.brassamber.classichostiles.entity.client.renderer.hostile;

import com.brassamber.classichostiles.entity.client.model.hostile.BoarModel;
import com.brassamber.classichostiles.entity.client.renderer.AbstractAnimalRenderer;
import com.brassamber.classichostiles.entity.hostile.BoarEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.2
 */
@OnlyIn(Dist.CLIENT)
public class BoarRenderer extends AbstractAnimalRenderer<BoarEntity> {

	public BoarRenderer(Context renderManager) {
		super(renderManager, new BoarModel(), 0.7f, 1.25f);
	}
}
