package com.kandivia.runecrafting.implement;

import com.kandivia.runecrafting.main.Runecrafting;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockBase extends Block
{
    protected String name;
    public BlockBase(Material material, String name)
    {
	super(material);
	this.name = name;
	this.setUnlocalizedName(name);
	this.setRegistryName(name);
	this.setCreativeTab(Runecrafting.RunecraftTab);
    }
    
    
    @Override
    public BlockBase setCreativeTab(CreativeTabs tab)
    {
	super.setCreativeTab(tab);
	return this;
    }
}