package com.brassamber.classichostiles.tags;

import com.brassamber.classichostiles.ClassicHostiles;

import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.7
 */
public class CHTags {

	/**
	 * Classic Hostiles BiomeTags
	 */
	public static class Biomes {
		public static final TagKey<Biome> SPAWNS_BOARS = biomeTag("spawns_boars");
		public static final TagKey<Biome> SPAWNS_PLAINS_FOXES = biomeTag("spawns_plains_foxes");
		public static final TagKey<Biome> SPAWNS_BLACK_BEARS = biomeTag("spawns_black_bears");
		public static final TagKey<Biome> SPAWNS_BROWN_BEARS = biomeTag("spawns_brown_bears");
		public static final TagKey<Biome> SPAWNS_MOOBLOOMS = biomeTag("spawns_mooblooms");

		private static TagKey<Biome> biomeTag(String tagName) {
			return TagKey.create(Registry.BIOME_REGISTRY, ClassicHostiles.locate(tagName));
		}
	}
}
