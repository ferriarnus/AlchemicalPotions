package com.ferri.arnus.alchemicaladvancement.crafting.potions;

import com.ferri.arnus.alchemicaladvancement.AlchemicalAdvancement;
import com.ferri.arnus.alchemicaladvancement.item.ItemRegistry;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Bus.MOD, modid = AlchemicalAdvancement.MODID)
public class PotionRecipes {

	@SubscribeEvent
	static void registerRecipe(FMLCommonSetupEvent event) {
		ItemStack s = new ItemStack(ItemRegistry.DRAUGHT.get());
		s.setCount(1);
		event.enqueueWork(() -> BrewingRecipeRegistry.addRecipe(new NBTPotionRecipe()));
	}
}
