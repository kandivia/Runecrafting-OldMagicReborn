package com.kandivia.runecrafting.world;

import javax.annotation.Nullable;

import com.kandivia.runecrafting.main.ModInfo;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class RCWorldData extends WorldSavedData
{
    private static final String DATA_NAME = ModInfo.MOD_ID + "_SavedData";
    
    private boolean allAltarsSpawned = false;
    
    private int[] airAltarPos = null;
    private int[] mindAltarPos = null;    
    private int[] waterAltarPos = null;
    private int[] earthAltarPos = null;
    private int[] fireAltarPos = null;
    private int[] bodyAltarPos = null;
    private int[] cosmicAltarPos = null;
    private int[] chaosAltarPos = null;
    private int[] astralAltarPos = null;
    private int[] natureAltarPos = null;
    private int[] lawAltarPos = null;
    private int[] deathAltarPos = null;
    private int[] bloodAltarPos = null;
    private int[] soulAltarPos = null;
    
    public RCWorldData()
    {
	super(DATA_NAME);
    }
    
    
    public RCWorldData(String name)
    {
	super(name);
    }
    
    
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
	allAltarsSpawned = nbt.getBoolean("allAltarsSpawned");
	
	if(nbt.hasKey("airAltarPos"))
	    airAltarPos = nbt.getIntArray("airAltarPos");
	if(nbt.hasKey("mindAltarPos"))
	    mindAltarPos = nbt.getIntArray("mindAltarPos");
	if(nbt.hasKey("waterAltarPos"))
	    waterAltarPos = nbt.getIntArray("waterAltarPos");
	if(nbt.hasKey("earthAltarPos"))
	    earthAltarPos = nbt.getIntArray("earthAltarPos");
	if(nbt.hasKey("fireAltarPos"))
	    fireAltarPos = nbt.getIntArray("fireAltarPos");
	if(nbt.hasKey("bodyAltarPos"))
	    bodyAltarPos = nbt.getIntArray("bodyAltarPos");
	if(nbt.hasKey("cosmicAltarPos"))
	    cosmicAltarPos = nbt.getIntArray("cosmicAltarPos");
	if(nbt.hasKey("chaosAltarPos"))
	    chaosAltarPos = nbt.getIntArray("chaosAltarPos");
	if(nbt.hasKey("astralAltarPos"))
	    astralAltarPos = nbt.getIntArray("astralAltarPos");
	if(nbt.hasKey("natureAltarPos"))
	    natureAltarPos = nbt.getIntArray("natureAltarPos");
	if(nbt.hasKey("lawAltarPos"))
	    lawAltarPos = nbt.getIntArray("lawAltarPos");
	if(nbt.hasKey("deathAltarPos"))
	    deathAltarPos = nbt.getIntArray("deathAltarPos");
	if(nbt.hasKey("bloodAltarPos"))
	    bloodAltarPos = nbt.getIntArray("bloodAltarPos");
	if(nbt.hasKey("soulAltarPos"))
	    soulAltarPos = nbt.getIntArray("soulAltarPos");
    }
    
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
	compound.setBoolean("allAltarsSpawned", allAltarsSpawned);
	
	if(airAltarPos != null)
	    compound.setIntArray("airAltarPos", airAltarPos);
	if(mindAltarPos != null)
	    compound.setIntArray("mindAltarPos", mindAltarPos);
	if(waterAltarPos != null)
	    compound.setIntArray("waterAltarPos", waterAltarPos);
	if(earthAltarPos != null)
	    compound.setIntArray("earthAltarPos", earthAltarPos);
	if(fireAltarPos != null)
	    compound.setIntArray("fireAltarPos", fireAltarPos);
	if(bodyAltarPos != null)
	    compound.setIntArray("bodyAltarPos", bodyAltarPos);
	if(cosmicAltarPos != null)
	    compound.setIntArray("cosmicAltarPos", cosmicAltarPos);
	if(chaosAltarPos != null)
	    compound.setIntArray("chaosAltarPos", chaosAltarPos);
	if(astralAltarPos != null)
	    compound.setIntArray("astralAltarPos", astralAltarPos);
	if(natureAltarPos != null)
	    compound.setIntArray("natureAltarPos", natureAltarPos);
	if(deathAltarPos != null)
	    compound.setIntArray("deathAltarPos", deathAltarPos);
	if(bloodAltarPos != null)
	    compound.setIntArray("bloodAltarPos", bloodAltarPos);
	if(soulAltarPos != null)
	    compound.setIntArray("soulAltarPos", soulAltarPos);
	
	return compound;
    }
    
    
    public boolean hasAllAltarsSpawned()
    {
	return allAltarsSpawned;
    }
    
        
    public int[] getAltarPosArray(int meta)
    {
	switch(meta) {
	case 0:
	    return airAltarPos;
	case 1:
	    return mindAltarPos;
	case 2:
	    return waterAltarPos;
	case 3:
	    return earthAltarPos;
	case 4:
	    return fireAltarPos;
	case 5:
	    return bodyAltarPos;
	case 6:
	    return cosmicAltarPos;
	case 7:
	    return chaosAltarPos;
	case 8:
	    return astralAltarPos;
	case 9:
	    return natureAltarPos;
	case 10:
	    return lawAltarPos;
	case 11:
	    return deathAltarPos;
	case 12:
	    return bloodAltarPos;
	case 13:
	    return soulAltarPos;
	    
	default:
	    return null;
	}	
    }
    
    
    public BlockPos getAltarBlockPos(int meta)
    {
	int[] a = getAltarPosArray(meta);
	
	if(a != null)
	    return new BlockPos(a[0], a[1], a[2]);
	return null;
    }
    
    
    public void setAltarPos(int meta, int x, int y, int z)
    {
	int[] pos = {x, y, z};
	setAltarPos(meta, pos);
    }
    
    
    public void setAltarPos(int meta, BlockPos pos)
    {
	int[] pos1 = {pos.getX(), pos.getY(), pos.getZ()};
	setAltarPos(meta, pos1);
    }
    
    
    public void setAltarPos(int meta, int[] pos)
    {
	switch(meta) {
	case 0:
	    airAltarPos = pos;
	    break;
	case 1:
	    mindAltarPos = pos;
	    break;
	case 2:
	    waterAltarPos = pos;
	    break;
	case 3:
	    earthAltarPos = pos;
	    break;
	case 4:
	    fireAltarPos = pos;
	    break;
	case 5:
	    bodyAltarPos = pos;
	    break;
	case 6:
	    cosmicAltarPos = pos;
	    break;
	case 7:
	    chaosAltarPos = pos;
	    break;
	case 8:
	    astralAltarPos = pos;
	    break;
	case 9:
	    natureAltarPos = pos;
	    break;
	case 10:
	    lawAltarPos = pos;
	    break;
	case 11:
	    deathAltarPos = pos;
	    break;
	case 12:
	    bloodAltarPos = pos;
	    break;
	case 13:
	    soulAltarPos = pos;
	    break;	    
	}
	
	if(airAltarPos != null && mindAltarPos != null && waterAltarPos != null && earthAltarPos != null && fireAltarPos != null && bodyAltarPos != null
		&& cosmicAltarPos != null && chaosAltarPos != null && astralAltarPos != null && natureAltarPos != null && lawAltarPos != null
		&& deathAltarPos != null && bloodAltarPos != null && soulAltarPos != null)
	    allAltarsSpawned = true;
	
	markDirty();
    }
    
    
    public static RCWorldData get(World world)
    {
	MapStorage storage = world.getPerWorldStorage();
	RCWorldData instance = (RCWorldData) storage.getOrLoadData(RCWorldData.class, DATA_NAME);
	
	if(instance == null)
	{
	    instance = new RCWorldData();
	    storage.setData(DATA_NAME, instance);
	}
	
	return instance;
    }
}