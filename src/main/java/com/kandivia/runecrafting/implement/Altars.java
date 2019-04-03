package com.kandivia.runecrafting.implement;

import java.util.List;

import com.kandivia.runecrafting.capabilities.IPlayerRcCap;
import com.kandivia.runecrafting.capabilities.PlayerRcCapProvider;
import com.kandivia.runecrafting.implement.EnumHandler.RuneTypes;
import com.kandivia.runecrafting.main.ModInfo;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Altars extends BlockBase implements IMetaBlockName
{
    public static final PropertyEnum TYPE = PropertyEnum.create("type", RuneTypes.class);
    public static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0, 0, 0, 1, 0.0625 * 15, 1);
    public static final AxisAlignedBB COLLISION_BOX = new AxisAlignedBB(0, 0, 0, 1, 0.0625 * 14, 1);
    
    public Altars(String name)
    {
	super(Material.ROCK, name);
	this.setLightLevel(8.0F);
	this.setHardness(-1.0F);
	this.setResistance(6000001.0F);
	this.setSoundType(SoundType.STONE);
	this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, RuneTypes.AIR));
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, 
	    float hitX, float hitY, float hitZ)
    {
	if(!player.isSneaking())
	{
	    int blockMeta = this.getMetaFromState(state);
	    if((playerHasTalisman(player, blockMeta)) || (playerHasTiara(player, blockMeta)))
	    {
		switch(blockMeta) {
		case 0:
			craftRunes(world, player, blockMeta, 0, 4);
			break;
		case 1:
			craftRunes(world, player, blockMeta, 0, 4);
			break;
		case 2:
			craftRunes(world, player, blockMeta, 5, 20);
			break;
		case 3:
			craftRunes(world, player, blockMeta, 9, 4);
			break;
		case 4:
			craftRunes(world, player, blockMeta, 14, 8);
			break;
		case 5:
			craftRunes(world, player, blockMeta, 20, 16);
			break;
		case 6:
			craftRunes(world, player, blockMeta, 27, 32);
			break;
		case 7:
			craftRunes(world, player, blockMeta, 35, 64);
			break;
		case 8:
			craftRunes(world, player, blockMeta, 40, 96);
			break;
		case 9:
			craftRunes(world, player, blockMeta, 44, 128);
			break;
		case 10:
			craftRunes(world, player, blockMeta, 54, 252);
			break;
		case 11:
			craftRunes(world, player, blockMeta, 65, 504);
			break;
		case 12:
			craftRunes(world, player, blockMeta, 77, 1008);
			break;
		case 13:
			craftRunes(world, player, blockMeta, 90, 2016);
			break;
		}
	    }
	}
	return true;
    }
    
    
    public boolean playerHasTalisman(EntityPlayer player, int meta)
    {
	return (player.inventory.getCurrentItem() != null) && (player.inventory.getCurrentItem().isItemEqual(new ItemStack(RegisterObjects.talismans, 1, meta)));
    }
    
    
    public boolean playerHasTiara(EntityPlayer player, int meta)
    {
	//return (player.inventory.armorItemInSlot(3) != null) && (player.inventory.armorItemInSlot(3).isItemEqualIgnoreDurability(new ItemStack(RegisterItems.tiaras, 1, meta)));
	return false;
    }
    
    
    public void craftRunes(World world, EntityPlayer player, int meta, int reqLv, int expReward)
    {
	IPlayerRcCap playerRcCap = player.getCapability(PlayerRcCapProvider.PLAYER_RC_CAP, null);
	if(!ModInfo.runecraftingRequiresLv || player.isCreative() || (playerRcCap.getRuneLevel() >= reqLv))
	{
	    int runesToCraft = 0;
	    
	    if(meta <= 5)
		runesToCraft += player.inventory.clearMatchingItems(RegisterObjects.essence, 0, -1, null);
	    
	    if(meta <= 12)
		runesToCraft += player.inventory.clearMatchingItems(RegisterObjects.essence, 1, -1, null);
	    
	    if(runesToCraft != 0)
	    {
		if(player.inventory.addItemStackToInventory(new ItemStack(RegisterObjects.runes, runesToCraft, meta)))
		{
		    if(!world.isRemote)
			playerRcCap.addRuneExp(player, expReward * runesToCraft);
		    player.playSound(SoundEvents.EVOCATION_ILLAGER_CAST_SPELL, 1.0F, 1.0F);
		}
		else if(!world.isRemote)
		    player.sendStatusMessage(new TextComponentString("You do not have enough room in your inventory!"), true);
	    }
	    else if(!world.isRemote)
		player.sendStatusMessage(new TextComponentString("You do not have any essence for this altar!"), true);
	}
	else if(!world.isRemote)
	{
	    player.sendStatusMessage(new TextComponentString("You need level " + reqLv + " Runecrafting for this altar."), true);
	    System.out.println("TESTING!!!");
	}
	    
	
    }
    
    
    @Override
    protected BlockStateContainer createBlockState()
    {
	return new BlockStateContainer(this, new IProperty[] {TYPE});
    }
    
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
	RuneTypes type = (RuneTypes) state.getValue(TYPE);
	return type.getID();
    }
    
    
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
	return this.getDefaultState().withProperty(TYPE, RuneTypes.values()[meta]);
    }
    
    
    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab)
    {
	for(int i = 0; i < RuneTypes.values().length; i++)
	    tab.add(new ItemStack(this, 1, i));
    }
    
    
    @Override
    public String getSpecialName(ItemStack stack)
    {
	return RuneTypes.values()[stack.getItemDamage()].getName();
    }
    
    
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
	return new ItemStack(Item.getItemFromBlock(this), 1, getMetaFromState(world.getBlockState(pos)));
    }
    
    
    @Override
    public int damageDropped(IBlockState state)
    {
	return getMetaFromState(state);
    }
    
    
    @Override
    public boolean isFullCube(IBlockState state)
    {
	return false;
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
	return false;
    }
    
    
    @Override
    public BlockRenderLayer getBlockLayer()
    {
    	return BlockRenderLayer.SOLID;
    }
    
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
    	return BOUNDING_BOX;
    }
    
    
    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
    		List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean a)
    {
    	super.addCollisionBoxToList(pos, entityBox, collidingBoxes, COLLISION_BOX);
    }
}