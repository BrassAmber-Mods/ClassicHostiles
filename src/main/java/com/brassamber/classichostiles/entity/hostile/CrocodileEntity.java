package com.brassamber.classichostiles.entity.hostile;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.brassamber.classichostiles.ClassicHostiles;
import com.brassamber.classichostiles.entity.CHEntityTypes;
import com.brassamber.classichostiles.entity.util.HasTextureVariant;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.15
 */
public class CrocodileEntity extends AbstractHostileAnimal implements IAnimatable, HasTextureVariant {
    private AnimationFactory factory = new AnimationFactory(this);
    private static final EntityDataAccessor<String> DATA_VARIANT_ID = SynchedEntityData.defineId(CrocodileEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> MOISTNESS_LEVEL = SynchedEntityData.defineId(CrocodileEntity.class, EntityDataSerializers.INT);
    private static final String DATA_VARIANT_TAG = "Variant";
    private static final double BREEDING_RANGE = 8.0D; // Range taken from {@link BreedGoal}
    public static final int TOTAL_AIR_SUPPLY = 4800;
    private static final int TOTAL_MOISTNESS_LEVEL = 2400;
    private static final Predicate<LivingEntity> PARTNER_SELECTOR = (entity) -> {
        return entity instanceof CrocodileEntity && !((CrocodileEntity) entity).isBaby() && ((CrocodileEntity) entity).getAge() == 0;
    };
    private static final TargetingConditions PARTNER_TARGETING = TargetingConditions.forNonCombat().range(BREEDING_RANGE).ignoreLineOfSight().selector(PARTNER_SELECTOR);
    private static final int AFTER_ATTACK_COOLDOWN = 80; // Little cooldown before breeding starts
    private static final int FIND_NEARBY_PARTNER_COOLDOWN = 60; // 10% of love duration
    private static final int BABY_AGE_INCREASE = 60;
    private static final int PARTICLE_DURATION = 20;
    private static final byte GROWTH_PARTICLE_EVENT = 38;
    public int ticksBeforeBreeding;
    private int findLoveInterval;
    private int particleTick;

    public CrocodileEntity(EntityType<? extends AbstractHostileAnimal> crocodileEntity, Level level) {
        super(crocodileEntity, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    /*********************************************************** Mob data ********************************************************/

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_VARIANT_ID, CrocodileVariant.CROCODILE.getName());
        this.entityData.define(MOISTNESS_LEVEL, 2400);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putString(DATA_VARIANT_TAG, this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (CrocodileVariant.isValidCrocodileVariant(compoundTag.getString(DATA_VARIANT_TAG))) {
            this.setVariant(compoundTag.getString(DATA_VARIANT_TAG));
        } else {
            this.setVariant(CrocodileVariant.getRandomVariant(this.getRandom()).getName());
        }
    }

    /*********************************************************** Goals ********************************************************/

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        // this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new CrocodileEntity.CrocodileAttackGoal(this));
        this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(6, new MeleeAttackGoal(this, (double)1.2F, true));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new CrocodileEntity.CrocodileTargetGoal<>(this, Player.class));


    }

