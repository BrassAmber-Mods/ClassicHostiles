package com.brassamber.classichostiles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib3.GeckoLib;

@Mod(value = ClassicHostiles.MOD_ID)
public class ClassicHostiles {

	public static final String MOD_ID = "classichostiles";
	public static Logger logger = LogManager.getLogger(MOD_ID);

	public ClassicHostiles() {
		GeckoLib.initialize();

		MinecraftForge.EVENT_BUS.register(this);
	}
}
