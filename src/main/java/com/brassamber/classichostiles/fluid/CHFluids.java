package com.brassamber.classichostiles.fluid;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.brassamber.classichostiles.ClassicHostiles;
import com.brassamber.classichostiles.block.CHBlocks;
import com.brassamber.classichostiles.fluid.fluidtype.FloralFluidType;
import com.brassamber.classichostiles.item.CHItems;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.5
 */
public class CHFluids {
	public static final DeferredRegister<FluidType> DEFERRED_FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, ClassicHostiles.MOD_ID);

	public static final RegistryObject<FluidType> FLORAL_FLUID_TYPE = registerFluidType("floral_fluid_type", () -> new FloralFluidType());

	public static final DeferredRegister<Fluid> DEFERRED_FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, ClassicHostiles.MOD_ID);

	// TODO Maybe add to Water FluidTag in the future
	public static final RegistryObject<ForgeFlowingFluid> FLORAL_FLUID = registerFluid("floral_fluid", () -> new ForgeFlowingFluid.Source(createProperties()));
	public static final RegistryObject<ForgeFlowingFluid> FLOWING_FLORAL_FLUID = registerFluid("flowing_floral_fluid", () -> new ForgeFlowingFluid.Flowing(createProperties()));

	@OnlyIn(Dist.CLIENT)
	public static void setFluidRenderTypes(final FMLClientSetupEvent event) {
		ItemBlockRenderTypes.setRenderLayer(FLORAL_FLUID.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FLOWING_FLORAL_FLUID.get(), RenderType.translucent());
	}

	public static FluidType.Properties createFluidTypeProperties() {
		return FluidType.Properties.create().canSwim(true).canDrown(false).pathType(BlockPathTypes.WATER).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY).rarity(Rarity.COMMON).density(1024).viscosity(1024).lightLevel(0);
	}

	/**
	 * Helper method for registering all Fluids
	 */
	public static <F extends ForgeFlowingFluid> RegistryObject<ForgeFlowingFluid> registerFluid(@Nonnull String registryName, Supplier<F> fluid) {
		return DEFERRED_FLUIDS.register(registryName, fluid);
	}

	/**
	 * Helper method for registering all Fluids
	 */
	public static <FT extends FluidType> RegistryObject<FluidType> registerFluidType(@Nonnull String registryName, Supplier<FT> fluidType) {
		return DEFERRED_FLUID_TYPES.register(registryName, fluidType);
	}

	public static ForgeFlowingFluid.Properties createProperties() {
		return new ForgeFlowingFluid.Properties(FLORAL_FLUID_TYPE, FLORAL_FLUID, FLOWING_FLORAL_FLUID).bucket(CHItems.FLORAL_FLUID_BUCKET).block(CHBlocks.FLORAL_FLUID_BLOCK).slopeFindDistance(1).levelDecreasePerBlock(2).tickRate(20);
	}
}
