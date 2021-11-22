package com.ferri.arnus.alchemicaladvancement.renderer;

import java.awt.Color;
import java.util.Random;

import com.ferri.arnus.alchemicaladvancement.blockentity.CauldronBE;
import com.ferri.arnus.alchemicaladvancement.particle.ColoredSmokeData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;

public class Cauldronrenderer implements BlockEntityRenderer<CauldronBE>{
	
	private Context ctx;
	
	public Cauldronrenderer(Context ctx) {
		this.ctx = ctx;
	}
	
	@Override
	public void render(CauldronBE pBlockEntity, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer,
			int pCombinedLight, int pCombinedOverlay) {
		TextureAtlasSprite s = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(Fluids.WATER.getAttributes().getStillTexture());
		drawTop(pBuffer.getBuffer(Sheets.translucentCullBlockSheet()), pMatrixStack.last().pose(), pMatrixStack.last().normal(), pBlockEntity.getHeigth(), s, new Color(pBlockEntity.getColor()), pCombinedOverlay, pCombinedLight);
		Level pLevel = pBlockEntity.getLevel();
		BlockPos pPos =pBlockEntity.getBlockPos();
		Random random = pLevel.random;
		if (pBlockEntity.hasHeat() && pLevel.getGameTime() % 20 == 0 && pBlockEntity.getHeigth() > 0F) {
		    //pLevel.addAlwaysVisibleParticle(ParticleTypes.BUBBLE, true, (double)pPos.getX() + 0.5D + random.nextDouble() / 3.0D * (double)(random.nextBoolean() ? 1 : -1), (double)pPos.getY() + random.nextDouble() + random.nextDouble(), (double)pPos.getZ() + 0.5D + random.nextDouble() / 3.0D * (double)(random.nextBoolean() ? 1 : -1), 0.0D, 0.01D, 0.0D);
		}
		if (pBlockEntity.isActive() && pLevel.getGameTime() % 10 == 0) {
		    pLevel.addAlwaysVisibleParticle(ColoredSmokeData.withColor(pBlockEntity.activeColor(), pBlockEntity.activeColor(), pBlockEntity.activeColor()), true, (double)pPos.getX() + 0.5D + random.nextDouble() / 3.0D * (double)(random.nextBoolean() ? 1 : -1), (double)pPos.getY() + random.nextDouble() + random.nextDouble(), (double)pPos.getZ() + 0.5D + random.nextDouble() / 3.0D * (double)(random.nextBoolean() ? 1 : -1), 0.0D, 0.01D, 0.0D);
		}
		if (!pBlockEntity.getResult().isEmpty() && pLevel.getGameTime() % 15 == 0) {
		    pLevel.addAlwaysVisibleParticle(ColoredSmokeData.withColor(1, 1, 1), true, (double)pPos.getX() + 0.5D + random.nextDouble() / 3.0D * (double)(random.nextBoolean() ? 1 : -1), (double)pPos.getY() + random.nextDouble() + random.nextDouble(), (double)pPos.getZ() + 0.5D + random.nextDouble() / 3.0D * (double)(random.nextBoolean() ? 1 : -1), 0.0D, 0.01D, 0.0D);
		}
	}
	
	private static void drawTop(VertexConsumer builder, Matrix4f matrix, Matrix3f normal, float height, TextureAtlasSprite tex, Color color, int overlay, int light) {
		float y = 3.99F/16F + (height * (14.99F/16F - 3.99F/16F));
		builder.vertex(matrix, 15.99F/16F, y, 0.01F/16F).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).uv(tex.getU(0.01F), tex.getV(0.01F)).overlayCoords(overlay).uv2(light).normal(normal, 0, (float) 1, 0).endVertex();
		builder.vertex(matrix, 0.01F/16F, y, 0.01F/16F).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).uv(tex.getU(16F), tex.getV(0.01F)).overlayCoords(overlay).uv2(light).normal(normal, 0, (float) 1, 0).endVertex();
		builder.vertex(matrix, 0.01F/16F, y, 15.99F/16F).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).uv(tex.getU(16F), tex.getV(16F)).overlayCoords(overlay).uv2(light).normal(normal, 0, (float) 1, 0).endVertex();
		builder.vertex(matrix, 15.99F/16F, y, 15.99F/16F).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).uv(tex.getU(0.01F), tex.getV(16F)).overlayCoords(overlay).uv2(light).normal(normal, 0, (float) 1, 0).endVertex();
	}
	
}
