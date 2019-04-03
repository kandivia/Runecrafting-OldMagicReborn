package com.kandivia.runecrafting.implement;

import java.util.Random;

import com.kandivia.runecrafting.main.ModInfo;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EssenceOre extends BlockBase
{
    private boolean hasSilkTouch = false;
    private int dropAmount = 1;
    private int metadata;
    private boolean playerIsCreative = false;
    
    public EssenceOre(String name)
    {
	super(Material.ROCK, name);
	this.setHardness(3.0F);
	this.setResistance(5.0F);
	this.setSoundType(SoundType.STONE);
    }
    
    
    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    {
	ItemStack heldItem = player.inventory.getCurrentItem();
	hasSilkTouch = false;
	dropAmount = 1;
	playerIsCreative = player.isCreative();
	
	if(playerIsCreative)
	    return;
	
	if(heldItem != null && heldItem.hasTagCompound() && heldItem.getTagCompound().getTag("ench") != null)
	{
	    NBTTagList enchants = (NBTTagList) heldItem.getTagCompound().getTag("ench");
	    for(int i = 0; i < enchants.tagCount(); i++)
	    {
		NBTTagCompound enchant = ((NBTTagList) enchants).getCompoundTagAt(i);
		if(enchant.getInteger("id") == 33)
		{
		    hasSilkTouch = true;
		    break;
		}
	    }
	}
	
	if(!hasSilkTouch)
	{
	    metadata = 0;
	    if(world.provider.getDimension() == ModInfo.altarDimensionId)
	    {
		dropAmount = 0;
		player.inventory.addItemStackToInventory(new ItemStack(RegisterObjects.essence, 1, 0));
	    }
	}
	else
	{
	    metadata = 1;
	    if(world.provider.getDimension() == ModInfo.altarDimensionId)
	    {
		dropAmount = 0;
		player.inventory.addItemStackToInventory(new ItemStack(RegisterObjects.essence, 1, 1));
	    }
	}	
    }
    
    
    @Override
    public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state)
    {
	if(world.provider.getDimension() == ModInfo.altarDimensionId && !playerIsCreative)
	{
	    world.setBlockState(pos, RegisterObjects.essence_ore.getDefaultState());
	}
    }
    
    
    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune)
    {
	return RegisterObjects.essence;
    }
    
    
    @Override
    public int damageDropped(IBlockState state)
    {
	return metadata;
    }    
    
    
    @Override
    public int quantityDropped(Random random)
    {
	return dropAmount;
    }
    
    
    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    {
	return false;
    }
}