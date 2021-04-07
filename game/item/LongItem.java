package game.item;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.*;
import game.entity.*;
import game.block.Block;
import util.BmpRes;
import game.block.AirBlock;
import graphics.Canvas;
import util.SerializeUtil;
import game.world.StatMode.StatResult;

public class LongItem extends Item{
	private static final long serialVersionUID=1844677L;
	public BmpRes getBmp(){return item2.getBmp();}
	public Item item1,item2;
	boolean broken=false,used=false;
	private LongItem(){}
	
	public static LongItem make(Item i1,Item i2){
		LongItem it=new LongItem();
		it.item1=i1;
		it.item2=i2;
		return it;
	}
	public double hardness(){return max(item1.hardness(),item2.hardness());}
	@Override
	public double getPrice(StatResult result){
		return result.getPrice0(item1)+result.getPrice0(item2);
	}
	@Override
	public boolean onDragTo(SingleItem src,SingleItem dst,boolean batch){
		if(isBroken())return false;
		if(src.isEmpty()){
			src.insert(item1.setAmount(1));
			dst.pop();
			dst.insert(item2.setAmount(1));
		}
		return false;
	}
	@Override
	public void drawInfo(Canvas cv){
		item2.drawInfo(cv);
		cv.drawBitmap(item1.getBmp(),-0.5f,0,0,0.5f);
	}
	@Override
	public String getName(){return item1.getName()+"+"+item2.getName();}//获取名字
	@Override
	public String getDoc(){return item2.getDoc();}//获取说明
	
	@Override
	public double light(){return 0;}
	@Override
	public int maxAmount(){return 1;}
	@Override
	public int pickaxVal(){return item2.pickaxVal();}
	@Override
	public int shovelVal(){return item2.shovelVal();}
	@Override
	public int axVal(){return item2.axVal();}
	@Override
	public int swordVal(){return item2.swordVal();}//剑值
	@Override
	public int fuelVal(){return item1.fuelVal()+item2.fuelVal();}//燃料值
	@Override
	public void onDesBlock(Block b){
		((LongItem1)item1).onLongDesBlock(this,b);
	}//被用于破坏方块
	@Override
	public boolean onLongPress(Agent a,double tx,double ty){return item2.onLongPress(a,tx,ty);}
	@Override
	public void onAttack(Entity a,Source src){
		((LongItem1)item1).onLongAttack(this,a,src);
	}//被用于攻击生物
	
	@Override
	public boolean isBroken(){return broken||used||item2.isBroken();}
	@Override
	public void onBroken(double x,double y){//损坏
		if(used);
		else if(item2.isBroken())item2.onBroken(x,y);
		else item2.drop(x,y);
		if(broken)item1.onBroken(x,y);
		else item1.drop(x,y);
	}
}
interface LongItem1{
	void onLongAttack(LongItem li,Entity e,Source src);
	void onLongDesBlock(LongItem li,Block block);
}
