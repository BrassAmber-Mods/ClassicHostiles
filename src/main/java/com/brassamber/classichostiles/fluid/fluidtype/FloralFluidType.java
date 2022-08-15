package com.brassamber.classichostiles.fluid.fluidtype;

import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.5
 */
public class FloralFluidType extends FluidType {

	public FloralFluidType() {
		super(FluidType.Properties.create().descriptionId("block.classichostiles.floral_fluid").motionScale(0.008D).density(2000).viscosity(4000).rarity(Rarity.RARE).canSwim(true).canDrown(false).canPushEntity(false).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY).sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH));
	}

	@Override
	public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
		consumer.accept(new IClientFluidTypeExtensions() {
			private static final ResourceLocation WATER_STILL = new ResourceLocation("minecraft", "block/water_still");
			private static final ResourceLocation WATER_FLOW = new ResourceLocation("minecraft", "block/water_flow");
			private static final ResourceLocation WATER_OVERLAY = new ResourceLocation("minecraft", "block/water_overlay");

			@Override
			public ResourceLocation getStillTexture() {
				return WATER_STILL;
			}

			@Override
			public ResourceLocation getFlowingTexture() {
				return WATER_FLOW;
			}

			@Override
			public ResourceLocation getOverlayTexture() {
				return WATER_OVERLAY;
			}

			@Override
			public int getTintColor() {
				return 0xCC7cc4a5;
			}
		});
	}
}
