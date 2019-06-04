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
	public static ItemList create(SingleItem...si){
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
	public void onClick(Player pl,SingleItem s,ItemContainer ic){
		s.showInfo(pl);
		if(ic!=null){
			if(pl.batch_op)ic.insert(s);
			else{
				SingleItem a=s.pop();
				ic.insert(a);
				s.insert(a);
			}
		}
	}
	public void draw(Canvas cv,SingleItem si,int flag){
		si.draw(cv,si,flag);
	}
}

