package de.dertoaster.classichostiles.entity.animal;

import de.dertoaster.classichostiles.entity.IHasTextureVariants;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;

public class FoxEntity extends net.minecraft.entity.passive.FoxEntity implements IHasTextureVariants<FoxEntity> {
	
	protected static final DataParameter<Integer> TEXTURE_VARIANT = EntityDataManager.defineId(FoxEntity.class, DataSerializers.INT);

	public FoxEntity(EntityType<? extends FoxEntity> type, World world) {
		super(type, world);
		//Init variants
		this.callInConstructor();
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		
		this.entityData.define(TEXTURE_VARIANT, 0);
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT nbt) {
		super.addAdditionalSaveData(nbt);
		
		nbt.putInt("ch_variant", this.getTextureIndexFromDataParameter());
	}
	
	@Override
	public void readAdditionalSaveData(CompoundNBT nbt) {
		super.readAdditionalSaveData(nbt);
		
		if(nbt.contains("ch_variant", Constants.NBT.TAG_INT)) {
			this.setTextureIndexOnDataParameter(nbt.getInt("ch_variant"));
		}
	}
	
	/*
	 * 0: Normal orange fox
	 * 1: Snowy fox
	 * 2: Yellow fox
	 */
	@Override
	public void setTextureIndexOnDataParameter(int value) {
		if(this.level.isClientSide()) {
			
		} else {
			this.entityData.set(TEXTURE_VARIANT, value);
		}
	}

	@Override
	public int getTextureIndexFromDataParameter() {
		return this.entityData.get(TEXTURE_VARIANT);
	}

	@Override
	public int getTextureVariationCount() {
		return 3;
	}
	
	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, ILivingEntityData p_213386_4_, CompoundNBT p_213386_5_) {
		ILivingEntityData result = super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
		
		final Biome spawnBiome = this.level.getBiome(this.blockPosition());
		switch(spawnBiome.getBiomeCategory()) {
		case ICY:
			this.setTextureIndexOnDataParameter(1);
			break;
		case FOREST:
		case TAIGA:
			this.setTextureIndexOnDataParameter(0);
			break;
		default:
			this.setTextureIndexOnDataParameter(2);
			break;
		}
		
		return result;
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
}
