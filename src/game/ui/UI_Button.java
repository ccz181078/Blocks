package game.ui;

import android.graphics.*;
import util.BmpRes;
import graphics.Canvas;

public abstract class UI_Button extends UI{
	private static final long serialVersionUID=1844677L;
	//描述一个普通的1x1按钮
	public UI_Button(float _x,float _y){x=_x;y=_y;}
	protected abstract BmpRes getBmp();
	protected abstract void onPress();
	protected void onDraw(Canvas cv){
		getBmp().drawInRect(cv,0.1f,0.1f,0.9f,0.9f);
	}
	protected boolean onPress(float tx,float ty){
		if(tx>=0&&ty>=0&&tx<1&&ty<1){
			onPress();
			return true;
		}
		return false;
	}
}

