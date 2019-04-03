package com.kandivia.runecrafting.main;

import org.apache.logging.log4j.Logger;

import com.kandivia.runecrafting.capabilities.IPlayerRcCap;
import com.kandivia.runecrafting.proxy.CommonProxy;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.MOD_VERSION, useMetadata = true)
public class Runecrafting {
    @SidedProxy(clientSide = ModInfo.PROXY_CLIENT, serverSide = ModInfo.PROXY_COMMON)
    public static CommonProxy proxy;
    
    @Instance
    public static Runecrafting instance;
    
    public static Logger logger;
    
    @CapabilityInject(IPlayerRcCap.class)
    public static final Capability<IPlayerRcCap> PLAYER_CAP = null;
    
    public static SimpleNetworkWrapper network;
    
    public static RunecraftingTab RunecraftTab = new RunecraftingTab(ModInfo.MOD_ID);
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
	logger = event.getModLog();
	proxy.preInit(event);
    }
    
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
	proxy.init(event);
    }
    
    
    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
	proxy.serverLoad(event);
    }
}