package com.example.examplemod.crafting;

import javax.annotation.Nonnull;

import com.example.examplemod.ExampleMod;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public interface IHeatSource extends Recipe<RecipeWrapper>{

	ResourceLocation TYPE_ID = new ResourceLocation(ExampleMod.MODID, "heatsource");
	
	@Nonnull
	int getTime();
	
	@Nonnull
	ItemStack getBlock();
	
	@Nonnull
	@Override
	default RecipeType<?> getType() {
		return Registry.RECIPE_TYPE.get(TYPE_ID);
	}

	
}
