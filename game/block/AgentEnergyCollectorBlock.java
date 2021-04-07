package game.block;

import util.BmpRes;
import game.ui.*;
import game.item.*;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.entity.*;

public class AgentEnergyCollectorBlock extends IronBasedType implements BlockWithUI{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/AgentEnergyCollectorBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 200;}
	private SpecialItem<EnergyCell> ec;
	@Override
	public void onPlace(int x,int y){
		ec=new SpecialItem<EnergyCell>(EnergyCell.class);
	}
	@Override
	public void touchEnt(int x,int y,Entity ent){
		if(ent instanceof EnergyContainer){
			EnergyContainer ep=(EnergyContainer)ent;
			if(ep.getEnergy()>=1){
				ep.loseEnergy(1);
				EnergyCell ec1=ec.get();
				if(ec1!=null)if(ec1.resCap()>=1)ec1.gainEnergy(1);
			}
		}
		super.touchEnt(x,y,ent);
	}
	@Override
	public void onDestroy(int x,int y){
		DroppedItem.dropItems(ec,x,y);
		ec=null;
		super.onDestroy(x,y);
	}
	@Override
	public UI getUI(BlockAt ba){
		return new UI_Group(-7,0){{
				addChild(new UI_ItemList(ec,pl.il));
			}
			public void open(){
				pl.il.setExItemList(ItemList.create(ec));
			}
			public void close(){
				pl.il.setExItemList(null);
			}
		}.setBlock(ba);
	}
	public boolean isDeep(){return true;}
};
