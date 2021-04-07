package game.entity;

import util.BmpRes;

import static util.MathUtil.*;

public class AbsorberMonster extends NormalAgent implements DroppedItem.Picker{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/AbsorberMonster");
	public BmpRes getBmp(){return bmp;}
	public double maxHp(){return 30;}
	public double hardness(){return game.entity.NormalAttacker.STONE;}

	@Override
	public double light(){
		return 0.5;
	}

	@Override
	public double maxXp(){return 30000;}
	
	Group group(){return Group.STONE;}
	public AbsorberMonster(double _x,double _y){
		super(_x,_y);
		xp=30;
	}
	public Entity getBall(){return new EnergyBall();}

	public AttackFilter getAttackFilter(){return energy_filter;}

	void onKill(){
		new game.item.EnergyStone().drop(x,y,rndi(2,3));
		super.onKill();
	}

	@Override
	public void pick(DroppedItem item){
		xp=Math.min(maxHp(),xp+item.item.getAmount()*5);
		item.remove();
	}

	@Override
	void touchAgent(Agent ent){
		if(ent.group()!=group()){
			double a=Math.max(0,Math.min(xp,ent.hp+1)*0.75);
			for(int i=5;i<a;i+=5){
				throwEntFromCenter(new EnergyBall(),rnd_gaussion(),rnd_gaussion(),rnd(0.1,0.2));
			}
			xp-=a;
			ent.onAttacked(a,this);			
		}
		super.touchAgent(ent);
	}
}
