package com.kandivia.runecrafting.spells;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.kandivia.runecrafting.capabilities.IPlayerRcCap;
import com.kandivia.runecrafting.capabilities.PlayerRcCapProvider;
import com.kandivia.runecrafting.implement.EnumHandler;
import com.kandivia.runecrafting.implement.RegisterObjects;
import com.kandivia.runecrafting.implement.EnumHandler.RuneTypes;
import com.kandivia.runecrafting.main.ModInfo;
import com.kandivia.runecrafting.main.Runecrafting;
import com.kandivia.runecrafting.network.RcTelePlayerMessage;
import com.kandivia.runecrafting.world.AltarDimGen;
import com.kandivia.runecrafting.world.RCWorldData;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

public class Spell
{
    private static Random rand = new Random();
    
    
    /*
     * Ender Pocket Spell
     * Opens the player's Ender Chest GUI
     */
    public static void enderPocket(EntityPlayer player)
    {
	int[][] runeList = {
		{EnumHandler.RuneTypes.LAW.getID(), 2},
		{EnumHandler.RuneTypes.COSMIC.getID(), 1}
	};
	
	if(hasRunesForCast(player, runeList))
	{
	    if(!player.world.isRemote)
		player.displayGUIChest(player.getInventoryEnderChest());
	    
	    player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
	    IPlayerRcCap playerRcCap = player.getCapability(PlayerRcCapProvider.PLAYER_RC_CAP, null);
	    
	    if(clearRunes(player, runeList))
		playerRcCap.addMagicExp(player, 50);
	}
	else
	    player.sendStatusMessage(new TextComponentString("You don't have the necessary runes to cast this spell!"), true);
    }
    
    
    /*Bones to Apples Spell
     * Finds up to 8 Bones in player's inventory & turns them into Apples
     */
    public static void bonesToApples(EntityPlayer player)
    {
	int[][] runeList = {
		{EnumHandler.RuneTypes.WATER.getID(), 2},
		{EnumHandler.RuneTypes.EARTH.getID(), 2},
		{EnumHandler.RuneTypes.NATURE.getID(), 1}
	};
	
	if(hasRunesForCast(player, runeList))
	{
	    if(player.inventory.hasItemStack(new ItemStack(Items.BONE)))
	    {
		if(player.inventory.getFirstEmptyStack() != -1)
		{
		    int amount = player.inventory.clearMatchingItems(Items.BONE, -1, 8, null);
		    player.inventory.addItemStackToInventory(new ItemStack(Items.APPLE, amount, 0));
		    IPlayerRcCap playerRcCap = player.getCapability(PlayerRcCapProvider.PLAYER_RC_CAP, null);
		    
		    if(clearRunes(player, runeList))
			playerRcCap.addMagicExp(player, 10 * amount);
		}
		else
		    player.sendStatusMessage(new TextComponentString("You don't have any room in your inventory!"), true);
	    }
	    else
		player.sendStatusMessage(new TextComponentString("You don't have any Bones to cast this spell on!"), true);
	}
	else
	    player.sendStatusMessage(new TextComponentString("You don't have the necessary runes to cast this spell!"), true);
    }
    
    
    /*
     * Erosion Spell
     * Finds up to 8 Cobblestone in player's inventory & turns them into Sand
     */
    public static void erosion(EntityPlayer player)
    {
	int[][] runeList = {
		{EnumHandler.RuneTypes.WATER.getID(), 3},
		{EnumHandler.RuneTypes.EARTH.getID(), 2}
	};
	
	if(hasRunesForCast(player, runeList))
	{
	    if(player.inventory.hasItemStack(new ItemStack(Blocks.COBBLESTONE)))
	    {
		if(player.inventory.getFirstEmptyStack() != -1)
		{
		    int amount = player.inventory.clearMatchingItems(Item.getItemFromBlock(Blocks.COBBLESTONE), -1, 8, null);
		    player.inventory.addItemStackToInventory(new ItemStack(Blocks.SAND, amount, 0));
		    IPlayerRcCap playerRcCap = player.getCapability(PlayerRcCapProvider.PLAYER_RC_CAP, null);
		    
		    if(clearRunes(player, runeList))
			playerRcCap.addMagicExp(player, 10 * amount);
		}
		else
		    player.sendStatusMessage(new TextComponentString("You don't have any room in your inventory!"), true);
	    }
	    else
		player.sendStatusMessage(new TextComponentString("You don't have any Cobblestone to cast this spell on!"), true);
	}
	else
	    player.sendStatusMessage(new TextComponentString("You don't have the necessary runes to cast this spell!"), true);
    }
    
    
    /*
     * Humidify Spell
     * Finds up to 8 Sand in player's inventory & turns them into Clay
     */
    public static void Humidify(EntityPlayer player)
    {
	int[][] runeList = {
		{EnumHandler.RuneTypes.AIR.getID(), 1},
		{EnumHandler.RuneTypes.WATER.getID(), 5}
	};
	
	if(hasRunesForCast(player, runeList))
	{
	    if(player.inventory.hasItemStack(new ItemStack(Blocks.SAND)))
	    {
		if(player.inventory.getFirstEmptyStack() != -1)
		{
		    int amount = player.inventory.clearMatchingItems(Item.getItemFromBlock(Blocks.SAND), -1, 8, null);
		    player.inventory.addItemStackToInventory(new ItemStack(Blocks.CLAY, amount, 0));
		    IPlayerRcCap playerRcCap = player.getCapability(PlayerRcCapProvider.PLAYER_RC_CAP, null);
		    
		    if(clearRunes(player, runeList))
			playerRcCap.addMagicExp(player, 10 * amount);
		}
		else
		    player.sendStatusMessage(new TextComponentString("You don't have any room in your inventory!"), true);
	    }
	    else
		player.sendStatusMessage(new TextComponentString("You don't have any Sand to cast this spell on!"), true);
	}
	else
	    player.sendStatusMessage(new TextComponentString("You don't have the necessary runes to cast this spell!"), true);
    }
    
    
    /*Superheat Spell
     * Finds up to 8 smeltable items in player's inventory & smelts them
     */
    public static void superheat(EntityPlayer player)
    {
	int[][] runeList = {
		{EnumHandler.RuneTypes.FIRE.getID(), 4},
		{EnumHandler.RuneTypes.NATURE.getID(), 1}
	};
	
	if(hasRunesForCast(player, runeList))
	{
	    NonNullList<ItemStack> toSmelt = getSmeltable(player);
	    if(!toSmelt.isEmpty())
	    {
		if(player.inventory.getFirstEmptyStack() != -1)
		{
		    int numToSmelt = 8;
		    for(ItemStack itemstack : toSmelt)
		    {
			numToSmelt -= performSmelt(player, itemstack, numToSmelt);
			if(numToSmelt <= 0)
			    break;
		    }
		    
		    player.playSound(SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, 4.0F, 1.0F);	    
		    IPlayerRcCap playerRcCap = player.getCapability(PlayerRcCapProvider.PLAYER_RC_CAP, null);
		    
		    if(clearRunes(player, runeList))
			playerRcCap.addMagicExp(player, 10 * (8 - numToSmelt));
		}
		else
		    player.sendStatusMessage(new TextComponentString("You don't have any room in your inventory!"), true);
	    }
	    else
		player.sendStatusMessage(new TextComponentString("You don't have any items to smelt!"), true);
	}
	else
	    player.sendStatusMessage(new TextComponentString("You don't have the necessary runes to cast this spell!"), true);
    }
    
    
    /*Low/Medium/High Enchant Spell
     * Enchants the Item found in player's off-hand slot
     * Cost dependant on Lv of Spell
     */
    public static void enchantHeldItem(EntityPlayer player, int spellLv)
    {
	int[][] runeList = {
		{EnumHandler.RuneTypes.WATER.getID(), 12 * spellLv},
		{EnumHandler.RuneTypes.COSMIC.getID(), 4 * spellLv}
	};
	
	if(hasRunesForCast(player, runeList))
	{
	    ItemStack offhand = player.getHeldItemOffhand();
	    if((offhand != ItemStack.EMPTY) && offhand.isItemEnchantable())
	    {
		int enchantLevel = EnchantmentHelper.calcItemStackEnchantability(rand, 2, spellLv * 5, offhand);
		if(enchantLevel > 0)
		{
		    List<EnchantmentData> list = EnchantmentHelper.buildEnchantmentList(rand, offhand, enchantLevel, false);
		    boolean isBook = offhand.getItem() == Items.BOOK;
		    ItemStack enchBook = new ItemStack(Items.ENCHANTED_BOOK);
		    
		    for(EnchantmentData data : list)
		    {
			if(isBook)
			{
			    if(player.inventory.getFirstEmptyStack() != -1)
				ItemEnchantedBook.addEnchantment(enchBook, data);
			    else
			    {
				player.sendStatusMessage(new TextComponentString("You don't have any room in your inventory!"), true);
				return;
			    }
			}
			else
			    offhand.addEnchantment(data.enchantment, data.enchantmentLevel);
		    }
		    
		    if(isBook)
		    {
			player.inventory.clearMatchingItems(Items.BOOK, -1, 1, null);
			player.inventory.addItemStackToInventory(enchBook);
		    }
		    
		    player.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, rand.nextFloat() * 0.1F + 0.9F);
		    IPlayerRcCap playerRcCap = player.getCapability(PlayerRcCapProvider.PLAYER_RC_CAP, null);
		    
		    if(clearRunes(player, runeList))
			playerRcCap.addMagicExp(player, 40 * spellLv);
		}
		else
		    player.sendStatusMessage(new TextComponentString("This item cannot be enchanted with this spell!"), true);
	    }
	    else
		player.sendStatusMessage(new TextComponentString("You're not holding an enchantable item in the off-hand!"), true);
	}
	else
	    player.sendStatusMessage(new TextComponentString("You don't have the necessary runes to cast this spell!"), true);
    }
    
    
    /*Blink Spell
     * Instantly moves the player to player's rayTrace location up to a certain # of blocks forward
     */
    public static void blink(World world, EntityPlayer player, Item book)
    {
	int[][] runeList = {
		{EnumHandler.RuneTypes.AIR.getID(), 15},
		{EnumHandler.RuneTypes.LAW.getID(), 1}
	};
	
	int distance = 10;
	
	//RayTraceResult eyeTrace = player.rayTrace(15, 1.0F);
	//System.out.println("TRACER: " + eyeTrace.toString());
	//System.out.println(eyeTrace.hitVec.y-player.posY + 1.1);
	
	if(hasRunesForCast(player, runeList)) {
	    for (int i = 0; i < 32; i++)
		world.spawnParticle(EnumParticleTypes.SPELL, player.posX, player.posY + rand.nextDouble() * 2.0D, player.posZ, rand.nextGaussian(), 0.0D, rand.nextGaussian(), new int[0]);
	    
	    if(world.isRemote) {
		RayTraceResult eyeTrace = player.rayTrace(distance, 1.0F);
		
		if(eyeTrace.typeOfHit == RayTraceResult.Type.MISS) {
		    double x = -distance*Math.sin(Math.toRadians(player.rotationYawHead))*Math.cos(Math.toRadians(player.rotationPitch)) + player.posX;
		    double y = -distance*Math.sin(Math.toRadians(player.rotationPitch)) + player.posY;
		    double z = distance*Math.cos(Math.toRadians(player.rotationYawHead))*Math.cos(Math.toRadians(player.rotationPitch)) + player.posZ;
		    Runecrafting.network.sendToServer(new RcTelePlayerMessage(x, y, z));
		} else {
		    double x = Math.round(eyeTrace.hitVec.x);
		    double y = Math.round(eyeTrace.hitVec.y);
		    double z = Math.round(eyeTrace.hitVec.z);
		    Runecrafting.network.sendToServer(new RcTelePlayerMessage(x, y, z));
		}
	    }
	    
	    for (int i = 0; i < 32; i++)
		world.spawnParticle(EnumParticleTypes.SPELL, player.posX, player.posY + rand.nextDouble() * 2.0D, player.posZ, rand.nextGaussian(), 0.0D, rand.nextGaussian(), new int[0]);
	    
	    world.playSound(player, player.getPosition(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
	    //player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
	    
	    player.getCooldownTracker().setCooldown(book, 40);
	    IPlayerRcCap playerRcCap = player.getCapability(PlayerRcCapProvider.PLAYER_RC_CAP, null);
	    
	    if(clearRunes(player, runeList))
		playerRcCap.addMagicExp(player, 80); 	    
	}
	else
	    player.sendStatusMessage(new TextComponentString("You don't have the necessary runes to cast this spell!"), true);
    }
    
    public static void blink2(EntityPlayer player, double x, double y, double z) {
	/*EnderTeleportEvent event = new EnderTeleportEvent(player, x, y, z, 0.0F);
		
	if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) {
	    player.setPositionAndUpdate(event.getTargetX(), event.getTargetY(), event.getTargetZ());
	    player.fallDistance = 0.0F;
	}
	*/
	player.setPositionAndUpdate(x, y, z);
	    player.fallDistance = 0.0F;
    }
    
    
    /*Locate Spell
     * Locates given Structure
     */
    public static void locateVanillaStructure(World world, EntityPlayer player, String struct)
    {
	int[][] runeList = {
		{EnumHandler.RuneTypes.LAW.getID(), 2},
		{EnumHandler.RuneTypes.SOUL.getID(), 1}
	};
	
	if(hasRunesForCast(player, runeList))
	{
	    BlockPos structPos = player.getEntityWorld().findNearestStructure(struct, player.getPosition(), false);
	    
	    if(structPos != null && !world.isRemote)
	    {
		int d0 = structPos.getX() - player.getPosition().getX(); // += east  -=west
            	int d1 = structPos.getZ() - player.getPosition().getZ(); // += south -=north
            	
            	String dirX = "";
            	String dirZ = "";
            	
            	if(d0 > 20)
            		dirX = "east";
            	else if (d0 < -20)
            		dirX = "west";
            	
            	if(d1 > 20)
            		dirZ = "south";
            	else if (d1 < -20)
            		dirZ = "north";
            	
            	if(dirX != "" || dirZ != "")
            	    player.sendStatusMessage(new TextComponentString("The " + struct + " lies to the " + dirZ + dirX + "!"), true);
            	else
            	    player.sendStatusMessage(new TextComponentString("The " + struct + " is closeby!"), true);
	    }
	    else
		player.sendStatusMessage(new TextComponentString(struct + " not found!"), true);
	    
	    IPlayerRcCap playerRcCap = player.getCapability(PlayerRcCapProvider.PLAYER_RC_CAP, null);
	    
	    if(clearRunes(player, runeList))
		playerRcCap.addMagicExp(player, 20);
	}
	else
	    player.sendStatusMessage(new TextComponentString("You don't have the necessary runes to cast this spell!"), true);
    }
    
    
    //####################################################################################################################################
    //-------------------------------------------Utility Methods--------------------------------------------------------------------------
    //####################################################################################################################################
    
    
    
    /*
     * @return true if player has enough runes or equipped staffs to cast a spell
     * Cost of Spell is dictated by int[][] runeList 
     */
    private static boolean hasRunesForCast(EntityPlayer player, int[][] runeList)
    {
	for(int i = 0; i < runeList.length; i++)
	{
	    switch(runeList[i][0]) {
	    case 0:
		if(!checkHeldStaff(player, 1) && !checkItems(player, new ItemStack(RegisterObjects.runes, 1, runeList[i][0]), runeList[i][1]))
		    return false;
		break;
	    case 1:
		if(!checkItems(player, new ItemStack(RegisterObjects.runes, 1, runeList[i][0]), runeList[i][1]))
		    return false;
		break;
	    case 2:
		if(!checkHeldStaff(player, 2) && !checkItems(player, new ItemStack(RegisterObjects.runes, 1, runeList[i][0]), runeList[i][1]))
		    return false;
		break;	
	    case 3:
		if(!checkHeldStaff(player, 3) && !checkItems(player, new ItemStack(RegisterObjects.runes, 1, runeList[i][0]), runeList[i][1]))
		    return false;
		break;
	    case 4:
		if(!checkHeldStaff(player, 4) && !checkItems(player, new ItemStack(RegisterObjects.runes, 1, runeList[i][0]), runeList[i][1]))
		    return false;
		break;
	    default:
		if(!checkItems(player, new ItemStack(RegisterObjects.runes, 1, runeList[i][0]), runeList[i][1]))
		    return false;
		break;
	    }
	}
	return true;
    }
    
    
    /*
     * @return true if player has staff of certain type equipped in their hands
     */
    private static boolean checkHeldStaff(EntityPlayer player, int type)
    {
	ItemStack staff = new ItemStack(RegisterObjects.staves, 1, type);
	if(((player.getHeldItem(EnumHand.MAIN_HAND) != null) &&	player.getHeldItem(EnumHand.MAIN_HAND).isItemEqual(staff)) ||
		((player.getHeldItem(EnumHand.OFF_HAND) != null) && player.getHeldItem(EnumHand.OFF_HAND).isItemEqual(staff)))
	    return true;
	return false;
    }
    
    
    /*
     * @return true if item * amount is found in player's inventory
     */
    private static boolean checkItems(EntityPlayer player, ItemStack item, int amount)
    {
	NonNullList<ItemStack> inventory = player.inventoryContainer.getInventory();
	int itemsFound = 0;
	
	for(int i = 0; i < inventory.size(); i++)
	{
	    if(inventory.get(i).isItemEqual(item))
		itemsFound += inventory.get(i).getCount();
	    
	    if(itemsFound >= amount)
		return true;
	}
	
	return false;
    }
    
    
    /*
     * Clears runes from player if respective staff is not in player's hand
     */
    private static boolean clearRunes(EntityPlayer player, int[][] runeList)
    {
	for(int i = 0; i < runeList.length; i++)
	{
	    switch(runeList[i][0]) {
	    case 0:
		if(!checkHeldStaff(player, 1))
		    player.inventory.clearMatchingItems(RegisterObjects.runes, runeList[i][0], runeList[i][1], null);
		break;
	    case 1:
		player.inventory.clearMatchingItems(RegisterObjects.runes, runeList[i][0], runeList[i][1], null);
		break;
	    case 2:
		if(!checkHeldStaff(player, 2))
		    player.inventory.clearMatchingItems(RegisterObjects.runes, runeList[i][0], runeList[i][1], null);
		break;	
	    case 3:
		if(!checkHeldStaff(player, 3))
		    player.inventory.clearMatchingItems(RegisterObjects.runes, runeList[i][0], runeList[i][1], null);
		break;
	    case 4:
		if(!checkHeldStaff(player, 4))
		    player.inventory.clearMatchingItems(RegisterObjects.runes, runeList[i][0], runeList[i][1], null);
		break;
	    default:
		player.inventory.clearMatchingItems(RegisterObjects.runes, runeList[i][0], runeList[i][1], null);
		break;
	    }
	}
	
	return true;
    }
    
    
    private static NonNullList<ItemStack> getSmeltable(EntityPlayer player)
    {
	NonNullList<ItemStack> inventory = player.inventoryContainer.getInventory();
	NonNullList<ItemStack> itemsToSmelt = NonNullList.create();
	
	for(int i = 0; i < inventory.size(); i++)
	{
	    if(FurnaceRecipes.instance().getSmeltingResult(inventory.get(i)) != ItemStack.EMPTY)
		itemsToSmelt.add(inventory.get(i));
	}
	
	return itemsToSmelt;
    }
    
    private static int performSmelt(EntityPlayer player, ItemStack itemstack, int amount)
    {
	ItemStack smeltedItem = FurnaceRecipes.instance().getSmeltingResult(itemstack);
	int smelted = player.inventory.clearMatchingItems(itemstack.getItem(), itemstack.getMetadata(), amount, null);
	player.inventory.addItemStackToInventory(new ItemStack(smeltedItem.getItem(), smelted, smeltedItem.getMetadata()));
	return smelted;
    }
    
    
    /*
     * Finds Runecrafting Structures
     * Based on vanilla MapGenStructure#findNearestStructurePosBySpacing
     */
    @Nullable
    public static BlockPos locateRcStructure(World world, BlockPos pos, int meta, int a, int b, int c, int e)
    {
	if(world.provider.getDimension() != 0)
	    return null;
	if(RCWorldData.get(world).getAltarBlockPos(meta) != null)
	    return new BlockPos(RCWorldData.get(world).getAltarBlockPos(meta));
	
	int i = pos.getX() >> 4;
	int j = pos.getZ() >> 4;
	int k = 0;
	
	for (Random random = new Random(); k <= e; ++k)
        {
            for (int l = -k; l <= k; ++l)
            {
                boolean flag = l == -k || l == k;

                for (int i1 = -k; i1 <= k; ++i1)
                {
                    boolean flag1 = i1 == -k || i1 == k;

                    if (flag || flag1)
                    {
                        int j1 = i + a * l;
                        int k1 = j + a * i1;

                        if (j1 < 0)
                            j1 -= a - 1;
                        
                        if (k1 < 0)
                            k1 -= a - 1;
                        
                        int l1 = j1 / a;
                        int i2 = k1 / a;
                        Random random1 = world.setRandomSeed(l1, i2, c);
                        l1 = l1 * a;
                        i2 = i2 * a;

                        l1 = l1 + (random1.nextInt(a - b) + random1.nextInt(a - b)) / 2;
                        i2 = i2 + (random1.nextInt(a - b) + random1.nextInt(a - b)) / 2;

                        MapGenBase.setupChunkSeed(world.getSeed(), random, l1, i2);
                        random.nextInt();
                        
                        if(RCWorldData.get(world).getAltarBlockPos(meta) != null)
                            return new BlockPos(RCWorldData.get(world).getAltarBlockPos(meta));
                        
                        if (attemptAltarSpawn(world, meta, l1, i2))
                        {
                            return RCWorldData.get(world).getAltarBlockPos(meta);
                        }
                        else if (k == 0)
                            break;
                    }
                }

                if (k == 0)
                    break;
            }
        }
	
	return null;
    }
    
    public static final ResourceLocation AIR_ALTAR_PORTAL = new ResourceLocation("runecrafting:altar_portal_air");
    
    public static boolean attemptAltarSpawn(World world, int meta, int chunkX, int chunkZ)
    {
	BlockPos target = new BlockPos(chunkX * 16 + rand.nextInt(16) , 0, chunkZ * 16 + rand.nextInt(16));
	target = new BlockPos(target.getX(), world.getChunkFromChunkCoords(chunkX, chunkZ).getHeight(target), target.getZ());
	
	if(AltarDimGen.isMinDistanceEnough(world, target))
	{
	    PlacementSettings settings = new PlacementSettings().setRotation(Rotation.NONE);
	    Template template = world.getSaveHandler().getStructureTemplateManager().getTemplate(world.getMinecraftServer(), AIR_ALTAR_PORTAL);
	    BlockPos size = template.getSize();
	    
	    if(AltarDimGen.strucValid(target, world, size.getX(), size.getY(), size.getZ(), Blocks.GRASS))
	    {
		target = new BlockPos(target.getX(), target.getY() - 3, target.getZ());
		BlockPos altarPos = new BlockPos(target.getX() + 5, target.getY() + 3, target.getZ() + 5);
		template.addBlocksToWorld(world, target, settings);
		world.setBlockState(altarPos, RegisterObjects.altar_portal.getStateFromMeta(meta));
		System.out.println("Altar " + meta + " Spawned at location: " + altarPos.getX() + ", " + altarPos.getY() + ", " + altarPos.getZ()); //###test line###
		RCWorldData.get(world).setAltarPos(meta, altarPos.getX(), altarPos.getY(), altarPos.getZ());
		return true;
	    }
	}
	return false;
    }
}