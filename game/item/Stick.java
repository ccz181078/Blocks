package game.item;

import util.BmpRes;
import game.entity.*;
import game.world.World;
import static util.MathUtil.*;
import game.block.*;

public class Stick extends Item implements LongItem1{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/Stick");
	public BmpRes getBmp(){return bmp;}
	public int fuelVal(){return 7;}
	public int swordVal(){return 2;}//剑值
	public double hardness(){return game.entity.NormalAttacker.WOODEN;}
	public Item clickAt(double x, double y, Agent a){
		if(EnergyPowerBlock.class.isAssignableFrom(World.cur.get(f2i(x),f2i(y)).getClass())){
			EnergyPowerBlock epl=(EnergyPowerBlock)World.cur.get(f2i(x),f2i(y));
			World.showText("能量"+new Integer(epl.energy).toString());
		}
		return super.clickAt(x,y,a);
	}
	public boolean onDragTo(SingleItem src,SingleItem dst,boolean batch){
		if(src.getAmount()==1){
			if(!src.get().longable())return false;
			Item w1=dst.popItem();
			Item w2=src.popItem();
			dst.insert(LongItem.make(w1,w2));
			return true;
		}
		return false;
	}
	public void onLongAttack(LongItem li,Entity a,Source src){
		li.item2.onAttack(a,src);
		if(rnd()<0.003)li.broken=true;
	}
	public void onLongDesBlock(LongItem li,Block block){
		li.item2.onDesBlock(block);
		if(rnd()<0.003)li.broken=true;
	}
	
}
