package com.example.examplemod.item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;

public class Draught extends MultiPotion{

	public Draught() {
		super(new Properties().tab(CreativeModeTab.TAB_BREWING).stacksTo(8));
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
										list.sort(null);
										PotionUtils.setCustomEffects(stack, list);
										ListTag tag = new ListTag();
										tag.add(StringTag.valueOf(potion1.getRegistryName().toString()));
										tag.add(StringTag.valueOf(potion2.getRegistryName().toString()));
										tag.sort((t1,t2) -> t1.getAsString().compareTo(t2.getAsString()));
										stack.getOrCreateTag().put("Potions", tag);
										pItems.forEach(i -> {
											if (ItemStack.tagMatches(stack, i)) {
												pItems.remove(i);
											}
										});
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
