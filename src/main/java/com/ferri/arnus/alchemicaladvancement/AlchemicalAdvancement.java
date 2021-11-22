package com.ferri.arnus.alchemicaladvancement;

import com.ferri.arnus.alchemicaladvancement.block.BlockRegistry;
import com.ferri.arnus.alchemicaladvancement.blockentity.BlockentityRegistry;
import com.ferri.arnus.alchemicaladvancement.entity.EntityRegistry;
import com.ferri.arnus.alchemicaladvancement.item.ItemRegistry;
import com.ferri.arnus.alchemicaladvancement.particle.ColoredSmokeData;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AlchemicalAdvancement.MODID)
public class AlchemicalAdvancement {
    public static final String MODID = "alchemicaladvancement";
    
    public AlchemicalAdvancement() {
        BlockRegistry.register();
        BlockentityRegistry.register();
        ItemRegistry.register();
        EntityRegistry.register();
        
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addGenericListener(ParticleType.class, this::registerParticleTypes);
        modEventBus.addListener(this::registerParticleFactory);
    }
    
	public void registerParticleTypes(RegistryEvent.Register<ParticleType<?>> event) {
        event.getRegistry().registerAll(
                new ColoredSmokeData.Type(false).setRegistryName("colored_smoke")
        );
    }

    public void registerParticleFactory(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(ColoredSmokeData.TYPE, ColoredSmokeData.Factory::new);
    }
}
