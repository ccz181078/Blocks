package game.ui;

import graphics.Canvas;
import java.util.ArrayList;

public class UI_Group extends UI{
	private static final long serialVersionUID=1844677L;
	//容器型控件，描述一组控件
	//这组控件同时得到或失去焦点，按加入顺序从前到后绘制，从后到前响应触摸事件
	private ArrayList<UI> uis=new ArrayList<>();
	
	public UI_Group(float _x,float _y){
		x=_x;y=_y;
	}
	
	//加入一个子控件
	public void addChild(UI ui){
		uis.add(ui);
	}
	protected void onDraw(Canvas cv){
		for(UI ui:uis)ui.draw(cv);
	}
	@Override
	protected boolean onTouch(float tx,float ty,int tp){
		for(int i=uis.size()-1;i>=0;--i)if(uis.get(i).touch(tx,ty,tp))return true;
		return false;
	}
	public void update(){
		for(UI ui:uis)ui.update();
	}
	public void close(){
		for(UI ui:uis)ui.close();
	}
	public void open(){
		for(UI ui:uis)ui.open();
	}
}
