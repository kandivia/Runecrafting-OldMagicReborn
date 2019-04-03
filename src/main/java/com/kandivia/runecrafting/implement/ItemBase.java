package com.kandivia.runecrafting.implement;

import com.kandivia.runecrafting.main.Runecrafting;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBase extends Item
{
    protected String name;
    protected String[] types;
    
    public ItemBase(String name)
    {
	this.name = name;
	this.setUnlocalizedName(name);
	this.setRegistryName(name);
	this.setCreativeTab(Runecrafting.RunecraftTab);
    }
    
    
    public ItemBase(String name, int stacksize)
    {
	this(name);
	this.setMaxStackSize(stacksize);
    }
    
    
    public ItemBase(String name, String[] types)
    {
	this(name);
	this.setHasSubtypes(true);
	this.types = types;
    }
    
    
    public ItemBase(String name, String[] types, int stacksize)
    {
	this(name, types);
	this.setMaxStackSize(stacksize);
    }
    
    
    @Override
    public ItemBase setCreativeTab(CreativeTabs tab)
    {
	super.setCreativeTab(tab);
	return this;
    }
    
    
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
    {
	if(getCreativeTab() != tab)
	    return;
	
	if(types == null)
	    subItems.add(new ItemStack(this));
	else
	{
	    for(int i = 0; i < types.length; i++)
		subItems.add(new ItemStack(this, 1, i));
	}
    }
    
    
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
	if(stack.getHasSubtypes())
	    return "item." + name + "_" + types[stack.getMetadata()];
	else
	    return "item." + name;
    }
}