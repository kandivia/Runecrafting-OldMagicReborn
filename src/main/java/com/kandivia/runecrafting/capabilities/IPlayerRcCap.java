package com.kandivia.runecrafting.capabilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public interface IPlayerRcCap
{
    public void addMagicExp(EntityPlayer player, int amount);
    public void addRuneExp(EntityPlayer player, int amount);
    public void setMagicExp(int amount);
    public void setRuneExp(int amount);
    public int getMagicExp();
    public int getRuneExp();
    public int getMagicLevel();
    public int getRuneLevel();
    public int getXPos();
    public int getYPos();
    public int getZPos();
    public void setLastPos(BlockPos pos);
    public void setSpellId(int id);
    public int getSpellId();
}
