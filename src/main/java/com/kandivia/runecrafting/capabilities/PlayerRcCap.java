package com.kandivia.runecrafting.capabilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class PlayerRcCap implements IPlayerRcCap
{
    private int magicExp = 0;
    private int runeExp = 0;
    private int xPos = 0;
    private int yPos = 0;
    private int zPos = 0;
    private int spellId;
    
    /*
     *Adds to magicExp and returns true if player levels up 
     */
    @Override
    public void addMagicExp(EntityPlayer player, int amount)
    {
	int oldLv = getMagicLevel();
	magicExp += amount;
	int newLv = getMagicLevel();
	
	if(newLv > oldLv)
	    player.sendStatusMessage(new TextComponentString("You have reached level " + newLv + " Magic!"), true);
    }
    
    
    /*
     *Adds to runeExp and returns true if player levels up 
     */
    @Override
    public void addRuneExp(EntityPlayer player, int amount)
    {
	int oldLv = getRuneLevel();
	runeExp += amount;
	int newLv = getRuneLevel();
	
	if(newLv > oldLv)
	    player.sendStatusMessage(new TextComponentString("You have reached level " + newLv + " Runecrafting!"), true);
    }

    @Override
    public void setMagicExp(int amount)
    {
	magicExp = amount;
    }

    @Override
    public void setRuneExp(int amount)
    {
	runeExp = amount;
    }
    
    @Override
    public int getMagicExp()
    {
	return magicExp;
    }
    
    @Override
    public int getRuneExp()
    {
	return runeExp;
    }
    
    
    @Override
    public int getMagicLevel()
    {
	return (int)(Math.pow(magicExp, 1.0/2.5) / 5);
    }

    @Override
    public int getRuneLevel()
    {
	return (int)(Math.pow(runeExp, 1.0/2.5) / 5);
    }


    @Override
    public int getXPos()
    {
	return xPos;
    }


    @Override
    public int getYPos()
    {
	return yPos;
    }


    @Override
    public int getZPos()
    {
	return zPos;
    }


    @Override
    public void setLastPos(BlockPos pos)
    {
	xPos = pos.getX();
	yPos = pos.getY();
	zPos = pos.getZ();
    }


    @Override
    public void setSpellId(int id)
    {
	spellId = id;
    }


    @Override
    public int getSpellId()
    {
	return spellId;
    }
}