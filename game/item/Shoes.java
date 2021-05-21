package game.item;

import game.entity.*;
import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;
import static game.ui.UI.drawProgressBar;

public abstract class Shoes extends DefendTool implements AttackFilter{
	private static final long serialVersionUID=1844677L;
	public double toolVal(){return 0.2;}
	public double width(){return 0.5;}
	public double height(){return 0.5;}
	public double maxvr(){return 1;}
	public double mass(){return 0.1;}
	
	public void onBroken(double x,double y){
		Fragment.gen(x,y,0.5,0.5,4,4,8,getBmp());
	}
	public Attack transform(Attack a){
		if(a instanceof NormalAttack){
			a.val*=0.7+0.3*((NormalAttack)a).getWeight(this);
		}
		return super.transform(a);
	}
	
	public int getCnt(){return 1;}
	
	@Override
	public void onUse(Human a){
		a.getCarriedItem().insert(this);
		SingleItem.exchange(a.getCarriedItem(),a.shoes);
	}
	@Override
	public BmpRes getUseBmp(){//获取使用按钮的贴图
		return armor_btn;
	}
	
	public Shoes update(Human h){
		if(isBroken()){
			onBroken(h.x,h.y-h.height()+0.5);
			return null;
		}
		return this;
	}
	
	public double friction(){return 1;}
	
	public void drawLeftInfo(graphics.Canvas cv){
		float damage=1-this.damage*1f/maxDamage();
		drawProgressBar(cv,0xff00ff00,0xff3f7f7f,damage,4.1f,0.1f,4.5f,0.3f);
	}

	@Override
	public double onImpact(Human h,double v){
		damage+=rf2i(v*5);
		return max(0,v-2);
	}//减小摔伤
	
	public double getJumpAcc(Human h,double v){
		damage+=1;
		return v*0.7;
	}
	
	public void touchAgent(Human w,Agent a){}
};

class GlueShoes extends Shoes{
	private static final long serialVersionUID=1844677L;
	public static BmpRes bmp=new BmpRes("Item/GlueShoes");
	public BmpRes getBmp(){return bmp;}
	public int maxDamage(){return 1000;}
	@Override
	public int getCnt(){return 2;}
	@Override
	public double friction(){return 8;}
	
	@Override
	public double onImpact(Human h,double v){
		damage+=rf2i(v);
		return max(0,v-5)*0.5;
	}//减小摔伤
	
	@Override
	public double getJumpAcc(Human h,double v){
		damage+=1;
		return v;
	}
}