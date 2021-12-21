package de.dertoaster.classichostiles.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.FoxRenderer;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.util.ResourceLocation;

public class FoxEntityRenderer extends FoxRenderer {
	
	public static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
			new ResourceLocation("textures/entity/fox/fox.png"),
			new ResourceLocation("textures/entity/fox/snow_fox.png"),
			new ResourceLocation("textures/entity/fox/yellow_fox.png"),
			
			new ResourceLocation("textures/entity/fox/fox_sleep.png"),
			new ResourceLocation("textures/entity/fox/snow_fox_sleep.png"),
			new ResourceLocation("textures/entity/fox/yellow_fox_sleep.png"),
	};

	public FoxEntityRenderer(EntityRendererManager p_i50969_1_) {
		super(p_i50969_1_);
	}
	
	@Override
	public ResourceLocation getTextureLocation(FoxEntity fox) {
		if(fox instanceof de.dertoaster.classichostiles.entity.animal.FoxEntity) {
			int indx = ((de.dertoaster.classichostiles.entity.animal.FoxEntity) fox).getTextureIndexFromDataParameter();
			if(fox.isSleeping()) {
				indx += ((de.dertoaster.classichostiles.entity.animal.FoxEntity) fox).getTextureVariationCount();
			}
			return TEXTURES[indx];
		}
		return super.getTextureLocation(fox);
	}

}
