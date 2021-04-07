package game.entity;

import util.BmpRes;
import game.item.*;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.World;

public class ClimberStick extends Agent{
private static final long serialVersionUID=1844677L;
	public game.item.ClimberStick item;
	public Human w=null;
	public ClimberStick(game.item.ClimberStick item){
		super(0,0);
		this.item=item;
		item.ent=this;
		removed=true;
	}
	
	@Override
	public double gA(){return 0.003;}
	@Override
	public double touchVal(){return 3;}
	@Override
	public Agent getSrc(){return w==null?this:w;}
	@Override
	public double maxHp(){return 2000;}
	@Override
	Group group(){return Group.STONE;}
	
	@Override
	public String getName(){
		if(w==null)return "攀爬杆";
		return w.getName();
	}

	public double width(){return 1;}
	public double height(){return 1/8.;}
	public double mass(){return 1;}
	public boolean chkRemove(long t){
		return false;
	}
	@Override
	public void update(){
		super.update();
		boolean exist=false;
		if(w!=null){
			Armor ar=w.armor.get();
			if(ar==item){
				exist=true;
				double xd=w.x-x,yd=w.y-y;
				if(abs(xd)>1.2||abs(yd-0.2)>1.2){
					exist=false;
					w.armor.popItem().drop(x,y);
				}else{
					impulse(xd,yd+0.3,0.2);
					w.impulse(xd,yd+0.3,-0.2);
					xdir=w.xdir;
				}
			}
		}else exist=true;
		
		if(!exist)removed=true;
	}
	@Override
	public void action(){}
	
	@Override
	public boolean chkBlock(){return true;}
	
	@Override
	public BmpRes getBmp(){return item.getBmp();}
}
