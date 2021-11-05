package com.example.examplemod.renderer;

import com.example.examplemod.blockentity.CauldronBE;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;

public class Cauldronrenderer implements BlockEntityRenderer<CauldronBE>{
	
	public Cauldronrenderer(Context ctx) {
	}

	@Override
	public void render(CauldronBE pBlockEntity, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer,
			int pCombinedLight, int pCombinedOverlay) {
		
	}

}
