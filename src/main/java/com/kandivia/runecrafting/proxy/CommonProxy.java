package com.kandivia.runecrafting.proxy;

import com.kandivia.runecrafting.capabilities.IPlayerRcCap;
import com.kandivia.runecrafting.capabilities.PlayerRcCap;
import com.kandivia.runecrafting.capabilities.PlayerRcCapStorage;
import com.kandivia.runecrafting.command.TeleportCommand;
import com.kandivia.runecrafting.config.ConfigReader;
import com.kandivia.runecrafting.event.EventManager;
import com.kandivia.runecrafting.implement.RegisterObjects;
import com.kandivia.runecrafting.main.ModInfo;
import com.kandivia.runecrafting.main.Runecrafting;
import com.kandivia.runecrafting.network.RcTelePlayerMessage;
import com.kandivia.runecrafting.network.RcPlayerDataMessage;
import com.kandivia.runecrafting.world.AltarDimGen;
import com.kandivia.runecrafting.world.RCDimensions;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy
{
    public void registerModelBakeryVariants() {}

    public void preInit(FMLPreInitializationEvent event)
    {
	ConfigReader.init(event.getSuggestedConfigurationFile());
	MinecraftForge.EVENT_BUS.register(new RegisterObjects());
	MinecraftForge.EVENT_BUS.register(new EventManager());
	RCDimensions.init();
	GameRegistry.registerWorldGenerator(new AltarDimGen(), 0);
	CapabilityManager.INSTANCE.register(IPlayerRcCap.class, new PlayerRcCapStorage(), PlayerRcCap.class);
    }

    public void init(FMLInitializationEvent event)
    {
	registerModelBakeryVariants();
	
	int packetId = 0;
	Runecrafting.network = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.MOD_ID);
	Runecrafting.network.registerMessage(RcPlayerDataMessage.Handler.class, RcPlayerDataMessage.class, packetId++, Side.CLIENT);
	Runecrafting.network.registerMessage(RcTelePlayerMessage.Handler.class, RcTelePlayerMessage.class, packetId++, Side.SERVER);
	
    }

    public void serverLoad(FMLServerStartingEvent event)
    {
	event.registerServerCommand(new TeleportCommand());	
    }
}
