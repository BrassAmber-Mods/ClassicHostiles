package com.brassamber.classichostiles.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Registry;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.7
 */
public class MobVariantSpawnData extends SpawnerData {
	@SuppressWarnings("deprecation")
	public static final Codec<MobVariantSpawnData> CODEC = RecordCodecBuilder.create((builder) -> {
		return builder.group(Registry.ENTITY_TYPE.byNameCodec().fieldOf("type").forGetter((entity) -> {
			return entity.type;
		}), Codec.STRING.fieldOf("variant").forGetter((variant) -> {
			return variant.mobVariant;
		}), Weight.CODEC.fieldOf("weight").forGetter(WeightedEntry.IntrusiveBase::getWeight), Codec.INT.fieldOf("minCount").forGetter((min) -> {
			return min.minCount;
		}), Codec.INT.fieldOf("maxCount").forGetter((max) -> {
			return max.maxCount;
		})).apply(builder, MobVariantSpawnData::new);
	});

	public final String mobVariant;

	public MobVariantSpawnData(EntityType<?> entity, String variant, Weight weight, int minCount, int maxCount) {
		super(entity, weight, minCount, maxCount);
		this.mobVariant = variant;
	}

	public String getVariantName() {
		return this.mobVariant;
	}
}
