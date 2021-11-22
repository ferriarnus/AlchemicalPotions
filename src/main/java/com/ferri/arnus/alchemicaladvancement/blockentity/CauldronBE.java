package com.ferri.arnus.alchemicaladvancement.blockentity;

import java.awt.Color;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.ferri.arnus.alchemicaladvancement.crafting.CraftingRegistry;
import com.ferri.arnus.alchemicaladvancement.crafting.brewing.IAlchemicalBrewing;
import com.ferri.arnus.alchemicaladvancement.crafting.heat.IHeatSource;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class CauldronBE extends BlockEntity{
	private CauldronHandler handler = new CauldronHandler(8);
	private LazyOptional<IItemHandler> lazy = LazyOptional.of(() -> handler);
	private FluidTank fluid = new FluidTank(1000) {
		@Override
		public FluidStack drain(int maxDrain, FluidAction action) {
			if ( 1000 <= maxDrain && handler.getStackInSlot(0).isEmpty()) {
				return super.drain(maxDrain, action);
			}else {
				super.drain(maxDrain, action);
			}
			return FluidStack.EMPTY;
		}
	};
	private LazyOptional<IFluidHandler> lazyfluid = LazyOptional.of(() -> fluid);
	private static final VoxelShape INSIDE = Block.box(2.0D, 4.0D, 2.0D, 14.0D, 8.0D, 14.0D);
	private int time = 0;
	private boolean active = false;
	private boolean heat = false;
	private int color = Fluids.WATER.getAttributes().getColor();
	private ItemStack result = ItemStack.EMPTY;
	private int totaltime = 0;
	
	public CauldronBE(BlockPos pWorldPosition, BlockState pBlockState) {
		super(BlockentityRegistry.CAULDRON.get(), pWorldPosition, pBlockState);
	}
	
	public boolean isActive() {
		return active;
	}
	
	public boolean hasHeat() {
		return heat;
	}
	
	public int getTime() {
		return time;
	}
	
	public float getHeigth() {
		return ((float) this.fluid.getFluidAmount())/1000F;
	}
	
	public int getColor() {
		return color ;
	}
	
	public ItemStack getResult() {
		return result;
	}
	
	public ItemStack HandleResult() {
		if (result.isEmpty() || active) {
			return ItemStack.EMPTY;
		}
		if (result.getCount() > 1) {
			result.shrink(1);
			fluid.drain(500, FluidAction.EXECUTE);
			ItemStack s = result.copy();
			s.setCount(1);
			return s;
		}
		handler.clear();
		fluid.setFluid(FluidStack.EMPTY);
		color = Fluids.WATER.getAttributes().getColor();
		ItemStack s = result.copy();
		result = ItemStack.EMPTY;
		return s;
	}
	
	public void clear() {
		handler.clear();
		fluid.drain(1000, FluidAction.EXECUTE);
		time = 0;
		active = false;
		result = ItemStack.EMPTY;
		color = Fluids.WATER.getAttributes().getColor();
	}
	
	public float activeColor() {
		return 1F - ((float) time/ (float) totaltime);
	}
	
	public static void servertick(Level pLevel, BlockPos pPos, BlockState pState, CauldronBE pBlockEntity) {
		if (pBlockEntity.time > 0) {
			pBlockEntity.time --;
		}
		List<IHeatSource> heat = pLevel.getRecipeManager().getAllRecipesFor(CraftingRegistry.HEATSOURCE_TYPE);
		Stream<IHeatSource> filter = heat.stream().filter(h -> h.getBlock().is(pLevel.getBlockState(pPos.below()).getBlock().asItem()));
		Optional<IHeatSource> heatrecipe = filter.findFirst();
		if (heatrecipe.isEmpty()) {
			pBlockEntity.heat = false;
			pLevel.sendBlockUpdated(pPos, pState, pState, 2);
			return;
		}
		pBlockEntity.heat = true;
		RecipeWrapper w = new RecipeWrapper(pBlockEntity.handler);
		Optional<IAlchemicalBrewing> recipeFor = pLevel.getRecipeManager().getRecipeFor(CraftingRegistry.ALCHEMICALBREWING_TYPE, w, pLevel);
		if (recipeFor.isPresent()) {
			if (!pBlockEntity.active && pBlockEntity.result.isEmpty()) {
				pBlockEntity.time = heatrecipe.get().getTimeModifier() * recipeFor.get().getTime();
				pBlockEntity.totaltime = pBlockEntity.time;
				pBlockEntity.active = true;
				pLevel.sendBlockUpdated(pPos, pState, pState, 2);
			}
			if (pBlockEntity.time == 0 && pBlockEntity.active == true) {
				pBlockEntity.result = recipeFor.get().assemble(w);
				pBlockEntity.active = false;
			}
		}
		pLevel.sendBlockUpdated(pPos, pState, pState, 2);
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
	public CompoundTag save(CompoundTag pTag) {
		pTag.putBoolean("active", active);
		pTag.putBoolean("heat", heat);
		pTag.putInt("color", color);
		pTag.putInt("time", time);
		pTag.putInt("totaltime", totaltime);
		fluid.writeToNBT(pTag);
		pTag.put("Items", handler.serializeNBT());
		return super.save(pTag);
	}
	
	@Override
	public void load(CompoundTag pTag) {
		this.active = pTag.getBoolean("active");
		this.heat = pTag.getBoolean("heat");
		this.color = pTag.getInt("color");
		this.time = pTag.getInt("time");
		this.totaltime = pTag.getInt("totaltime");
		fluid.readFromNBT(pTag);
		handler.deserializeNBT(pTag.getCompound("Items"));
		super.load(pTag);
	}
	
	@Override
	public void handleUpdateTag(CompoundTag tag) {
		super.handleUpdateTag(tag);;
		load(tag);
	}
	
	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag pTag = super.getUpdateTag();
		save(pTag);
		return pTag;
	}
	
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return new ClientboundBlockEntityDataPacket(getBlockPos(), 1, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		handleUpdateTag(pkt.getTag());
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
		
		public void clear() {
			this.stacks = NonNullList.withSize(getSlots(), ItemStack.EMPTY);
		}
		
		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			if (active || !result.isEmpty() || fluid.getFluidAmount() != fluid.getCapacity()) {
				return stack;
			}
			return super.insertItem(slot, stack, simulate);
		}
		
		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			return ItemStack.EMPTY;
		}

		@Override
		public int getSlotLimit(int slot) {
			return 1;
		}
		
		@Override
		protected void onContentsChanged(int slot) {
			if (stacks.get(slot).is(Items.POTION)) {
				color =  PotionUtils.getColor(stacks.get(slot));
			} else {
				color = Color.YELLOW.getRGB();
			}
			setChanged();
		}
	}
}
