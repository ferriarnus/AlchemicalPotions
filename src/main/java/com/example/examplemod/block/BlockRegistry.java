package com.example.examplemod.block;

import com.example.examplemod.ExampleMod;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistry {
	
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ExampleMod.MODID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExampleMod.MODID);
	
	public static void register() {
		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	public static final RegistryObject<AlchemicalCauldron> ALCHEMICALCAULDRON = BLOCKS.register("alchemicalcauldron", AlchemicalCauldron::new);
	public static final RegistryObject<Item> ALCHEMICALCAULDRON_ITEM = ITEMS.register("alchemicalcauldron", () -> new BlockItem(ALCHEMICALCAULDRON.get(), new Properties()));
}
