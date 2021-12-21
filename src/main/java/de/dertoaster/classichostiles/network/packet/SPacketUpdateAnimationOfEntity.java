package de.dertoaster.classichostiles.network.packet;

import javax.annotation.Nullable;

import de.dertoaster.classichostiles.entity.IServerAnimationReceiver;
import net.minecraft.network.PacketBuffer;

public class SPacketUpdateAnimationOfEntity {

	private int entityId;
	private String animationID;

	public SPacketUpdateAnimationOfEntity() {

	}

	SPacketUpdateAnimationOfEntity(Builder builder) {
		this.entityId = builder.getEntityID();
		this.animationID = builder.getValue();
	}

	public String getAnimationID() {
		return this.animationID;
	}

	public static SPacketUpdateAnimationOfEntity fromBytes(PacketBuffer buf) {
		SPacketUpdateAnimationOfEntity result = new SPacketUpdateAnimationOfEntity();
		result.entityId = buf.readInt();
		result.animationID = buf.readUtf();
		
		return result;
	}

	public static void toBytes(SPacketUpdateAnimationOfEntity packet, PacketBuffer buf) {
		buf.writeInt(packet.entityId);
		buf.writeUtf(packet.animationID);
	}

	public static Builder builder(IServerAnimationReceiver<?> entity) {
		return new Builder(entity);
	}

	public static class Builder {

		private Builder(IServerAnimationReceiver<?> entity) {
			this.entityID = entity.getEntity().getId();
		}

		private int entityID;
		private String value;

		public Builder animate(String value) {
			this.value = value;
			return this;
		}

		String getValue() {
			return this.value;
		}

		int getEntityID() {
			return this.entityID;
		}

		@Nullable
		public SPacketUpdateAnimationOfEntity build() {
			try {
				return new SPacketUpdateAnimationOfEntity(this);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}
	}

	public int getEntityId() {
		return this.entityId;
	}

}
