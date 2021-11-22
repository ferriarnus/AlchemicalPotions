package com.ferri.arnus.alchemicaladvancement.item;

import com.ferri.arnus.alchemicaladvancement.AlchemicalAdvancement;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AlchemicalAdvancement.MODID);
	
	public static void register() {
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	public static final RegistryObject<MultiPotionBottle> DRAUGHTBOTTLE = ITEMS.register("draught_bottle", MultiPotionBottle::new);
	public static final RegistryObject<Draught> DRAUGHT = ITEMS.register("draught", Draught::new);
	public static final RegistryObject<ThrowableMultiPotion> SPLASH_DRAUGHT = ITEMS.register("splash_draught", () -> new ThrowableMultiPotion(new Properties().tab(CreativeModeTab.TAB_BREWING).stacksTo(8), false));
	public static final RegistryObject<ThrowableMultiPotion> LINGERING_DRAUGHT = ITEMS.register("lingering_draught", () -> new ThrowableMultiPotion(new Properties().tab(CreativeModeTab.TAB_BREWING).stacksTo(8), true));
	
	public static final RegistryObject<MultiPotionBottle> ELIXIRBOTTLE = ITEMS.register("elixir_bottle", MultiPotionBottle::new);
	public static final RegistryObject<Elixir> ELIXIR = ITEMS.register("elixir", Elixir::new);
	public static final RegistryObject<ThrowableMultiPotion> SPLASH_ELIXIR = ITEMS.register("splash_elixir", () -> new ThrowableMultiPotion(new Properties().tab(CreativeModeTab.TAB_BREWING).stacksTo(6), false));
	public static final RegistryObject<ThrowableMultiPotion> LINGERING_ELIXIR = ITEMS.register("lingering_elixir", () -> new ThrowableMultiPotion(new Properties().tab(CreativeModeTab.TAB_BREWING).stacksTo(6), true));
	
	public static final RegistryObject<MultiTippedArrow> MULTITIPPED_ARROW = ITEMS.register("multitipped_arrow", MultiTippedArrow::new);

}
