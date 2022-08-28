package com.brassamber.classichostiles.entity.neutral;

import java.util.List;

import javax.annotation.Nullable;

import com.brassamber.classichostiles.ClassicHostiles;
import com.brassamber.classichostiles.entity.CHEntityTypes;
import com.brassamber.classichostiles.entity.util.HasTextureVariant;
import com.brassamber.classichostiles.world.MobVariantSpawnData;
import com.google.common.collect.Lists;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
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
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;

/**
 * TODO Fix sound subtitles
 * 
 * @author  Xrated_junior
 * @version 1.19.2-1.0.9
 */
public class BearEntity extends PolarBear implements HasTextureVariant {
	private static final EntityDataAccessor<String> DATA_VARIANT_ID = SynchedEntityData.defineId(BearEntity.class, EntityDataSerializers.STRING);
	private static final String DATA_VARIANT_TAG = "Variant";

	public BearEntity(EntityType<? extends PolarBear> bearEntity, Level level) {
		super(bearEntity, level);
	}

	@Override
	public ResourceLocation getDefaultLootTable() {
		switch (this.getBearVariant()) {
		default:
		case BLACK:
			return ClassicHostiles.locate("entities/bear_black");
		case BROWN:
			return ClassicHostiles.locate("entities/bear_brown");
		}
	}

	/*********************************************************** Mob data ********************************************************/

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_VARIANT_ID, BearVariant.BLACK.getName());
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compoundTag) {
		super.addAdditionalSaveData(compoundTag);
		compoundTag.putString(DATA_VARIANT_TAG, this.getVariant());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compoundTag) {
		super.readAdditionalSaveData(compoundTag);
		this.setVariant(compoundTag.getString(DATA_VARIANT_TAG));
	}

	/*********************************************************** Spawning ********************************************************/

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
		String bearVariant;
		if (spawnGroupData instanceof BearGroupData) {
			bearVariant = ((BearGroupData) spawnGroupData).getGroupVariant();
		}
		// Set Bear variant according to SpawnData
		else {
			Holder<Biome> biome = world.getBiome(this.blockPosition());
			WeightedRandomList<SpawnerData> spawnlist = biome.value().getMobSettings().getMobs(this.getType().getCategory());
			if (!spawnlist.isEmpty()) {
				List<MobVariantSpawnData> bearVariantsInBiome = Lists.newArrayList();
				for (SpawnerData spawnerData : spawnlist.unwrap()) {
					if (spawnerData instanceof MobVariantSpawnData variantData) {
						if (variantData.type.equals(CHEntityTypes.BEAR.get())) {
							bearVariantsInBiome.add(variantData);
						}
					}
				}

				if (!bearVariantsInBiome.isEmpty()) {
					WeightedRandomList<MobVariantSpawnData> bearSpawnList = WeightedRandomList.create(bearVariantsInBiome);
					bearVariant = bearSpawnList.getRandom(this.getRandom()).get().getVariantName();
					spawnGroupData = new BearGroupData(bearVariant);
					this.setVariant(bearVariant);
					return super.finalizeSpawn(world, difficulty, mobSpawnType, spawnGroupData, compoundTag);
				}
			}

			ClassicHostiles.LOGGER.error("Couldn't find matching Bear variant for Biome: \"{}\". Picked a random Bear variant.", biome.unwrapKey().get().location());

			bearVariant = BearVariant.getRandomVariant(world.getRandom()).getName();
			spawnGroupData = new BearGroupData(bearVariant);
			this.setVariant(bearVariant);
		}
		return super.finalizeSpawn(world, difficulty, mobSpawnType, spawnGroupData, compoundTag);
	}

	/**
	 * TODO Tweak for spawning
	 */
	public static boolean checkBearSpawnRules(EntityType<BearEntity> p_218250_, LevelAccessor p_218251_, MobSpawnType p_218252_, BlockPos p_218253_, RandomSource p_218254_) {
		return checkAnimalSpawnRules(p_218250_, p_218251_, p_218252_, p_218253_, p_218254_);
		//		Holder<Biome> holder = p_218251_.getBiome(p_218253_);
		//		if (!holder.is(BiomeTags.POLAR_BEARS_SPAWN_ON_ALTERNATE_BLOCKS)) {
		//			return checkAnimalSpawnRules(p_218250_, p_218251_, p_218252_, p_218253_, p_218254_);
		//		} else {
		//			return isBrightEnoughToSpawn(p_218251_, p_218253_) && p_218251_.getBlockState(p_218253_.below()).is(BlockTags.POLAR_BEARS_SPAWNABLE_ON_ALTERNATE);
		//		}
	}

	/*********************************************************** Baby ********************************************************/

	@Override
	public BearEntity getBreedOffspring(ServerLevel serverLevel, AgeableMob entityIn) {
		BearEntity babyBoar = CHEntityTypes.BEAR.get().create(serverLevel);
		babyBoar.setVariant(this.random.nextBoolean() ? this.getVariant() : babyBoar.getVariant());
		return babyBoar;
	}

	/*********************************************************** Variants ********************************************************/

	public BearVariant getBearVariant() {
		return BearVariant.getByName(this.entityData.get(DATA_VARIANT_ID));
	}

	@Override
	public String getVariant() {
		return this.getBearVariant().getName();
	}

	public void setBearVariant(BearVariant variant) {
		this.setVariant(variant.getName());
	}

	@Override
	public void setVariant(String variantName) {
		this.entityData.set(DATA_VARIANT_ID, variantName);
	}

	public static enum BearVariant {
		BLACK("black"),
		BROWN("brown");

		final String variantName;

		private static final BearVariant[] ALL_VARIANTS = values();

		private BearVariant(String name) {
			this.variantName = name;
		}

		public String getName() {
			return this.variantName;
		}

		public static BearVariant getRandomVariant(RandomSource random) {
			return Util.getRandom(ALL_VARIANTS, random);
		}

		static BearVariant getByName(String name) {
			for (BearVariant bearVariant : ALL_VARIANTS) {
				if (bearVariant.getName().equals(name.toLowerCase())) {
					return bearVariant;
				}
			}
			ClassicHostiles.LOGGER.error("Couldn't find Bear variant for: {}.", name);
			return BLACK;
		}
	}

	/*********************************************************** Extra Classes ********************************************************/

	/**
	 * Referenced from {@link Llama}
	 */
	static class BearGroupData extends AgeableMob.AgeableMobGroupData {
		private final String variant;

		BearGroupData(String variant) {
			super(true);
			this.variant = variant;
		}

		public String getGroupVariant() {
			return this.variant;
		}
	}
}
