package game.block;

import util.BmpRes;
import game.world.World;
import graphics.Canvas;
import static java.lang.Math.*;
import game.item.Wire;
import game.item.OneWayWire;
import game.item.EnergyStone;
import game.item.EnergyBlocker;
import game.item.AndGate;

public class GlassBlock extends StoneType{
	private static final long serialVersionUID=1844677L;	
	static BmpRes bmp=new BmpRes("Block/GlassBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 40;}
	public double transparency(){return 0;}
	public void onDestroy(int x,int y){}
};
