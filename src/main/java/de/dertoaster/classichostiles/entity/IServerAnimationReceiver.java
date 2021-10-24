package de.dertoaster.classichostiles.entity;

import de.dertoaster.classichostiles.ClassicHostilesMod;
import de.dertoaster.classichostiles.network.packet.SPacketUpdateAnimationOfEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;
import software.bernie.geckolib3.core.IAnimatable;

public interface IServerAnimationReceiver<T extends LivingEntity & IAnimatable> {
	
	public default LivingEntity getEntity() {
		if(this instanceof LivingEntity) {
			return (LivingEntity) this;
		}
		return null;
	}

	@OnlyIn(Dist.CLIENT)
	public void processAnimationUpdate(String animationID);
	
	public default void sendAnimationUpdate(final String animationName) {
		SPacketUpdateAnimationOfEntity message = SPacketUpdateAnimationOfEntity.builder(this).animate(animationName).build();
		ClassicHostilesMod.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(this::getEntity), message);
	}

}
