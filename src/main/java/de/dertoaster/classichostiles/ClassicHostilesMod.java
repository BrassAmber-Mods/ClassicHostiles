package de.dertoaster.classichostiles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.dertoaster.classichostiles.init.CHEntities;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import software.bernie.geckolib3.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ClassicHostilesMod.MODID)
public class ClassicHostilesMod {
	
	public static final String MODID = "classichostiles";
	
	// Directly reference a log4j logger.
	public static final Logger LOGGER = LogManager.getLogger();
	
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(
	    new ResourceLocation(MODID, "main-network-channel"),
	    () -> PROTOCOL_VERSION,
	    PROTOCOL_VERSION::equals,
	    PROTOCOL_VERSION::equals
	);

	public ClassicHostilesMod() {
		//Initialize Geckolib
		GeckoLib.initialize();
		
		IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
		
		CHEntities.registerToEventBus(modbus);
		
		MinecraftForge.EVENT_BUS.register(this);
	}
}
