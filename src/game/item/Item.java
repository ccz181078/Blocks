package game.item;

import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.*;
import game.entity.*;
import game.block.Block;
import util.BmpRes;
import game.block.AirBlock;
import graphics.Canvas;
import util.SerializeUtil;

public abstract class Item implements java.io.Serializable,Cloneable{
	private static final long serialVersionUID=1844677L;
	
	//所有物品的基类
	//物品一般只能存在于某个SingleItem中
	
	public int maxAmount(){return 99;}//最大叠加
	public int pickaxVal(){return 1;}//镐值
	public int shovelVal(){return 1;}//铲值
	public int axVal()    {return 1;}//斧值
	public int swordVal() {return 1;}//剑值
	public int fuelVal() {return 0;}//燃料值
	public int foodVal() {return 0;}//食物值
	public void onDesBlock(Block b){}//被用于破坏方块
	public void onAttack(Agent a){}//被用于攻击生物
	public void onUse(Human a){//按下使用按钮
		if(foodVal()>0)a.eat(this);
		else a.items.getSelected().insert(this);
	}
	public String getName(){return util.AssetLoader.loadString(getClass(),"name");}//获取名字
	public String getDoc(){return util.AssetLoader.loadString(getClass(),"doc");}//获取说明
	
	static BmpRes
		eat_btn=new BmpRes("UI/eat"),
		empty_btn=new BmpRes("UI/empty"),
		use_btn=new BmpRes("UI/use"),
		rotate_btn=new BmpRes("UI/rotate");
	
	public BmpRes getUseBmp(){//获取使用按钮的贴图
		return foodVal()>0?eat_btn:empty_btn;
	}
	public int heatingTime(boolean in_furnace){return 1000000000;}//获取加热后发生变化所需的期望时间
	public Item heatingProduct(boolean in_furnace){return null;}//获取加热后得到的物品
	public Item clone(){//浅拷贝，可叠加的物品不应有复杂的内部结构
		Item i=null;
		try{
			i=(Item)super.clone();
		}catch(Exception e){e.printStackTrace();}
		return i;
	}
	
	boolean cmpType(Item it){//比较是否可以合为一项
		return this.getClass()==it.getClass();
	}
	
	//被生物a用于点击世界中的x,y位置
	//return this or null, null means "this" is removed
	public Item clickAt(double x,double y,Agent a){
		if(!World._.get(x,y).onClick(f2i(x),f2i(y),a)){
			if(a instanceof Human)((Human)a).attack();
		}
		return this;
	}
	public static final Item air(){return new AirBlock().asItem();}
	
	public BmpRes getBmp(){//获取默认贴图
		return new BmpRes("Item/"+getClass().getSimpleName());
	}
	public SingleItem setAmount(int amount){//转化为一个SingleItem
		return new SingleItem(this,amount);
	}
	
	//掉落指定数量
	public DroppedItem drop(int x,int y,int c){
		DroppedItem d=new DroppedItem(x,y,setAmount(c));
		d.add();
		return d;
	}
	public DroppedItem drop(double x,double y,int c){
		DroppedItem d=new DroppedItem(x,y,setAmount(c));
		d.add();
		return d;
	}
	//掉落一个
	public DroppedItem drop(int x,int y){
		return drop(x,y,1);
	}
	public DroppedItem drop(double x,double y){
		return drop(x,y,1);
	}
	
	//绘制除默认贴图外的额外信息
	public void drawInfo(Canvas cv){}
};

