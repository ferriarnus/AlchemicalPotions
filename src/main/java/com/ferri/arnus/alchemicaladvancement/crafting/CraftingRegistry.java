package com.ferri.arnus.alchemicaladvancement.crafting;



import com.ferri.arnus.alchemicaladvancement.AlchemicalAdvancement;
import com.ferri.arnus.alchemicaladvancement.crafting.arrow.MultiTippedArrowRecipe;
import com.ferri.arnus.alchemicaladvancement.crafting.brewing.AlchemicalBrewing;
import com.ferri.arnus.alchemicaladvancement.crafting.brewing.IAlchemicalBrewing;
import com.ferri.arnus.alchemicaladvancement.crafting.heat.HeatSource;
import com.ferri.arnus.alchemicaladvancement.crafting.heat.IHeatSource;

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

@EventBusSubscriber(bus = Bus.MOD, modid = AlchemicalAdvancement.MODID)
public class CraftingRegistry {
	public static final ModRecipeType<IAlchemicalBrewing> ALCHEMICALBREWING_TYPE = new ModRecipeType<IAlchemicalBrewing>();
	public static final ModRecipeType<IHeatSource> HEATSOURCE_TYPE = new ModRecipeType<IHeatSource>();

	@SubscribeEvent
	public static void registerSerializers(RegistryEvent.Register<RecipeSerializer<?>> event) {
		event.getRegistry().register(name(AlchemicalBrewing.SERIALIZER, "alchemicalbrewing"));
		event.getRegistry().register(name(HeatSource.SERIALIZER, "heatsource"));
		event.getRegistry().register(name(MultiTippedArrowRecipe.SERIALIZER, "mutitipped_arrow"));
		Registry.register(Registry.RECIPE_TYPE, IAlchemicalBrewing.TYPE_ID, ALCHEMICALBREWING_TYPE);
		Registry.register(Registry.RECIPE_TYPE, IHeatSource.TYPE_ID, HEATSOURCE_TYPE);
	}
	
	private static <T extends IForgeRegistryEntry<? extends T>> T name(T entry, String name) {
		return entry.setRegistryName(new ResourceLocation(AlchemicalAdvancement.MODID, name));
	}
	
	private static class ModRecipeType<T extends Recipe<?>> implements RecipeType<T> {
		@Override
		public String toString() {
			return Registry.RECIPE_TYPE.getKey(this).toString();
		}
	}
}
