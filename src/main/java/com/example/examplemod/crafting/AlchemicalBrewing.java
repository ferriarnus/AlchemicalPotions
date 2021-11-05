package com.example.examplemod.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class AlchemicalBrewing implements IAlchemicalBrewing{
	
	public static final RecipeSerializer<IAlchemicalBrewing> SERIALIZER = new Serializer();
	private NonNullList<Ingredient> potions;
	private NonNullList<Ingredient> binders;
	private NonNullList<Ingredient> ingredients = NonNullList.create();
	private ItemStack resultitem;
	private boolean isSimple;
	private ResourceLocation id;
	
	public AlchemicalBrewing(ResourceLocation id, NonNullList<Ingredient> potions, NonNullList<Ingredient> binders,  ItemStack result) {
		this.id = id;
		this.potions = potions;
		this.resultitem = result;
		this.binders = binders;
		this.ingredients.addAll(binders);
		this.ingredients.addAll(potions);
		this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
	}

	@Override
	public boolean matches(RecipeWrapper pContainer, Level pLevel) {
		StackedContents stackedcontents = new StackedContents();
		java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
		int i = 0;
		
		for(int j = 0; j < pContainer.getContainerSize(); ++j) {
			ItemStack itemstack = pContainer.getItem(j);
			if (!itemstack.isEmpty()) {
				++i;
				if (isSimple)
					stackedcontents.accountStack(itemstack, 1);
				else inputs.add(itemstack);
			}
		}
		
		return i == this.ingredients.size() && (isSimple ? stackedcontents.canCraft(this, (IntList)null) : net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs,  this.ingredients) != null);
	}	
	

	@Override
	public ItemStack assemble(RecipeWrapper pContainer) {
		ItemStack stack = getResultItem().copy();
		List<MobEffectInstance> list = new ArrayList<>();
		if (PotionUtils.getPotion(getPotions().get(0).getItems()[0]) == PotionUtils.getPotion(getPotions().get(1).getItems()[0])) {
			//return ItemStack.EMPTY;
		}
		List<MobEffectInstance> e1 = PotionUtils.getMobEffects(getPotions().get(0).getItems()[0]);
		List<MobEffectInstance> e2 = PotionUtils.getMobEffects(getPotions().get(1).getItems()[0]);
		AtomicBoolean same = new AtomicBoolean(false);
		e1.forEach(effect1 -> {
			e2.forEach(effect2 -> {
				if (effect1.getDescriptionId().equals(effect2.getDescriptionId())) {
					same.set(true);
				}
			});
		});
		if (same.get()) {
			//return ItemStack.EMPTY;
		}
		list.addAll(e1);
		list.addAll(e2);
		PotionUtils.setCustomEffects(stack, list);
		return stack;
	}

	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight) {
		return true;
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}

	@Override
	public ItemStack getResultItem() {
		return resultitem;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
	public NonNullList<Ingredient> getPotions() {
		return potions;
	}
	
	public NonNullList<Ingredient> getBinders() {
		return binders;
	}
	
	static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<IAlchemicalBrewing> {

		@Override
		public IAlchemicalBrewing fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
			NonNullList<Ingredient> potions = itemsFromJson(GsonHelper.getAsJsonArray(pSerializedRecipe, "potions"));
			NonNullList<Ingredient> binders = itemsFromJson(GsonHelper.getAsJsonArray(pSerializedRecipe, "binders"));
			ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "result"));
			return new AlchemicalBrewing(pRecipeId, potions, binders, itemstack);
			
		}

		@Override
		public IAlchemicalBrewing fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
			int i = pBuffer.readVarInt();
			NonNullList<Ingredient> potions = NonNullList.withSize(i, Ingredient.EMPTY);
			for(int j = 0; j < potions.size(); ++j) {
				potions.set(j, Ingredient.fromNetwork(pBuffer));
			}
			NonNullList<Ingredient> binders = NonNullList.withSize(i, Ingredient.EMPTY);
			for(int j = 0; j < binders.size(); ++j) {
				binders.set(j, Ingredient.fromNetwork(pBuffer));
			}
			ItemStack itemstack = pBuffer.readItem();
			return new AlchemicalBrewing(pRecipeId, potions, binders, itemstack);
		}

		@Override
		public void toNetwork(FriendlyByteBuf pBuffer, IAlchemicalBrewing pRecipe) {
			pBuffer.writeVarInt(pRecipe.getIngredients().size());
			for(Ingredient ingredient : pRecipe.getIngredients()) {
				ingredient.toNetwork(pBuffer);
			}
			pBuffer.writeItem(pRecipe.getResultItem());
		}
		
		private static NonNullList<Ingredient> itemsFromJson(JsonArray pIngredientArray) {
			NonNullList<Ingredient> nonnulllist = NonNullList.create();
			for(int i = 0; i < pIngredientArray.size(); ++i) {
				Ingredient ingredient = Ingredient.fromJson(pIngredientArray.get(i));
				if (!ingredient.isEmpty()) {
					nonnulllist.add(ingredient);
				}
			}
			return nonnulllist;
		}
		
	}

}