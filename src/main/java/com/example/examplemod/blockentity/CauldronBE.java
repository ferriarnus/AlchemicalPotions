package com.example.examplemod.blockentity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.example.examplemod.crafting.CraftingRegistry;
import com.example.examplemod.crafting.IAlchemicalBrewing;
import com.example.examplemod.crafting.IHeatSource;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class CauldronBE extends BlockEntity{
	private ItemStackHandler handler = new ItemStackHandler(5);
	private LazyOptional<IItemHandler> lazy = LazyOptional.of(() -> handler);
	public int time = 0;
	public boolean active = false;
	
	public CauldronBE(BlockPos pWorldPosition, BlockState pBlockState) {
		super(BlockentityRegistry.CAULDRON.get(), pWorldPosition, pBlockState);
	}
	
	public ItemStackHandler getHandler() {
		handler.insertItem(0, new ItemStack(Items.POTION), false);
		handler.insertItem(1, new ItemStack(Items.POTION), false);
		handler.insertItem(2, new ItemStack(Items.GLOWSTONE), false);
		handler.insertItem(3, new ItemStack(Items.GLOWSTONE), false);
		return handler;
	}
	
	public static void servertick(Level pLevel, BlockPos pPos, BlockState pState, CauldronBE pBlockEntity) {
		List<IHeatSource> heat = pLevel.getRecipeManager().getAllRecipesFor(CraftingRegistry.HEATSOURCE_TYPE);
		Stream<IHeatSource> filter = heat.stream().filter(h -> h.getBlock().is(pLevel.getBlockState(pPos.below()).getBlock().asItem()));
		Optional<IHeatSource> heatrecipe = filter.findFirst();
		if (heatrecipe.isEmpty()) {
			return;
		}
		List<IAlchemicalBrewing> allRecipes = pLevel.getRecipeManager().getAllRecipesFor(CraftingRegistry.ALCHEMICALBREWING_TYPE);
		allRecipes.forEach(r -> {
			RecipeWrapper w = new RecipeWrapper(pBlockEntity.getHandler());
			if (r.matches(w, pLevel)) {
				if (!pBlockEntity.active) {
					pBlockEntity.time = heatrecipe.get().getTime();
					pBlockEntity.active = true;
				}
				if (pBlockEntity.time == 0 && pBlockEntity.active == true) {
					Containers.dropItemStack(pLevel, pPos.getX(), pPos.getY() + 1.0D, pPos.getZ(), r.assemble(w));
					pBlockEntity.active = false;
				}
			}
		});
		if (pBlockEntity.time > 0) {
			pBlockEntity.time --;
		}
	}

}
