package com.kandivia.runecrafting.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class PlayerRcCapProvider implements ICapabilitySerializable<NBTBase>
{
    @CapabilityInject(IPlayerRcCap.class)
    public static final Capability<IPlayerRcCap> PLAYER_RC_CAP = null;
    
    private IPlayerRcCap instance = PLAYER_RC_CAP.getDefaultInstance();

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
	return capability == PLAYER_RC_CAP;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
	return capability == PLAYER_RC_CAP ? PLAYER_RC_CAP.<T> cast(this.instance) : null;
    }

    @Override
    public NBTBase serializeNBT()
    {
	return PLAYER_RC_CAP.getStorage().writeNBT(PLAYER_RC_CAP, this.instance, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
	PLAYER_RC_CAP.getStorage().readNBT(PLAYER_RC_CAP, this.instance, null, nbt);
    }
}