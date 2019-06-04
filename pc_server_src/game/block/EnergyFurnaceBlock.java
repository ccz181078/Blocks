package game.block;

import util.BmpRes;
import game.ui.*;
import game.item.*;
import game.entity.DroppedItem;
import static util.MathUtil.*;

public class EnergyFurnaceBlock extends CraftHelperBlock implements BlockWithUI,EnergyProvider{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/EnergyFurnaceBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 200;}
	ItemList ecs;

	public void onPlace(int x,int y){
		super.onPlace(x,y);
		ecs=ItemList.emptyNonOverlapList(4);
	}

	public void onDestroy(int x,int y){
		DroppedItem.dropItems(ecs,x,y);
		ecs=null;
		super.onDestroy(x,y);
	}

	public boolean onUpdate(int x,int y){
		if(super.onUpdate(x,y))return true;
		EnergyProvider ec1=ec.get();
		if(ec1==null)return false;
		SingleItem si[]=ecs.toArray();
		Item it=si[rndi(0,si.length-1)].get();
		if(it==null||!(it instanceof EnergyReceiver))return false;
		EnergyReceiver ec2=(EnergyReceiver)it;
		int c=Math.min(rndi(1,40),Math.min(ec1.getEnergy(),ec2.resCap()));
		ec1.loseEnergy(c);
		ec2.gainEnergy(rf2i(c*0.98));
		return false;
	}
	
	public UI getUI(BlockAt ba){
		return new UI_MultiPage(){
			{
				addPage(new EnergyCell(),new UI_ItemList(ec,pl.il));
				addPage(new BlueCrystalEnergyCell(),new UI_ItemList(ecs,pl.il));
				addPage(new game.item.IronPickax(),new UI_Craft(Craft.getAllEq(CraftInfo._heat)));
			}
		}.setBlock(ba).setCraftHelper(ch);
	}
	public int getCraftType(){return ch.free()?CraftInfo._heat:0;}
};
