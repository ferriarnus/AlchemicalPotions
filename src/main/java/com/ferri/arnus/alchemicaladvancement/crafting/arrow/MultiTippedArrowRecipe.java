package com.ferri.arnus.alchemicaladvancement.crafting.arrow;

import com.ferri.arnus.alchemicaladvancement.item.ItemRegistry;
import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class MultiTippedArrowRecipe extends CustomRecipe{
	
	public static final RecipeSerializer<MultiTippedArrowRecipe> SERIALIZER = new Serializer();
	
	public MultiTippedArrowRecipe(ResourceLocation pId) {
		super(pId);
	}
	
	public boolean matches(CraftingContainer pInv, Level pLevel) {
		if (pInv.getWidth() == 3 && pInv.getHeight() == 3) {
			for(int i = 0; i < pInv.getWidth(); ++i) {
				for(int j = 0; j < pInv.getHeight(); ++j) {
					ItemStack itemstack = pInv.getItem(i + j * pInv.getWidth());
					if (itemstack.isEmpty()) {
						return false;
					}
					if (i == 1 && j == 1) {
						if (!itemstack.is(ItemRegistry.LINGERING_DRAUGHT.get()) && !itemstack.is(ItemRegistry.LINGERING_ELIXIR.get())) {
							return false;
						}
					} else if (!itemstack.is(Items.ARROW)) {
						return false;
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	public ItemStack assemble(CraftingContainer pInv) {
		ItemStack itemstack = pInv.getItem(1 + pInv.getWidth());
		if (!itemstack.is(ItemRegistry.LINGERING_DRAUGHT.get()) && !itemstack.is(ItemRegistry.LINGERING_ELIXIR.get())) {
			return ItemStack.EMPTY;
		} else {
			ItemStack itemstack1 = new ItemStack(ItemRegistry.MULTITIPPED_ARROW.get(), 8);
			//PotionUtils.setPotion(itemstack1, PotionUtils.getPotion(itemstack));
			PotionUtils.setCustomEffects(itemstack1, PotionUtils.getCustomEffects(itemstack));
			return itemstack1;
		}
	}
	
	public boolean canCraftInDimensions(int pWidth, int pHeight) {
		return pWidth >= 2 && pHeight >= 2;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
	static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<MultiTippedArrowRecipe> {
		
		@Override
		public MultiTippedArrowRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
			return new MultiTippedArrowRecipe(pRecipeId);
		}
		
		@Override
		public MultiTippedArrowRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
			return new MultiTippedArrowRecipe(pRecipeId);
				
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf pBuffer, MultiTippedArrowRecipe pRecipe) {
			
		}
	}
}
