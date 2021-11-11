package com.example.examplemod.item;

import com.example.examplemod.ExampleMod;

import net.minecraft.world.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExampleMod.MODID);
	
	public static void register() {
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	public static final RegistryObject<MultiPotionBottle> DRAUGHTBOTTLE = ITEMS.register("draught_bottle", MultiPotionBottle::new);
	public static final RegistryObject<Draught> DRAUGHT = ITEMS.register("draught", Draught::new);
	public static final RegistryObject<SplashDraught> SPLASH_DRAUGHT = ITEMS.register("splash_draught", SplashDraught::new);
	public static final RegistryObject<LingeringDraught> LINGERING_DRAUGHT = ITEMS.register("lingering_draught", LingeringDraught::new);
	
	public static final RegistryObject<MultiPotionBottle> ELIXERBOTTLE = ITEMS.register("elixer_bottle", MultiPotionBottle::new);
	public static final RegistryObject<Elixer> ELIXER = ITEMS.register("elixer", Elixer::new);
}
