package com.brassamber.classichostiles.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.brassamber.classichostiles.entity.ai.BeePollinateMoobloomGoal;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.level.Level;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.11
 */
@Mixin(Bee.class)
public abstract class MixinBeeEntity extends Animal implements NeutralMob, FlyingAnimal {

	public MixinBeeEntity(EntityType<? extends Bee> bee, Level level) {
		super(bee, level);
	}

	@Inject(at = @At(value = "HEAD"), method = "registerGoals")
	public void onRegisterGoals(CallbackInfo ci) {
		this.goalSelector.addGoal(4, new BeePollinateMoobloomGoal((Bee) (Object) this));
	}
}
