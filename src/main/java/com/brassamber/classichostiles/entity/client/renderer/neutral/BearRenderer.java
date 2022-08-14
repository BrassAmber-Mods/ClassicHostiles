package com.brassamber.classichostiles.entity.client.renderer.neutral;

import com.brassamber.classichostiles.ClassicHostiles;
import com.brassamber.classichostiles.entity.neutral.BearEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.PolarBearRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.PolarBear;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.3
 */
public class BearRenderer extends PolarBearRenderer {
	private static final ResourceLocation[] BEAR_TEXTURES = new ResourceLocation[] {
		ClassicHostiles.locate("textures/entity/bear/black_bear.png"),
		ClassicHostiles.locate("textures/entity/bear/brown_bear.png")
	};

	public BearRenderer(Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(PolarBear bearEntity) {
		if (bearEntity instanceof BearEntity) {
			return BEAR_TEXTURES[((BearEntity) bearEntity).getVariant()];
		} else {
			return super.getTextureLocation(bearEntity);
		}
	}
}
