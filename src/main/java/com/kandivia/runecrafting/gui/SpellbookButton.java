package com.kandivia.runecrafting.gui;

import com.kandivia.runecrafting.main.ModInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class SpellbookButton extends GuiButton
{
    protected static final ResourceLocation SpellbookIcons = new ResourceLocation(ModInfo.MOD_ID, "textures/gui/spellbook_icons.png");
    
    public SpellbookButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText)
    {
	super(buttonId, x, y, widthIn, heightIn, buttonText);
    }
    
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	
	drawTexturedModalRect(x, y, 0, 221, width, height);
	// x y: where it appears on screen
	// 0, 221 : texture start pos
	//width height: texture width & height
    }
    
}
