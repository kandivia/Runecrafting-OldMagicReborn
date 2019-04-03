package com.kandivia.runecrafting.world;

import java.util.Random;

import com.kandivia.runecrafting.implement.RegisterObjects;
import com.kandivia.runecrafting.main.ModInfo;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.fml.common.IWorldGenerator;

public class AltarDimGen implements IWorldGenerator
{
    
    private static final ResourceLocation AIR_ALTAR = new ResourceLocation("runecrafting:altar_air");
    public static final ResourceLocation AIR_ALTAR_PORTAL = new ResourceLocation("runecrafting:altar_portal_air");

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
	/*if(world.provider.getDimension() == 0)
	    genOverworld(random, chunkX, chunkZ, world);
	    
	else*/ if(world.provider.getDimension() == ModInfo.altarDimensionId)
	    genAltarDim(chunkX, chunkZ, world);
    }
    
    
    public void genOverworld(Random random, int chunkX, int chunkZ, World world)
    {
	if(!RCWorldData.get(world).hasAllAltarsSpawned())
	{
	    BlockPos target = new BlockPos(chunkX * 16 + random.nextInt(16) , 0, chunkZ * 16 + random.nextInt(16));
	    target = new BlockPos(target.getX(), world.getChunkFromChunkCoords(chunkX, chunkZ).getHeight(target), target.getZ());
	    
	    for(int i = 0; i < 13; i++)
	    {
		if(RCWorldData.get(world).getAltarPosArray(i) == null)
		{
		    if(isMinDistanceEnough(world, target))
		    {
			final PlacementSettings settings = new PlacementSettings().setRotation(Rotation.NONE);
			final Template template = world.getSaveHandler().getStructureTemplateManager().getTemplate(world.getMinecraftServer(), AIR_ALTAR_PORTAL);
			BlockPos size = template.getSize();
			
			if(strucValid(target, world, size.getX(), size.getY(), size.getZ(), Blocks.GRASS) && RCWorldData.get(world).getAltarPosArray(i) == null)
			{
			    target = new BlockPos(target.getX(), target.getY() - 3, target.getZ());
			    BlockPos altarPos = new BlockPos(target.getX() + 5, target.getY() + 3, target.getZ() + 5);
			    template.addBlocksToWorld(world, target, settings);
			    world.setBlockState(altarPos, RegisterObjects.altar_portal.getStateFromMeta(i));
			    System.out.println("Altar " + i + " Spawned at location: " + altarPos.getX() + ", " + altarPos.getY() + ", " + altarPos.getZ()); //###test line###
			    RCWorldData.get(world).setAltarPos(i, altarPos.getX(), altarPos.getY(), altarPos.getZ());
			    
			    return;
			}
			else
			    return;
		    }
		    else
			return;
		}
	    }
	}
    }
    
    
    public static boolean isMinDistanceEnough(World world, BlockPos pos)
    {
	for(int i = 0; i < 13; i++)
	{
	    if(RCWorldData.get(world).getAltarPosArray(i) != null && !(pos.getDistance(RCWorldData.get(world).getAltarPosArray(i)[0],
		    RCWorldData.get(world).getAltarPosArray(i)[1], RCWorldData.get(world).getAltarPosArray(i)[2]) > ModInfo.altarMinDistance))
	    {
		return false;
	    }
	}
	return true;
    }
    
    
    public void genAltarDim(int chunkX, int chunkZ, World world)
    {
	if(chunkX == 0 && chunkZ == 0)
	{
	    final BlockPos basePos = new BlockPos(0, 125, 0);
	    final PlacementSettings settings = new PlacementSettings().setRotation(Rotation.NONE);
	    final Template template = world.getSaveHandler().getStructureTemplateManager().getTemplate(world.getMinecraftServer(), AIR_ALTAR);
	    
	    template.addBlocksToWorld(world, basePos, settings);
	}
    }
    
    public static boolean strucValid(BlockPos pos, World world, int xSize, int ySize, int zSize, Block validBlock)
    {
	BlockPos pos1 = pos.down();
	BlockPos pos2 = pos.down();
	BlockPos pos3 = pos.down();
	BlockPos pos4 = pos.down();
	
	pos2 = new BlockPos(pos2.getX() + (xSize + 1), pos2.getY(), pos2.getZ());
	pos3 = new BlockPos(pos3.getX(), pos3.getY(), pos3.getZ() + (zSize + 1));
	pos4 = new BlockPos(pos4.getX() + (xSize + 1), pos4.getY(), pos4.getZ() + (zSize + 1));
	
	Block b1 = world.getBlockState(pos1).getBlock();
	Block b2 = world.getBlockState(pos2).getBlock();
	Block b3 = world.getBlockState(pos3).getBlock();
	Block b4 = world.getBlockState(pos4).getBlock();
	
	if(b1 == validBlock && b2 == validBlock && b3 == validBlock && b4 == validBlock)
	    return true;
	
	return false;
    }
}