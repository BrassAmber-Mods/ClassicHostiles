package com.brassamber.classichostiles.event;

import com.brassamber.classichostiles.entity.CHEntityTypes;
import com.brassamber.classichostiles.entity.neutral.BearEntity;
import com.brassamber.classichostiles.entity.neutral.PlainsFoxEntity;
import com.brassamber.classichostiles.tags.CHTags;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.10
 */
public class CHSpawnEvents {

	@SubscribeEvent
	public void onEntityJoinWorld(final LivingSpawnEvent.SpecialSpawn event) {
		Mob mobEntity = event.getEntity();

		// Replace Polar Bears with Bears
		if (mobEntity instanceof PolarBear polarBear && !event.getLevel().isClientSide()) {
			ServerLevel level = (ServerLevel) polarBear.getLevel();
			Holder<Biome> holder = level.getBiome(polarBear.blockPosition());

			if (holder.is(CHTags.Biomes.SPAWNS_BLACK_BEARS) || holder.is(CHTags.Biomes.SPAWNS_BROWN_BEARS)) {
				BearEntity bear = CHEntityTypes.BEAR.get().create(level);
				this.replaceMob(polarBear, bear, level, event);
			}
		}

		// Replace Foxes with Plains Foxes
		if (mobEntity instanceof Fox fox && !event.getLevel().isClientSide()) {
			ServerLevel level = (ServerLevel) fox.getLevel();
			Holder<Biome> holder = level.getBiome(fox.blockPosition());

			if (holder.is(CHTags.Biomes.SPAWNS_PLAINS_FOXES)) {
				PlainsFoxEntity plainsFox = CHEntityTypes.PLAINS_FOX.get().create(level);
				this.replaceMob(fox, plainsFox, level, event);
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
	

	private <M extends Mob> void replaceMob(M vanillaMob, M replacementMob, ServerLevel level, LivingSpawnEvent.SpecialSpawn event) {
		// Stop Vanilla Mob spawning
		event.setCanceled(true);

		// Spawn Classic Hostiles Mob
		replacementMob.copyPosition(vanillaMob);
		replacementMob.finalizeSpawn(level, level.getCurrentDifficultyAt(replacementMob.blockPosition()), event.getSpawnReason(), null, null);
		level.addFreshEntity(replacementMob);
	}
}
