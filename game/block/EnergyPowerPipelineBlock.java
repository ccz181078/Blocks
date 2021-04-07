package game.block;
import util.*;
import game.entity.*;
import game.world.World;
import graphics.*;

public class EnergyPowerPipelineBlock extends EnergyPowerBlock{
	private static final long serialVersionUID=1844677L;
	static BmpRes[] bmp=BmpRes.load("Block/EnergyPowerPipelineBlock_",14);
	public BmpRes getBmp(){
		return bmp[0];
	}
	
	int maxDamage(){return 50;}
	
	public int maxEnergy(){return 1000;}
	public int outEnergy(){return 2;}
	public int inEnergy(){return 2;}
	public int sEnergy(){return 3;}
	public int tp(){return 3;}
	
	public void draw(Canvas cv){
		
	}
	
	public boolean onCheck(int x,int y){
		super.onCheck(x,y);
		EnergyPowerBlock[] ebl=getNearEnergyBlock(x,y);
		a(ebl);
		return true;
	}
}
