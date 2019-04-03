package com.kandivia.runecrafting.main;

import net.minecraft.world.DimensionType;

public class ModInfo
{
    public static final String MOD_ID = "runecrafting";
    public static final String MOD_NAME = "Runecrafting";
    public static final String MOD_VERSION = "alpha-0.3";
    public static final String MC_VERSION = "1.12.2";
    public static final String PROXY_CLIENT = "com.kandivia.runecrafting.proxy.ClientProxy";
    public static final String PROXY_COMMON = "com.kandivia.runecrafting.proxy.CommonProxy";
    
    //Types
    public static final String[] runeTypes = new String[] {"air", "mind", "water", "earth", "fire", "body",
	    "cosmic", "chaos", "astral", "nature", "law", "death", "blood", "soul"};
    public static final String[] essenceTypes = new String[] {"rune", "pure"};
    public static final String[] staffTypes = new String[] {"normal", "air", "water", "earth", "fire"};
    
    //ConfigReader
    public static float mobsTalismanDropChance, mobsRuneDropChance, bossTalismanDropChance, bossRuneDropChance;
    public static boolean runecraftingRequiresLv;
    public static int altarDimensionId, altarMinDistance;
}