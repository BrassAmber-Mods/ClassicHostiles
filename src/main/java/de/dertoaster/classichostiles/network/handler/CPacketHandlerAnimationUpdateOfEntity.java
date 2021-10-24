package de.dertoaster.classichostiles.network.handler;

import java.util.function.Supplier;

import de.dertoaster.classichostiles.entity.IServerAnimationReceiver;
import de.dertoaster.classichostiles.network.packet.SPacketUpdateAnimationOfEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class CPacketHandlerAnimationUpdateOfEntity {

	public static void handlePacket(final SPacketUpdateAnimationOfEntity packet, Supplier<Context> context) {
		context.get().enqueueWork(() -> {
			World world = Minecraft.getInstance().level;
			Entity entity = world.getEntity(packet.getEntityId());

			if (entity instanceof IServerAnimationReceiver) {
				IServerAnimationReceiver animationReceiver = (IServerAnimationReceiver) entity;

				animationReceiver.processAnimationUpdate(packet.getAnimationID());
			}
		});
		context.get().setPacketHandled(true);
	}

}
