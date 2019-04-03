package com.kandivia.runecrafting.event;

import java.util.Collection;
import java.util.Random;

import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.kandivia.runecrafting.capabilities.IPlayerRcCap;
import com.kandivia.runecrafting.capabilities.PlayerRcCapProvider;
import com.kandivia.runecrafting.implement.EnumHandler;
import com.kandivia.runecrafting.implement.RegisterObjects;
import com.kandivia.runecrafting.main.Runecrafting;
import com.kandivia.runecrafting.main.ModInfo;
import com.kandivia.runecrafting.network.RcPlayerDataMessage;
import com.kandivia.runecrafting.spells.Spell;
import com.kandivia.runecrafting.world.RCWorldData;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.KilledByPlayer;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.conditions.RandomChanceWithLooting;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

public class EventManager
{
    @SubscribeEvent
    public void onWorldCreation(WorldEvent.Load event)
    {
	if(!RCWorldData.get(event.getWorld()).hasAllAltarsSpawned() && event.getWorld().provider.getDimension() == 0 && !event.getWorld().isRemote &&
		event.getWorld().getWorldType() != WorldType.FLAT)
	{
	    for(int i = 0; i < 13; i++)
	    {
		Spell.locateRcStructure(event.getWorld(), event.getWorld().getSpawnPoint(), i, 5, 2, 10387313, 100);
	    }
	}
    }
    
    
    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event)
    {
	EntityPlayer player = event.getEntityPlayer();
	IPlayerRcCap playerRcCap = player.getCapability(PlayerRcCapProvider.PLAYER_RC_CAP, null);
	IPlayerRcCap oldPlayerLevels = event.getOriginal().getCapability(PlayerRcCapProvider.PLAYER_RC_CAP, null);
	
	playerRcCap.setMagicExp(oldPlayerLevels.getMagicExp());
	playerRcCap.setRuneExp(oldPlayerLevels.getRuneExp());
	playerRcCap.setLastPos(new BlockPos(oldPlayerLevels.getXPos(), oldPlayerLevels.getYPos(), oldPlayerLevels.getZPos()));
	playerRcCap.setSpellId(oldPlayerLevels.getSpellId());
    }
    
    
    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event)
    {
	Entity entity = event.getObject();
	
	if(entity instanceof EntityPlayer)
	{
	    event.addCapability(new ResourceLocation(ModInfo.MOD_ID, "capability_player_levels"), new PlayerRcCapProvider());
	}
    }
    
    
    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event)
    {
	
	IPlayerRcCap playerRcCap = event.player.getCapability(PlayerRcCapProvider.PLAYER_RC_CAP, null);
	if(!event.player.world.isRemote)
	    Runecrafting.network.sendTo(new RcPlayerDataMessage(playerRcCap.getMagicExp(), playerRcCap.getRuneExp(), playerRcCap.getXPos(),
		    playerRcCap.getYPos(), playerRcCap.getZPos(), playerRcCap.getSpellId()), (EntityPlayerMP) event.player);
    }
    
    
    @SubscribeEvent
    public void playerChangeDimension(PlayerChangedDimensionEvent event)
    {
	IPlayerRcCap playerRcCap = event.player.getCapability(PlayerRcCapProvider.PLAYER_RC_CAP, null);
	if(!event.player.world.isRemote)
	    Runecrafting.network.sendTo(new RcPlayerDataMessage(playerRcCap.getMagicExp(), playerRcCap.getRuneExp(), playerRcCap.getXPos(),
		    playerRcCap.getYPos(), playerRcCap.getZPos(), playerRcCap.getSpellId()), (EntityPlayerMP) event.player);
    }
    
    
    @SubscribeEvent
    public void playerRespawn(PlayerRespawnEvent event)
    {
	IPlayerRcCap playerRcCap = event.player.getCapability(PlayerRcCapProvider.PLAYER_RC_CAP, null);
	if(!event.player.world.isRemote)
	    Runecrafting.network.sendTo(new RcPlayerDataMessage(playerRcCap.getMagicExp(), playerRcCap.getRuneExp(), playerRcCap.getXPos(),
		    playerRcCap.getYPos(), playerRcCap.getZPos(), playerRcCap.getSpellId()), (EntityPlayerMP) event.player);
    }
    
    
    @SubscribeEvent
    public void addLootTables(LootTableLoadEvent event)
    {
	String runePool = "RunePool";
	String taliPool = "TalismanPool";
	String chests = "minecraft:chests/";
	String entities = "minecraft:entities/";
	String eventName = event.getName().toString();
	
	if(eventName.startsWith(entities))
	{
	    switch(eventName.substring(entities.length())) {
	    case "bat":
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 0, 0, 8, 16, true, false);
		
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 0, 0, 1, 1, true, false);
		break;
	    case "blaze":
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 0, 4, 1, 16, true, false);
		
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 0, 4, 1, 1, true, false);
		break;
	    case "cave_spider":
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 0, 3, 1, 16, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	3, 0, 5, 1, 16, true, false);
		
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	3, 0, 3, 1, 1, true, false);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	6, 0, 5, 1, 1, true, false);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 0, 12, 1, 1, true, false);
		
		break;
	    case "creeper":
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	10, 0, 3, 1, 16, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	10, 0, 4, 1, 16, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 1, 9, 1, 8, true, false);
		
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	3, 0, 4, 1, 1, true, false);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 1, 9, 1, 1, true, false);
		break;
	    case "elder_guardian":
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	4, 0, 2, 6, 24, true, true);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	4, 0, 5, 8, 16, true, true);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	2, 1, 7, 6, 12, true, true);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	2, 1, 9, 6, 12, true, true);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 1, 13, 2, 8, true, true);
		
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	2, 0, 2, 1, 1, true, true);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	2, 0, 5, 1, 1, true, true);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 1, 7, 1, 1, true, true);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 1, 9, 1, 1, true, true);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 1, 13, 1, 1, true, true);
		break;
	    case "ender_dragon":
		addEntityPoolEntry(event, runePool, 1, 4, RegisterObjects.runes, 	2, 0, 0, 12, 48, true, true);
		addEntityPoolEntry(event, runePool, 1, 4, RegisterObjects.runes, 	2, 0, 1, 12, 48, true, true);
		addEntityPoolEntry(event, runePool, 1, 4, RegisterObjects.runes, 	2, 0, 4, 12, 48, true, true);
		addEntityPoolEntry(event, runePool, 1, 4, RegisterObjects.runes, 	2, 0, 5, 12, 48, true, true);
		addEntityPoolEntry(event, runePool, 1, 4, RegisterObjects.runes, 	1, 0, 6, 6, 32, true, true);
		addEntityPoolEntry(event, runePool, 1, 4, RegisterObjects.runes, 	3, 1, 7, 6, 32, true, true);
		addEntityPoolEntry(event, runePool, 1, 4, RegisterObjects.runes, 	1, 1, 8, 6, 32, true, true);
		addEntityPoolEntry(event, runePool, 1, 4, RegisterObjects.runes, 	3, 1, 10, 6, 32, true, true);
		addEntityPoolEntry(event, runePool, 1, 4, RegisterObjects.runes, 	1, 1, 11, 6, 32, true, true);
		addEntityPoolEntry(event, runePool, 1, 4, RegisterObjects.runes, 	1, 1, 12, 6, 32, true, true);
		
		addEntityPoolEntry(event, taliPool, 1, 3, RegisterObjects.talismans, 	2, 0, 0, 1, 1, true, true);
		addEntityPoolEntry(event, taliPool, 1, 3, RegisterObjects.talismans, 	2, 0, 1, 1, 1, true, true);
		addEntityPoolEntry(event, taliPool, 1, 3, RegisterObjects.talismans, 	2, 0, 4, 1, 1, true, true);
		addEntityPoolEntry(event, taliPool, 1, 3, RegisterObjects.talismans, 	2, 0, 5, 1, 1, true, true);
		addEntityPoolEntry(event, taliPool, 1, 3, RegisterObjects.talismans, 	1, 1, 6, 1, 1, true, true);
		addEntityPoolEntry(event, taliPool, 1, 3, RegisterObjects.talismans, 	1, 1, 7, 1, 1, true, true);
		addEntityPoolEntry(event, taliPool, 1, 3, RegisterObjects.talismans, 	1, 1, 8, 1, 1, true, true);
		addEntityPoolEntry(event, taliPool, 1, 3, RegisterObjects.talismans, 	1, 1, 10, 1, 1, true, true);
		addEntityPoolEntry(event, taliPool, 1, 3, RegisterObjects.talismans, 	1, 1, 11, 1, 1, true, true);
		addEntityPoolEntry(event, taliPool, 1, 3, RegisterObjects.talismans, 	1, 1, 12, 1, 1, true, true);
		break;
	    case "enderman":
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	3, 0, 5, 1, 16, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 0, 6, 1, 8, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	2, 0, 7, 1, 8, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	2, 1, 10, 1, 8, true, false);
		
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 0, 6, 1, 1, true, false);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 0, 10, 1, 1, true, false);
		break;
	    case "endermite":
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	4, 0, 3, 1, 6, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	4, 0, 5, 1, 5, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 0, 6, 1, 4, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	2, 0, 10, 1, 3, true, false);
		
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 0, 6, 1, 1, true, false);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 0, 10, 1, 1, true, false);
		break;
	    case "evocation_illager":
		addEntityPoolEntry(event, runePool, 1, 2, RegisterObjects.runes, 	2, 0, 1, 24, 56, true, true);
		addEntityPoolEntry(event, runePool, 1, 2, RegisterObjects.runes, 	2, 0, 3, 32, 64, true, true);
		addEntityPoolEntry(event, runePool, 1, 2, RegisterObjects.runes, 	2, 0, 5, 24, 56, true, true);
		addEntityPoolEntry(event, runePool, 1, 2, RegisterObjects.runes, 	1, 1, 7, 16, 48, true, true);
		addEntityPoolEntry(event, runePool, 1, 2, RegisterObjects.runes, 	2, 1, 9, 16, 48, true, true);
		addEntityPoolEntry(event, runePool, 1, 2, RegisterObjects.runes, 	1, 1, 11, 8, 32, true, true);
		addEntityPoolEntry(event, runePool, 1, 2, RegisterObjects.runes, 	1, 1, 13, 8, 32, true, true);
		
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	2, 0, 1, 1, 1, true, true);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	2, 0, 3, 1, 1, true, true);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	2, 0, 5, 1, 1, true, true);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 1, 7, 1, 1, true, true);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	2, 1, 9, 1, 1, true, true);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 1, 11, 1, 1, true, true);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 1, 13, 1, 1, true, true);
		break;
	    case "ghast":
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	8, 0, 0, 1, 12, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	8, 0, 4, 1, 12, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	4, 1, 11, 1, 8, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 1, 13, 1, 4, true, false);
		
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	4, 1, 11, 1, 1, true, false);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 1, 13, 1, 1, true, false);
		break;
	    case "guardian":
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	4, 0, 2, 1, 16, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	2, 0, 5, 1, 8, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 1, 7, 1, 4, true, false);
		
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	2, 0, 2, 1, 1, true, false);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 0, 7, 1, 1, true, false);
		break;
	    case "husk":
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 0, 1, 1, 16, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	3, 0, 3, 1, 16, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 0, 5, 1, 16, true, false);
		
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 0, 1, 1, 1, true, false);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	3, 0, 3, 1, 1, true, false);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 0, 5, 1, 1, true, false);
		break;
	    case "magma_cube":
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 0, 4, 1, 8, true, false);
		
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 0, 4, 1, 1, true, false);
		break;
	    case "shulker":
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	2, 0, 0, 8, 32, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 0, 3, 1, 16, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 0, 5, 1, 16, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	4, 1, 6, 8, 32, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	4, 1, 8, 8, 32, true, false);
		
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 1, 6, 1, 1, true, false);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 1, 8, 1, 1, true, false);
		break;
	    case "silverfish":
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 0, 3, 1, 8, true, false);
		
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 0, 3, 1, 1, true, false);
		break;
	    case "skeleton":
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 0, 0, 1, 8, true, false);
		
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 0, 0, 1, 1, true, false);
		break;
	    case "skeleton_horse":
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	2, 0, 0, 16, 48, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	2, 0, 3, 16, 48, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 1, 7, 1, 24, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	3, 1, 9, 8, 16, true, false);
		
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	2, 1, 7, 1, 1, true, false);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 1, 9, 1, 1, true, false);
		break;
	    case "slime":
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 0, 2, 1, 8, true, false);
		
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 0, 2, 1, 1, true, false);
		break;
	    case "spider":
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 0, 3, 1, 16, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	2, 0, 5, 1, 16, true, false);
		
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 0, 3, 1, 1, true, false);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	2, 0, 5, 1, 1, true, false);
		break;
	    case "stray":
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	2, 0, 0, 1, 16, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	2, 0, 2, 1, 16, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	2, 0, 5, 1, 16, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 1, 9, 1, 8, true, false);
		
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	2, 0, 0, 1, 1, true, false);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	2, 0, 2, 1, 1, true, false);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	2, 0, 5, 1, 1, true, false);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 1, 9, 1, 1, true, false);
		break;
	    case "vex":
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	3, 0, 0, 8, 32, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 0, 4, 8, 24, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	4, 1, 8, 1, 12, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	2, 1, 11, 1, 12, true, false);
		
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	3, 0, 0, 1, 1, true, false);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 0, 4, 1, 1, true, false);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	4, 1, 8, 1, 1, true, false);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	2, 1, 11, 1, 1, true, false);
		break;
	    case "vindication_illager":
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	2, 0, 3, 8, 24, true, true);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 1, 7, 1, 16, true, true);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	2, 1, 9, 1, 16, true, true);
		
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	2, 0, 3, 1, 1, true, true);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 1, 7, 1, 1, true, true);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	2, 1, 9, 1, 1, true, true);
		break;
	    case "witch":
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	6, 0, 1, 1, 16, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	6, 0, 5, 1, 16, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	4, 1, 7, 1, 8, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 1, 12, 1, 8, true, false);
		
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	3, 1, 7, 1, 1, true, false);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 1, 12, 1, 1, true, false);
		break;
	    case "wither_skeleton":
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	3, 0, 5, 1, 32, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 1, 7, 1, 16, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	2, 1, 11, 1, 16, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	2, 1, 12, 1, 16, true, false);
		
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 1, 7, 1, 1, true, false);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	2, 1, 11, 1, 1, true, false);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	2, 1, 12, 1, 1, true, false);
		break;
	    case "zombie":
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	2, 0, 1, 1, 16, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 0, 3, 1, 16, true, false);
		
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	2, 0, 1, 1, 1, true, false);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 0, 3, 1, 1, true, false);
		break;
	    case "zombie_horse":
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	2, 0, 1, 16, 48, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 0, 3, 16, 48, true, false);
		
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	2, 0, 1, 1, 1, true, false);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 0, 3, 1, 1, true, false);
		break;
	    case "zombie_pigman":
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	4, 0, 1, 1, 16, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	3, 1, 3, 1, 16, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	2, 1, 7, 4, 16, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 1, 11, 4, 16, true, false);
		
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	3, 0, 1, 1, 1, true, false);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	2, 0, 3, 1, 1, true, false);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 1, 7, 1, 1, true, false);
		break;
	    case "zombie_villager":
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	2, 0, 1, 1, 16, true, false);
		addEntityPoolEntry(event, runePool, 1, 1, RegisterObjects.runes, 	1, 0, 3, 1, 16, true, false);
		
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	8, 0, 1, 1, 1, true, false);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	5, 0, 3, 1, 1, true, false);
		addEntityPoolEntry(event, taliPool, 1, 1, RegisterObjects.talismans, 	1, 1, 9, 1, 1, true, false);
		break;
	    }
	}
	else if(eventName.startsWith(chests))
	{
	    switch(eventName.substring(chests.length())) {
	    case "abandoned_mineshaft":
		addChestPoolEntry(event, runePool, 0, 5, RegisterObjects.runes, 	1, 0, 0, 10, 1, 48, 0.25f);
		
		addChestPoolEntry(event, taliPool, 0, 2, RegisterObjects.talismans, 	1, 0, 0, 5, 1, 1, 0.5f);
		break;
	    case "desert_pyramid":
		addChestPoolEntry(event, runePool, 0, 2, RegisterObjects.runes, 	1, 0, 0, 10, 1, 48, 0.4f);
		
		addChestPoolEntry(event, taliPool, 0, 2, RegisterObjects.talismans, 	1, 0, 0, 5, 1, 1, 0.5f);
		break;
	    case "end_city_treasure":
		addChestPoolEntry(event, runePool, 0, 2, RegisterObjects.runes, 	1, 0, 0, 13, 1, 48, 0.4f);
		
		addChestPoolEntry(event, taliPool, 0, 2, RegisterObjects.talismans, 	1, 0, 0, 13, 1, 1, 0.3f);
		break;
	    case "igloo_chest":
		addChestPoolEntry(event, runePool, 0, 3, RegisterObjects.runes, 	1, 0, 0, 5, 1, 64, 0.5f);
		
		addChestPoolEntry(event, taliPool, 0, 1, RegisterObjects.talismans, 	1, 0, 0, 5, 1, 1, 0.5f);
		break;
	    case "jungle_temple":
		addChestPoolEntry(event, runePool, 0, 4, RegisterObjects.runes, 	1, 0, 0, 10, 1, 64, 0.5f);
		
		addChestPoolEntry(event, taliPool, 0, 2, RegisterObjects.talismans, 	1, 0, 0, 10, 1, 1, 0.5f);
		break;
	    case "nether_bridge":
		addChestPoolEntry(event, runePool, 0, 4, RegisterObjects.runes, 	1, 0, 0, 10, 1, 48, 0.4f);
		
		addChestPoolEntry(event, taliPool, 0, 2, RegisterObjects.talismans, 	1, 0, 0, 10, 1, 1, 0.5f);
		break;
	    case "simple_dungeon":
		addChestPoolEntry(event, runePool, 0, 2, RegisterObjects.runes, 	1, 0, 0, 10, 1, 64, 0.4f);
		
		addChestPoolEntry(event, taliPool, 0, 2, RegisterObjects.talismans, 	1, 0, 0, 5, 1, 1, 0.3f);
		break;
	    case "spawn_bonus_chest":
		addChestPoolEntry(event, runePool, 0, 4, RegisterObjects.runes, 	1, 0, 0, 1, 1, 16, 0.5f);
		
		addChestPoolEntry(event, taliPool, 0, 2, RegisterObjects.talismans, 	1, 0, 0, 1, 1, 1, 0.5f);
		break;
	    case "stronghold_corridor":
		addChestPoolEntry(event, runePool, 0, 2, RegisterObjects.runes, 	1, 0, 0, 10, 1, 64, 0.5f);
		
		addChestPoolEntry(event, taliPool, 0, 2, RegisterObjects.talismans, 	1, 0, 0, 10, 1, 1, 0.5f);
		break;
	    case "stronghold_crossing":
		addChestPoolEntry(event, runePool, 0, 3, RegisterObjects.runes, 	1, 0, 0, 10, 1, 48, 0.4f);
		
		addChestPoolEntry(event, taliPool, 0, 1, RegisterObjects.talismans, 	1, 0, 0, 10, 1, 1, 0.5f);
		break;
	    case "stronghold_library":
		addChestPoolEntry(event, taliPool, 0, 2, RegisterObjects.talismans, 	1, 0, 0, 10, 1, 1, 0.3f);
		break;
	    case "village_blacksmith":
		addChestPoolEntry(event, runePool, 0, 4, RegisterObjects.runes, 	1, 0, 0, 5, 1, 64, 0.4f);
		
		addChestPoolEntry(event, taliPool, 0, 1, RegisterObjects.talismans, 	1, 0, 0, 5, 1, 1, 0.5f);
		break;
	    case "woodland_mansion":
		addChestPoolEntry(event, runePool, 0, 2, RegisterObjects.runes, 	1, 0, 0, 13, 1, 48, 0.4f);
		
		addChestPoolEntry(event, taliPool, 0, 2, RegisterObjects.talismans, 	1, 0, 0, 13, 1, 1, 0.4f);
		break;
	    }
	}
    }
    
    
    public void addEntityPoolEntry(LootTableLoadEvent event, String poolName, int minPoolRolls, int maxPoolRolls, Item item, int weight, int quality, int meta, int minAmount, int maxAmount, boolean playerDropOnly, boolean boss)
    {
	if(event.getTable().getPool(poolName) == null)
	    event.getTable().addPool(new LootPool(new LootEntry[0], new LootCondition[0], new RandomValueRange(minPoolRolls, maxPoolRolls), new RandomValueRange(0), poolName));
	
	LootPool pool = event.getTable().getPool(poolName);
	float chance = 0.0f;
	String entryName = ModInfo.MOD_ID;
	
	if(poolName.equals("RunePool"))
	{
	    if(boss)
		chance = ModInfo.bossRuneDropChance;
	    else
		chance = ModInfo.mobsRuneDropChance;
	    
	    entryName += ":rune_" + EnumHandler.RuneTypes.values()[meta].getName();
	}
	else if(poolName.equals("TalismanPool"))
	{
	    if(boss)
		chance = ModInfo.bossTalismanDropChance;
	    else
		chance = ModInfo.mobsTalismanDropChance;
	    
	    entryName += ":talisman_" + EnumHandler.RuneTypes.values()[meta].getName();
	}
	else
	    entryName += ":missing_entry_name";
		
	pool.addEntry(new LootEntryItem(item, weight, quality,
		new LootFunction[]
			{
				new SetMetadata(new LootCondition[0], new RandomValueRange(meta)),
				new SetCount(new LootCondition[0], new RandomValueRange(minAmount, maxAmount))
			},
		new LootCondition[]
			{
				new KilledByPlayer(!playerDropOnly),
				new RandomChanceWithLooting(chance, 0.01f)
			},
		entryName));
    }
    
    
    public void addChestPoolEntry(LootTableLoadEvent event, String poolName, int minPoolRolls, int maxPoolRolls, Item item, int weight, int quality, int minMeta, int maxMeta, int minAmount, int maxAmount, float chance)
    {
	if(event.getTable().getPool(poolName) == null)
	    event.getTable().addPool(new LootPool(new LootEntry[0], new LootCondition[0], new RandomValueRange(minPoolRolls, maxPoolRolls), new RandomValueRange(0), poolName));
	
	LootPool pool = event.getTable().getPool(poolName);
	String entryName = ModInfo.MOD_ID;
	
	if(poolName.equals("RunePool"))
	    entryName += ":random_runes";
	else if(poolName.equals("TalismanPool"))
	    entryName += ":random_talismans";
	else
	    entryName += ":missing_entry_name";
	
	pool.addEntry(new LootEntryItem(item, weight, quality,
		new LootFunction[]
			{
				new SetMetadata(new LootCondition[0], new RandomValueRange(minMeta, maxMeta)),
				new SetCount(new LootCondition[0], new RandomValueRange(minAmount, maxAmount))
			},
		new LootCondition[]
			{
			    new RandomChance(chance)
			},
		entryName));
    }
}