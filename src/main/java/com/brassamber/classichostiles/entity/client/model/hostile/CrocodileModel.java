package com.brassamber.classichostiles.entity.client.model.hostile;

import java.util.Map;

import javax.annotation.Nullable;

import com.brassamber.classichostiles.ClassicHostiles;
import com.brassamber.classichostiles.entity.client.renderer.hostile.CrocodileRenderer;
import com.brassamber.classichostiles.entity.hostile.CrocodileEntity;
import com.brassamber.classichostiles.entity.hostile.CrocodileEntity.CrocodileVariant;
import com.google.common.collect.Maps;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
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
 * @version 1.19.2-1.0.15
 */
@OnlyIn(Dist.CLIENT)
public class CrocodileModel extends AnimatedGeoModel<CrocodileEntity> {
    private static final Map<CrocodileVariant, ResourceLocation> CROCODILE_TEXTURES = Util.make(Maps.newHashMap(), (texture) -> {
        texture.put(CrocodileVariant.CROCODILE, ClassicHostiles.locate("textures/entity/crocodile/crocodile.png"));
        texture.put(CrocodileVariant.BLACK, ClassicHostiles.locate("textures/entity/crocodile/crocodile_black.png"));
        texture.put(CrocodileVariant.BROWN, ClassicHostiles.locate("textures/entity/crocodile/crocodile_brown.png"));
        texture.put(CrocodileVariant.TAN, ClassicHostiles.locate("textures/entity/crocodile/crocodile_tan.png"));
    });

    private boolean initialized = false;
    private IBone crocodile;
    private IBone body;
    private IBone head;
    private IBone lower_jaw;
    private IBone upper_jaw;
    private IBone tail_base;
    private IBone tail_middle;
    private IBone tail_end;
    private IBone tail_tip;
    private IBone leg_left_back;
    private IBone leg_left_front;
    private IBone leg_right_back;
    private IBone leg_right_front;

    @Override
    public GeoModel getModel(ResourceLocation location) {
        GeoModel geoModel = super.getModel(location);
        if (!initialized) {
            this.crocodile = this.getBone("crocodile");
            this.body = this.getBone("body");
            this.head = this.getBone("head");
            this.lower_jaw = this.getBone("lower_jaw");
            this.upper_jaw = this.getBone("upper_jaw");
            this.tail_base = this.getBone("tail_base");
            this.tail_middle = this.getBone("tail_middle");
            this.tail_end = this.getBone("tail_end");
            this.tail_tip = this.getBone("tail_tip");
            this.leg_left_back = this.getBone("leg_left_back");
            this.leg_left_front = this.getBone("leg_left_front");
            this.leg_right_back = this.getBone("leg_right_back");
            this.leg_right_front = this.getBone("leg_right_front");
            this.initialized = true;
        }
        return geoModel;
    }

    @Override
    public ResourceLocation getModelResource(CrocodileEntity entityIn) {
        return ClassicHostiles.locate("geo/crocodile.geo.json");
    }

    @Override
    public ResourceLocation getAnimationResource(CrocodileEntity entityIn) {
        return ClassicHostiles.locate("animations/Crocodile.animation.json");
    }

    /**
     * Texture will change with variants in {@link CrocodileRenderer}
     */
    @Override
    public ResourceLocation getTextureResource(CrocodileEntity entityIn) {
        return CROCODILE_TEXTURES.get(entityIn.getCrocodileVariant());
    }


    protected void performWalkingAnimations(CrocodileEntity entity, float tick) {
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

        // ANIMATIONS NEED TO BE FIXED!!
        this.crocodile.setRotationX(Mth.cos(f5 * 0.0F) * 0.0F * f8);
        this.body.setRotationX(Mth.cos(f5 * 0.0F + (float) Math.PI) * 0.0F * f8);
        this.head.setRotationX(Mth.cos(f5 * 0.0F + (float) Math.PI) * 0.0F * f8);
        this.lower_jaw.setRotationX(Mth.cos(f5 * 0.0F) * 0.0F * f8);
        this.upper_jaw.setRotationX(Mth.cos(f5 * 0.0F) * 0.0F * f8);
        this.tail_base.setRotationX(Mth.cos(f5 * 0.0F + (float) Math.PI) * 0.0F * f8);
        this.tail_middle.setRotationX(Mth.cos(f5 * 0.0F + (float) Math.PI) * 0.0F * f8);
        this.tail_end.setRotationX(Mth.cos(f5 * 0.0F) * 0.0F * f8);
        this.tail_tip.setRotationX(Mth.cos(f5 * 0.0F) * 0.0F * f8);
        this.leg_left_back.setRotationX(Mth.cos(f5 * 0.0F + (float) Math.PI) * 0.0F * f8);
        this.leg_left_front.setRotationX(Mth.cos(f5 * 0.0F + (float) Math.PI) * 0.0F * f8);
        this.leg_right_back.setRotationX(Mth.cos(f5 * 0.0F + (float) Math.PI) * 0.0F * f8);
        this.leg_right_front.setRotationX(Mth.cos(f5 * 0.0F + (float) Math.PI) * 0.0F * f8);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setLivingAnimations(CrocodileEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
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

        if (entity.isBaby()) {
            this.head.setScaleX(1.8F);
            this.head.setScaleY(1.8F);
            this.head.setScaleZ(1.8F);
            this.head.setPositionY(4.0f);
            this.head.setPositionZ(2.0f);
        }

    }
}
