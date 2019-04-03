package com.kandivia.runecrafting.main;

import com.kandivia.runecrafting.implement.RegisterObjects;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class RunecraftingTab extends CreativeTabs
{
    public RunecraftingTab(String label)
    {
	super(label);
    }
    
    
    @Override
    public ItemStack getTabIconItem()
    {
	return ItemStack.EMPTY;
    }
    
    
    @Override
    public ItemStack getIconItemStack()
    {
	return new ItemStack(RegisterObjects.runes, 1, 0);
    }
}