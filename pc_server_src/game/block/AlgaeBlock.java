package game.block;

import util.BmpRes;
import game.item.Algae;
import static util.MathUtil.*;
import game.world.World;

public class AlgaeBlock extends AquaticPlantBlock{
	private static final long serialVersionUID=1844677L;
	public BmpRes getBmp(){return Algae.bmp;}
	public boolean onUpdate(int x,int y){
		if(light_v>4){
			light_v=0;
			if(rnd()<0.001){
				int x1=x+(rnd()<0.5?-1:1);
				int y1=y+(rnd()<0.5?-1:1);
				Class tp=World.cur.get(x1,y1).rootBlock().getClass();
				if(tp==WaterBlock.class){
					World.cur.place(x1,y1,new AlgaeBlock());
				}else if(tp==AlgaeBlock.class){
					World.cur.set(x,y,new WaterBlock());
					return true;
				}
			}
		}
		return LiquidType.onUpdate(x,y,this);
	}
	public void onDestroy(int x,int y){
		new Algae().drop(x,y);
		super.onDestroy(x,y);
	}
}
