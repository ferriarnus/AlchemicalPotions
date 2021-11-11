package com.example.examplemod;


import com.example.examplemod.block.BlockRegistry;
import com.example.examplemod.blockentity.BlockentityRegistry;
import com.example.examplemod.entity.EntityRegistry;
import com.example.examplemod.item.ItemRegistry;
import com.example.examplemod.renderer.Cauldronrenderer;
import com.example.examplemod.renderer.PotionColor;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(bus = Bus.MOD, modid = ExampleMod.MODID)
public class ClientSetup {

	@SubscribeEvent
	static void registerBERS(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockentityRegistry.CAULDRON.get(), Cauldronrenderer::new);
        event.registerEntityRenderer(EntityRegistry.MULTIPOTION.get(), ThrownItemRenderer::new);
    }
	
	@SubscribeEvent
	static void setup(FMLClientSetupEvent event) {
		ItemBlockRenderTypes.setRenderLayer(BlockRegistry.ALCHEMICALCAULDRON.get(), RenderType.cutoutMipped());

	}
	
	@SubscribeEvent
	public static void registerItemColor(ColorHandlerEvent.Item event) {
		event.getItemColors().register(new PotionColor(), ItemRegistry.DRAUGHT.get());
	}
}
