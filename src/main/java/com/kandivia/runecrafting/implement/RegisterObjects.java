package com.kandivia.runecrafting.implement;

import com.kandivia.runecrafting.main.ModInfo;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(ModInfo.MOD_ID)
public class RegisterObjects
{
    //Debug Item
    @ObjectHolder("debugger")
    public static final Item debugger = null;
    
    
    //Items
    @ObjectHolder("essence")
    public static final Item essence = null;
    @ObjectHolder("rune")
    public static final Item runes = null;
    @ObjectHolder("talisman")
    public static final Item talismans = null;
    @ObjectHolder("staff")
    public static final Item staves = null;
    @ObjectHolder("spellbook")
    public static final Item spellbook = null;
    
    
    //Blocks
    @ObjectHolder("essence_ore")
    public static final Block essence_ore = null;
    @ObjectHolder("essence_ore")
    public static final Item essence_ore_item = null;
    @ObjectHolder("altar")
    public static final Block altars = null;
    @ObjectHolder("altar")
    public static final Item altars_item = null;
    @ObjectHolder("altar_portal")
    public static final Block altar_portal = null;
    @ObjectHolder("altar_portal")
    public static final Item altar_portal_item = null;
    @ObjectHolder("altar_portal_exit")
    public static final Block altar_portal_exit = null;
    @ObjectHolder("altar_portal_exit")
    public static final Item altar_portal_exit_item = null;
    
    
    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event)
    {
	event.getRegistry().registerAll(
		new ItemBase("essence", ModInfo.essenceTypes, 1),
		new ItemBase("rune", ModInfo.runeTypes),
		new Talismans("talisman", ModInfo.runeTypes, 1),
		new Staves("staff", ModInfo.staffTypes, 1),
		new Spellbook("spellbook"),
		new DebugTool("debugger", 1),
		
		new ItemBlock(essence_ore).setRegistryName("essence_ore"),
		new ItemSubBlock(altars).setRegistryName("altar"),
		new ItemSubBlock(altar_portal).setRegistryName("altar_portal"),
		new ItemBlock(altar_portal_exit).setRegistryName("altar_portal_exit")
		);
    }
    
    
    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event)
    {
	event.getRegistry().registerAll(
		new EssenceOre("essence_ore"),
		new Altars("altar"),
		new AltarPortal("altar_portal"),
		new AltarPortalExit("altar_portal_exit")
		);
    }
    
    
    @SubscribeEvent
    public void registerRender(ModelRegistryEvent event)
    {
	for(int i = 0; i < EnumHandler.EssenceTypes.values().length; i++)
	    registerRender(RegisterObjects.essence, i, EnumHandler.EssenceTypes.values()[i].getName());
	
	for(int i = 0; i < EnumHandler.StaffTypes.values().length; i++)
	    registerRender(RegisterObjects.staves, i, EnumHandler.StaffTypes.values()[i].getName());
	
	registerRender(this.spellbook);
	registerRender(this.essence_ore_item);
	
	for(int i = 0; i < EnumHandler.RuneTypes.values().length; i++)
	{
	    registerRender(this.runes, i, EnumHandler.RuneTypes.values()[i].getName());
	    registerRender(this.talismans, i, EnumHandler.RuneTypes.values()[i].getName());
	    
	    registerRender(this.altars, i, EnumHandler.RuneTypes.values()[i].getName());
	    registerRender(this.altar_portal, i, EnumHandler.RuneTypes.values()[i].getName());
	}
    }
    
    
    public void registerRender(Item item)
    {
	ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
    
    
    public void registerRender(Block block, int meta, String subName)
    {
	registerRender(Item.getItemFromBlock(block), meta, subName);
    }
    
    
    public void registerRender(Item item, int meta, String subName)
    {
	ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName() + "_" + subName, "inventory"));
    }
}