package com.example.examplemod.item;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MultiThrowablePotion extends MultiPotion{

	public MultiThrowablePotion(Properties pProperties) {
		super(pProperties);
		// TODO Auto-generated constructor stub
	}
	
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
		if (!pLevel.isClientSide) {
			ThrownPotion thrownpotion = new ThrownPotion(pLevel, pPlayer);
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
