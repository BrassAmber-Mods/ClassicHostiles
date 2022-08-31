package com.brassamber.classichostiles.entity.hostile;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.network.NetworkHooks;

/**
 * Referenced from {@link Monster}
 * 
 * @author  Xrated_junior
 * @version 1.19.2-1.0.14
 */
public abstract class AbstractHostileAnimal extends Animal implements Enemy {

	protected AbstractHostileAnimal(EntityType<? extends Animal> entityIn, Level level) {
		super(entityIn, level);
		this.xpReward = XP_REWARD_MEDIUM;
	}

	/*********************************************************** Required hostile attributes ********************************************************/

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.ATTACK_DAMAGE);
	}

	/*********************************************************** Spawn rules ********************************************************/

	public static boolean checkHostileAnimalSpawnRules(EntityType<? extends AbstractHostileAnimal> entityIn, LevelAccessor world, MobSpawnType spawnReason, BlockPos spawnPos, RandomSource random) {
		return world.getDifficulty() != Difficulty.PEACEFUL && checkAnimalSpawnRules(entityIn, world, spawnReason, spawnPos, random);
	}

	/*********************************************************** Tick ********************************************************/

	@Override
	public void aiStep() {
		this.updateAttackTime();
		super.aiStep();
	}

	/**
	 * Because swing time for animals is confusing
	 */
	protected void updateAttackTime() {
		this.updateSwingTime();
	}

	/*********************************************************** Drops ********************************************************/

	/**
	 * Normally disabled for babies
	 */
	@Override
	public boolean shouldDropExperience() {
		return true;
	}

	/**
	 * Normally disabled for babies
	 */
	@Override
	protected boolean shouldDropLoot() {
		return true;
	}

	/*********************************************************** Sounds ********************************************************/

	@Override
	public SoundSource getSoundSource() {
		return SoundSource.HOSTILE;
	}

	@Override
	protected SoundEvent getSwimSound() {
		return SoundEvents.HOSTILE_SWIM;
	}

	@Override
	protected SoundEvent getSwimSplashSound() {
		return SoundEvents.HOSTILE_SPLASH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource p_33034_) {
		return SoundEvents.HOSTILE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.HOSTILE_DEATH;
	}

	@Override
	public LivingEntity.Fallsounds getFallSounds() {
		return new LivingEntity.Fallsounds(SoundEvents.HOSTILE_SMALL_FALL, SoundEvents.HOSTILE_BIG_FALL);
	}

	/*********************************************************** Networking ********************************************************/

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
