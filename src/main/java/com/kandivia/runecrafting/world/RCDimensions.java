package com.kandivia.runecrafting.world;

import com.kandivia.runecrafting.main.ModInfo;

import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class RCDimensions
{
    public static DimensionType altarDimensionType;

    public static void init() {
        registerDimensionTypes();
        registerDimensions();
    }

    private static void registerDimensionTypes() {
	altarDimensionType = DimensionType.register(ModInfo.MOD_ID, "_altar", ModInfo.altarDimensionId, AltarWorldProvider.class, false);
    }

    private static void registerDimensions() {
        DimensionManager.registerDimension(ModInfo.altarDimensionId, altarDimensionType);
    }
}
