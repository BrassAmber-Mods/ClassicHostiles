package com.brassamber.classichostiles.entity.client.renderer;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.2
 */
public abstract class AbstractAnimalRenderer<T extends LivingEntity & IAnimatable> extends GeoEntityRenderer<T> {
	protected float modelScale;

	public AbstractAnimalRenderer(Context renderManager, AnimatedGeoModel<T> modelProvider, float shadowRadius) {
		this(renderManager, modelProvider, shadowRadius, 1.0f);
	}

	public AbstractAnimalRenderer(Context renderManager, AnimatedGeoModel<T> modelProvider, float shadowRadius, float modelScale) {
		super(renderManager, modelProvider);
		this.shadowRadius = shadowRadius;
		this.modelScale = modelScale;
	}

	/**
	 * Scale the baby
	 */
	@Override
	public RenderType getRenderType(T animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
		if (animatable.isBaby()) {
			stack.scale(this.modelScale * 0.7f, this.modelScale * 0.7f, this.modelScale * 0.75f);
		} else {
			stack.scale(this.modelScale, this.modelScale, this.modelScale);
		}
		return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
	}
}
