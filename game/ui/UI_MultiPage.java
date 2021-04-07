package game.ui;

import static util.MathUtil.*;
import graphics.Canvas;
import util.BmpRes;
import java.util.*;
import game.item.*;

public class UI_MultiPage extends UI{
	private static final long serialVersionUID=1844677L;
	//多页视图
	//在得到焦点时，恰好有一页得到焦点
	//在失去焦点时，所有页失去焦点
	//点击一页的图标，可以使这一页得到焦点
	//只有得到焦点的页能和用户交互、更新
	
	ArrayList<UI_Page>pages;
	int page;
	public UI_MultiPage(){
		x=-7;y=0;
		pages=new ArrayList<>();
		page=0;
	}
	
	//添加一页，传入一个物品作为图标，一个控件作为页的内容
	public void addPage(Item icon,UI ui){
		pages.add(new UI_Page(icon,ui));
	}
	protected void onDraw(Canvas cv){
		for(int i=0;i<pages.size();++i){
			(i==page?selected_item_frame_left:item_frame_left)
				.drawInRect(cv,-1,i,0,i+1);
			pages.get(i).icon.getBmp().drawInRect(cv,-0.75f,i+0.25f,-0.25f,i+0.75f);
		}
		pages.get(page).ui.draw(cv);
	}
	@Override
	protected boolean onTouch(float tx,float ty,int tp){
		int px=f2i(tx),py=f2i(ty);
		if(px==-1&&py>=0&&py<pages.size()){
			if(py!=page){
				pages.get(page).ui.close();
				page=py;
				pages.get(page).ui.open();
			}
			return true;
		}
		if(pages.get(page).ui.touch(tx,ty,tp))return true;
		return tx>=0&&tx<4&&ty>=0&&ty<8;
	}
	public void open(){
		pages.get(page).ui.open();
	}
	public void close(){
		pages.get(page).ui.close();
	}
	public void update(){
		pages.get(page).ui.update();
	}
}

class UI_Page{
	private static final long serialVersionUID=1844677L;
	Item icon;
	UI ui;
	UI_Page(Item _icon,UI _ui){
		icon=_icon;
		ui=_ui;
	}
}
