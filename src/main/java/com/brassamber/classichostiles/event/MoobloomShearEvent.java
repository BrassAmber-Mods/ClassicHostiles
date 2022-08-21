package com.brassamber.classichostiles.event;

import com.brassamber.classichostiles.entity.passive.MoobloomEntity;

import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Prevent the shearing animation when the Moobloom can't actually be sheared.
 * 
 * @author  Xrated_junior
 * @version 1.19.2-1.0.7
 */
public class MoobloomShearEvent {

	@SubscribeEvent
	public void onEntityJoinWorld(final PlayerInteractEvent.EntityInteract event) {
		if (event.getTarget() instanceof MoobloomEntity moobloomEntity) {
			if (!moobloomEntity.readyForShearing() && event.getItemStack().is(Tags.Items.SHEARS)) {
				event.setCanceled(true);
			}
		}
	}
}
