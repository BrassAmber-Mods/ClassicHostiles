package com.brassamber.classichostiles.entity.neutral;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

/**
 * Replace all foxes spawning in Plains with this variant.
 * 
 * @author  Xrated_junior
 * @version 1.19.2-1.0.1
 */
public class PlainsFoxEntity extends Fox {

	public PlainsFoxEntity(EntityType<? extends Fox> foxEntity, Level level) {
		super(foxEntity, level);
	}

	/**
	 * Use Vanilla Fox loot table.
	 */
	@Override
	public ResourceLocation getDefaultLootTable() {
		return new ResourceLocation("minecraft", "entities/fox");
	}

	@SuppressWarnings("unchecked")
	public static boolean checkPlainsFoxSpawnRules(EntityType<? extends Fox> entityIn, LevelAccessor world, MobSpawnType spawnReason, BlockPos spawnPos, RandomSource random) {
		return checkFoxSpawnRules((EntityType<Fox>) entityIn, world, spawnReason, spawnPos, random);
	}
}
