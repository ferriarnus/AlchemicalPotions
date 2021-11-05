package com.example.examplemod.blockentity;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.block.BlockRegistry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockentityRegistry {

	public static final DeferredRegister<BlockEntityType<?>> BLOCKENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, ExampleMod.MODID);
	
	public static void register() {
		BLOCKENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	public static final RegistryObject<BlockEntityType<CauldronBE>> CAULDRON = BLOCKENTITIES.register("cauldron", ()-> BlockEntityType.Builder.of(CauldronBE::new, BlockRegistry.ALCHEMICALCAULDRON.get()).build(null));
}
