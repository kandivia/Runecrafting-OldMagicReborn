package com.kandivia.runecrafting.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class PlayerRcCapStorage implements IStorage<IPlayerRcCap>
{

    @Override
    public NBTBase writeNBT(Capability<IPlayerRcCap> capability, IPlayerRcCap instance, EnumFacing side)
    {
	NBTTagCompound compound = new NBTTagCompound();
	
	compound.setInteger("magicExp", instance.getMagicExp());
	compound.setInteger("runeExp", instance.getRuneExp());
	compound.setInteger("xPos", instance.getXPos());
	compound.setInteger("yPos", instance.getYPos());
	compound.setInteger("zPos", instance.getZPos());
	compound.setInteger("spellId", instance.getSpellId());
	
	return compound;
    }

    @Override
    public void readNBT(Capability<IPlayerRcCap> capability, IPlayerRcCap instance, EnumFacing side, NBTBase nbt)
    {
	if(nbt instanceof NBTTagCompound)
	{
	    if(!(instance instanceof PlayerRcCap))
		throw new IllegalArgumentException("Cannot deserialize to an instance that isn't the default implementation.");
	    
	    NBTTagCompound compound = (NBTTagCompound)nbt;
	    
	    instance.setMagicExp(compound.getInteger("magicExp"));
	    instance.setRuneExp(compound.getInteger("runeExp"));
	    instance.setLastPos(new BlockPos(compound.getInteger("xPos"), compound.getInteger("yPos"), compound.getInteger("zPos")));
	    instance.setSpellId(compound.getInteger("spellId"));
	}
	else
	    throw new IllegalArgumentException("Cannot deserialize if nbt isn't NBTTagCompound.");
    }
}