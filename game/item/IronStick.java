package game.item;

import util.BmpRes;
import game.entity.*;
import game.world.World;
import static util.MathUtil.*;
import game.block.*;

public class IronStick extends Item implements LongItem1{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/IronStick");
	public BmpRes getBmp(){return bmp;}
	public int swordVal(){return 2;}//剑值
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	static long last_click_t=-100;
	static int click_t=0;
	public Item clickAt(double x,double y,Agent a){
		if(a==World.cur.getRootPlayer()){
			if(World.cur.time<last_click_t+10){
				++click_t;
				if(click_t>20){
					click_t=0;
					last_click_t=-100;
					new debug.script.ScriptEditor().show();
				}
			}else click_t=0;
			last_click_t=World.cur.time;
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
		if(rnd()<0.001)li.broken=true;
	}
	public void onLongDesBlock(LongItem li,Block block){
		li.item2.onDesBlock(block);
		if(rnd()<0.001)li.broken=true;
	}
}
