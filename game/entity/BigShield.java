package game.entity;

import util.BmpRes;
import game.item.Airship;
import game.item.AirshipFlank;
import game.item.Armor;
import game.item.FlankArmor;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.World;
import game.block.Block;

public class BigShield extends Agent implements DroppedItem.Picker{
	public game.item.BigShield shield;
	public BigShield(game.item.BigShield s){
		super(0,0);
		this.shield=s;
		s.ent=this;
		hp=s.maxHp();
		removed=true;
	}
	public boolean hasBlood(){return false;}
	public boolean pickable(){return true;}
	public void pickedBy(Agent a){
		if(!removed){
			removed=true;
			shield.drop(a.x,a.y);
		}
	}
	
	public boolean onClick(double x,double y,Agent w){
		return shield.onClickEnt(x,y,w);
	}
	
	@Override
	public double maxHp(){return shield==null?1:shield.maxHp();}
	public double hardness(){return shield.hardness();}
	@Override
	Group group(){return Group.STONE;}
	@Override
	public void action(){}
	@Override
	public double touchVal(){return shield.touchVal();}
	
	@Override
	public String getName(){
		return shield.getName();
	}
	
	public double width(){
		return shield.width();
	}
	public double height(){
		return shield.height();
	}
	public double mass(){
		return shield.mass();
	}
	@Override
	public AttackFilter getAttackFilter(){return shield;}
	
	void touchEnt(Entity ent){//事件：接触实体
		super.touchEnt(ent);
		exchangeVel(ent,0.3);
		shield.touchEnt(ent);
	}
	
	@Override
	public void update(){
		super.update();
		shield.update();
	}
	@Override
	public boolean drawRev(){return dir==-1;}
	@Override
	public boolean chkBlock(){return true;}
	public boolean chkEnt(){return true;}
	@Override
	public boolean chkRigidBody(){return true;}
	@Override
	public BmpRes getBmp(){return shield.getBmp();}
	@Override
	void onKill(){
		BmpRes bmp=getBmp();
		if(bmp!=null){
			new Fragment.Config().setEnt(this).setGrid(2,4,4).setTime(50).apply();
		}
		shield.onKill();
		super.onKill();
	}
	
	@Override
	public double onImpact(double v){return v*1e-3;}
	
	@Override
	public void pick(DroppedItem item){
		shield.pick(item);
	}

}
