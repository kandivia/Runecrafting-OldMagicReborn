package com.kandivia.runecrafting.implement;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemSubBlock extends ItemBlock
{
    public ItemSubBlock(Block block)
    {
	super(block);
	
	if(!(block instanceof IMetaBlockName))
	    throw new IllegalArgumentException(String.format("The given Block %s is not an instance of IMetalBlockName!", block.getUnlocalizedName()));
	
	this.setHasSubtypes(true);
	this.setMaxDamage(0);
    }
    
    
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
	return super.getUnlocalizedName() + "." + ((IMetaBlockName) this.block).getSpecialName(stack);
    }
    
    
    @Override
    public int getMetadata(int damage)
    {
	return damage;
    }
}