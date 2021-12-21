package de.dertoaster.classichostiles.client.init;

import de.dertoaster.classichostiles.ClassicHostilesMod;
import de.dertoaster.classichostiles.client.renderer.entity.FoxEntityRenderer;
import de.dertoaster.classichostiles.init.CHEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ClassicHostilesMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(CHEntities.FOX.get(), FoxEntityRenderer::new);
	}
	
}
