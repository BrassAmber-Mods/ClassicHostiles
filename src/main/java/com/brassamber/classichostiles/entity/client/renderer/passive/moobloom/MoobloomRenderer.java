package com.brassamber.classichostiles.entity.client.renderer.passive.moobloom;

import java.util.Map;

import com.brassamber.classichostiles.ClassicHostiles;
import com.brassamber.classichostiles.entity.passive.MoobloomEntity;
import com.google.common.collect.Maps;

import net.minecraft.Util;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.MushroomCowRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Referenced form {@link MushroomCowRenderer}
 * 
 * @author  Xrated_junior
 * @version 1.19.2-1.0.7
 */
@OnlyIn(value = Dist.CLIENT)
public class MoobloomRenderer extends MobRenderer<MoobloomEntity, CowModel<MoobloomEntity>> {
	private static final Map<MoobloomEntity.MoobloomVariant, ResourceLocation> MOOBLOOM_TEXTURES = Util.make(Maps.newHashMap(), (texture) -> {
		// Small flowers
		texture.put(MoobloomEntity.MoobloomVariant.DANDELION, ClassicHostiles.locate("textures/entity/moobloom/moobloom_dandelion.png"));
		texture.put(MoobloomEntity.MoobloomVariant.POPPY, ClassicHostiles.locate("textures/entity/moobloom/moobloom_poppy.png"));
		texture.put(MoobloomEntity.MoobloomVariant.BLUE_ORCHID, ClassicHostiles.locate("textures/entity/moobloom/moobloom_blue_orchid.png"));
		texture.put(MoobloomEntity.MoobloomVariant.ALLIUM, ClassicHostiles.locate("textures/entity/moobloom/moobloom_allium.png"));
		texture.put(MoobloomEntity.MoobloomVariant.AZURE_BLUET, ClassicHostiles.locate("textures/entity/moobloom/moobloom_azure_bluet.png"));
		texture.put(MoobloomEntity.MoobloomVariant.RED_TULIP, ClassicHostiles.locate("textures/entity/moobloom/moobloom_red_tulip.png"));
		texture.put(MoobloomEntity.MoobloomVariant.ORANGE_TULIP, ClassicHostiles.locate("textures/entity/moobloom/moobloom_orange_tulip.png"));
		texture.put(MoobloomEntity.MoobloomVariant.WHITE_TULIP, ClassicHostiles.locate("textures/entity/moobloom/moobloom_white_tulip.png"));
		texture.put(MoobloomEntity.MoobloomVariant.PINK_TULIP, ClassicHostiles.locate("textures/entity/moobloom/moobloom_pink_tulip.png"));
		texture.put(MoobloomEntity.MoobloomVariant.OXEYE_DAISY, ClassicHostiles.locate("textures/entity/moobloom/moobloom_oxeye_daisy.png"));
		texture.put(MoobloomEntity.MoobloomVariant.CORNFLOWER, ClassicHostiles.locate("textures/entity/moobloom/moobloom_cornflower.png"));
		texture.put(MoobloomEntity.MoobloomVariant.LILY_OF_THE_VALLEY, ClassicHostiles.locate("textures/entity/moobloom/moobloom_lily_of_the_valley.png"));
		// Big flowers
		texture.put(MoobloomEntity.MoobloomVariant.SUNFLOWER, ClassicHostiles.locate("textures/entity/moobloom/moobloom_sunflower.png"));
		texture.put(MoobloomEntity.MoobloomVariant.LILAC, ClassicHostiles.locate("textures/entity/moobloom/moobloom_lilac.png"));
		texture.put(MoobloomEntity.MoobloomVariant.ROSE_BUSH, ClassicHostiles.locate("textures/entity/moobloom/moobloom_rose_bush.png"));
		texture.put(MoobloomEntity.MoobloomVariant.PEONY, ClassicHostiles.locate("textures/entity/moobloom/moobloom_peony.png"));
		// No flowers
		texture.put(MoobloomEntity.MoobloomVariant.TILLED, ClassicHostiles.locate("textures/entity/moobloom/moobloom_tilled.png"));
	});

	public MoobloomRenderer(Context context) {
		super(context, new CowModel<>(context.bakeLayer(ModelLayers.MOOSHROOM)), 0.7F);
		this.addLayer(new MoobloomFlowerLayer<>(this, context.getBlockRenderDispatcher()));
	}

	@Override
	public ResourceLocation getTextureLocation(MoobloomEntity moobloomEntity) {
		return MOOBLOOM_TEXTURES.get(moobloomEntity.getMoobloomVariant());
	}

	@Override
	protected boolean isShaking(MoobloomEntity moobloomEntity) {
		return super.isShaking(moobloomEntity) || moobloomEntity.isConverting();
	}
}
