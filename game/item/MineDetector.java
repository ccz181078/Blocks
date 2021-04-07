package game.item;

import util.BmpRes;
import game.block.Block;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.entity.Agent;
import game.world.World;

public class MineDetector extends EnergyTool{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp[]=BmpRes.load("Item/MineDetector_",2);
	public BmpRes getBmp(){return bmp[state];}
	public int maxDamage(){return 1000;}
	public double toolVal(){return 0;}
	@Override
	public double light(){return state;}
	int state=0;
	@Override
	public void onCarried(Agent a){
		if(hasEnergy(1)){
			if(rnd()<0.01)loseEnergy(1);
			if(rnd()<0.03)++damage;
			if(rnd()<0.1)state=0;
			for(int i=0;i<5;++i){
				if(World.cur.get(a.x+rnd_gaussion()*3,a.y+rnd_gaussion()*3) instanceof game.block.MineBlock){
					state=1;
					break;
				}
			}
		}else state=0;
	}
}

