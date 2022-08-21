package com.brassamber.classichostiles.world;

import com.brassamber.classichostiles.ClassicHostiles;
import com.google.common.base.Supplier;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo.BiomeInfo.Builder;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.7
 */
public class CHMobSpawnModifiers {
	public static final DeferredRegister<Codec<? extends BiomeModifier>> DEFERRED_BIOME_MODIFIER_SERIALIZER = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, ClassicHostiles.MOD_ID);

	public static final RegistryObject<Codec<CHMobSpawnBiomeModifier>> ADD_SPAWN_VARIANT_BIOME_MODIFIER_TYPE = registerSpawnModifier("add_spawn_variant", CHMobSpawnBiomeModifier::makeCodec);

	private static <BM extends BiomeModifier> RegistryObject<Codec<BM>> registerSpawnModifier(String name, Supplier<Codec<BM>> sup) {
		return DEFERRED_BIOME_MODIFIER_SERIALIZER.register(name, sup);
	}

	private record CHMobSpawnBiomeModifier(HolderSet<Biome> spawnBiomes, MobVariantSpawnData spawnData) implements BiomeModifier {
		private static final RegistryObject<Codec<? extends BiomeModifier>> SERIALIZER = RegistryObject.create(ClassicHostiles.locate("classichostiles_spawn_serializer"), ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, ClassicHostiles.MOD_ID);

		@Override
		public void modify(Holder<Biome> biome, Phase phase, Builder builder) {
			if (phase == Phase.ADD && this.spawnBiomes.contains(biome)) {
				builder.getMobSpawnSettings().addSpawn(this.spawnData.type.getCategory(), this.spawnData);
			}
		}

		@Override
		public Codec<? extends BiomeModifier> codec() {
			return SERIALIZER.get();
		}

		public static Codec<CHMobSpawnBiomeModifier> makeCodec() {
			return RecordCodecBuilder.create(builder -> builder.group(//
					Biome.LIST_CODEC.fieldOf("biomes").forGetter(CHMobSpawnBiomeModifier::spawnBiomes), //
					MobVariantSpawnData.CODEC.fieldOf("spawn").forGetter(CHMobSpawnBiomeModifier::spawnData)//
			).apply(builder, CHMobSpawnBiomeModifier::new));
		}
	}
}
