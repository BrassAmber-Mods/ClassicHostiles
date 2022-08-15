package com.brassamber.classichostiles.block.block;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Material;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.5
 */
public class FloralFluidBlock extends LiquidBlock {

	public FloralFluidBlock(Supplier<? extends FlowingFluid> flowingFluid) {
		super(flowingFluid, Properties.of(Material.WATER).noCollission().strength(100.0F).noLootTable());
	}

	/**
	 * TODO Give effects as discussed with Brass
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		super.entityInside(state, world, pos, entity);
		if (entity instanceof LivingEntity livingEntity) {
			livingEntity.removeAllEffects();
		}
	}
}
