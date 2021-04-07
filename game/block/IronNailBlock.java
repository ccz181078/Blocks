package game.block;
import game.entity.*;

public class IronNailBlock extends IronBasedType{
	private static final long serialVersionUID=1844677L;
	int maxDamage(){return 40;}
	//public double getJumpAcc(){return 1;}
	public void touchEnt(int x,int y,Entity ent){
		ent.onAttacked(Math.min(intersection(x,y,ent)*2,0.2),SourceTool.block(x,y,this),this);
		super.touchEnt(x,y,ent);
	}
}
