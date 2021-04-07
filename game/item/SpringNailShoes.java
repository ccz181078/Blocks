package game.item;

import util.BmpRes;
import game.world.World;
import game.entity.*;
import static util.MathUtil.*;
import static java.lang.Math.*;

public class SpringNailShoes extends SpringShoes{
	private static final long serialVersionUID=1844677L;
	public static BmpRes bmp=new BmpRes("Item/SpringNailShoes");
	public BmpRes getBmp(){return bmp;}
	public int maxDamage(){return super.maxDamage()/2;}
	
	@Override
	public void touchAgent(Human w,Agent a){
		if(a.bottom>w.bottom)return;
		double difx=w.xv-a.xv,dify=w.yv-a.yv;
		a.onAttacked((difx*difx+dify*dify)*12+0.25,SourceTool.shoes(w,this),this);
	}
	@Override
	protected double getJumpAcc(){
		return (super.getJumpAcc()-0.4)*0.7+0.4;
	}

};
