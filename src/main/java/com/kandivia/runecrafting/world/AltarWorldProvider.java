package com.kandivia.runecrafting.world;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;

public class AltarWorldProvider extends WorldProvider
{

    @Override
    public DimensionType getDimensionType()
    {
	return RCDimensions.altarDimensionType;
    }
    
    
    @Override
    public String getSaveFolder()
    {
	return "DIM-ALTAR";
    }
    
    
    @Override
    public IChunkGenerator createChunkGenerator()
    {
	return new AltarChunkGenerator(world);
    }
    
    
    @Override
    public boolean canRespawnHere()
    {
        return false;
    }
    
    
    @Override
    public boolean canMineBlock(EntityPlayer player, BlockPos pos)
    {
	return false;
    }
    
    
    @Override
    public boolean canDoLightning(Chunk chunk)
    {
	return false;
    }
    
    
    @Override
    public boolean canDoRainSnowIce(Chunk chunk)
    {
	return false;
    }
    
    
    public void onPlayerAdded(EntityPlayerMP player)
    {
	if(player.interactionManager.getGameType() != null && player.interactionManager.getGameType() == GameType.SURVIVAL)
	    player.setGameType(GameType.ADVENTURE);
    }
    
    
    public void onPlayerRemoved(EntityPlayerMP player)
    {
	if(player.interactionManager.getGameType() != null && player.interactionManager.getGameType() == GameType.ADVENTURE)
	    player.setGameType(GameType.SURVIVAL);
    }
}
