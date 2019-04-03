package com.kandivia.runecrafting.proxy;

import com.kandivia.runecrafting.capabilities.IPlayerRcCap;
import com.kandivia.runecrafting.implement.RegisterObjects;
import com.kandivia.runecrafting.main.ModInfo;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

public class ClientProxy extends CommonProxy
{
    private static final Capability<IPlayerRcCap> RC_CAP = null;
    
    @Override
    public void registerModelBakeryVariants()
    {
	ModelBakery.registerItemVariants(Item.getItemFromBlock(RegisterObjects.altars),
		new ResourceLocation(ModInfo.MOD_ID, "altar_air"),
		new ResourceLocation(ModInfo.MOD_ID, "altar_mind"),
		new ResourceLocation(ModInfo.MOD_ID, "altar_water"),
		new ResourceLocation(ModInfo.MOD_ID, "altar_earth"),
		new ResourceLocation(ModInfo.MOD_ID, "altar_fire"),
		new ResourceLocation(ModInfo.MOD_ID, "altar_body"),
		new ResourceLocation(ModInfo.MOD_ID, "altar_cosmic"),
		new ResourceLocation(ModInfo.MOD_ID, "altar_chaos"),
		new ResourceLocation(ModInfo.MOD_ID, "altar_astral"),
		new ResourceLocation(ModInfo.MOD_ID, "altar_nature"),
		new ResourceLocation(ModInfo.MOD_ID, "altar_law"),
		new ResourceLocation(ModInfo.MOD_ID, "altar_death"),
		new ResourceLocation(ModInfo.MOD_ID, "altar_blood"),
		new ResourceLocation(ModInfo.MOD_ID, "altar_soul"));
	
	ModelBakery.registerItemVariants(Item.getItemFromBlock(RegisterObjects.altar_portal),
		new ResourceLocation(ModInfo.MOD_ID, "altar_portal_air"),
		new ResourceLocation(ModInfo.MOD_ID, "altar_portal_mind"),
		new ResourceLocation(ModInfo.MOD_ID, "altar_portal_water"),
		new ResourceLocation(ModInfo.MOD_ID, "altar_portal_earth"),
		new ResourceLocation(ModInfo.MOD_ID, "altar_portal_fire"),
		new ResourceLocation(ModInfo.MOD_ID, "altar_portal_body"),
		new ResourceLocation(ModInfo.MOD_ID, "altar_portal_cosmic"),
		new ResourceLocation(ModInfo.MOD_ID, "altar_portal_chaos"),
		new ResourceLocation(ModInfo.MOD_ID, "altar_portal_astral"),
		new ResourceLocation(ModInfo.MOD_ID, "altar_portal_nature"),
		new ResourceLocation(ModInfo.MOD_ID, "altar_portal_law"),
		new ResourceLocation(ModInfo.MOD_ID, "altar_portal_death"),
		new ResourceLocation(ModInfo.MOD_ID, "altar_portal_blood"),
		new ResourceLocation(ModInfo.MOD_ID, "altar_portal_soul"));
    }
}