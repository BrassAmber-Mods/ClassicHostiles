package de.dertoaster.classichostiles.entity;

import net.minecraft.entity.LivingEntity;

public interface IHasTextureVariants<T extends LivingEntity> {
	
	public void setTextureIndexOnDataParameter(int value);
	public int getTextureIndexFromDataParameter();
	public int getTextureVariationCount();
	
	public default void callInConstructor() {
		if(this instanceof LivingEntity) {
			LivingEntity self = (LivingEntity) this;
			
			this.setTextureIndexOnDataParameter(self.getRandom().nextInt(getTextureVariationCount()));
		}
	}

}
