package game.block;

import util.BmpRes;
import static util.MathUtil.*;
import game.entity.*;
import game.world.World;

public class CoalBlock extends StoneType{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/CoalBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 80;}
	public int fuelVal(){return 1600;}
	public void onDestroy(int x,int y){
		new game.item.Coal().drop(x,y,rndi(14,16));
	}
	@Override
	public void onBurn(int x,int y){
		if(rnd()<1/40.){
			World.cur.setAir(x,y);
			Source ex=SourceTool.gen(SourceTool.block((int)x,(int)y,this));
			new game.entity.BurningCoalBall().initPos(x+0.5,y+0.5,0,0,ex).add();
		}
		super.onBurn(x,y);
	}
};
