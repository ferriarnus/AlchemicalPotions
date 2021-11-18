package com.ferri.arnus.alchemicaladvancement.crafting.heat;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class HeatSource implements IHeatSource{
	
	public static final RecipeSerializer<IHeatSource> SERIALIZER = new Serializer();
	private ResourceLocation id ;
	private ItemStack block;
	private int time;

	public HeatSource(ResourceLocation id, ItemStack block, int time) {
		this.id = id;
		this.block = block;
		this.time = time;
	}
	
	@Override
	public boolean matches(RecipeWrapper pContainer, Level pLevel) {
		return true;
	}

	@Override
	public ItemStack assemble(RecipeWrapper pContainer) {
		return getResultItem();
	}

	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight) {
		return true;
	}

	@Override
	public ItemStack getResultItem() {
		return ItemStack.EMPTY;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public int getTime() {
		return time;
	}
	
	@Override
	public ItemStack getBlock() {
		return block;
	}
	
	static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<IHeatSource> {

		@Override
		public IHeatSource fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
			ItemStack block = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "block"));
			int time = pSerializedRecipe.getAsJsonObject().get("time").getAsInt();
			return new HeatSource(pRecipeId, block, time);
		}

		@Override
		public IHeatSource fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
			ItemStack block = pBuffer.readItem();
			int time = pBuffer.readVarInt();
			return new HeatSource(pRecipeId, block, time);
		}

		@Override
		public void toNetwork(FriendlyByteBuf pBuffer, IHeatSource pRecipe) {
			pBuffer.writeItem(pRecipe.getBlock());
			pBuffer.writeVarInt(pRecipe.getTime());
		}
		
	}

}
