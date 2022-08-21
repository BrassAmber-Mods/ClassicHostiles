package com.brassamber.classichostiles.entity.client.renderer.passive.moobloom;

import com.brassamber.classichostiles.entity.passive.MoobloomEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.MushroomCowMushroomLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Referenced from {@link MushroomCowMushroomLayer}
 * 
 * @author  Xrated_junior
 * @version 1.19.2-1.0.7
 */
@OnlyIn(value = Dist.CLIENT)
public class MoobloomFlowerLayer<E extends MoobloomEntity> extends RenderLayer<E, CowModel<E>> {
	private final BlockRenderDispatcher blockRenderer;

	public MoobloomFlowerLayer(RenderLayerParent<E, CowModel<E>> entityModel, BlockRenderDispatcher blockRenderDispatcher) {
		super(entityModel);
		this.blockRenderer = blockRenderDispatcher;
	}

	/**
	 * TODO Not render flower after shearing
	 */
	@Override
	public void render(PoseStack poseStack, MultiBufferSource bufferSource, int p_117258_, E moobloomEntity, float p_117260_, float p_117261_, float p_117262_, float p_117263_, float p_117264_, float p_117265_) {
		if (!moobloomEntity.isBaby() && !moobloomEntity.isSheared()) {
			Minecraft minecraft = Minecraft.getInstance();
			boolean isGlowingOrInvisible = minecraft.shouldEntityAppearGlowing(moobloomEntity) && moobloomEntity.isInvisible();
			if (!moobloomEntity.isInvisible() || isGlowingOrInvisible) {
				BlockState blockstate = moobloomEntity.getMoobloomVariant().getFlowerBlock().defaultBlockState();
				int overlayCoords = LivingEntityRenderer.getOverlayCoords(moobloomEntity, 0.0F);
				BakedModel bakedmodel = this.blockRenderer.getBlockModel(blockstate);
				float scale = 0.8F;
				double scaleCorrection = -0.24D;
				
				// Reduce scale if Moobloom type is a double flower
				if (blockstate.getBlock() instanceof DoublePlantBlock) {
					scale = 0.6F;
					scaleCorrection = -0.32D;
				}
				
				// Back flower
				poseStack.pushPose();
				// Set position on the back
				poseStack.translate((double) 0.2F, (double) -0.35F, 0.5D);
				// Align block with EntityModel
				poseStack.mulPose(Vector3f.YP.rotationDegrees(132.0F));
				// Scale
				poseStack.scale(-scale, -scale, scale);
				// Correct position after scaling
				poseStack.translate(-0.5D, -0.5D + scaleCorrection, -0.5D);
				this.renderFlowerBlock(poseStack, bufferSource, p_117258_, isGlowingOrInvisible, blockstate, overlayCoords, bakedmodel);
				poseStack.popPose();
				
				// Middle flower
				poseStack.pushPose();
				// Set position on the back
				poseStack.translate((double) 0.2F, (double) -0.35F, 0.5D);
				// Align block with EntityModel
				poseStack.mulPose(Vector3f.YP.rotationDegrees(42.0F));
				// Tweak position to the middle
				poseStack.translate((double) 0.1F, 0.0D, (double) -0.6F);
				// Offset rotation
				poseStack.mulPose(Vector3f.YP.rotationDegrees(-48.0F));
				// scale
				poseStack.scale(-scale, -scale, scale);
				// Correct position after scaling
				poseStack.translate(-0.5D, -0.5D + scaleCorrection, -0.5D);
				this.renderFlowerBlock(poseStack, bufferSource, p_117258_, isGlowingOrInvisible, blockstate, overlayCoords, bakedmodel);
				poseStack.popPose();
				
				// Head flower
				poseStack.pushPose();
				this.getParentModel().getHead().translateAndRotate(poseStack);
				poseStack.translate(0.0D, (double) -0.7F, (double) -0.2F);
				poseStack.mulPose(Vector3f.YP.rotationDegrees(-78.0F));
				poseStack.scale(-scale, -scale, scale);
				poseStack.translate(-0.5D, -0.5D + scaleCorrection, -0.5D);
				this.renderFlowerBlock(poseStack, bufferSource, p_117258_, isGlowingOrInvisible, blockstate, overlayCoords, bakedmodel);
				poseStack.popPose();
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void renderFlowerBlock(PoseStack poseStack, MultiBufferSource bufferSource, int p_234855_, boolean isGlowingOrInvisible, BlockState blockstate, int overlayCoords, BakedModel blockModel) {
		if (isGlowingOrInvisible) {
			this.blockRenderer.getModelRenderer().renderModel(poseStack.last(), bufferSource.getBuffer(RenderType.outline(TextureAtlas.LOCATION_BLOCKS)), blockstate, blockModel, 0.0F, 0.0F, 0.0F, p_234855_, overlayCoords);
		} else {
			if (blockstate.getBlock() instanceof DoublePlantBlock doublePlant) {
				this.blockRenderer.renderSingleBlock(blockstate.setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), poseStack, bufferSource, p_234855_, overlayCoords);
			} else {
				this.blockRenderer.renderSingleBlock(blockstate, poseStack, bufferSource, p_234855_, overlayCoords);
			}
		}
	}
}
