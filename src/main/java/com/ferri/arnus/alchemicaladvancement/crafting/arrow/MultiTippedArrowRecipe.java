package com.ferri.arnus.alchemicaladvancement.crafting.arrow;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class MultiTippedArrowRecipe extends CustomRecipe{

	public MultiTippedArrowRecipe(ResourceLocation pId) {
		super(pId);
	}

	@Override
	public boolean matches(CraftingContainer pContainer, Level pLevel) {
		return false;
	}

	@Override
	public ItemStack assemble(CraftingContainer pContainer) {
		return null;
	}

	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight) {
		return false;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return null;
	}

}
