package com.brassamber.classichostiles.entity.neutral;

import javax.annotation.Nullable;

import com.brassamber.classichostiles.entity.CHEntityTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.4
 */
public class BearEntity extends PolarBear {
	private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(BearEntity.class, EntityDataSerializers.INT);
	private static final int EXTRA_VARIANTS = 2;

	public BearEntity(EntityType<? extends PolarBear> bearEntity, Level level) {
		super(bearEntity, level);
	}

	/*********************************************************** Mob data ********************************************************/

	@Override
	public void addAdditionalSaveData(CompoundTag compoundTag) {
		super.addAdditionalSaveData(compoundTag);
		compoundTag.putInt("Variant", this.getVariant());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compoundTag) {
		super.readAdditionalSaveData(compoundTag);
		this.setVariant(compoundTag.getInt("Variant"));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_VARIANT_ID, 0);
	}

	/*********************************************************** Spawning ********************************************************/

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
		int variant;
		if (spawnGroupData instanceof BearGroupData) {
			variant = ((BearGroupData) spawnGroupData).variant;
		} else {
			variant = this.random.nextInt(EXTRA_VARIANTS);
			spawnGroupData = new BearGroupData(variant);
		}

		this.setVariant(variant);
		return super.finalizeSpawn(world, difficulty, mobSpawnType, spawnGroupData, compoundTag);
	}

	/**
	 * TODO Tweak for spawning
	 */
	public static boolean checkBearSpawnRules(EntityType<BearEntity> p_218250_, LevelAccessor p_218251_, MobSpawnType p_218252_, BlockPos p_218253_, RandomSource p_218254_) {
		Holder<Biome> holder = p_218251_.getBiome(p_218253_);
		if (!holder.is(BiomeTags.POLAR_BEARS_SPAWN_ON_ALTERNATE_BLOCKS)) {
			return checkAnimalSpawnRules(p_218250_, p_218251_, p_218252_, p_218253_, p_218254_);
		} else {
			return isBrightEnoughToSpawn(p_218251_, p_218253_) && p_218251_.getBlockState(p_218253_.below()).is(BlockTags.POLAR_BEARS_SPAWNABLE_ON_ALTERNATE);
		}
	}

	/*********************************************************** Baby ********************************************************/

	@Override
	public BearEntity getBreedOffspring(ServerLevel serverLevel, AgeableMob entityIn) {
		BearEntity babyBoar = CHEntityTypes.BEAR.get().create(serverLevel);
		babyBoar.setVariant(this.random.nextBoolean() ? this.getVariant() : babyBoar.getVariant());
		return babyBoar;
	}

	/*********************************************************** Variants ********************************************************/

	public int getVariant() {
		return Mth.clamp(this.entityData.get(DATA_VARIANT_ID), 0, EXTRA_VARIANTS - 1);
	}

	/**
	 * 0: Black
	 * 1: Brown
	 */
	public void setVariant(int variant) {
		this.entityData.set(DATA_VARIANT_ID, variant);
	}

	/*********************************************************** Extra Classes ********************************************************/

	/**
	 * Referenced from {@link Llama}
	 */
	static class BearGroupData extends AgeableMob.AgeableMobGroupData {
		public final int variant;

		BearGroupData(int variant) {
			super(true);
			this.variant = variant;
		}
	}
}
