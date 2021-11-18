package com.example.examplemod.block;

import javax.annotation.Nullable;

import com.example.examplemod.blockentity.BlockentityRegistry;
import com.example.examplemod.blockentity.CauldronBE;
import com.example.examplemod.item.ItemRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidUtil;

public class AlchemicalCauldron extends Block implements EntityBlock{
	private static final VoxelShape INSIDE = box(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	protected static final VoxelShape SHAPE = Shapes.join(Shapes.block(), Shapes.or(box(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), box(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), INSIDE), BooleanOp.ONLY_FIRST);   

	public AlchemicalCauldron() {
		super(Properties.copy(Blocks.CAULDRON));
	}

	@Override
	public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_,
			CollisionContext p_60558_) {
		return SHAPE;
	}
	
	@Override
	public VoxelShape getInteractionShape(BlockState p_60547_, BlockGetter p_60548_, BlockPos p_60549_) {
		return INSIDE;
	}
	
	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand,
			BlockHitResult pHit) {
		if (pLevel.getBlockEntity(pPos) instanceof CauldronBE be) {
			if (pPlayer.isCrouching() && pPlayer.getItemInHand(pHand).isEmpty()) {
				be.clear();
				return InteractionResult.SUCCESS;
			}
			FluidUtil.interactWithFluidHandler(pPlayer, pHand, pLevel, pPos, null);
			if (pPlayer.getItemInHand(pHand).is(ItemRegistry.DRAUGHTBOTTLE.get()) && !be.getResult().isEmpty() && be.getResult().is(ItemRegistry.DRAUGHT.get()) ) {
				pPlayer.getItemInHand(pHand).shrink(1);
				pPlayer.addItem(be.HandleResult());
			}
			if (pPlayer.getItemInHand(pHand).is(ItemRegistry.ELIXIRBOTTLE.get()) && !be.getResult().isEmpty() && be.getResult().is(ItemRegistry.ELIXIR.get()) ) {
				pPlayer.getItemInHand(pHand).shrink(1);
				pPlayer.addItem(be.HandleResult());
			}
			return InteractionResult.SUCCESS;
		}
		return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
		return BlockentityRegistry.CAULDRON.get().create(p_153215_, p_153216_);
	}
	
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState,
			BlockEntityType<T> pBlockEntityType) {
		return createTickerHelper(pBlockEntityType, BlockentityRegistry.CAULDRON.get(), CauldronBE::servertick);
	}
	
	@Nullable
	protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> p_152133_, BlockEntityType<E> p_152134_, BlockEntityTicker<? super E> p_152135_) {
		return p_152134_ == p_152133_ ? (BlockEntityTicker<A>)p_152135_ : null;
	}
	
	@Override
	public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
		if (pLevel.getBlockEntity(pPos) instanceof CauldronBE be) {
			CauldronBE.entityInside(pLevel, pPos, pState, pEntity, be);
		}
	}
}
