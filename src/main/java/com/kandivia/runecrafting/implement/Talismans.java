package com.kandivia.runecrafting.implement;

import com.kandivia.runecrafting.spells.Spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class Talismans extends ItemBase
{
    public Talismans(String name, String[] types, int stacksize)
    {
	super(name, types, stacksize);
    }
    
    
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
	if(player.isSneaking() && !world.isRemote)
	{
	    BlockPos a = Spell.locateRcStructure(world, player.getPosition(), player.getHeldItem(hand).getMetadata(), 5, 2, 10387313, 100);
	    if(a != null)
		player.sendStatusMessage(new TextComponentString(a.getX() + ", " + a.getY() + ", " + a.getZ()), true);
	}
	return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
    }
}