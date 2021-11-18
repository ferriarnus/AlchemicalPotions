package com.example.examplemod.crafting;



import com.example.examplemod.ExampleMod;
import com.example.examplemod.crafting.brewing.AlchemicalBrewing;
import com.example.examplemod.crafting.brewing.IAlchemicalBrewing;
import com.example.examplemod.crafting.heat.HeatSource;
import com.example.examplemod.crafting.heat.IHeatSource;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistryEntry;

@EventBusSubscriber(bus = Bus.MOD, modid = ExampleMod.MODID)
public class CraftingRegistry {
	public static final ModRecipeType<IAlchemicalBrewing> ALCHEMICALBREWING_TYPE = new ModRecipeType<IAlchemicalBrewing>();
	public static final ModRecipeType<IHeatSource> HEATSOURCE_TYPE = new ModRecipeType<IHeatSource>();

	@SubscribeEvent
	public static void registerSerializers(RegistryEvent.Register<RecipeSerializer<?>> event) {
		event.getRegistry().register(name(AlchemicalBrewing.SERIALIZER, "alchemicalbrewing"));
		event.getRegistry().register(name(HeatSource.SERIALIZER, "heatsource"));
		Registry.register(Registry.RECIPE_TYPE, IAlchemicalBrewing.TYPE_ID, ALCHEMICALBREWING_TYPE);
		Registry.register(Registry.RECIPE_TYPE, IHeatSource.TYPE_ID, HEATSOURCE_TYPE);
	}
	
	private static <T extends IForgeRegistryEntry<? extends T>> T name(T entry, String name) {
		return entry.setRegistryName(new ResourceLocation(ExampleMod.MODID, name));
	}
	
	private static class ModRecipeType<T extends Recipe<?>> implements RecipeType<T> {
		@Override
		public String toString() {
			return Registry.RECIPE_TYPE.getKey(this).toString();
		}
	}
}
