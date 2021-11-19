package com.ferri.arnus.alchemicaladvancement;

import com.ferri.arnus.alchemicaladvancement.block.BlockRegistry;
import com.ferri.arnus.alchemicaladvancement.blockentity.BlockentityRegistry;
import com.ferri.arnus.alchemicaladvancement.entity.EntityRegistry;
import com.ferri.arnus.alchemicaladvancement.item.ItemRegistry;

import net.minecraftforge.fml.common.Mod;

@Mod(AlchemicalAdvancement.MODID)
public class AlchemicalAdvancement {
    public static final String MODID = "alchemicaladvancement";
    
    public AlchemicalAdvancement() {
        BlockRegistry.register();
        BlockentityRegistry.register();
        ItemRegistry.register();
        EntityRegistry.register();
    }
}
