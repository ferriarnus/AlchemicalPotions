package com.ferri.arnus.alchemicaladvancement.crafting.potions;

import com.ferri.arnus.alchemicaladvancement.item.ItemRegistry;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public class NBTPotionRecipe implements IBrewingRecipe{
	

	@Override
	public boolean isInput(ItemStack input) {
		return input.is(ItemRegistry.DRAUGHT.get()) || input.is(ItemRegistry.ELIXIR.get());
	}

	@Override
	public boolean isIngredient(ItemStack ingredient) {
		return ingredient.is(Items.GUNPOWDER) || ingredient.is(Items.DRAGON_BREATH);
	}

	@Override
	public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
		if (input.is(ItemRegistry.DRAUGHT.get())) {
			if (ingredient.is(Items.GUNPOWDER)) {
				ItemStack s = new ItemStack(ItemRegistry.SPLASH_DRAUGHT.get());
				s.setTag(input.getOrCreateTag());
				return s;
			}
			if (ingredient.is(Items.DRAGON_BREATH)) {
				ItemStack s = new ItemStack(ItemRegistry.LINGERING_DRAUGHT.get());
				s.setTag(input.getOrCreateTag());
				return s;
			}
		}
		if (input.is(ItemRegistry.ELIXIR.get())) {
			if (ingredient.is(Items.GUNPOWDER)) {
				ItemStack s = new ItemStack(ItemRegistry.SPLASH_ELIXIR.get());
				s.setTag(input.getOrCreateTag());
				return s;
			}
			if (ingredient.is(Items.DRAGON_BREATH)) {
				ItemStack s = new ItemStack(ItemRegistry.LINGERING_ELIXIR.get());
				s.setTag(input.getOrCreateTag());
				return s;
			}
		}
		return ItemStack.EMPTY;
	}

}
