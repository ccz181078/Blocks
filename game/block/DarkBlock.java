package game.block;

import static util.MathUtil.*;
import util.BmpRes;
import game.entity.*;

public class DarkBlock extends PureStoneType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/DarkBlock");
	public BmpRes getBmp(){return bmp;}
	
	public double light(){return 1;}
	public boolean circuitCanBePlaced(){return false;}
	public void touchEnt(int x,int y,Entity ent){
		ent.onAttackedByDark(0.2*intersectionLength(x,y,ent),SourceTool.block(x,y,this));
		super.touchEnt(x,y,ent);
	}
};
