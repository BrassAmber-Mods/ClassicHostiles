package com.brassamber.classichostiles.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.entity.animal.Bee;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.11
 */
@Mixin(Bee.class)
public interface MixinBeeAccessor {

	@Invoker
	void callSetFlag(int flag, boolean hasNectar);

	@Accessor
	int getRemainingCooldownBeforeLocatingNewFlower();

	@Accessor
	void setRemainingCooldownBeforeLocatingNewFlower(int time);
}
