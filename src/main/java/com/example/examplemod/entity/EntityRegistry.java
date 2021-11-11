package com.example.examplemod.entity;

import com.example.examplemod.ExampleMod;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityRegistry {

public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ExampleMod.MODID);
	
	public static void register() {
		ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	public static final RegistryObject<EntityType<ThrownMultiPotion>> MULTIPOTION = ENTITIES.register("multipotion", ()-> EntityType.Builder.<ThrownMultiPotion>of((t,l)-> new ThrownMultiPotion(l), MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("multipotion"));
}
