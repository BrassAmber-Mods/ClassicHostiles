package com.brassamber.classichostiles.entity.client.renderer.neutral;

import java.util.Map;

import com.brassamber.classichostiles.ClassicHostiles;
import com.brassamber.classichostiles.entity.neutral.BearEntity;
import com.brassamber.classichostiles.entity.neutral.BearEntity.BearVariant;
import com.google.common.collect.Maps;

import net.minecraft.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.PolarBearRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.PolarBear;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.7
 */
public class BearRenderer extends PolarBearRenderer {
	private static final Map<BearVariant, ResourceLocation> BEAR_TEXTURES = Util.make(Maps.newHashMap(), (texture) -> {
		texture.put(BearVariant.BLACK, ClassicHostiles.locate("textures/entity/bear/black_bear.png"));
		texture.put(BearVariant.BROWN, ClassicHostiles.locate("textures/entity/bear/brown_bear.png"));
	});

	public BearRenderer(Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(PolarBear bearEntity) {
		if (bearEntity instanceof BearEntity bearVariant) {
			return BEAR_TEXTURES.get(bearVariant.getBearVariant());
		} else {
			return super.getTextureLocation(bearEntity);
		}
	}
}
