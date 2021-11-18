package com.ferri.arnus.alchemicaladvancement.crafting.brewing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
		for (int i=0; i< 8; i++) {
			if (i%2==0) {
				this.ingredients.add(this.potions.get(i/2));
			} else {
				this.ingredients.add(this.binders.get((i-1)/2));
			}
		}
		this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
	}

	@Override
	public boolean matches(RecipeWrapper pContainer, Level pLevel) {
		for (int i = 0; i< pContainer.getContainerSize(); i++) {
			if (!ingredients.get(i).test(pContainer.getItem(i))) {
				return false;
			}
		}
		return true;
	}	
	

	@Override
	public ItemStack assemble(RecipeWrapper pContainer) {
		ItemStack stack = getResultItem().copy();
		List<MobEffectInstance> list = new ArrayList<>();
		List<ItemStack> potions = new ArrayList<>();
		for (int i=0; i< pContainer.getContainerSize(); i++) {
			if (i%2==0) {
				if (pContainer.getItem(i).is(Items.POTION)) {
					potions.add(pContainer.getItem(i));
				}
			}
		}
		AtomicBoolean empty = new AtomicBoolean(false);
		potions.forEach(p -> {
			if (PotionUtils.getPotion(p).getEffects().isEmpty()) {
				empty.set(true);
			}
		});
		if (empty.get()) {
			return ItemStack.EMPTY;
		}
		potions.forEach(p -> list.addAll(PotionUtils.getMobEffects(p)));
		if (list.size() != list.stream().map(e -> e.getDescriptionId()).distinct().count()) {
			return ItemStack.EMPTY;
		}
		list.sort(null);
		PotionUtils.setCustomEffects(stack, list);
		ListTag tag = new ListTag();
		potions.forEach(p -> tag.add(StringTag.valueOf(PotionUtils.getPotion(p).getRegistryName().toString())));
		tag.sort((t1,t2) -> t1.getAsString().compareTo(t2.getAsString()));
		stack.getOrCreateTag().put("Potions", tag);
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
			NonNullList<Ingredient> nonnulllist = NonNullList.withSize(4, Ingredient.of(ItemStack.EMPTY));
			for(int i = 0; i < pIngredientArray.size(); ++i) {
				Ingredient ingredient = Ingredient.fromJson(pIngredientArray.get(i));
				if (!ingredient.isEmpty()) {
					nonnulllist.set(i,ingredient);
				}
			}
			return nonnulllist;
		}
		
	}

}
