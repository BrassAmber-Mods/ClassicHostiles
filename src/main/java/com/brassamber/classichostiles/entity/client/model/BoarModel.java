package com.brassamber.classichostiles.entity.client.model;

import javax.annotation.Nullable;

import com.brassamber.classichostiles.ClassicHostiles;
import com.brassamber.classichostiles.entity.client.renderer.BoarRenderer;
import com.brassamber.classichostiles.entity.hostile.BoarEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.2
 */
@OnlyIn(Dist.CLIENT)
public class BoarModel extends AnimatedGeoModel<BoarEntity> {
	private static final ResourceLocation[] BOAR_TEXTURES = new ResourceLocation[] {
		ClassicHostiles.locate("textures/entity/boar/boar_black.png"),
		ClassicHostiles.locate("textures/entity/boar/boar_brown.png"),
		ClassicHostiles.locate("textures/entity/boar/boar_grey.png"),
		ClassicHostiles.locate("textures/entity/boar/boar_tan.png")
	};
	private boolean initialized = false;
	private IBone head;
	private IBone right_leg;
	private IBone left_leg;
	private IBone right_arm;
	private IBone left_arm;

	@Override
	public GeoModel getModel(ResourceLocation location) {
		GeoModel geoModel = super.getModel(location);
		if (!initialized) {
			this.head = this.getBone("head");
			this.right_leg = this.getBone("rightleg");
			this.left_leg = this.getBone("leftleg");
			this.right_arm = this.getBone("rightarm");
			this.left_arm = this.getBone("leftarm");
			this.initialized = true;
		}
		return geoModel;
	}

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

	/**
	 * Referenced from {@link LivingEntityRenderer#render()}
	 */
	protected void performWalkingAnimations(BoarEntity entity, float tick) {
		boolean shouldSit = entity.isPassenger() && (entity.getVehicle() != null && entity.getVehicle().shouldRiderSit());

		float f8 = 0.0F;
		float f5 = 0.0F;
		if (!shouldSit && entity.isAlive()) {
			f8 = (float) Mth.lerp(tick, entity.animationSpeedOld, entity.animationSpeed);
			f5 = entity.animationPosition - entity.animationSpeed * (1.0F - (float) tick);
			if (entity.isBaby()) {
				f5 *= 3.0F;
			}

			if (f8 > 1.0F) {
				f8 = 1.0F;
			}
		}

		// Animations copied from {@link QuadrupedModel}
		this.right_leg.setRotationX(Mth.cos(f5 * 0.6662F) * 1.4F * f8);
		this.left_leg.setRotationX(Mth.cos(f5 * 0.6662F + (float) Math.PI) * 1.4F * f8);
		this.right_arm.setRotationX(Mth.cos(f5 * 0.6662F + (float) Math.PI) * 1.4F * f8);
		this.left_arm.setRotationX(Mth.cos(f5 * 0.6662F) * 1.4F * f8);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setLivingAnimations(BoarEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
		Minecraft minecraft = Minecraft.getInstance();
		float tick = minecraft.getPartialTick();

		float bodyRot = Mth.rotLerp(tick, entity.yBodyRotO, entity.yBodyRot);
		float headRot = Mth.rotLerp(tick, entity.yHeadRotO, entity.yHeadRot);
		float netHeadRot = headRot - bodyRot;

		float headPitch = Mth.lerp(tick, entity.xRotO, entity.getXRot());

		// Make the head rotate where this entity is Looking
		this.head.setRotationY(-netHeadRot * ((float) Math.PI / 180F));
		this.head.setRotationX(-headPitch * ((float) Math.PI / 180F));

		this.performWalkingAnimations(entity, tick);
	}
}
