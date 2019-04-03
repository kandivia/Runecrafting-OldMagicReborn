package com.kandivia.runecrafting.implement;

import java.util.List;

import javax.annotation.Nullable;

import com.kandivia.runecrafting.capabilities.IPlayerRcCap;
import com.kandivia.runecrafting.capabilities.PlayerRcCapProvider;
import com.kandivia.runecrafting.command.CustomTeleporter;
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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class AltarPortal extends BlockBase implements IMetaBlockName
{
    public static final PropertyEnum TYPE = PropertyEnum.create("type", RuneTypes.class);
    public static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0, 0, 0, 1, 0.0625 * 15, 1);
    public static final AxisAlignedBB COLLISION_BOX = new AxisAlignedBB(0, 0, 0, 1, 0.0625 * 20, 1);
    
    public AltarPortal(String name)
    {
	super(Material.ROCK, name);
	this.setLightLevel(8.0F);
	this.setHardness(-1.0F);
	this.setResistance(6000001.0F);
	this.setSoundType(SoundType.STONE);
	this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, RuneTypes.AIR));
    }
    
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
	    EnumFacing side, float hitX, float hitY, float hitZ)
    {
	if(!player.isSneaking())
	{
	    int blockMeta = this.getMetaFromState(state);
	    if((playerHasTalisman(player, blockMeta)) || (playerHasTiara(player, blockMeta)))
	    {
		IPlayerRcCap playerRcCap = player.getCapability(PlayerRcCapProvider.PLAYER_RC_CAP, null);
		playerRcCap.setLastPos(player.getPosition());
		addDestroyTags(player);
		
		if(!world.isRemote)
		{
		    switch(blockMeta) {
			case 0:
				CustomTeleporter.teleportToDimension(player, ModInfo.altarDimensionId, 15, 152, 19);
				break;
			case 1:
				System.out.println("Tele to Mind!");
				break;
			case 2:
				System.out.println("Tele to Water!");
				break;
			case 3:
				System.out.println("Tele to Earth!");
				break;
			case 4:
				System.out.println("Tele to Fire!");
				break;
			case 5:
				System.out.println("Tele to Body!");
				break;
			case 6:
				System.out.println("Tele to Cosmic!");
				break;
			case 7:
				System.out.println("Tele to Chaos!");
				break;
			case 8:
				System.out.println("Tele to Astral!");
				break;
			case 9:
				System.out.println("Tele to Nature!");
				break;
			case 10:
				System.out.println("Tele to Law!");
				break;
			case 11:
				System.out.println("Tele to Death!");
				break;
			case 12:
				System.out.println("Tele to Blood!");
				break;
			case 13:
				System.out.println("Tele to Soul!");
				break;				
			}
		}
	    }
	}
	return true;
    }
    
    
    public void addDestroyTags(EntityPlayer player)
    {
	NonNullList<ItemStack> inventory = player.inventoryContainer.getInventory();
	
	for(int i = 0; i < inventory.size(); i++)
	{
	    if(inventory.get(i).getItem() instanceof ItemPickaxe)
	    {
		NBTTagCompound nbt = (inventory.get(i).hasTagCompound()) ? inventory.get(i).getTagCompound() : new NBTTagCompound();
		
		if(!nbt.hasKey("CanDestroy", 9))
		    nbt.setTag("CanDestroy", new NBTTagList());
		
		if(!nbt.getTag("CanDestroy").toString().contains("runecrafting:essence_ore"))
		    nbt.getTagList("CanDestroy", 8).appendTag(new NBTTagString("runecrafting:essence_ore"));
		
		inventory.get(i).setTagCompound(nbt);
	    }
	}
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