    /*********************************************************** Attributes ********************************************************/

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractHostileAnimal.createAttributes().add(Attributes.MAX_HEALTH, 25.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.ATTACK_DAMAGE, 4.0D).add(Attributes.ATTACK_KNOCKBACK);
    }

    protected PathNavigation createNavigation(Level p_28362_) {
        return new WaterBoundPathNavigation(this, p_28362_);
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return this.isBaby() ? sizeIn.height * 0.9F : sizeIn.height * 0.8F;
    }

    public int getMaxAirSupply() {
        return 4800;
    }

    protected int increaseAirSupply(int p_28389_) {
        return this.getMaxAirSupply();
    }

    public int getMoistnessLevel() {
        return this.entityData.get(MOISTNESS_LEVEL);
    }

    public void setMoistnessLevel(int p_28344_) {
        this.entityData.set(MOISTNESS_LEVEL, p_28344_);
    }

    public boolean canBreatheUnderwater() {
        return false;
    }

    protected void handleAirSupply(int p_28326_) {
    }

    /*********************************************************** Spawning ********************************************************/

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        String variant;
        if (spawnGroupData instanceof CrocodileGroupData) {
            variant = ((CrocodileGroupData) spawnGroupData).variant;
        } else {
            variant = CrocodileVariant.getRandomVariant(world.getRandom()).getName();
            spawnGroupData = new CrocodileGroupData(variant);
        }

        this.setVariant(variant);
        return super.finalizeSpawn(world, difficulty, mobSpawnType, spawnGroupData, compoundTag);
    }

    /*********************************************************** Attack Specifications ********************************************************/

    static class CrocodileAttackGoal extends MeleeAttackGoal {
        public CrocodileAttackGoal(CrocodileEntity p_33822_) {
            super(p_33822_, 1.0D, true);
        }

        public boolean canUse() {
            return super.canUse() && !this.mob.isVehicle();
        }

        public boolean canContinueToUse() {
            float f = this.mob.getLightLevelDependentMagicValue();
            if (f >= 0.5F && this.mob.getRandom().nextInt(100) == 0) {
                this.mob.setTarget((LivingEntity)null);
                return false;
            } else {
                return super.canContinueToUse();
            }
        }

        protected double getAttackReachSqr(LivingEntity p_33825_) {
            return (double)(4.0F + p_33825_.getBbWidth());
        }
    }

    static class CrocodileTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        public CrocodileTargetGoal(CrocodileEntity p_33832_, Class<T> p_33833_) {
            super(p_33832_, p_33833_, true);
        }

        public boolean canUse() {
            float f = this.mob.getLightLevelDependentMagicValue();
            return f >= 0.5F ? false : super.canUse();
        }
    }


    /*********************************************************** Killing ********************************************************/

    /**
     * Share the love for killing
     */
    private void shareKillWithOtherAttackingCrocodiles() {
        List<CrocodileEntity> nearbyCrocodiles = this.level.getNearbyEntities(CrocodileEntity.class, PARTNER_TARGETING, this, this.getBoundingBox().inflate(BREEDING_RANGE));
        for (CrocodileEntity Crocodile : nearbyCrocodiles) {
            Crocodile.ticksBeforeBreeding = this.resetAfterAttackCooldown();
            if (this.random.nextInt(10) == 0) {
                Crocodile.setCrocodileKill();
            }
        }
    }

    private void setCrocodileKill() {
        if (this.getAge() == 0) {
            this.setInLove(null);
        } else {
            this.ageUp(BABY_AGE_INCREASE);
            this.particleTick = PARTICLE_DURATION;
        }
    }

    @Override
    public void handleEntityEvent(byte event) {
        if (event == GROWTH_PARTICLE_EVENT) {
            this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
        } else {
            super.handleEntityEvent(event);
        }
    }

    /*********************************************************** Breeding ********************************************************/

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.ticksBeforeBreeding > 0) {
            this.ticksBeforeBreeding--;
        }

        // Show particles for baby after kill
        if (this.particleTick > 0) {
            if ((this.ticksBeforeBreeding) % 4 == 0 && this.isBaby()) {
                if (this.level instanceof ServerLevel serverLevel) {
                    serverLevel.broadcastEntityEvent(this, GROWTH_PARTICLE_EVENT);
                }
            }
            this.particleTick--;
        }

        if (this.findLoveInterval > 0) {
            this.findLoveInterval--;
        }

        if (this.isCrocodileReadyForLove() && this.findLoveInterval <= 0) {
            this.findOrCreateFreePartner();
            this.findLoveInterval = FIND_NEARBY_PARTNER_COOLDOWN;
        }
    }

    /**
     * Check if there's a partner nearby, otherwise create a partner to promote breeding
     */
    private void findOrCreateFreePartner() {
        List<CrocodileEntity> nearbyCrocodiles = this.level.getNearbyEntities(CrocodileEntity.class, PARTNER_TARGETING, this, this.getBoundingBox().inflate(BREEDING_RANGE));
        double distanceToClosestCrocodile = Double.MAX_VALUE;
        CrocodileEntity partner = null;

        // Find another Crocodile in love
        for (CrocodileEntity Crocodile : nearbyCrocodiles) {
            if (this.distanceToSqr(Crocodile) < distanceToClosestCrocodile) {
                if (super.canMate(Crocodile) && this.distanceToSqr(Crocodile) < distanceToClosestCrocodile) {
                    partner = Crocodile;
                    distanceToClosestCrocodile = this.distanceToSqr(Crocodile);
                }
            }
        }

        // Create another Crocodile in love
        if (partner == null) {
            partner = this.level.getNearestEntity(nearbyCrocodiles, PARTNER_TARGETING, this, this.getX(), this.getY(), this.getZ());
            if (partner != null) {
                partner.setInLove(null);
            }
        }
    }

    private int resetAfterAttackCooldown() {
        int half = AFTER_ATTACK_COOLDOWN / 2;
        return this.random.nextInt(half) + half;
    }

    private boolean isCrocodileReadyForLove() {
        return this.isInLove() && this.ticksBeforeBreeding <= 0 && this.getAge() == 0;
    }

    @Override
    public boolean canMate(Animal animal) {
        return super.canMate(animal) && this.ticksBeforeBreeding <= 0;
    }

    /*********************************************************** Baby ********************************************************/

    @Override
    public CrocodileEntity getBreedOffspring(ServerLevel serverLevel, AgeableMob entityIn) {
        CrocodileEntity babyCrocodile = CHEntityTypes.CROCODILE.get().create(serverLevel);
        babyCrocodile.setVariant(this.random.nextBoolean() ? this.getVariant() : babyCrocodile.getVariant());
        return babyCrocodile;
    }

    /**
     * Can't feed Crocodile food
     */
    @Override
    public boolean isFood(ItemStack itemStack) {
        return false;
    }

    /*********************************************************** Geckolib ********************************************************/

    /**
     * DELETED Using code for animations
     */
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<CrocodileEntity>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    /*********************************************************** Sounds ********************************************************/

    //UPDATE SOUNDS FOR CROC

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.CAT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.CAT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CAT_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.CAT_PURR, 0.15F, 1.0F);
    }

    /*********************************************************** Variants ********************************************************/

    public CrocodileVariant getCrocodileVariant() {
        return CrocodileVariant.getByName(this.entityData.get(DATA_VARIANT_ID));
    }

    @Override
    public String getVariant() {
        return this.getCrocodileVariant().getName();
    }

    public void setCrocodileVariant(CrocodileVariant variant) {
        this.setVariant(variant.getName());
    }

    @Override
    public void setVariant(String variantName) {
        this.entityData.set(DATA_VARIANT_ID, variantName);
    }

    public static enum CrocodileVariant {
        CROCODILE ("crocodile"),
        BLACK ("black"),
        BROWN ("brown"),
        TAN ("tan");

        final String variantName;

        private static final CrocodileVariant[] ALL_VARIANTS = values();

        private CrocodileVariant(String name) {
            this.variantName = name;
        }

        public String getName() {
            return this.variantName;
        }

        public static CrocodileVariant getRandomVariant(RandomSource random) {
            return Util.getRandom(ALL_VARIANTS, random);
        }

        public static boolean isValidCrocodileVariant(String name) {
            for (CrocodileVariant CrocodileVariant : ALL_VARIANTS) {
                if (CrocodileVariant.getName().equals(name.toLowerCase())) {
                    return true;
                }
            }
            return false;
        }

        public static CrocodileVariant getByName(String name) {
            for (CrocodileVariant CrocodileVariant : ALL_VARIANTS) {
                if (CrocodileVariant.getName().equals(name.toLowerCase())) {
                    return CrocodileVariant;
                }
            }
            ClassicHostiles.LOGGER.error("Couldn't find Crocodile variant for: {}.", name);
            return CROCODILE;
        }
    }

    /*********************************************************** Extra Classes ********************************************************/

    /**
     * Referenced from {@link Llama}
     */

    public void travel(Vec3 p_28383_) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), p_28383_);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.travel(p_28383_);
        }

    }

    public void tick() {
        super.tick();
        if (this.isNoAi()) {
            this.setAirSupply(this.getMaxAirSupply());
        } else {
            if (this.isInWaterRainOrBubble()) {
                this.setMoistnessLevel(2400);
            } else {
                this.setMoistnessLevel(this.getMoistnessLevel() - 1);
                if (this.getMoistnessLevel() <= 0) {
                    this.hurt(DamageSource.DRY_OUT, 1.0F);
                }

                if (this.onGround) {
                    this.setDeltaMovement(this.getDeltaMovement().add((double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.2F), 0.5D, (double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.2F)));
                    this.setYRot(this.random.nextFloat() * 360.0F);
                    this.onGround = false;
                    this.hasImpulse = true;
                }
            }

            if (this.level.isClientSide && this.isInWater() && this.getDeltaMovement().lengthSqr() > 0.03D) {
                Vec3 vec3 = this.getViewVector(0.0F);
                float f = Mth.cos(this.getYRot() * ((float)Math.PI / 180F)) * 0.3F;
                float f1 = Mth.sin(this.getYRot() * ((float)Math.PI / 180F)) * 0.3F;
                float f2 = 1.2F - this.random.nextFloat() * 0.7F;
            }

        }
    }

    static class CrocodileGroupData extends AgeableMob.AgeableMobGroupData {
        public final String variant;

        CrocodileGroupData(String variant) {
            super(true);
            this.variant = variant;
        }
    }
}
