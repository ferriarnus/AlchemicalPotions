package com.example.examplemod.item;

import net.minecraft.world.item.CreativeModeTab;

public class Elixir extends MultiPotion{

	public Elixir() {
		super(new Properties().stacksTo(6).tab(CreativeModeTab.TAB_BREWING));
	}

}
