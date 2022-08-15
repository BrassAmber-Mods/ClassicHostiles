package com.brassamber.classichostiles.block;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.brassamber.classichostiles.ClassicHostiles;
import com.brassamber.classichostiles.block.block.FloralFluidBlock;
import com.brassamber.classichostiles.fluid.CHFluids;
import com.brassamber.classichostiles.item.CHItems;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.5
 */
public class CHBlocks {
	public static final DeferredRegister<Block> DEFERRED_BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ClassicHostiles.MOD_ID);

	public static final RegistryObject<LiquidBlock> FLORAL_FLUID_BLOCK = registerBlock("floral_fluid_block", () -> new FloralFluidBlock(CHFluids.FLORAL_FLUID));

	/*********************************************************** Helper Methods ********************************************************/

	/**
	 * Helper method for registering Blocks to the default CreativeModeTab.
	 */
	@SuppressWarnings("unused")
	private static <B extends Block> RegistryObject<B> registerBlockAndBlockItem(@Nonnull String registryName, Supplier<B> blockSupplier) {
		return registerBlockAndBlockItem(registryName, blockSupplier, CreativeModeTab.TAB_MISC);
	}

	/**
	 * Helper method for registering Blocks with basic BlockItems.
	 * 
	 * @param  registryName  The name to register the Block with.
	 * @param  blockSupplier The Block to register.
	 * @param  itemGroup     The CreativeModeTab where the Item will show.
	 * @return               The Block that was registered
	 */
	private static <B extends Block> RegistryObject<B> registerBlockAndBlockItem(@Nonnull String registryName, Supplier<B> blockSupplier, CreativeModeTab itemGroup) {
		RegistryObject<B> registryBlock = registerBlock(registryName, blockSupplier);
		// Blocks are registered before Items
		CHItems.registerItem(registryName, () -> new BlockItem(registryBlock.get(), new Item.Properties().tab(itemGroup)));
		return registryBlock;
	}

	/**
	 * Helper method for registering all Blocks. Can also be used to register Blocks with no Items.
	 * 
	 * @param  registryName  The name to register the Block with.
	 * @param  blockSupplier The Block to register.
	 * @return               The Block that was registered
	 */
	private static <B extends Block> RegistryObject<B> registerBlock(@Nonnull String registryName, Supplier<B> blockSupplier) {
		return DEFERRED_BLOCKS.register(registryName, blockSupplier);
	}
}
