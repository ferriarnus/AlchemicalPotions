package com.example.examplemod.renderer;

import com.example.examplemod.blockentity.CauldronBE;
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
import net.minecraft.world.level.material.Fluids;

public class Cauldronrenderer implements BlockEntityRenderer<CauldronBE>{
	
	private Context ctx;
	
	private static final float MIN_X =  0.01F/16F;
    private static final float MAX_X = 15.99F/16F;
    private static final float MIN_Y =  0.01F/16F;
    private static final float MAX_Y = 15.99F/16F;
    private static final float MIN_Z =  0.01F/16F;
    private static final float MAX_Z = 15.99F/16F;

    private static final float MIN_UV_T =  0.01F;
    private static final float MAX_UV_T = 16F;
	
	public Cauldronrenderer(Context ctx) {
		this.ctx = ctx;
	}
	
	@Override
	public void render(CauldronBE pBlockEntity, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer,
			int pCombinedLight, int pCombinedOverlay) {
		TextureAtlasSprite s = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(Fluids.WATER.getAttributes().getStillTexture());
		drawTop(pBuffer.getBuffer(Sheets.translucentCullBlockSheet()), pMatrixStack.last().pose(), pMatrixStack.last().normal(), 0.9F, s, 255, pCombinedOverlay, pCombinedLight);
	}
	
	private static void drawTop(VertexConsumer builder, Matrix4f matrix, Matrix3f normal, float height, TextureAtlasSprite tex, int color, int overlay, int light) {
		float minX = MIN_X;
		float maxX = MAX_X;
		float y = MIN_Y + (height * (MAX_Y - MIN_Y));
		float ny =  1;
		
		float minU = tex.getU(MIN_UV_T);
		float maxU = tex.getU(MAX_UV_T);
		float minV = tex.getV(MIN_UV_T);
		float maxV = tex.getV(MAX_UV_T);
		
		builder.vertex(matrix, maxX, y, MIN_Z).color(color, color, color, color).uv(minU, minV).overlayCoords(overlay).uv2(light).normal(normal, 0, ny, 0).endVertex();
		builder.vertex(matrix, minX, y, MIN_Z).color(color, color, color, color).uv(maxU, minV).overlayCoords(overlay).uv2(light).normal(normal, 0, ny, 0).endVertex();
		builder.vertex(matrix, minX, y, MAX_Z).color(color, color, color, color).uv(maxU, maxV).overlayCoords(overlay).uv2(light).normal(normal, 0, ny, 0).endVertex();
		builder.vertex(matrix, maxX, y, MAX_Z).color(color, color, color, color).uv(minU, maxV).overlayCoords(overlay).uv2(light).normal(normal, 0, ny, 0).endVertex();
	}
	
}
