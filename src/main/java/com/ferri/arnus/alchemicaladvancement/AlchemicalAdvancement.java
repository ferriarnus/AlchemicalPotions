package com.ferri.arnus.alchemicaladvancement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferri.arnus.alchemicaladvancement.block.BlockRegistry;
import com.ferri.arnus.alchemicaladvancement.blockentity.BlockentityRegistry;
import com.ferri.arnus.alchemicaladvancement.entity.EntityRegistry;
import com.ferri.arnus.alchemicaladvancement.item.ItemRegistry;

import net.minecraftforge.fml.common.Mod;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AlchemicalAdvancement.MODID)
public class AlchemicalAdvancement
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "alchemicaladvancement";
    
    public AlchemicalAdvancement() {
        BlockRegistry.register();
        BlockentityRegistry.register();
        ItemRegistry.register();
        EntityRegistry.register();
    }
}
