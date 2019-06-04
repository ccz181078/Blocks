package game.block;

import static util.MathUtil.*;
import util.BmpRes;

public class EnergyStoneBlock extends StoneType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/EnergyStoneBlock");
	public BmpRes getBmp(){return bmp;}
	
	int maxDamage(){return 300;}
	
	public int energyValX(){return 32;}
	public int energyValY(){return 32;}
	public boolean circuitCanBePlaced(){return false;}
	
};
