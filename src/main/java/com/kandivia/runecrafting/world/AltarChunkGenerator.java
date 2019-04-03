package com.kandivia.runecrafting.world;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.template.Template;

public class AltarChunkGenerator implements IChunkGenerator
{
    private final World worldObj;
    
    public AltarChunkGenerator(World worldObj)
    {
        this.worldObj = worldObj;
    }
    
    
    @Override
    public Chunk generateChunk(int x, int z)
    {
        ChunkPrimer chunkprimer = new ChunkPrimer();
        Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);
        chunk.generateSkylightMap();
        return chunk;
    }
    
    
    @Override
    public void populate(int x, int z) {}
    
    
    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z)
    {
        return false;
    }
    
    
    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
    {
        return ImmutableList.of();
    }
    
    
    @Nullable
    @Override
    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored)
    {
        return null;
    }
    
    
    @Override
    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos)
    {
        return false;
    }
    
    
    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z) {}
}