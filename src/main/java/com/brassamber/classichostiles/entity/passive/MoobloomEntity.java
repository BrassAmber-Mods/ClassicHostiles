package com.brassamber.classichostiles.entity.passive;

import javax.annotation.Nullable;

import com.brassamber.classichostiles.ClassicHostiles;
import com.brassamber.classichostiles.entity.util.HasTextureVariant;
import com.brassamber.classichostiles.item.CHItems;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.common.Tags;

/**
 * Referenced from {@link MushroomCow}
 * 
 * @author  Xrated_junior
 * @version 1.19.2-1.0.7
 */
@SuppressWarnings("deprecation")
public class MoobloomEntity extends Cow implements Shearable, IForgeShearable, HasTextureVariant {
	private static final EntityDataAccessor<String> DATA_VARIANT_ID = SynchedEntityData.defineId(MoobloomEntity.class, EntityDataSerializers.STRING);
	private static final EntityDataAccessor<Integer> DATA_FLOWER_GROW_ID = SynchedEntityData.defineId(MoobloomEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> DATA_MILKING_ID = SynchedEntityData.defineId(MoobloomEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> DATA_FLORAL_FLUID_ID = SynchedEntityData.defineId(MoobloomEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> DATA_CONVERTING_ID = SynchedEntityData.defineId(MoobloomEntity.class, EntityDataSerializers.INT);
	private static final String DATA_VARIANT_TAG = "Variant";
	private static final String DATA_FLOWER_GROW_TAG = "FlowerGrowTime";
	private static final String DATA_MILKING_TAG = "MilkingInterval";
	private static final String DATA_FLORAL_FLUID_TAG = "FloralFluid";
	private static final String DATA_CONVERTING_TAG = "ConversionTime";

	public MoobloomEntity(EntityType<? extends MoobloomEntity> moobloomEntity, Level level) {
		super(moobloomEntity, level);
	}

	/*********************************************************** Mob data ********************************************************/

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_VARIANT_ID, MoobloomVariant.DANDELION.getName());
		this.entityData.define(DATA_FLOWER_GROW_ID, 0);
		this.entityData.define(DATA_MILKING_ID, 0);
		this.entityData.define(DATA_FLORAL_FLUID_ID, 0);
		this.entityData.define(DATA_CONVERTING_ID, 0);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compoundTag) {
		super.addAdditionalSaveData(compoundTag);
		compoundTag.putString(DATA_VARIANT_TAG, this.getMoobloomVariant().getName());
		compoundTag.putInt(DATA_FLOWER_GROW_TAG, this.getShearInterval());
		compoundTag.putInt(DATA_MILKING_TAG, this.getMilkInterval());
		compoundTag.putInt(DATA_FLORAL_FLUID_TAG, this.getRemainingFloralFluidTime());
		compoundTag.putInt(DATA_CONVERTING_TAG, this.isConverting() ? this.getConvertionTime() : -1);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compoundTag) {
		super.readAdditionalSaveData(compoundTag);
		this.setMoobloomVariant(MoobloomVariant.getByName(compoundTag.getString(DATA_VARIANT_TAG)));
		this.setShearInterval(compoundTag.getInt(DATA_FLOWER_GROW_TAG));
		this.setMilkInterval(compoundTag.getInt(DATA_MILKING_TAG));
		this.setRemainingFloralFluidTime(compoundTag.getInt(DATA_FLORAL_FLUID_TAG));
		if (compoundTag.contains(DATA_CONVERTING_TAG, Tag.TAG_ANY_NUMERIC) && this.isConverting()) {
			this.startConverting(compoundTag.getInt(DATA_CONVERTING_TAG));
		}
	}

	/*********************************************************** Mob data ********************************************************/

	/**
	 * Runs until entity is removed
	 */
	@Override
	public void aiStep() {
		super.aiStep();
		if (this.isAlive()) {

			// Interval counter for getting Floral Fluid
			if (this.isMilked()) {
				int currentMilkInterval = this.getMilkInterval();
				this.setMilkInterval(currentMilkInterval - 1);
			}

			if (!this.getLevel().isClientSide()) {
				// Keep track of time needed to grow flowers
				if (this.isSheared()) {
					int currentShearInterval = this.getShearInterval();
					this.setShearInterval(currentShearInterval - 1);
				}

				// Keep track of time Floral Fluid is effective
				if (this.hasConsumedFloralFluid()) {
					int remainingTime = this.getRemainingFloralFluidTime();
					this.setRemainingFloralFluidTime(remainingTime - 1);
				}

				//				ClassicHostiles.LOGGER.info(this.isConverting());

				// Keep track of time needed to convert Moobloom variants
				if (this.isConverting()) {
					int timeReduction = this.getConversionReduction();
					this.setConvertionTime(this.getConvertionTime() - timeReduction);
					if (this.getConvertionTime() <= 0) {
						this.finishConversion((ServerLevel) this.level);
					}
				}
			}
		}
	}

	/**
	 * Extra time reduction if flowers are close
	 */
	private int getConversionReduction() {
		int timeReduction = 1;
		// 1% chance
		if (this.random.nextFloat() < 0.01F) {
			int matchingBlockCount = 0;
			int maxMatchingBlockCount = 14;
			BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

			for (int xPos = (int) this.getX() - 2; xPos < (int) this.getX() + 2 && matchingBlockCount < maxMatchingBlockCount; ++xPos) {
				for (int yPos = (int) this.getY() - 2; yPos < (int) this.getY() + 2 && matchingBlockCount < maxMatchingBlockCount; ++yPos) {
					for (int zPos = (int) this.getZ() - 2; zPos < (int) this.getZ() + 2 && matchingBlockCount < maxMatchingBlockCount; ++zPos) {
						BlockState blockstate = this.level.getBlockState(blockpos$mutableblockpos.set(xPos, yPos, zPos));
						if (blockstate.is(BlockTags.FLOWERS)) {
							// 30% chance to gain extra time reduction
							if (this.random.nextFloat() < 0.3F) {
								++timeReduction;
							}

							++matchingBlockCount;
						}
					}
				}
			}
		}

		return timeReduction;
	}

	private void startConverting(int conversionTime) {
		this.setConvertionTime(conversionTime);
		this.setRemainingFloralFluidTime(0);
	}

	//TODO Fix subtitles
	private void finishConversion(ServerLevel serverLevel) {
		((ServerLevel) this.level).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(0.5D), this.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
		this.setMoobloomVariant(MoobloomVariant.getRandomVariant(this.random));
		this.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED);
	}

