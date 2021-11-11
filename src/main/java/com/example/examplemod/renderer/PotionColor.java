package com.example.examplemod.renderer;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.Registry;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;

public class PotionColor implements ItemColor{

	@Override
	public int getColor(ItemStack p_92672_, int p_92673_) {
		if (!p_92672_.getOrCreateTag().contains("Potions")) {
			return 0xffffff;
		}
		ListTag list =  p_92672_.getOrCreateTag().getList("Potions", 8);
		if (p_92673_ == list.size()+1) {
			Potion potion = Registry.POTION.get(new ResourceLocation(list.getString(list.size()/2)));
			return PotionUtils.getColor(potion);
		}
		for (int i=0; i< list.size(); i++) {
			if (i+1 == p_92673_) {
				Potion potion = Registry.POTION.get(new ResourceLocation(list.getString(i)));
				return PotionUtils.getColor(potion);
			}
		}
		return 0xffffff;
	}

}
