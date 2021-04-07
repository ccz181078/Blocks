package game.entity;

import game.item.FieldGen;
import graphics.Canvas;

public class FieldBuff extends Buff{
	FieldGen field;
	public double radius,r0;
	@Override
	public String getName(){
		String s=field.getName();
		return s;
	}
	public FieldBuff(Agent tg,FieldGen fg,double r){
		super(tg);
		source=SourceTool.make(tg,"设置的");
		field=fg;
		r0=r;
		radius=0;
		hp=160;
		x=tg.x;
		y=tg.y;
		xv=tg.xv;
		yv=tg.yv;
	}
	public double light(){return field.light(this);}
	public void update(){
		hp-=0.5;
		field.update(this);
		xv+=(target.x-x+(target.xv-xv))*0.15;
		yv+=(target.y-y+(target.yv-yv))*0.15;
		double c=hp/160;
		radius=r0*c*c*c*(1-c)*9.5;
		super.update();
	}
	public void draw(Canvas cv){
		field.draw(this,cv);
	}
	public void move(){
		x+=xv;
		y+=yv;
	}
}