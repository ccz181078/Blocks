package game.block;

import static util.MathUtil.*;
import util.BmpRes;
import game.entity.*;

public class EnergyStoneBlock extends PureStoneType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/EnergyStoneBlock");
	public BmpRes getBmp(){return bmp;}
	
	public double light(){return 1;}
	
	public int energyValL(){return 32;}
	public int energyValR(){return 32;}
	public int energyValD(){return 32;}
	public int energyValU(){return 32;}
	public boolean circuitCanBePlaced(){return false;}
	public void touchEnt(int x,int y,Entity ent){
		ent.onAttackedByEnergy(0.2*intersectionLength(x,y,ent),SourceTool.block(x,y,this));
		super.touchEnt(x,y,ent);
	}

};
