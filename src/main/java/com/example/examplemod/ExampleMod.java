package com.example.examplemod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.examplemod.block.BlockRegistry;
import com.example.examplemod.blockentity.BlockentityRegistry;
import com.example.examplemod.entity.EntityRegistry;
import com.example.examplemod.item.ItemRegistry;

import net.minecraftforge.fml.common.Mod;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExampleMod.MODID)
public class ExampleMod
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "examplemod";
    
    public ExampleMod() {
        BlockRegistry.register();
        BlockentityRegistry.register();
        ItemRegistry.register();
        EntityRegistry.register();
    }

    

}
