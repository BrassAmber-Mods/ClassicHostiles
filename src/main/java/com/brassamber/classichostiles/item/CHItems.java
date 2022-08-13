package com.brassamber.classichostiles.item;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.brassamber.classichostiles.ClassicHostiles;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.0
 */
public class CHItems {
	public static final DeferredRegister<Item> DEFERRED_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ClassicHostiles.MOD_ID);

	/**
	 * Helper method for registering all Items
	 */
	public static <I extends Item> RegistryObject<Item> registerItem(@Nonnull String registryName, Supplier<I> item) {
		return DEFERRED_ITEMS.register(registryName, item);
	}
}
