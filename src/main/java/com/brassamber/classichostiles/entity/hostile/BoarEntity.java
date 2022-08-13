package com.brassamber.classichostiles.entity.hostile;

import javax.annotation.Nullable;

import com.brassamber.classichostiles.entity.CHEntityTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.0
 */
public class BoarEntity extends AbstractHostileAnimal implements IAnimatable {
	private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(BoarEntity.class, EntityDataSerializers.INT);
	private static final int VARIANTS = 4;
	// TODO Should be configurable
	private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.CARROT, Items.POTATO, Items.BEETROOT);
	private AnimationFactory factory = new AnimationFactory(this);

	public BoarEntity(EntityType<? extends AbstractHostileAnimal> boarEntity, Level level) {
		super(boarEntity, level);
	}

	/*********************************************************** Mob data ********************************************************/

	public void addAdditionalSaveData(CompoundTag compoundTag) {
		super.addAdditionalSaveData(compoundTag);
		compoundTag.putInt("Variant", this.getVariant());
	}

	public void readAdditionalSaveData(CompoundTag compoundTag) {
		super.readAdditionalSaveData(compoundTag);
		this.setVariant(compoundTag.getInt("Variant"));
	}

	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_VARIANT_ID, 0);
	}

	/*********************************************************** Goals ********************************************************/

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
		// TODO Shouldn't be able to tempt I think? Check with Brass
		//		this.goalSelector.addGoal(4, new TemptGoal(this, 1.2D, FOOD_ITEMS, false));
		this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(BoarEntity.class));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	/*********************************************************** Attributes ********************************************************/

	public static AttributeSupplier.Builder createAttributes() {
		return AbstractHostileAnimal.createAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.ATTACK_DAMAGE, 4.0D).add(Attributes.ATTACK_KNOCKBACK);
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
		return this.isBaby() ? sizeIn.height * 0.5F : sizeIn.height * 0.85F;
	}

	/*********************************************************** Attack Specifications ********************************************************/

	/**
	 * Changed to bypass armor and a chance disable shields.
	 * Referenced {@link Mob#doHurtTarget}
	 */
	public boolean doHurtTarget(Entity targetEntity) {
		float attackDamage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
		float attackKnockback = (float) this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
		if (targetEntity instanceof LivingEntity) {
			attackDamage += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity) targetEntity).getMobType());
			attackKnockback += (float) EnchantmentHelper.getKnockbackBonus(this);
		}

		int fireAspectTicks = EnchantmentHelper.getFireAspect(this);
		if (fireAspectTicks > 0) {
			targetEntity.setSecondsOnFire(fireAspectTicks * 4);
		}

		// Hurt the target and bypass armor
		boolean canHurt = targetEntity.hurt(DamageSource.mobAttack(this).bypassArmor(), attackDamage);
		if (canHurt) {
			// Knock back the target
			if (attackKnockback > 0.0F && targetEntity instanceof LivingEntity) {
				((LivingEntity) targetEntity).knockback((double) (attackKnockback * 0.5F), (double) Mth.sin(this.getYRot() * ((float) Math.PI / 180F)), (double) (-Mth.cos(this.getYRot() * ((float) Math.PI / 180F))));
				this.setDeltaMovement(this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
			}

			// Possibility to disable Shield
			if (targetEntity instanceof Player) {
				Player player = (Player) targetEntity;
				this.maybeDisableShield(player, player.isUsingItem() ? player.getUseItem() : ItemStack.EMPTY);
			}

			this.doEnchantDamageEffects(this, targetEntity);
			this.setLastHurtMob(targetEntity);
		}

		return canHurt;
	}

	/**
	 * Reference {@link Mob#maybeDisableShield}
	 */
	private void maybeDisableShield(Player targetPlayer, ItemStack playerItem) {
		if (!playerItem.isEmpty() && playerItem.is(Items.SHIELD)) {
			float chanceToBlock = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
			if (this.random.nextFloat() < chanceToBlock) {
				targetPlayer.getCooldowns().addCooldown(Items.SHIELD, 100);
				this.level.broadcastEntityEvent(targetPlayer, (byte) 30);
			}
		}

	}

	/*********************************************************** Spawning ********************************************************/

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
		int variant;
		if (spawnGroupData instanceof BoarGroupData) {
			variant = ((BoarGroupData) spawnGroupData).variant;
		} else {
			variant = this.random.nextInt(VARIANTS);
			spawnGroupData = new BoarGroupData(variant);
		}

		this.setVariant(variant);
		return super.finalizeSpawn(world, difficulty, mobSpawnType, spawnGroupData, compoundTag);
	}

	/*********************************************************** Baby ********************************************************/

	public BoarEntity getBreedOffspring(ServerLevel serverLevel, AgeableMob entityIn) {
		BoarEntity babyBoar = CHEntityTypes.BOAR.get().create(serverLevel);
		babyBoar.setVariant(this.random.nextBoolean() ? this.getVariant() : babyBoar.getVariant());
		return babyBoar;
	}

	/**
	 * Test for breeding items
	 */
	@Override
	public boolean isFood(ItemStack itemStack) {
		return FOOD_ITEMS.test(itemStack);
	}

	/*********************************************************** Variants ********************************************************/

	public int getVariant() {
		return Mth.clamp(this.entityData.get(DATA_VARIANT_ID), 0, VARIANTS - 1);
	}

	public void setVariant(int variant) {
		this.entityData.set(DATA_VARIANT_ID, variant);
	}

	/*********************************************************** Geckolib ********************************************************/

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.boar.idle", true));
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<BoarEntity>(this, "controller", 0, this::predicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	/*********************************************************** Sounds ********************************************************/

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.PIG_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return SoundEvents.PIG_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.PIG_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(SoundEvents.PIG_STEP, 0.15F, 1.0F);
	}

	/*********************************************************** Extra Classes ********************************************************/

	/**
	 * Referenced from {@link Llama}
	 */
	static class BoarGroupData extends AgeableMob.AgeableMobGroupData {
		public final int variant;

		BoarGroupData(int variant) {
			super(true);
			this.variant = variant;
		}
	}
}
