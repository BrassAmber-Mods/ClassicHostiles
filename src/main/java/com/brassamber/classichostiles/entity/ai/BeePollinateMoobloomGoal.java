package com.brassamber.classichostiles.entity.ai;

import java.util.EnumSet;

import javax.annotation.Nullable;

import com.brassamber.classichostiles.entity.passive.MoobloomEntity;
import com.brassamber.classichostiles.mixin.MixinBeeAccessor;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Referenced from {@link Bee.BeePollinateGoal}
 * 
 * @author  Xrated_junior
 * @version 1.19.2-1.0.11
 */
public class BeePollinateMoobloomGoal extends Goal {
	private static final int MIN_POLLINATION_TICKS = 400;
	private static final int POSITION_CHANGE_CHANCE = 25;
	private static final double ARRIVAL_THRESHOLD = 0.5D;
	private static final float SPEED_MODIFIER = 0.35F;
	private static final float HOVER_HEIGHT_WITHIN_FLOWER = 0.5F;
	private static final float HOVER_POS_OFFSET = 0.33333334F;
	private static final int MAX_RANGE = 12;
	private final Bee beeEntity;
	private MoobloomEntity moobloomEntity;
	private int lastSoundPlayedTick;
	private int successfulPollinatingTicks;
	private int pollinatingTicks;
	private boolean pollinating;
	@Nullable
	private Vec3 hoverPos;

	public BeePollinateMoobloomGoal(Bee mixinBeeGoals) {
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		this.beeEntity = mixinBeeGoals;
	}

	/*********************************************************** Start Checks ********************************************************/

	@Override
	public boolean canUse() {
		return this.canBeeUse() && !this.beeEntity.isAngry();
	}

	@Override
	public boolean canContinueToUse() {
		return this.canBeeContinueToUse() && !this.beeEntity.isAngry();
	}

