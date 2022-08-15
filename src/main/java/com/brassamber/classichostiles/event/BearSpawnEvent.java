package com.brassamber.classichostiles.event;

import com.brassamber.classichostiles.entity.CHEntityTypes;
import com.brassamber.classichostiles.entity.neutral.BearEntity;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.6
 */
public class BearSpawnEvent {

	/**
	 * TODO Temporary for testing
	 */
	@SubscribeEvent
	public void onEntityJoinWorld(final LivingSpawnEvent.SpecialSpawn event) {
		if (event.getEntity() instanceof PolarBear polarBear && !event.getLevel().isClientSide()) {
			ServerLevel level = (ServerLevel) polarBear.getLevel();
			Biome biome = level.getBiome(event.getEntity().blockPosition()).value();

			// Replace Polar Bears in warmer Biomes where it can't snow.
			if (!biome.coldEnoughToSnow(polarBear.blockPosition())) {
				event.setCanceled(true);

				BearEntity bear = CHEntityTypes.BEAR.get().create(level);
				bear.copyPosition(polarBear);
				bear.finalizeSpawn(level, level.getCurrentDifficultyAt(bear.blockPosition()), MobSpawnType.CONVERSION, null, null);
				level.addFreshEntity(bear);
			}
		}
	}

	/**
	 * Replace Vanilla Polar Bear Spawn Egg ToolTip
	 */
	@SubscribeEvent
	public void onTooltip(final ItemTooltipEvent event) {
		if (event.getItemStack().getItem().equals(Items.POLAR_BEAR_SPAWN_EGG)) {
			event.getToolTip().set(0, Component.translatable("item.classichostiles.bear_spawn_egg"));
		}
	}
}
