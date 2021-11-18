package com.example.examplemod.item;

import java.util.List;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class MultiPotion extends Item{

	public MultiPotion(Properties pProperties) {
		super(pProperties);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
		Player player = pLivingEntity instanceof Player ? (Player)pLivingEntity : null;
		if (player instanceof ServerPlayer) {
			CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)player, pStack);
		}
		if (!pLevel.isClientSide) {
			for(MobEffectInstance mobeffectinstance : PotionUtils.getMobEffects(pStack)) {
				if (mobeffectinstance.getEffect().isInstantenous()) {
					mobeffectinstance.getEffect().applyInstantenousEffect(player, player, pLivingEntity, mobeffectinstance.getAmplifier(), 1.0D);
				} else {
					pLivingEntity.addEffect(new MobEffectInstance(mobeffectinstance));
				}
			}
		}
		
		if (player != null) {
			player.awardStat(Stats.ITEM_USED.get(this));
			if (!player.getAbilities().instabuild) {
				pStack.shrink(1);
			}
		}
		
		if (player == null || !player.getAbilities().instabuild) {
			if (pStack.isEmpty()) {
				return this.bottle();
			}
			
			if (player != null) {
				player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
			}
		}
		
		pLevel.gameEvent(pLivingEntity, GameEvent.DRINKING_FINISH, pLivingEntity.eyeBlockPosition());
		return pStack;
	}
	
	public ItemStack bottle() {
		return new ItemStack(Items.GLASS_BOTTLE);
	}
	
	@Override
	public int getUseDuration(ItemStack pStack) {
		return 32;
	}
	
	@Override
	public UseAnim getUseAnimation(ItemStack pStack) {
		return UseAnim.DRINK;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
		return ItemUtils.startUsingInstantly(pLevel, pPlayer, pHand);
	}
	
	@Override
	public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents,
			TooltipFlag pIsAdvanced) {
		PotionUtils.addPotionTooltip(pStack, pTooltipComponents, 1.0F);
	}
	
	@Override
	public boolean isFoil(ItemStack pStack) {
		return true;
	}

}
