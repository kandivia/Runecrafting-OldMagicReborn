package com.kandivia.runecrafting.network;

import com.kandivia.runecrafting.capabilities.IPlayerRcCap;
import com.kandivia.runecrafting.capabilities.PlayerRcCapProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RcPlayerDataMessage implements IMessage
{
    public int pkMagicExp, pkRuneExp, pkXPos, pkYPos, pkZPos, pkSpellId;
    
    public RcPlayerDataMessage() {}
    
    public RcPlayerDataMessage(int pkMagicExp, int pkRuneExp, int pkXPos, int pkYPos, int pkZPos, int pkSpellId)
    {
	this.pkMagicExp = pkMagicExp;
	this.pkRuneExp = pkRuneExp;
	this.pkXPos = pkXPos;
	this.pkYPos = pkYPos;
	this.pkZPos = pkZPos;
	this.pkSpellId = pkSpellId;
    }
    
    
    @Override
    public void toBytes(ByteBuf buf)
    {
	buf.writeInt(pkMagicExp);
	buf.writeInt(pkRuneExp);
	buf.writeInt(pkXPos);
	buf.writeInt(pkYPos);
	buf.writeInt(pkZPos);
	buf.writeInt(pkSpellId);
    }
    
    
    @Override
    public void fromBytes(ByteBuf buf)
    {
	this.pkMagicExp = buf.readInt();
	this.pkRuneExp = buf.readInt();
	this.pkXPos = buf.readInt();
	this.pkYPos = buf.readInt();
	this.pkZPos = buf.readInt();
	this.pkSpellId = buf.readInt();
    }
    
    public static class Handler implements IMessageHandler<RcPlayerDataMessage, IMessage>
    {
	@Override
	public IMessage onMessage(RcPlayerDataMessage message, MessageContext ctx)
	{
	    EntityPlayerSP clientPlayer = Minecraft.getMinecraft().player;
	    
	    try
	    {
		IPlayerRcCap playerRcCap = clientPlayer.getCapability(PlayerRcCapProvider.PLAYER_RC_CAP, null);
		
		playerRcCap.setMagicExp(message.pkMagicExp);
		playerRcCap.setRuneExp(message.pkRuneExp);
		playerRcCap.setLastPos(new BlockPos(message.pkXPos, message.pkYPos, message.pkZPos));
		playerRcCap.setSpellId(message.pkSpellId);
	    }
	    catch(NullPointerException e)
	    {
		System.out.println("Runecrafting: Player Cap not found");
	    }
	    return null;
	}
    }
}
