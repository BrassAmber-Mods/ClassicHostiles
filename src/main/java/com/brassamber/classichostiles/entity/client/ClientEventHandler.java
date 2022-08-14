package com.brassamber.classichostiles.entity.client;

import com.brassamber.classichostiles.ClassicHostiles;
import com.brassamber.classichostiles.entity.CHEntityTypes;
import com.brassamber.classichostiles.entity.client.renderer.hostile.BoarRenderer;
import com.brassamber.classichostiles.entity.client.renderer.neutral.BearRenderer;
import com.brassamber.classichostiles.entity.client.renderer.neutral.PlainsFoxRenderer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.3
 */
@Mod.EventBusSubscriber(modid = ClassicHostiles.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {

	/**
	 * Register EntityRenderers
	 */
	@SubscribeEvent
	public static void registerEntityRenders(final EntityRenderersEvent.RegisterRenderers event) {
		ClassicHostiles.LOGGER.debug("Register Entity Renderers");
		/*********************************************************** Hostile ********************************************************/
		event.registerEntityRenderer(CHEntityTypes.BOAR.get(), BoarRenderer::new);

		/*********************************************************** Neutral ********************************************************/
		event.registerEntityRenderer(CHEntityTypes.PLAINS_FOX.get(), PlainsFoxRenderer::new);
		event.registerEntityRenderer(CHEntityTypes.BEAR.get(), BearRenderer::new);
	}
}