	/*********************************************************** Player Interaction ********************************************************/

	@Override
	public java.util.List<ItemStack> onSheared(@org.jetbrains.annotations.Nullable Player player, @org.jetbrains.annotations.NotNull ItemStack item, Level world, BlockPos pos, int fortune) {
		this.gameEvent(GameEvent.SHEAR, player);
		return this.getShearedDrops(player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS);
	}

	@Override
	public boolean isShearable(@org.jetbrains.annotations.NotNull ItemStack item, Level world, BlockPos pos) {
		return this.readyForShearing();
	}

	/*********************************************************** Dispenser Interaction ********************************************************/

	@Override
	public void shear(SoundSource soundSource) {
		this.getShearedDrops(soundSource).forEach(droppedItemStack -> {
			this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(1.0D), this.getZ(), droppedItemStack));
		});
	}

	@Override
	public boolean readyForShearing() {
		return this.isAlive() && !this.isBaby() && !this.isSheared() && !this.getMoobloomVariant().equals(MoobloomVariant.TILLED);
	}

	/*********************************************************** Shear Interaction ********************************************************/

	private java.util.List<ItemStack> getShearedDrops(SoundSource soundSource) {
		this.level.playSound((Player) null, this, SoundEvents.MOOSHROOM_SHEAR, soundSource, 1.0F, 1.0F);
		if (!this.level.isClientSide()) {
			// Set interval to 5 minutes
			this.setShearInterval(6_000);

			// Drop between 1 and 3 flowers
			int dropAmount = 1 + this.random.nextInt(3);

			java.util.List<ItemStack> droppedItemStacks = new java.util.ArrayList<>();
			for (int dropCount = 0; dropCount < dropAmount; ++dropCount) {
				droppedItemStacks.add(new ItemStack(this.getMoobloomVariant().getFlowerBlock()));
			}
			return droppedItemStacks;
		}
		return java.util.Collections.emptyList();
	}

	/*********************************************************** Other Interactions ********************************************************/

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack itemStackInHand = player.getItemInHand(hand);

		// Convert to tilled Moobloom after interaction with a hoe
		if (itemStackInHand.is(Tags.Items.TOOLS_HOES) && !this.isBaby() && !this.getMoobloomVariant().equals(MoobloomVariant.TILLED)) {
			this.playSound(SoundEvents.HOE_TILL);
			this.addParticlesAroundSelf(ParticleTypes.CRIMSON_SPORE);
			this.addParticlesAroundSelf(ParticleTypes.POOF, 1);
			this.addParticlesAroundSelf(ParticleTypes.WARPED_SPORE);
			if (!this.getLevel().isClientSide()) {
				this.setMoobloomVariant(MoobloomVariant.TILLED);
			}
			return InteractionResult.sidedSuccess(this.getLevel().isClientSide());
		}

		// Right-clicking with an empty bucket gives the player a Floral Fluid Bucket
		else if (itemStackInHand.getItem().equals(Items.BUCKET)) {
			if (!this.isBaby() && !this.getMoobloomVariant().equals(MoobloomVariant.TILLED) && this.canBeMilked()) {
				this.playSound(SoundEvents.COW_MILK);
				// Set interval to 10 minutes
				this.setMilkInterval(12_000);

				if (!this.getLevel().isClientSide()) {
					ItemStack filledBucket = ItemUtils.createFilledResult(itemStackInHand, player, CHItems.FLORAL_FLUID_BUCKET.get().getDefaultInstance());
					player.setItemInHand(hand, filledBucket);
				}
				return InteractionResult.sidedSuccess(this.getLevel().isClientSide());
			}
			// This disables normal milk from Cows
			return InteractionResult.PASS;
		}

		// Convert to flowering Moobloom after interaction with a Floral Fluid Bucket
		else if (itemStackInHand.getItem().equals(CHItems.FLORAL_FLUID_BUCKET.get()) && this.getMoobloomVariant().equals(MoobloomVariant.TILLED)) {
			this.playSound(SoundEvents.HONEY_DRINK);
			this.addParticlesAroundSelf(ParticleTypes.AMBIENT_ENTITY_EFFECT);
			// Is effective for 1 minute
			this.setRemainingFloralFluidTime(1200);
			return InteractionResult.sidedSuccess(this.getLevel().isClientSide());
		}

		else if (itemStackInHand.getItem().equals(Items.WHEAT_SEEDS) && this.getMoobloomVariant().equals(MoobloomVariant.TILLED) && this.hasConsumedFloralFluid()) {
			this.playSound(SoundEvents.CROP_PLANTED);
			this.addParticlesAroundSelf(ParticleTypes.HAPPY_VILLAGER);

			// Decrease item stack by 1 if player is not creative
			if (!player.getAbilities().instabuild) {
				itemStackInHand.shrink(1);
			}

			// Start converting Moobloom variant
			if (!this.getLevel().isClientSide()) {
				// Conversion time is 2 minutes +/- 1 minute
				this.startConverting(2400 + this.random.nextInt(1200));
			}

			return InteractionResult.sidedSuccess(this.getLevel().isClientSide());
		}

		return super.mobInteract(player, hand);
	}

	protected void addParticlesAroundSelf(ParticleOptions particle, int amount) {
		for (int particles = 0; particles < amount; ++particles) {
			double d0 = this.random.nextGaussian() * 0.02D;
			double d1 = this.random.nextGaussian() * 0.02D;
			double d2 = this.random.nextGaussian() * 0.02D;
			this.level.addParticle(particle, this.getRandomX(1.0D), this.getRandomY() + 1.0D, this.getRandomZ(1.0D), d0, d1, d2);
		}
	}

	protected void addParticlesAroundSelf(ParticleOptions particle) {
		this.addParticlesAroundSelf(particle, 5);
	}

	/*********************************************************** Loot table ********************************************************/

	//TODO
	@Override
	public ResourceLocation getDefaultLootTable() {
		if (this.isSheared()) {
			return this.getType().getDefaultLootTable();
		} else {
			switch (this.getMoobloomVariant()) {
			default:
				return super.getDefaultLootTable();
			//			case DANDELION:
			//				               return BuiltInLootTables.SHEEP_WHITE;
			//	            case ORANGE:
			//	               return BuiltInLootTables.SHEEP_ORANGE;
			//	            case MAGENTA:
			//	               return BuiltInLootTables.SHEEP_MAGENTA;
			//	            case LIGHT_BLUE:
			//	               return BuiltInLootTables.SHEEP_LIGHT_BLUE;
			//	            case YELLOW:
			//	               return BuiltInLootTables.SHEEP_YELLOW;
			//	            case LIME:
			//	               return BuiltInLootTables.SHEEP_LIME;
			//	            case PINK:
			//	               return BuiltInLootTables.SHEEP_PINK;
			//	            case GRAY:
			//	               return BuiltInLootTables.SHEEP_GRAY;
			//	            case LIGHT_GRAY:
			//	               return BuiltInLootTables.SHEEP_LIGHT_GRAY;
			//	            case CYAN:
			//	               return BuiltInLootTables.SHEEP_CYAN;
			//	            case PURPLE:
			//	               return BuiltInLootTables.SHEEP_PURPLE;
			//	            case BLUE:
			//	               return BuiltInLootTables.SHEEP_BLUE;
			//	            case BROWN:
			//	               return BuiltInLootTables.SHEEP_BROWN;
			//	            case GREEN:
			//	               return BuiltInLootTables.SHEEP_GREEN;
			//	            case RED:
			//	               return BuiltInLootTables.SHEEP_RED;
			//	            case BLACK:
			//	               return BuiltInLootTables.SHEEP_BLACK;
			}
		}
	}

	/*********************************************************** Spawning ********************************************************/

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
		this.setMoobloomVariant(MoobloomVariant.getRandomVariant(world.getRandom()));
		return super.finalizeSpawn(world, difficulty, mobSpawnType, spawnGroupData, compoundTag);
	}

	/*********************************************************** Baby ********************************************************/

	@Override
	public boolean canMate(Animal p_27569_) {
		return false;
	}

	@Override
	public boolean canFallInLove() {
		return false;
	}

	/*********************************************************** Milk data ********************************************************/

	public int getMilkInterval() {
		return this.entityData.get(DATA_MILKING_ID);
	}

	public void setMilkInterval(int interval) {
		this.entityData.set(DATA_MILKING_ID, interval);
	}

	public boolean isMilked() {
		return this.getMilkInterval() > 0;
	}

	public boolean canBeMilked() {
		return this.getMilkInterval() <= 0;
	}

	/*********************************************************** Shear data ********************************************************/

	public int getShearInterval() {
		return this.entityData.get(DATA_FLOWER_GROW_ID);
	}

	public void setShearInterval(int interval) {
		this.entityData.set(DATA_FLOWER_GROW_ID, interval);
	}

	public boolean isSheared() {
		return this.getShearInterval() > 0;
	}

	/*********************************************************** Moobloom variant conversion data ********************************************************/

	public void setConvertionTime(int conversionTime) {
		this.entityData.set(DATA_CONVERTING_ID, conversionTime);
	}

	public int getConvertionTime() {
		return this.entityData.get(DATA_CONVERTING_ID);
	}

	public boolean isConverting() {
		return this.getConvertionTime() > 0;
	}

	public int getRemainingFloralFluidTime() {
		return this.entityData.get(DATA_FLORAL_FLUID_ID);
	}

	public void setRemainingFloralFluidTime(int remainingTime) {
		this.entityData.set(DATA_FLORAL_FLUID_ID, remainingTime);
	}

	public boolean hasConsumedFloralFluid() {
		return this.getRemainingFloralFluidTime() > 0;
	}

	/*********************************************************** Moobloom variant data ********************************************************/

	public MoobloomVariant getMoobloomVariant() {
		return MoobloomVariant.getByName(this.entityData.get(DATA_VARIANT_ID));
	}

	public void setMoobloomVariant(MoobloomVariant variant) {
		this.setVariant(variant.getName());
	}

	@Override
	public String getVariant() {
		return this.getMoobloomVariant().getName();
	}

	@Override
	public void setVariant(String variant) {
		this.entityData.set(DATA_VARIANT_ID, variant);
	}

	public static enum MoobloomVariant {
		// Small flowers
		DANDELION("dandelion", Blocks.DANDELION),
		POPPY("poppy", Blocks.POPPY),
		BLUE_ORCHID("blue_orchid", Blocks.BLUE_ORCHID),
		ALLIUM("allium", Blocks.ALLIUM),
		AZURE_BLUET("azure_bluet", Blocks.AZURE_BLUET),
		RED_TULIP("red_tulip", Blocks.RED_TULIP),
		ORANGE_TULIP("orange_tulip", Blocks.ORANGE_TULIP),
		WHITE_TULIP("white_tulip", Blocks.WHITE_TULIP),
		PINK_TULIP("pink_tulip", Blocks.PINK_TULIP),
		OXEYE_DAISY("oxeye_daisy", Blocks.OXEYE_DAISY),
		CORNFLOWER("cornflower", Blocks.CORNFLOWER),
		LILY_OF_THE_VALLEY("lily_of_the_valley", Blocks.LILY_OF_THE_VALLEY),
		// Big flowers
		SUNFLOWER("sunflower", Blocks.SUNFLOWER),
		LILAC("lilac", Blocks.LILAC),
		ROSE_BUSH("rose_bush", Blocks.ROSE_BUSH),
		PEONY("peony", Blocks.PEONY),
		// No flowers
		TILLED("tilled", Blocks.AIR);

		final String variantName;
		final Block flowerBlock;

		private static final MoobloomVariant[] ALL_VARIANTS = values();

		private MoobloomVariant(String name, Block blockState) {
			this.variantName = name;
			this.flowerBlock = blockState;
		}

		public String getName() {
			return this.variantName;
		}

		public Block getFlowerBlock() {
			return this.flowerBlock;
		}

		public static MoobloomVariant getRandomVariant(RandomSource random) {
			MoobloomVariant randomMoobloomVariant = Util.getRandom(ALL_VARIANTS, random);
			if (randomMoobloomVariant.equals(MoobloomVariant.TILLED)) {
				return getRandomVariant(random);
			}
			return randomMoobloomVariant;
		}

		static MoobloomVariant getByName(String name) {
			for (MoobloomVariant moobloomVariant : ALL_VARIANTS) {
				if (moobloomVariant.getName().equals(name)) {
					return moobloomVariant;
				}
			}
			ClassicHostiles.LOGGER.error("Couldn't find Moobloom variant for: {}.", name);
			return TILLED;
		}

		static MoobloomVariant getByItem(Item item) {
			for (MoobloomVariant moobloomVariant : ALL_VARIANTS) {
				if (moobloomVariant.getFlowerBlock().asItem().equals(item)) {
					return moobloomVariant;
				}
			}
			ClassicHostiles.LOGGER.error("Couldn't find Moobloom variant for: {}.", item.toString());
			return TILLED;
		}

		static boolean canAcceptFlower(Item item) {
			for (MoobloomVariant moobloomVariant : ALL_VARIANTS) {
				if (moobloomVariant.getFlowerBlock().asItem().equals(item)) {
					return true;
				}
			}
			return false;
		}
	}
}
