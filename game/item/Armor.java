package game.item;

import game.block.Block;
import game.entity.*;
import static util.MathUtil.*;
import static java.lang.Math.*;
import util.BmpRes;
import static game.ui.UI.drawProgressBar;

public abstract class Armor extends DefendTool{
	private static final long serialVersionUID=1844677L;
	public void onUpdate(Human w){}
	public void onAttacked(Human w,Attack a){}
	public void touchAgent(Human w,Agent a){}
	//返回true表示屏蔽默认事件
	public boolean onArmorClick(Human a,double tx,double ty){return false;}//穿戴时被用于点击
	public boolean onArmorLongPress(Human a,double tx,double ty){return false;}//穿戴时被用于长按
	
	public double mass(){return 0.5;}
	public double width(){return 0.29;}
	public double height(){return 0.95;}
	public double maxvr(){return 1;}
	public double frictionXr(){return 1;}
	
	public float getRotation(){return 0;}
	public Attack transform(Attack a){
		if(a instanceof NormalAttack){
			a.val*=0.2+0.8*((NormalAttack)a).getWeight(this);
		}
		return super.transform(a);
	}
	
	@Override
	public void onUse(Human a){
		a.getCarriedItem().insert(this);
		SingleItem.exchange(a.getCarriedItem(),a.armor);
	}
	@Override
	public BmpRes getUseBmp(){//获取使用按钮的贴图
		return armor_btn;
	}

	public boolean isClosed(){return false;}
	
	public double getJumpAcc(Human h,double v){return v;}
	
	@Override
	public double onImpact(Human h,double v){
		if(h.dir*h.xdep>0){
			damage+=rf2i(v*5);
			return max(0,v-5);
		}
		return v;
	}//减小水平撞击导致的伤害
	
	public void drawLeftInfo(graphics.Canvas cv){
		float damage=1-this.damage*1f/maxDamage();
		drawProgressBar(cv,0xff00ff00,0xff3f7f7f,damage,2.1f,0.1f,3.9f,0.3f);
	}

	@Override
	public void onBroken(double x,double y){
		Fragment.gen(x,y+0.95*0.45,0.29,0.95*0.55,2,3,6,getBmp());
		if(this instanceof ItemContainer)DroppedItem.dropItems((ItemContainer)this,x,y);
	}
	
	public void draw(graphics.Canvas cv,Human hu){
		hu.defaultDrawHuman(cv);
	}

}
