package game.ui;

import util.BmpRes;
import graphics.Canvas;

public abstract class UI_Button extends UI{
	private static final long serialVersionUID=1844677L;
	//描述一个普通的1x1按钮
	public UI_Button(float _x,float _y){x=_x;y=_y;}
	protected abstract BmpRes getBmp();
	protected abstract void onPress();
	protected void onRelease(){}
	protected String getText(){return null;}
	protected void onDraw(Canvas cv){
		getBmp().drawInRect(cv,0.1f,0.1f,0.9f,0.9f);
		String s=getText();
		if(s!=null){
			float sz=game.GlobalSetting.getGameSetting().text_size;
			cv.drawText(s,0.85f,0.85f,Math.min(sz,0.7f),1);
		}
	}
	@Override
	protected final boolean onTouch(float tx,float ty,int tp){
		if(tx>=0&&ty>=0&&tx<1&&ty<1){
			if(tp==0){
				UI.pl.last_pressed_ui=this;
				onPress();
			}
			if(tp==1){
				onRelease();
			}
			return true;
		}
		return false;
	}
}

