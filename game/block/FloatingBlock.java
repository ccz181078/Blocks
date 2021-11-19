package game.block;

import util.BmpRes;
import game.ui.*;
import game.item.*;
import static util.MathUtil.*;
import game.entity.DroppedItem;

public class FloatingBlock extends IronBasedType implements BlockWithUI,game.item.DefaultEnergyContainer{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/FloatingBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 200;}
	public long last_move_time=0;
	private SpecialItem<EnergyCell> ec;
	public EnergyContainer getEnergyContainer(){return ec.get();}
	@Override
	public void onPlace(int x,int y){
		ec=new SpecialItem<EnergyCell>(EnergyCell.class);
	}
	@Override
	public void onDestroy(int x,int y){
		DroppedItem.dropItems(ec,x,y);
		ec=null;
		super.onDestroy(x,y);
	}
	@Override
	public final boolean circuitCanBePlaced(){return false;}
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
