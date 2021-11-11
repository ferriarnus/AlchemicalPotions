package com.example.examplemod.item;

import net.minecraft.world.item.CreativeModeTab;

public class Elixer extends MultiPotion{

	public Elixer() {
		super(new Properties().stacksTo(6).tab(CreativeModeTab.TAB_BREWING));
	}

}
