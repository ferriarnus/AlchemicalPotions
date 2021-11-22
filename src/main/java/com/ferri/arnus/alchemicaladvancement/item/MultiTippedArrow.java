package com.ferri.arnus.alchemicaladvancement.item;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;

public class MultiTippedArrow extends ArrowItem{

	public MultiTippedArrow() {
		super(new Properties());
	}
	
	@Override
	public AbstractArrow createArrow(Level pLevel, ItemStack pStack, LivingEntity pShooter) {
		Arrow arrow = new Arrow(pLevel, pShooter);
		Collection<MobEffectInstance> collection = PotionUtils.getCustomEffects(pStack);
        if (!collection.isEmpty()) {
           for(MobEffectInstance mobeffectinstance : collection) {
        	   arrow.effects.add(new MobEffectInstance(mobeffectinstance));
           }
        }
        
		return arrow;
	}
	
	public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
		PotionUtils.addPotionTooltip(pStack, pTooltip, 0.125F);
	}

}
