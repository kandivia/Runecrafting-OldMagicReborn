package com.kandivia.runecrafting.implement;

import java.util.Random;

import javax.annotation.Nullable;

import com.kandivia.runecrafting.capabilities.IPlayerRcCap;
import com.kandivia.runecrafting.capabilities.PlayerRcCapProvider;
import com.kandivia.runecrafting.command.CustomTeleporter;
import com.kandivia.runecrafting.main.ModInfo;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AltarPortalExit extends BlockBase
{

    public AltarPortalExit(String name)
    {
	super(Material.AIR, name);
    }
    
    
    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entityIn)
    {
	
	
	
	if(!world.isRemote && entityIn instanceof EntityPlayer)
	{
	    EntityPlayer player = (EntityPlayer) entityIn;
	    if(player.dimension == ModInfo.altarDimensionId)
	    {
		IPlayerRcCap playerRcCap = player.getCapability(PlayerRcCapProvider.PLAYER_RC_CAP, null);
		System.out.println(playerRcCap.getXPos() + " " + playerRcCap.getYPos() + " " + playerRcCap.getZPos());
		CustomTeleporter.teleportToDimension(player, 0, playerRcCap.getXPos(), playerRcCap.getYPos(), playerRcCap.getZPos());    
	    }
	}
    }
    
    
    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return ItemStack.EMPTY;
    }
    
    
    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
    {
        return false;
    }
    
    
    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }
    
    
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    
    
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
    
    @Override
    public int quantityDropped(Random random)
    {
        return 0;
    }
    
    
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
}
