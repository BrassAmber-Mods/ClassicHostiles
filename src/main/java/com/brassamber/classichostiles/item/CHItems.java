package com.brassamber.classichostiles.item;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.brassamber.classichostiles.ClassicHostiles;
import com.brassamber.classichostiles.fluid.CHFluids;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.5
 */
public class CHItems {
	public static final DeferredRegister<Item> DEFERRED_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ClassicHostiles.MOD_ID);

	public static final RegistryObject<Item> FLORAL_FLUID_BUCKET = registerItem("floral_fluid_bucket", () -> new BucketItem(CHFluids.FLORAL_FLUID, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));

	/**
	 * Helper method for registering all Items
	 */
	public static <I extends Item> RegistryObject<Item> registerItem(@Nonnull String registryName, Supplier<I> item) {
		return DEFERRED_ITEMS.register(registryName, item);
	}
}
