package com.kandivia.runecrafting.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class RcGuiHandler implements IGuiHandler
{
    public static final int SPELLBOOK = 0;
    
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
	return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
	switch(ID) {
	case SPELLBOOK:
	    return new SpellbookGUI();
	default:
	    return null;
	}
    }
}
