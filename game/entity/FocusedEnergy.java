package game.entity;

import graphics.Canvas;
import static java.lang.Math.*;

public final class FocusedEnergy extends Ball{
	public double radius(){return min(1,sqrt(max(0,hp))*0.07);}
	public FocusedEnergy(){
		hp=100;
	}
	public double hardness(){return game.entity.NormalAttacker.HD;}
	public boolean chkEnt(){return true;}
	public double light(){return radius()*2;}
	@Override
	public double gA(){return 0;}
	@Override
	public double mass(){return 1;}
	public void update(){
		hp-=1;
		super.update();
	}
	@Override
	public void draw(Canvas cv){
		float r=(float)radius();
		cv.drawRect(-r,-r,r,r,0x8000a0ff);
	}
	@Override
	void touchEnt(Entity e){
		if(hp<=0||e.hp<=0||e.getClass()==FocusedEnergy.class)return;
		e.onAttackedByEnergy(hp*0.2,this);
		e.onAttacked(hp*0.2,this);
		hp*=0.9;
	}
	@Override
	void touchBlock(int px,int py,game.block.Block b){
		if(b.rootBlock().transparency()<0.7)return;
		if(!b.isCoverable()){
			hp-=0.5;
			b.des(px,py,3,this);
		}
	}
	public double touchVal(){return 0;}
}
