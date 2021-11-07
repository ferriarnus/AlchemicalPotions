package com.example.examplemod.blockentity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.example.examplemod.crafting.CraftingRegistry;
import com.example.examplemod.crafting.IAlchemicalBrewing;
import com.example.examplemod.crafting.IHeatSource;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class CauldronBE extends BlockEntity{
	private CauldronHandler handler = new CauldronHandler(8);
	private LazyOptional<IItemHandler> lazy = LazyOptional.of(() -> handler);
	private FluidTank fluid = new FluidTank(1000);
	private LazyOptional<IFluidHandler> lazyfluid = LazyOptional.of(() -> fluid);
	public static final VoxelShape INSIDE = Block.box(2.0D, 4.0D, 2.0D, 14.0D, 8.0D, 14.0D);
	public int time = 0;
	public boolean active = false;
	
	public CauldronBE(BlockPos pWorldPosition, BlockState pBlockState) {
		super(BlockentityRegistry.CAULDRON.get(), pWorldPosition, pBlockState);
	}
	
	public static void servertick(Level pLevel, BlockPos pPos, BlockState pState, CauldronBE pBlockEntity) {
		if (pBlockEntity.time > 0) {
			pBlockEntity.time --;
		}
		List<IHeatSource> heat = pLevel.getRecipeManager().getAllRecipesFor(CraftingRegistry.HEATSOURCE_TYPE);
		Stream<IHeatSource> filter = heat.stream().filter(h -> h.getBlock().is(pLevel.getBlockState(pPos.below()).getBlock().asItem()));
		Optional<IHeatSource> heatrecipe = filter.findFirst();
		if (heatrecipe.isEmpty()) {
			return;
		}
		List<IAlchemicalBrewing> allRecipes = pLevel.getRecipeManager().getAllRecipesFor(CraftingRegistry.ALCHEMICALBREWING_TYPE);
		allRecipes.forEach(r -> {
			RecipeWrapper w = new RecipeWrapper(pBlockEntity.handler);
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
	}
	
	public static void entityInside(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity, CauldronBE be) {
		if (pEntity instanceof ItemEntity ie && Shapes.joinIsNotEmpty(Shapes.create(pEntity.getBoundingBox().move((double)(-pPos.getX()), (double)(-pPos.getY()), (double)(-pPos.getZ()))), INSIDE, BooleanOp.AND)) {
			for (int i=0; i< be.handler.getSlots(); i++) {
				ItemStack insertItem = be.handler.insertItem(i, ie.getItem(), true);
				if (!ItemStack.matches(insertItem, ie.getItem())) {
					ItemStack s = be.handler.insertItem(i, ie.getItem().copy(), false);
					if (s.isEmpty()) {
						ie.discard();
					} else {
						ie.getItem().shrink(s.getCount());
					}
					break;
				}
			}
			
		}
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return lazyfluid.cast();
		}
		return super.getCapability(cap, side);
	}
	
	private class CauldronHandler extends ItemStackHandler {
		
		public CauldronHandler(int size) {
			super(size);
		}
		
		@Override
		public int getSlotLimit(int slot) {
			return 1;
		}
	}

}
