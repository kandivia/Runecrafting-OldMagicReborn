package com.kandivia.runecrafting.implement;

import net.minecraft.util.IStringSerializable;

public class EnumHandler
{
    public static enum EssenceTypes implements IStringSerializable
    {
	RUNE("rune", 0),
	PURE("pure", 1);
	
	private String name;
	private int ID;
	
	private EssenceTypes(String name, int ID)
	{
	    this.name = name;
	    this.ID = ID;
	}
	
	
	@Override
	public String getName()
	{
	    return this.name;
	}
	
	
	public int getID()
	{
	    return ID;
	}
	
	
	@Override
	public String toString()
	{
	    return getName();
	}
    }
    
    
    public static enum RuneTypes implements IStringSerializable
    {
	AIR("air", 0),
	MIND("mind", 1),
	WATER("water", 2),
	EARTH("earth", 3),
	FIRE("fire", 4),
	BODY("body", 5),
	COSMIC("cosmic", 6),
	CHAOS("chaos", 7),
	ASTRAL("astral", 8),
	NATURE("nature", 9),
	LAW("law", 10),
	DEATH("death", 11),
	BLOOD("blood", 12),
	SOUL("soul", 13);
	
	private String name;
	private int ID;
	
	private RuneTypes(String name, int ID)
	{
	    this.name = name;
	    this.ID = ID;
	}
	
	
	@Override
	public String getName()
	{
	    return this.name;
	}
	
	
	public int getID()
	{
	    return ID;
	}
	
	
	@Override
	public String toString()
	{
	    return getName();
	}
    }
    
    
    public static enum StaffTypes implements IStringSerializable
    {
	NORMAL("normal", 0),
	AIR("air", 1),
	WATER("water", 2),
	EARTH("earth", 3),
	FIRE("fire", 4),;
	
	private String name;
	private int ID;
	
	private StaffTypes(String name, int ID)
	{
	    this.name = name;
	    this.ID = ID;
	}
	
	
	@Override
	public String getName()
	{
	    return this.name;
	}
	
	
	public int getID()
	{
	    return ID;
	}
	
	
	@Override
	public String toString()
	{
	    return getName();
	}
    }
}