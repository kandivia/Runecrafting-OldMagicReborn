package com.kandivia.runecrafting.network;

import com.kandivia.runecrafting.main.Runecrafting;
import com.kandivia.runecrafting.spells.Spell;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RcTelePlayerMessage implements IMessage
{
    public double x, y, z;
    
    public RcTelePlayerMessage() {}
    
    public RcTelePlayerMessage(double x, double y, double z)
    {
	this.x = x;
	this.y = y;
	this.z = z;	
    }
    
    
    @Override
    public void toBytes(ByteBuf buf)
    {
	buf.writeDouble(x);
	buf.writeDouble(y);
	buf.writeDouble(z);
    }
    
    
    @Override
    public void fromBytes(ByteBuf buf)
    {
	this.x = buf.readDouble();
	this.y = buf.readDouble();
	this.z = buf.readDouble();
    }
    
    
    public static class Handler implements IMessageHandler<RcTelePlayerMessage, IMessage>
    {
	@Override
	public IMessage onMessage(RcTelePlayerMessage message, MessageContext ctx)
	{
	    EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
	    Spell.blink2(serverPlayer, message.x, message.y, message.z);
	    return null;
	}
    }
}
