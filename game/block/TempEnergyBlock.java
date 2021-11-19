package game.block;

import static util.MathUtil.*;
import util.BmpRes;
import game.item.Stone;
import graphics.Canvas;
import game.world.World;

public class TempEnergyBlock extends StoneType{
	static BmpRes bmp=new BmpRes("Block/TempEnergyBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 1;}
	public void onDestroy(int x,int y){}
	public void fall(int x,int y,double xmv,double ymv){
		World.cur.setAir(x,y);
	}
	public boolean onUpdate(int x,int y){
		World.cur.setAir(x,y);
		return true;
	}
};
