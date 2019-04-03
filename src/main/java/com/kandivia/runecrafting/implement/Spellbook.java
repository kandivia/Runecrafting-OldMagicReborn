package com.kandivia.runecrafting.implement;

import com.kandivia.runecrafting.capabilities.IPlayerRcCap;
import com.kandivia.runecrafting.capabilities.PlayerRcCapProvider;
import com.kandivia.runecrafting.spells.Spell;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class Spellbook extends ItemBase
{	
	public Spellbook(String name)
	{
	    super(name);
	    this.setMaxStackSize(1);
	}
	
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
	    IPlayerRcCap playerRcCap = player.getCapability(PlayerRcCapProvider.PLAYER_RC_CAP, null);
	    if(!player.isSneaking())
	    {
		switch (playerRcCap.getSpellId())
		{
		case 0:
		    Spell.blink(world, player, this);
		    break;
		case 1:
		    Spell.bonesToApples(player);
		    break;
		case 2:
		    Spell.enchantHeldItem(player, 3);
		    break;
		case 3:
		    Spell.enderPocket(player);
		    break;
		case 4:
		    Spell.erosion(player);
		    break;
		case 5:
		    Spell.Humidify(player);
		    break;
		case 6:
		    Spell.superheat(player);
		    break;
		    
		default:
		    player.sendStatusMessage(new TextComponentString("Spell not selected!"), true);
		    break;
		}
	    }
	    else
	    {
		String msg = "";
		int id;
		switch (playerRcCap.getSpellId())
		{
		case 0:
		    id = 1;
		    msg = "Bones To Apples spell selected!";
		    break;
		case 1:
		    id = 2;
		    msg = "Enchant spell selected!";
		    break;
		case 2:
		    id = 3;
		    msg = "Ender Pocket spell selected!";
		    break;
		case 3:
		    id = 4;
		    msg = "Erosion spell selected!";
		    break;
		case 4:
		    id = 5;
		    msg = "Humidify spell selected!";
		    break;
		case 5:
		    id = 6;
		    msg = "Superheat spell selected!";
		    break;
		case 6:
		    id = 0;
		    msg = "Blink spell selected!";
		    break;

		default:
		    id = 0;
		    msg = "Blink spell selected!";
		    break;
		}
		playerRcCap.setSpellId(id);
		player.sendStatusMessage(new TextComponentString(msg), true);
	    }
	    
	    return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
}