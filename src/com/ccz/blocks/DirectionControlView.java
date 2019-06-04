package com.ccz.blocks;

import android.content.*;
import android.view.*;
import android.app.*;
import android.graphics.*;
import android.util.*;
import game.ui.Action;
import static java.lang.Math.*;
import game.GlobalSetting;

public class DirectionControlView extends android.view.View{
	private static final long serialVersionUID=1844677L;
	Action action;
	float mx,my,tx,ty;
	boolean down;
	final float K,D=0.3f;
	DirectionControlView(Context con,Action a,float _K){
		super(con);
		action=a;
		setBackgroundColor(0);
		K=_K;
	}
	private void init(){
		down=false;
		action.l=action.r=action.u=action.d=false;
		invalidate();
	}
	public boolean onTouchEvent(MotionEvent event){
		if(GlobalSetting.playing_screen_record)return false;
		float x=event.getX()/K;
		float y=event.getY()/K;
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:{
				if(x>3)return false;
				down=true;
				mx=x;
				my=y;
			}
			case MotionEvent.ACTION_MOVE:{
				if(!down)return false;
				x-=mx;
				y-=my;
				tx=x;
				ty=y;
				action.l=x<-D;
				action.r=x>D;
				action.u=y<-D;
				action.d=y>D;
				invalidate();
				return true;
			}
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:{
				init();
				return true;
			}
		}
		return false;
	}
	
	protected void onDraw(Canvas cv){
		if(!down)return;
		Paint pa=new Paint();
		pa.setColor(0xffcccccc);
		cv.scale(K,K);
		cv.translate(mx,my);
		tx=max(-D*2,min(tx,D*2));
		ty=max(-D*2,min(ty,D*2));
		cv.drawRect(tx-D,ty-D,tx+D,ty+D,pa);
		pa.setStyle(Paint.Style.STROKE);
		pa.setStrokeWidth(0.1f);
		pa.setColor(0xffaaaaaa);
		cv.drawRect(-D*2,-D*2,D*2,D*2,pa);
	}
}
