package com.kandivia.runecrafting.config;

import java.io.File;

import com.kandivia.runecrafting.main.ModInfo;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigReader
{
    public static void init(File configFile)
    {
	Configuration config = new Configuration(configFile);		
	try
	{
	    config.load();
	    
	    Property mobsTalismanDropProp = config.get("General", "mobsTalismanDropChance", 0.01);
	    mobsTalismanDropProp.setComment("Set the chance for Mobs to drop talismans. 0.01 = 1%");
	    ModInfo.mobsTalismanDropChance = (float) mobsTalismanDropProp.getDouble(0.01);
	    
	    Property mobsRuneDropProp = config.get("General", "mobsRuneDropChance", 0.1);
	    mobsRuneDropProp.setComment("Set the chance for Mobs to drop runes. 0.1 = 10%");
	    ModInfo.mobsRuneDropChance = (float) mobsRuneDropProp.getDouble(0.1);
	    
	    Property bossTalismanDropProp = config.get("General", "bossTalismanDropChance", 0.5);
	    bossTalismanDropProp.setComment("Set the chance for Boss Mobs to drop talismans. 0.5 = 50%");
	    ModInfo.bossTalismanDropChance = (float) bossTalismanDropProp.getDouble(0.5);
	    
	    Property bossRuneDropProp = config.get("General", "bossRuneDropChance", 0.7);
	    bossRuneDropProp.setComment("Set the chance for Boss Mobs to drop runes. 0.7 = 70%");
	    ModInfo.bossRuneDropChance = (float) bossRuneDropProp.getDouble(0.7);
	    
	    Property runecraftingRequiresLvProp = config.get("Runecrafting", "runecraftingRequiresLv", true);
	    runecraftingRequiresLvProp.setComment("If true, runecrafting altars require the appropriate runecrafting level");
	    ModInfo.runecraftingRequiresLv = runecraftingRequiresLvProp.getBoolean(true);
	    
	    Property altarDimensionIdProp = config.get("General", "altarDimensionId", 100);
	    altarDimensionIdProp.setComment("Sets Dimension ID to be used");
	    ModInfo.altarDimensionId = altarDimensionIdProp.getInt(100);
	    
	    Property altarMinDistanceProp = config.get("General", "altarMinDistance", 300);
	    altarMinDistanceProp.setComment("Sets mininum distance altars can generate from each other");
	    ModInfo.altarMinDistance = altarMinDistanceProp.getInt(300);
	}
	catch(Exception e)
	{
	    System.out.println("Runecrafting: Failed to Load Config File!");
	}
	finally
	{
	    config.save();
	}
    }
}