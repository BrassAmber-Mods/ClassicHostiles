package com.brassamber.classichostiles.entity.client.model.hostile;

import java.util.Map;

import javax.annotation.Nullable;

import com.brassamber.classichostiles.ClassicHostiles;
import com.brassamber.classichostiles.entity.client.renderer.hostile.BigCatRenderer;
import com.brassamber.classichostiles.entity.hostile.BigCatEntity;
import com.brassamber.classichostiles.entity.hostile.BigCatEntity.BigCatVariant;
import com.google.common.collect.Maps;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Cat;
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
public class BigCatModel extends AnimatedGeoModel<BigCatEntity> {
    private static final Map<BigCatVariant, ResourceLocation> BIG_CAT_TEXTURES = Util.make(Maps.newHashMap(), (texture) -> {
        texture.put(BigCatVariant.JUNGLE, ClassicHostiles.locate("textures/entity/big_cat/big_cat_jungle.png"));
        texture.put(BigCatVariant.PANTHER, ClassicHostiles.locate("textures/entity/big_cat/big_cat_panther.png"));
        texture.put(BigCatVariant.PLAINS, ClassicHostiles.locate("textures/entity/big_cat/big_cat_plains.png"));
        texture.put(BigCatVariant.SNOW, ClassicHostiles.locate("textures/entity/big_cat/big_cat_snow.png"));
        texture.put(BigCatVariant.TIGER_JUNGLE, ClassicHostiles.locate("textures/entity/big_cat/big_cat_tiger_jungle.png"));
        texture.put(BigCatVariant.TIGER_SNOW, ClassicHostiles.locate("textures/entity/big_cat/big_cat_tiger_snow.png"));
    });

    private boolean initialized = false;
    private IBone cat;
    private IBone body;
    private IBone head;
    private IBone earright;
    private IBone earleft;
    private IBone tail;
    private IBone legleftfront;
    private IBone legleftback;
    private IBone legrightfront;
    private IBone legrightback;

    @Override
    public GeoModel getModel(ResourceLocation location) {
        GeoModel geoModel = super.getModel(location);
        if (!initialized) {
            this.cat = this.getBone("cat");
            this.body = this.getBone("body");
            this.head = this.getBone("head");
            this.earright = this.getBone("earright");
            this.earleft = this.getBone("earleft");
            this.tail = this.getBone("tail");
            this.legleftfront = this.getBone("legleftfront");
            this.legleftback = this.getBone("legleftback");
            this.legrightfront = this.getBone("legrightfront");
            this.legrightback = this.getBone("legrightback");
            this.initialized = true;
        }
        return geoModel;
    }

    @Override
    public ResourceLocation getModelResource(BigCatEntity entityIn) {
        return ClassicHostiles.locate("geo/big_cat.geo.json");
    }

    @Override
    public ResourceLocation getAnimationResource(BigCatEntity entityIn) {
        return ClassicHostiles.locate("animations/big_cat.animation.json");
    }

    /**
     * Texture will change with variants in {@link BigCatRenderer}
     */
    @Override
    public ResourceLocation getTextureResource(BigCatEntity entityIn) {
        return BIG_CAT_TEXTURES.get(entityIn.getBigCatVariant());
    }


    protected void performWalkingAnimations(BigCatEntity entity, float tick) {
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
        this.cat.setRotationX(Mth.cos(f5 * 0.0F) * 0.0F * f8);
        this.body.setRotationX(Mth.cos(f5 * 0.0F) * 0.0F * f8);
        this.head.setRotationX(Mth.cos(f5 * 0.0F) * 0.0F * f8);
        this.earright.setRotationX(Mth.cos(f5 * 0.2F) * 0.3F * f8);
        this.earleft.setRotationX(Mth.cos(f5 * 0.2F) * 0.3F * f8);
        this.tail.setRotationZ(Mth.cos(f5 * 0.7F) * 0.5F * f8);
        this.legleftfront.setRotationZ(Mth.cos(f5 * 0.6662F + (float) Math.PI) * 1.4F * f8);
        this.legleftback.setRotationZ(Mth.cos(f5 * 0.6662F) * 1.4F * f8);
        this.legrightfront.setRotationZ(Mth.cos(f5 * 0.6662F) * 1.4F * f8);
        this.legrightback.setRotationZ(Mth.cos(f5 * 0.6662F + (float) Math.PI) * 1.4F * f8);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setLivingAnimations(BigCatEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
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
