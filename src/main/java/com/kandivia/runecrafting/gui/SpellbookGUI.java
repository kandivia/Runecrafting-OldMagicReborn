package com.kandivia.runecrafting.gui;

import java.io.IOException;

import org.fusesource.jansi.Ansi.Color;

import com.kandivia.runecrafting.main.ModInfo;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class SpellbookGUI extends GuiScreen
{
    private static final ResourceLocation BOOK_BG = new ResourceLocation(ModInfo.MOD_ID, "textures/gui/test_book_background.png");
    private static final ResourceLocation BUTTON_ICON = new ResourceLocation(ModInfo.MOD_ID, "textures/gui/test_button_icon.png");
    
    private static final int TEXTURE_WIDTH = 148; //148
    private static final int TEXTURE_HEIGHT = 182; //182
    private static final int TEXTURE_XPOS = 0;
    private static final int TEXTURE_YPOS = 0;
    
    private static final int TEST_BUTTON_ID = 0; //0, 221
    
    @Override
    public void initGui()
    {
	super.initGui();
	GlStateManager.color(1, 1, 1, 1);
	
	this.buttonList.add(new SpellbookButton(TEST_BUTTON_ID, 155, 45, 16, 16, "test"));
	
    }
    
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
	//this.drawDefaultBackground();
	mc.getTextureManager().bindTexture(BOOK_BG);
	super.drawScreen(mouseX, mouseY, partialTicks);
	int x = (width - TEXTURE_WIDTH) / 2;
	int y = (height - TEXTURE_HEIGHT) / 2;
	drawTexturedModalRect(x, y, TEXTURE_XPOS, TEXTURE_YPOS, TEXTURE_WIDTH, TEXTURE_HEIGHT);
	super.drawScreen(mouseX, mouseY, partialTicks);
	
	
    }
    
    
    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
	if(button.id == 0)
	    System.out.println(button.displayString + " Button Pressed!");
    }
    
    
    @Override
    public boolean doesGuiPauseGame()
    {
	return false;
    }
    
    
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 1 || typedChar == 'e')
        {
            this.mc.displayGuiScreen((GuiScreen)null);

            if (this.mc.currentScreen == null)
            {
                this.mc.setIngameFocus();
            }
        }
    }
}
