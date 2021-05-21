package game.item;

import util.BmpRes;
import game.world.World;
import game.block.*;
import game.entity.*;
import static util.MathUtil.*;
import static java.lang.Math.*;

public class EnergyStone extends BasicBall implements BlockItem{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/EnergyStone");
	public BmpRes getBmp(){return bmp;}
	public Item clickAt(double x,double y,game.entity.Agent a){
		Block b=World.cur.get(x,y);
		if(b instanceof CircuitBlock){
			if(((CircuitBlock)b).ins(f2i(x),f2i(y),WireBlock.mid,WireBlock.mid))return null;
		}
		return super.clickAt(x,y,a);
	}
	
	public void onExplode(Entity pos,double tx,double ty,int amount,Source src){
		pos.explode(50,()->new game.entity.EnergyBall().setHpScale(amount),false);
	}
	@Override
	game.entity.Entity getBall(){return new game.entity.EnergyBall().setHpScale(35);}
};
