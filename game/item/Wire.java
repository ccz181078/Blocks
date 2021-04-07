package game.item;

import util.BmpRes;
import game.entity.Human;
import game.entity.Agent;
import game.world.World;
import game.block.*;
import static util.MathUtil.*;

public class Wire extends Item implements BlockItem{
	private static final long serialVersionUID=1844677L;
	static BmpRes[] bmp=BmpRes.load("Item/Wire_",2);
	public BmpRes getBmp(){return bmp[tp];}
	static int tps[]={WireBlock.lr_in,WireBlock.ud_in};
	int tp=0;
	@Override
	public int maxAmount(){return 999;}
	public void onUse(Human a){
		tp^=1;
		a.items.getSelected().insert(this);
	}
	public Item clickAt(double x,double y,Agent a){
		Block b=World.cur.get(x,y);
		int px=f2i(x),py=f2i(y);
		if(b.circuitCanBePlaced()){
			FastWireBlock w=FastWireBlock.make(b,tp);
			World.cur.setCircuit(px,py,w);
			return null;
		}
		if(b instanceof CircuitBlock){
			if(((CircuitBlock)b).ins(px,py,tps[tp],tps[tp]))return null;
		}
		return super.clickAt(x,y,a);
	}
	public BmpRes getUseBmp(){return rotate_btn;}
	public boolean xRev(){return false;}
};
