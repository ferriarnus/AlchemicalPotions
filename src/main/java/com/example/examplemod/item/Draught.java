package com.example.examplemod.item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class Draught extends Item{

	public Draught() {
		super(new Properties().tab(CreativeModeTab.TAB_BREWING));
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
				return new ItemStack(Items.GLASS_BOTTLE);
			}
			
			if (player != null) {
				player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
			}
		}
		
		pLevel.gameEvent(pLivingEntity, GameEvent.DRINKING_FINISH, pLivingEntity.eyeBlockPosition());
		return pStack;
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
	public String getDescriptionId(ItemStack pStack) {
		AtomicReference<String> atom = new AtomicReference<String>(this.getDescriptionId());
		PotionUtils.getMobEffects(pStack).forEach(e -> {
			atom.set(atom.get()  + "." + e.getDescriptionId());
		});
		return atom.get();
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
	
	@Override
	public void fillItemCategory(CreativeModeTab pCategory, NonNullList<ItemStack> pItems) {
		if (this.allowdedIn(pCategory)) {
			for(Potion potion1 : Registry.POTION) {
				if (potion1 != Potions.EMPTY) {
					for(Potion potion2 : Registry.POTION) {
						if (potion2 != Potions.EMPTY) {
							if (potion1 != potion2) {
								if (!potion1.getEffects().isEmpty() && !potion2.getEffects().isEmpty()) {
									AtomicBoolean same = new AtomicBoolean(false);
									potion1.getEffects().forEach(e1 -> {
										potion2.getEffects().forEach(e2 -> {
											if (e1.getDescriptionId().equals(e2.getDescriptionId())) {
												same.set(true);
											}
										});
									});
									if (!same.get()) {
										ItemStack stack = new ItemStack(this);
										List<MobEffectInstance> list = new ArrayList<>();
										list.addAll(potion1.getEffects());
										list.addAll(potion2.getEffects());
										PotionUtils.setCustomEffects(stack, list);
										pItems.add(stack);
									}
								}
							}
						}
					}
				}
			}
		}
		
	}

}
