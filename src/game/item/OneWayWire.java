package game.item;

import util.BmpRes;
import game.entity.Human;
import game.block.Block;
import game.world.World;
import game.block.WireBlock;
import static util.MathUtil.*;

public class OneWayWire extends Item{
	private static final long serialVersionUID=1844677L;
	static BmpRes[] bmp=BmpRes.load("Item/OneWayWire_",4);
	public BmpRes getBmp(){return bmp[tp];}
	static int tps[]={WireBlock.r_in,WireBlock.d_in,WireBlock.l_in,WireBlock.u_in};
	int tp=0;
	public void onUse(Human a){
		tp=(tp+1)%4;
		a.items.getSelected().insert(this);
	}
	public Item clickAt(double x,double y,game.entity.Agent a){
		Block b=World._.get(x,y);
		int px=f2i(x),py=f2i(y);
		if(b.circuitCanBePlaced()){
			WireBlock w=new WireBlock(b);
			World._.setCircuit(px,py,w);
			b=w;
		}
		if(b.getClass()==WireBlock.class){
			if(((WireBlock)b).ins(px,py,tps[tp],tps[tp]|tps[tp^2]))return null;
		}
		return super.clickAt(x,y,a);
	}
	public BmpRes getUseBmp(){return rotate_btn;}
};
