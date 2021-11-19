package game.item;

import util.BmpRes;
import game.world.World;
import game.entity.*;
import static util.MathUtil.*;

public class TempBlockShoes extends EnergyShoes{
	public static BmpRes bmp[]=BmpRes.load("Item/TempBlockShoes_",2);
	public BmpRes getBmp(){return bmp[cd>0?1:0];}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	int cd=0;
	public int maxDamage(){return 1000;}
	
	@Override
	public double getJumpAcc(Human h,double v){
		return v;
	}

	@Override
	public Shoes update(Human h){
		if(cd>0)--cd;
		if(cd==0&&hasEnergy(20)&&h.ydir==1){
			++damage;
			double y0=h.y-h.height();
			int x=f2i(h.x),y=f2i(y0-0.5);
			if(World.cur.get(x,y).isCoverable()&&y+1<y0){
				loseEnergy(20);
				World.cur.place(x,y,new game.block.TempEnergyBlock());
				cd=20;
			}
		}
		return super.update(h);
	}
};
