package game.item;

import util.BmpRes;
import game.entity.*;
import game.world.World;
import static util.MathUtil.*;
import game.block.*;

public class FireStick extends Item implements LongItem1{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/FireStick");
	public BmpRes getBmp(){return bmp;}
	public int swordVal(){return 2;}//剑值
	public boolean onDragTo(SingleItem src,SingleItem dst,boolean batch){
		if(src.getAmount()==1){
			if(!(src.get() instanceof Warhead))return false;
			Item w1=dst.popItem();
			Item w2=src.popItem();
			dst.insert(LongItem.make(w1,w2));
			return true;
		}
		return false;
	}
	public void onLongAttack(LongItem li,Entity e,Source src){
		if(li.used)return;
		li.broken=true;
		li.used=true;
		((Warhead)li.item2).explode(e.x+rnd_gaussion()*e.width()*0.5,e.y+rnd_gaussion()*e.height()*0.5,rnd_gaussion()*0.1,rnd_gaussion()*0.7,src,null);
	}
	public void onLongDesBlock(LongItem li,Block block){
		li.item2.onDesBlock(block);
		if(rnd()<0.001)li.broken=true;
	}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
}
