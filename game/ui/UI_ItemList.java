package game.ui;

import static util.MathUtil.*;
import static java.lang.Math.*;
import game.item.*;
import graphics.Canvas;
import game.entity.Player;
import util.BmpRes;

public class UI_ItemList extends UI_Group{
	private static final long serialVersionUID=1844677L;
	//描述一个普通的物品列表界面
	//列表从左到右，从上到下绘制，如果位置不够会自动分页
	//这个界面的物品被点击时，把对应物品insert到绑定的ItemContainer中
	
	ShowableItemContainer il;
	private UI_ItemList il2;
	public UI_ItemList duel_ui_il=null;
	private ItemContainer ex_il;
	int width,height,cnt,page;
	SingleItem si[];
	
	
	public UI_ItemList(ShowableItemContainer _il,UI_ItemList _il2){
		this(0,0,4,8,_il,_il2);
	}
	
	//左上角坐标，宽高，物品列表（容量不能为0），绑定的物品列表界面（允许null）
	//如果绑定了物品列表界面，点击时会向界面传物品
	//否则如果绑定了物品列表，点击时会向列表传物品
	public UI_ItemList(float _x,float _y,int _width,int _height,ShowableItemContainer _il,UI_ItemList _il2){
		this(_x,_y,_width,_height,_il,_il2,false);
	}
	public UI_ItemList(float _x,float _y,int _width,int _height,ShowableItemContainer _il,UI_ItemList _il2,boolean newpage){
		super(_x,_y);
		width=_width;
		height=_height;
		cnt=width*height;
		page=0;
		il=_il;
		setExUIItemList(_il2);
		si=il.toArray();
		if(cnt<si.length||newpage){
			cnt=width*(height-1);
			addChild(new UI_Button(0,height-1){
				@Override
				protected BmpRes getBmp(){return page>0?left_btn:empty_btn;}
				@Override
				protected void onPress(){if(page>0)--page;}
			});
			addChild(new UI_Button(width-1,height-1){
				@Override
				protected BmpRes getBmp(){return (page+1)*cnt<si.length?right_btn:empty_btn;}
				@Override
				protected void onPress(){if((page+1)*cnt<si.length)++page;}
			});
		}
	}
	protected void onDraw(Canvas cv){
		cv.gridBegin(0.5f,0.5f);
		int L=page*cnt,R=min((page+1)*cnt,si.length);
		for(int i=L;i<R;++i){
			il.draw(cv,si[i],0);
			if((i-L+1)%width==0)cv.gridNewLine();
			else cv.gridNext();
		}
		cv.end();
		super.onDraw(cv);
	}
	private transient int drag_w=0;
	@Override
	protected boolean onTouch(float tx,float ty,int tp){
		if(super.onTouch(tx,ty,tp))return true;
		int px=f2i(tx),py=f2i(ty);
		if(px>=0&&px<width&&py>=0&&py<height){
			int w=py*width+px;
			if(w>=0&&w<cnt){
				w+=page*cnt;
				if(w>=0&&w<si.length){
					if(tp==0){
						pl.last_pressed_ui=this;
						drag_w=w;
					}
					if(tp==1){
						if(pl.last_pressed_ui instanceof UI_ItemList){
							UI_ItemList ex=(UI_ItemList)pl.last_pressed_ui;
							if(ex==this||getExItemList()==ex.il||duel_ui_il==ex||pl.creative_mode){
								if(ex==this&&w==drag_w){
									il.onClick(pl,si[w],getExItemList());
								}else if(il.dragable()&&ex.il.dragable()){
									SingleItem.onDrag(ex.si[ex.drag_w],si[w],pl.batch_op);
								}
							}
						}
					}
				}
			}
			return true;
		}
		return false;
	}
	
	//绑定的物品列表界面（允许null）
	public void setExUIItemList(UI_ItemList _il2){
		il2=_il2;
	}
	
	//绑定的物品列表（允许null）
	public void setExItemList(ItemContainer _ex_il){
		ex_il=_ex_il;
	}
	public ItemContainer getExItemList(){
		if(il2!=null)return il2.il;
		return ex_il;
	}
	public void open(){
		if(il2!=null)il2.setExUIItemList(this);
		super.open();
	}
	public void close(){
		if(il2!=null)il2.setExUIItemList(null);
		super.close();
	}
}

