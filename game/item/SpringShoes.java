package game.item;

import util.BmpRes;
import game.world.World;
import game.entity.*;
import static util.MathUtil.*;
import static java.lang.Math.*;
import static game.ui.UI.drawProgressBar;

public class SpringShoes extends Shoes{
	private static final long serialVersionUID=1844677L;
	public static BmpRes bmp=new BmpRes("Item/SpringShoes");
	public BmpRes getBmp(){return bmp;}
	public int maxDamage(){return 1000;}
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	
	@Override
	public Attack transform(Attack a){
		return super.transform(a);
	}
	
	@Override
	public double onImpact(Human h,double v){
		damage+=rf2i(v);
		return max(0,(v-60)*0.2);
	}
	
	@Override
	public double height(){
		return (1-compressValue()*0.6)*super.height();
	}
	@Override
	public double getJumpAcc(Human h,double v){
		damage+=1;
		return v*1.6;
	}
	
	@Override
	public void drawLeftInfo(graphics.Canvas cv){
		super.drawLeftInfo(cv);
		drawProgressBar(cv,0x80808080,0,(float)compressValue(),4.1f,0.1f,4.5f,0.3f);
	}
	protected final double compressValue(){
		double c=up_time*0.05;
		c=c/(1+c);
		return c;
	}
	int up_time=0;
	protected double getJumpAcc(){
		return 0.8*compressValue()+0.4;
	}
	@Override
	public Shoes update(Human h){
		if(h.ydir==-1)++up_time;
		else if(up_time>0){
			if(h.ydep<0)h.ya+=getJumpAcc();
			else if(h.inblock>0)h.ya+=min(1,h.inblock*4)*getJumpAcc();
			++damage;
			up_time=0;
		}
		return super.update(h);
	}

};
