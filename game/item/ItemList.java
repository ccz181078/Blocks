package game.item;

import java.io.*;
import graphics.Canvas;
import game.entity.Player;

public class ItemList implements java.io.Serializable,ShowableItemContainer{
	private static final long serialVersionUID=1844677L;
	//物品列表
	
	protected SingleItem si[];
	protected ItemList(int n){
		si=new SingleItem[n];
	}
	protected ItemList(SingleItem _si[]){
		si=_si;
	}
	public void drop(double x,double y){
		game.entity.DroppedItem.dropItems(this,x,y);
	}
	
	public void lockAll(){
		for(int i=0;i<si.length;++i){
			if(!(si[i] instanceof LockedItem)){
				si[i]=new LockedItem(si[i]);
			}
		}
	}
	public boolean lock(){
		for(int i=si.length-1;i>=0;--i){
			if(!(si[i] instanceof LockedItem)){
				si[i]=new LockedItem(si[i]);
				return true;
			}
		}
		return false;
	}
	public void unlock(int cnt){
		for(int i=0;i<cnt;++i)unlock();
	}
	public boolean unlock(){
		for(int i=0;i<si.length;++i){
			if(si[i] instanceof LockedItem){
				si[i]=((LockedItem)si[i]).unlock();
				return true;
			}
		}
		return false;
	}
	
	//空列表
	public static ItemList emptyList(int n){
		ItemList il=new ItemList(n);
		for(int i=0;i<n;++i)il.si[i]=new SingleItem();
		return il;
	}
	
	//空的可选择列表
	public static SelectableItemList emptySelectableList(int n){
		SelectableItemList il=new SelectableItemList(n);
		for(int i=0;i<n;++i)il.si[i]=new SingleItem();
		il.select();
		return il;
	}
	
	//空的不可叠加的列表
	public static ItemList emptyNonOverlapList(int n){
		ItemList il=new ItemList(n);
		for(int i=0;i<n;++i)il.si[i]=new NonOverlapItem();
		return il;
	}
	
	public static SelectableItemList craftList(Craft[] cls){
		SelectableItemList il=new SelectableItemList(cls.length);
		for(int i=0;i<cls.length;++i)il.si[i]=cls[i].toItem();
		il.select();
		return il;
	}
	
	//从SingleItem数组创建
	public static ItemList create(ShowableItemContainer...sic){
		int sz=0;
		for(ShowableItemContainer i:sic){
			sz+=i.toArray().length;
		}
		SingleItem si[]=new SingleItem[sz];
		sz=0;
		for(ShowableItemContainer i:sic){
			for(SingleItem j:i.toArray())si[sz++]=j;
		}
		return new ItemList(si);
	}
	
	public void insert(SingleItem it){
		for(SingleItem i:si){
			if(it.isEmpty())return;
			if(!i.isEmpty())i.insert(it);
		}
		for(SingleItem i:si){
			if(it.isEmpty())return;
			i.insert(it);
		}
	}
	public SingleItem[] toArray(){return si;}
	@Override
	public void onClick(Player pl,SingleItem s,ItemContainer ic){
		if(ic!=null){
			if(pl.batch_op)ic.insert(s);
			else{
				SingleItem a=s.pop();
				ic.insert(a);
				s.insert(a);
			}
		}else{
			s.showInfo(pl);
		}
	}
	public boolean dragable(){return true;}
	public void draw(Canvas cv,SingleItem si,int flag){
		si.draw(cv,si,flag);
	}
}

