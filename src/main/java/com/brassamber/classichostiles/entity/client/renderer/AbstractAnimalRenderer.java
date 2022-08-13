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
 * @version 1.19.2-1.0.0
 */
public abstract class AbstractAnimalRenderer<T extends LivingEntity & IAnimatable> extends GeoEntityRenderer<T> {

	public AbstractAnimalRenderer(Context renderManager, AnimatedGeoModel<T> modelProvider, float shadowRadius) {
		super(renderManager, modelProvider);
		this.shadowRadius = shadowRadius;
	}

	/**
	 * Scale the baby
	 */
	@Override
	public RenderType getRenderType(T animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
		if (animatable.isBaby()) {
			stack.scale(0.7f, 0.7f, 0.75f);
		}
		return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
	}
}
