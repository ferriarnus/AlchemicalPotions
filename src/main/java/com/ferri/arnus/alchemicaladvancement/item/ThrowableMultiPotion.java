package com.ferri.arnus.alchemicaladvancement.item;

import com.ferri.arnus.alchemicaladvancement.entity.ThrownMultiPotion;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ThrowableMultiPotion extends MultiPotion{

	private boolean lingering =false;

	public ThrowableMultiPotion(Properties pProperties, boolean lingering) {
		super(pProperties);
		this.lingering  = lingering;
	}
	
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
		if (!pLevel.isClientSide) {
			ThrownMultiPotion thrownpotion = new ThrownMultiPotion(pLevel, pPlayer, lingering);
			thrownpotion.setItem(itemstack);
			thrownpotion.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), -20.0F, 0.5F, 1.0F);
			pLevel.addFreshEntity(thrownpotion);
		}
		
		pPlayer.awardStat(Stats.ITEM_USED.get(this));
		if (!pPlayer.getAbilities().instabuild) {
			itemstack.shrink(1);
		}
		
		return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
	}

}
