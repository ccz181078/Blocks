package game.item;

import util.BmpRes;
import game.entity.Human;
import game.world.World;
import game.block.*;
import static util.MathUtil.*;

public class OneWayWire extends Item implements BlockItem{
	private static final long serialVersionUID=1844677L;
	static BmpRes[] bmp=BmpRes.load("Item/OneWayWire_",4);
	public BmpRes getBmp(){return bmp[tp];}
	static int tps[]={WireBlock.r_in,WireBlock.d_in,WireBlock.l_in,WireBlock.u_in};
	int tp=0;
	public OneWayWire(){}
	OneWayWire(int tp){this.tp=tp&3;}
	@Override
	public int maxAmount(){return 999;}
	public void onUse(Human a){
		tp=(tp+1)%4;
		a.items.getSelected().insert(this);
	}
	public Item clickAt(double x,double y,game.entity.Agent a){
		Block b=World.cur.get(x,y);
		int px=f2i(x),py=f2i(y);
		if(b.circuitCanBePlaced()){
			WireBlock w=new WireBlock(b);
			World.cur.setCircuit(px,py,w);
			b=w;
		}
		if(b instanceof CircuitBlock){
			if(((CircuitBlock)b).ins(px,py,tps[tp],tps[tp]))return null;
		}
		return super.clickAt(x,y,a);
	}
	public BmpRes getUseBmp(){return rotate_btn;}
	public boolean xRev(){return false;}
};
