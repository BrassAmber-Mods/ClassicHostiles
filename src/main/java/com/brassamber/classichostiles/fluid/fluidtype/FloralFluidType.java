package com.brassamber.classichostiles.fluid.fluidtype;

import java.util.function.Consumer;

import com.brassamber.classichostiles.ClassicHostiles;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.13
 */
public class FloralFluidType extends FluidType {

	public FloralFluidType() {
		super(FluidType.Properties.create().descriptionId("block.classichostiles.floral_fluid")//
				.motionScale(0.004D)//
				.density(2000)//
				.viscosity(4000)//
				.rarity(Rarity.RARE)//
				.canSwim(false)//
				.canDrown(false)//
				.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)//
				.sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)//
				.sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH));
	}

	@Override
	public void setItemMovement(ItemEntity entity) {
		double speedModifier = 0.7D;
		Vec3 vec3 = entity.getDeltaMovement();
		entity.setDeltaMovement(vec3.x * speedModifier, vec3.y, vec3.z * speedModifier);
	}

	@Override
	public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
		consumer.accept(new IClientFluidTypeExtensions() {
			private static final ResourceLocation FLORAL_FLUID_STILL_TEXTURE = ClassicHostiles.locate("block/floral_fluid_still");
			private static final ResourceLocation FLORAL_FLUID_FLOWING_TEXTURE = ClassicHostiles.locate("block/floral_fluid_flowing");

			@Override
			public ResourceLocation getStillTexture() {
				return FLORAL_FLUID_STILL_TEXTURE;
			}

			@Override
			public ResourceLocation getFlowingTexture() {
				return FLORAL_FLUID_FLOWING_TEXTURE;
			}
		});
	}
}
