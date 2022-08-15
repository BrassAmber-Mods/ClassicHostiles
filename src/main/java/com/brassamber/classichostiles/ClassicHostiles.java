package com.brassamber.classichostiles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.brassamber.classichostiles.block.CHBlocks;
import com.brassamber.classichostiles.entity.CHEntityTypes;
import com.brassamber.classichostiles.event.BearSpawnEvent;
import com.brassamber.classichostiles.fluid.CHFluids;
import com.brassamber.classichostiles.item.CHItems;
import com.brassamber.classichostiles.util.CustomDispenserBehavior;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.5
 */
@Mod(value = ClassicHostiles.MOD_ID)
public class ClassicHostiles {

	public static final String MOD_ID = "classichostiles";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public ClassicHostiles() {
		GeckoLib.initialize();

		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		CHItems.DEFERRED_ITEMS.register(modEventBus);
		CHBlocks.DEFERRED_BLOCKS.register(modEventBus);
		CHEntityTypes.DEFERRED_ENTITY_TYPES.register(modEventBus);
		CHFluids.DEFERRED_FLUID_TYPES.register(modEventBus);
		CHFluids.DEFERRED_FLUIDS.register(modEventBus);

		MinecraftForge.EVENT_BUS.register(this);

		modEventBus.addListener(this::commonSetup);
		modEventBus.addListener(this::clientSetup);
	}

	/*
	 * Will run at launch (preInit)
	 */
	private void commonSetup(final FMLCommonSetupEvent event) {
		MinecraftForge.EVENT_BUS.register(new BearSpawnEvent());

		// Register behavior for Items like Arrows and Buckets when fired from a Dispenser.
		CustomDispenserBehavior.init();

		LOGGER.debug("Common Setup method registered.");
	}

	private void clientSetup(final FMLClientSetupEvent event) {
		CHFluids.setFluidRenderTypes(event);
		LOGGER.debug("Client Setup method registered.");
	}

	/**
	 * Gets a resource location for Classic Hostiles
	 */
	public static ResourceLocation locate(String name) {
		return new ResourceLocation(MOD_ID, name);
	}

	public static String find(String key) {
		return new String(MOD_ID + ":" + key);
	}
}
