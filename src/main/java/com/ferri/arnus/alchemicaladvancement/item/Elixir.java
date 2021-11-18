package com.ferri.arnus.alchemicaladvancement.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class Elixir extends MultiPotion{
	
	public Elixir() {
		super(new Properties().stacksTo(6).tab(CreativeModeTab.TAB_BREWING));
	}
	
	@Override
	public ItemStack bottle() {
		return new ItemStack(ItemRegistry.ELIXIRBOTTLE.get());
	}
	
//	@Override
//	public void fillItemCategory(CreativeModeTab pCategory, NonNullList<ItemStack> pItems) {
//		
//		if (this.allowdedIn(pCategory)) {
//			for(Potion potion1 : Registry.POTION) {
//				if (potion1 != Potions.EMPTY) {
//						for(Potion potion2 : Registry.POTION) {
//							if (potion2 != Potions.EMPTY) {
//								for(Potion potion3 : Registry.POTION) {
//									if (potion3 != Potions.EMPTY) {
//										List<MobEffectInstance> list = new ArrayList<MobEffectInstance>();
//										list.addAll(potion1.getEffects());
//										list.addAll(potion2.getEffects());
//										list.addAll(potion3.getEffects());
//										list.sort(null);
//									if (list.size() == list.stream().map(e -> e.getDescriptionId()).distinct().count()) {
//										ItemStack stack3 = new ItemStack(this);
//										PotionUtils.setCustomEffects(stack3 , list);
//										ListTag tag = new ListTag();
//										tag.add(StringTag.valueOf(potion1.getRegistryName().toString()));
//										tag.add(StringTag.valueOf(potion2.getRegistryName().toString()));
//										tag.add(StringTag.valueOf(potion3.getRegistryName().toString()));
//										tag.sort((t1,t2) -> t1.getAsString().compareTo(t2.getAsString()));
//										stack3.getOrCreateTag().put("Potions", tag);
////										pItems.forEach(i -> {
////											if (ItemStack.isSame(stack3, i) && ItemStack.tagMatches(stack3, i)) {
////												pItems.remove(i);
////											}
////										});
//										if (!pItems.contains(stack3)) {
//											pItems.add(stack3);
//										}
//										for(Potion potion4 : Registry.POTION) {
//											if (potion4 != Potions.EMPTY) {
////												list.addAll(potion4.getEffects());
////												list.sort(null);
////												if (list.size() != list.stream().map(e -> e.getDescriptionId()).distinct().count()) {
////													ItemStack stack4 = new ItemStack(this);
////													PotionUtils.setCustomEffects(stack4 , list);
////													tag.add(StringTag.valueOf(potion4.getRegistryName().toString()));
////													tag.sort((t1,t2) -> t1.getAsString().compareTo(t2.getAsString()));
////													stack4.getOrCreateTag().put("Potions", tag);
//////													pItems.forEach(i -> {
//////														if (ItemStack.isSame(stack4, i) && ItemStack.tagMatches(stack4, i)) {
//////															pItems.remove(i);
//////														}
//////													});
//////													pItems.add(stack4);
////													if (!pItems.contains(stack4)) {
////														pItems.add(stack4);
////													}
////												}
//											}
//										}
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//	}
}
