package de.dertoaster.classichostiles.init;

import de.dertoaster.classichostiles.ClassicHostilesMod;
import de.dertoaster.classichostiles.network.handler.CPacketHandlerAnimationUpdateOfEntity;
import de.dertoaster.classichostiles.network.packet.SPacketUpdateAnimationOfEntity;

public class CHPackets {
	
	// Start the IDs at 1 so any unregistered messages (ID 0) throw a more obvious exception when received
	private static int messageId = 1;
	
	public static void registerPackets() {
		ClassicHostilesMod.NETWORK.registerMessage(
				messageId++, 
				SPacketUpdateAnimationOfEntity.class, 
				SPacketUpdateAnimationOfEntity::toBytes, 
				SPacketUpdateAnimationOfEntity::fromBytes, 
				CPacketHandlerAnimationUpdateOfEntity::handlePacket
		);
	}
	
}
