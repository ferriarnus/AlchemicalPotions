package com.ferri.arnus.alchemicaladvancement.blockentity;

import com.ferri.arnus.alchemicaladvancement.AlchemicalAdvancement;
import com.ferri.arnus.alchemicaladvancement.block.BlockRegistry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockentityRegistry {

	public static final DeferredRegister<BlockEntityType<?>> BLOCKENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, AlchemicalAdvancement.MODID);
	
	public static void register() {
		BLOCKENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	public static final RegistryObject<BlockEntityType<CauldronBE>> CAULDRON = BLOCKENTITIES.register("cauldron", ()-> BlockEntityType.Builder.of(CauldronBE::new, BlockRegistry.ALCHEMICALCAULDRON.get()).build(null));
}
