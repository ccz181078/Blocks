package game.item;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.entity.*;
import game.block.*;
import game.world.*;


public class BlockActivator extends Tool{
	public int maxDamage(){return 1000;}
	protected double toolVal(){return 0;}
	private static BmpRes bmp=new BmpRes("Item/BlockActivator");
	public BmpRes getBmp(){return bmp;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	
	public Item clickAt(double x,double y,Agent w){
		if(max(abs(x-w.x),abs(y-w.y))>4)return this;
		int px=f2i(x),py=f2i(y);
		Block b=World.cur.get(px,py).deStatic(px,py);
		if(b.fallable()&&b.isSolid()){
			damage+=b.maxHp();
			new BlockAgent(b).initPos(px+0.5,py+0.5,0,0,SourceTool.place(w)).add();
			World.cur.setVoid(px,py);
		}
		return this;
	}
}

