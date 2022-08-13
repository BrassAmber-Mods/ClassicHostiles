package com.brassamber.classichostiles.entity.client.model;

import com.brassamber.classichostiles.ClassicHostiles;
import com.brassamber.classichostiles.entity.client.renderer.BoarRenderer;
import com.brassamber.classichostiles.entity.hostile.BoarEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.model.AnimatedGeoModel;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.0
 */
@OnlyIn(Dist.CLIENT)
public class BoarModel extends AnimatedGeoModel<BoarEntity> {
	private static final ResourceLocation[] BOAR_TEXTURES = new ResourceLocation[] {
		ClassicHostiles.locate("textures/entity/boar/boar_black.png"),
		ClassicHostiles.locate("textures/entity/boar/boar_brown.png"),
		ClassicHostiles.locate("textures/entity/boar/boar_grey.png"),
		ClassicHostiles.locate("textures/entity/boar/boar_tan.png")
	};

	@Override
	public ResourceLocation getModelResource(BoarEntity entityIn) {
		return ClassicHostiles.locate("geo/boar.geo.json");
	}

	@Override
	public ResourceLocation getAnimationResource(BoarEntity entityIn) {
		return ClassicHostiles.locate("animations/boar.animation.json");
	}

	/**
	 * Texture will change with variants in {@link BoarRenderer}
	 */
	@Override
	public ResourceLocation getTextureResource(BoarEntity entityIn) {
		return BOAR_TEXTURES[entityIn.getVariant()];
	}
}
