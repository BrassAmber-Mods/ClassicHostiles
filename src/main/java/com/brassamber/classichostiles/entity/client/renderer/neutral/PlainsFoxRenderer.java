package com.brassamber.classichostiles.entity.client.renderer.neutral;

import com.brassamber.classichostiles.ClassicHostiles;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.FoxRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Fox;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author  Xrated_junior
 * @version 1.19.2-1.0.1
 */
@OnlyIn(value = Dist.CLIENT)
public class PlainsFoxRenderer extends FoxRenderer {
	private static final ResourceLocation PLAINS_FOX_TEXTURE = ClassicHostiles.locate("textures/entity/fox/plains_fox.png");
	private static final ResourceLocation PLAINS_FOX_SLEEP_TEXTURE = ClassicHostiles.locate("textures/entity/fox/plains_fox_sleep.png");

	public PlainsFoxRenderer(Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(Fox foxEntity) {
		return foxEntity.isSleeping() ? PLAINS_FOX_SLEEP_TEXTURE : PLAINS_FOX_TEXTURE;
	}
}
