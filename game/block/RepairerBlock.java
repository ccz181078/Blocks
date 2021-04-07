package game.block;

import util.BmpRes;
import game.ui.*;
import game.item.*;
import static util.MathUtil.*;
import game.entity.DroppedItem;

public class RepairerBlock extends IronBasedType implements BlockWithUI{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/RepairerBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 200;}
	private SpecialItem<EnergyCell> ec;
	private SpecialItem<Tool>tool[];
	@Override
	public boolean isDeep(){return true;}
	public void onPlace(int x,int y){
		ec=new SpecialItem<EnergyCell>(EnergyCell.class);
		tool=new SpecialItem[4];
		for(int i=0;i<4;++i)tool[i]=new SpecialItem<Tool>(Tool.class);
	}
	public boolean onUpdate(int x,int y){
		if(super.onUpdate(x,y))return true;
		EnergyCell ec1=ec.get();
		if(ec1==null)return false;
		Tool s=tool[rndi(0,tool.length-1)].get();
		if(s==null)return false;
		int c=Math.min(rndi(1,20),Math.min(ec.get().getEnergy(),s.damage));
		ec1.loseEnergy(c);
		s.damage-=rf2i(c*s.repairRate());
		if(s.damage<0)s.damage=0;
		return false;
	}
	public void onDestroy(int x,int y){
		DroppedItem.dropItems(ec,x,y);
		DroppedItem.dropItems(tool,x,y);
		ec=null;
		tool=null;
		super.onDestroy(x,y);
	}
	public UI getUI(BlockAt ba){
		return new UI_Group(-7,0){{
				addChild(new UI_ItemList(ec,pl.il));
				addChild(new UI_ItemList(1,1,2,2,ItemList.create(tool),pl.il));
			}
			public void open(){
				pl.il.setExItemList(ItemList.create(ec,tool[0],tool[1],tool[2],tool[3]));
			}
			public void close(){
				pl.il.setExItemList(null);
			}
		}.setBlock(ba);
	}
};
