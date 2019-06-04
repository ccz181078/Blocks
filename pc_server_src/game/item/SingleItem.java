package game.item;
import graphics.Canvas;
import game.block.Block;
import game.GlobalSetting;
import java.io.ObjectInputStream;
import java.io.IOException;

public class SingleItem implements ShowableItemContainer,Cloneable,java.io.Serializable{
	private static final long serialVersionUID=1844677L;
	//单个物品容器
	//所有单个物品容器的基类
	//可以容纳物品，允许叠加
	
	public static int SELECTED_FLAG=1,ALPHA_FLAG=2,NO_AMOUNT_FLAG=4;
	private Item item;
	private int amount;
	
	//空
	public SingleItem(){
		item=null;
		amount=0;
	}
	
	//指定物品和数量
	public SingleItem(Item _item,int _amount){
		item=_item;
		amount=_amount;
	}
	
	/*private void readObject(ObjectInputStream is)throws ClassNotFoundException,IOException{
		is.defaultReadObject();
		if(item!=null&&item.getClass()==BlockItem.class)item=((BlockItem)item).block;
	}*/
	
	/*//浅拷贝
	public SingleItem clone(){
		try{
			SingleItem si=(SingleItem)super.clone();
			si.item=item.clone();
			return si;
		}catch(Exception e){e.printStackTrace();}
		return null;
	}*/
	
	//分裂出一个物品容器，将当前容器中物品取出一个放到新容器，如果容器为空则返回空容器
	public SingleItem pop(){
		Item it=popItem();
		if(it==null)return new SingleItem();
		return it.setAmount(1);
	}
	
	//当前容器中物品取出一个，如果容器为空则返回null
	public Item popItem(){
		if(amount==0)return null;
		--amount;
		if(amount==0)return item;
		return item.clone();
	}
	
	public void dec(){
		--amount;
	}
	
	public void dec(int n){
		amount-=n;
	}
	public void clear(){
		amount=0;
	}
	public boolean isEmpty(){
		return amount==0;
	}
	public final int getAmount(){
		return amount;
	}
	public int maxAmount(){
		if(item==null)return 99;
		return item.maxAmount();
	}
	private boolean insertable(Item it){
		if(it==null)return false;
		if(isEmpty())return true;
		return item.cmpType(it);
	}
	
	//把si中的物品转移到当前容器
	public void insert(SingleItem si){
		if(si.isEmpty())return;
		if(insertable(si.item)){
			if(isEmpty())item=si.item.clone();
			int c=Math.min(maxAmount()-amount,si.amount);
			si.amount-=c;
			amount+=c;
		}
	}
	
	//插入一个物品
	public void insert(Item it){
		if(it==null)return;
		if(insertable(it)&&amount<maxAmount()){
			item=it;
			++amount;
		}
	}
	
	//获取容器中的物品，但不取出，容器为空返回null
	public Item get(){return amount>0?item:null;}
	
	//直接设置容器中的物品
	public void set(Item _item){item=_item;}
	
	public SingleItem[] toArray(){
		return new SingleItem[]{this};
	}
	
	//显示提示信息
	public void showInfo(game.entity.Player pl){
		Item a=get();
		if(a!=null)pl.addText(a.getName()+'\n'+a.getDoc());
	}
	
	//显示在物品列表界面中时，被点击
	public void onClick(game.entity.Player pl,SingleItem s,ItemContainer ic){
		if(ic!=null){
			if(pl.batch_op)ic.insert(s);
			else ic.insert(s.pop());
		}
	}
	public void draw(Canvas cv,SingleItem si,int flag){
		cv.drawItemFrame((flag&SELECTED_FLAG)!=0);
		if(si.isEmpty())return;
		Item it=si.get();
		cv.drawItem(it.getBmp(),(flag&ALPHA_FLAG)==0);
		it.drawInfo(cv);
		int c=si.getAmount();
		if((flag&NO_AMOUNT_FLAG)==0&&c!=1){
			float sz=GlobalSetting.getGameSetting().text_size;
			cv.drawText(Integer.toString(c),0.35f,0.35f,Math.min(sz,0.7f),1);
		}
	}
	public static SingleItem cast(Object obj){
		if(obj instanceof Item)return ((Item)obj).setAmount(1);
		return (SingleItem)obj;
	}
}

class NonOverlapItem extends SingleItem{
	private static final long serialVersionUID=1844677L;
	public int maxAmount(){return 1;}
}
