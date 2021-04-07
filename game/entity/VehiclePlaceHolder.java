package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import java.io.*;
import util.BmpRes;
import game.item.*;
import game.world.*;
import graphics.Canvas;
import game.block.Block;
import game.block.StaticBlock;
import game.block.AirBlock;

public class VehiclePlaceHolder extends Human{
	private static final long serialVersionUID=1844677L;
	public double light(){return 0;}
	static BmpRes
	body=new BmpRes("Entity/Player/body"),
	hand=new BmpRes("Entity/Player/hand"),
	leg=new BmpRes("Entity/Player/leg");

	BmpRes bodyBmp(){return body;}
	BmpRes handBmp(){return hand;}
	BmpRes legBmp(){return leg;}
	Group group(){return Group.PLAYER;}
	public boolean hasBlood(){return false;}
	
	public boolean pickable(){return true;}
	public void pickedBy(Agent a){
		if(!removed){
			removed=true;
			armor.drop(a.x,a.y);
		}
	}

	public VehiclePlaceHolder(double _x,double _y,Armor ar){
		super(_x,_y);
		armor.insert(ar);
	}
	
	@Override
	protected int[] getItemListSize(){
		return new int[]{0,0};
	}
	
	public AttackFilter getAttackFilter(){
		return new AttackFilter(){public Attack transform(Attack a){
			if(a!=null)VehiclePlaceHolder.super.getAttackFilter().transform(a);
			return null;
		}};
	}
	
	@Override
	public void touchAgent(Agent a){
		if(a instanceof Human){
			Human hu=(Human)a;
			if(hu.armor.isEmpty()&&!isRemoved()&&!armor.isEmpty()){
				hu.armor.insert(armor.popItem());
				new SetRelPos(hu,null,x,y);
				kill();
				return;
			}
		}
		super.touchAgent(a);
	}
	
	@Override
	public void update(){
		super.update();
		hp=maxHp();
		if(armor.isEmpty())kill();
	}
	
	@Override
	public void draw(Canvas cv){
		Armor ar=armor.get();
		if(ar!=null)ar.draw(cv,this);
	}
	
	@Override
	public void defaultDrawHuman(Canvas cv){}
	
	@Override
	public boolean chkRemove(long t){
		return false;
	}
	
	@Override
	void onKill(){}
	
	@Override
	public double onImpact(double v){return 0;}
	
	@Override
	public void pick(DroppedItem item){}
}