	public boolean canBeeUse() {
		if (((MixinBeeAccessor) this.beeEntity).getRemainingCooldownBeforeLocatingNewFlower() > 0) {
			return false;
		} else if (this.beeEntity.hasNectar()) {
			return false;
		} else if (this.beeEntity.level.isRaining()) {
			return false;
		} else {
			this.findMoobloomInRange();
			if (this.moobloomEntity != null && this.moobloomEntity.isAlive()) {
				this.beeEntity.getNavigation().moveTo((double) this.moobloomEntity.getX() + 0.5D, (double) this.moobloomEntity.getY() + 0.5D, (double) this.moobloomEntity.getZ() + 0.5D, (double) 1.2F);
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean canBeeContinueToUse() {
		if (!this.isPollinating()) {
			return false;
		} else if (this.moobloomEntity == null || !this.moobloomEntity.isAlive()) {
			return false;
		} else if (this.beeEntity.level.isRaining()) {
			return false;
		} else if (this.hasPollinatedLongEnough()) {
			return this.beeEntity.getRandom().nextFloat() < 0.2F;
		} else if (this.beeEntity.tickCount % 20 == 0 && !this.beeEntity.level.isLoaded(this.moobloomEntity.blockPosition())) {
			return false;
		} else {
			return true;
		}
	}

	boolean isPollinating() {
		return this.pollinating;
	}

	private boolean hasPollinatedLongEnough() {
		return this.successfulPollinatingTicks > MIN_POLLINATION_TICKS;
	}

	/*********************************************************** Start / Stop ********************************************************/

	@Override
	public void start() {
		this.successfulPollinatingTicks = 0;
		this.pollinatingTicks = 0;
		this.lastSoundPlayedTick = 0;
		this.pollinating = true;
		this.beeEntity.resetTicksWithoutNectarSinceExitingHive();
	}

	@Override
	public void stop() {
		if (this.hasPollinatedLongEnough()) {
			this.setHasNectar(true);
		}

		this.pollinating = false;
		this.beeEntity.getNavigation().stop();
		((MixinBeeAccessor) this.beeEntity).setRemainingCooldownBeforeLocatingNewFlower(200);
	}

	/**
	 * Exact copy of {@link Bee#setHasNectar()}
	 */
	void setHasNectar(boolean hasNectar) {
		if (hasNectar) {
			this.beeEntity.resetTicksWithoutNectarSinceExitingHive();
		}

		((MixinBeeAccessor) this.beeEntity).callSetFlag(8, hasNectar);
	}

	/*********************************************************** Tick ********************************************************/

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void tick() {
		++this.pollinatingTicks;
		if (this.pollinatingTicks > 600) {
			this.moobloomEntity = null;
		} else {
			Vec3 vec3 = Vec3.atBottomCenterOf(this.moobloomEntity.blockPosition()).add(0.0D, this.moobloomEntity.getBoundingBox().getYsize() + (double) HOVER_HEIGHT_WITHIN_FLOWER, 0.0D);
			if (vec3.distanceTo(this.beeEntity.position()) > 2.0D) {
				this.hoverPos = vec3;
				// Move faster than hover speed to correct for Moobloom movement speed
				this.setWantedPos(0.5D);
			} else {
				if (this.hoverPos == null) {
					this.hoverPos = vec3;
				}

				boolean hasStartedPollinating = this.beeEntity.position().distanceTo(this.hoverPos) <= ARRIVAL_THRESHOLD;
				boolean changePosition = true;
				// Took to long to start hovering
				if (!hasStartedPollinating && this.pollinatingTicks > 600) {
					this.moobloomEntity = null;
				} else {
					if (hasStartedPollinating) {
						boolean stopPollinatingChance = this.beeEntity.getRandom().nextInt(POSITION_CHANGE_CHANCE) == 0;
						if (stopPollinatingChance) {
							this.hoverPos = new Vec3(vec3.x() + (double) this.getHoverOffset(), vec3.y(), vec3.z() + (double) this.getHoverOffset());
							this.beeEntity.getNavigation().stop();
						} else {
							changePosition = false;
						}

						this.beeEntity.getLookControl().setLookAt(vec3.x(), vec3.y(), vec3.z());
					}

					if (changePosition) {
						this.setWantedPos();
					}

					++this.successfulPollinatingTicks;
					if (this.beeEntity.getRandom().nextFloat() < 0.05F && this.successfulPollinatingTicks > this.lastSoundPlayedTick + 60) {
						this.lastSoundPlayedTick = this.successfulPollinatingTicks;
						this.beeEntity.playSound(SoundEvents.BEE_POLLINATE, 1.0F, 1.0F);
					}

				}
			}
		}
	}

	/*********************************************************** Hover ********************************************************/

	private void setWantedPos(double speedModifier) {
		this.beeEntity.getMoveControl().setWantedPosition(this.hoverPos.x(), this.hoverPos.y(), this.hoverPos.z(), (double) SPEED_MODIFIER + speedModifier);
	}

	private void setWantedPos() {
		this.setWantedPos(0);
	}

	private float getHoverOffset() {
		return (this.beeEntity.getRandom().nextFloat() * 2.0F - 1.0F) * HOVER_POS_OFFSET;
	}

	/*********************************************************** Closest Moobloom ********************************************************/

	protected AABB getTargetSearchArea(double range) {
		return this.beeEntity.getBoundingBox().inflate(range, 4.0D, range);
	}

	protected void findMoobloomInRange() {
		this.moobloomEntity = this.beeEntity.level.getNearestEntity(this.beeEntity.level.getEntitiesOfClass(MoobloomEntity.class, this.getTargetSearchArea(MAX_RANGE), (nearestMoobloomEntity) -> {
			return true;
		}), TargetingConditions.forNonCombat().range(MAX_RANGE), this.beeEntity, this.beeEntity.getX(), this.beeEntity.getEyeY(), this.beeEntity.getZ());
	}
}