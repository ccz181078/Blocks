package game.entity;
import util.BmpRes;

import static util.MathUtil.*;

public class Villager extends Human{
	private static final long serialVersionUID=1844677L;
	static BmpRes
	body=new BmpRes("Entity/Villager/body"),
	hand=new BmpRes("Entity/Villager/hand"),
	leg=new BmpRes("Entity/Villager/leg");

	BmpRes bodyBmp(){return body;}
	BmpRes handBmp(){return hand;}
	BmpRes legBmp(){return leg;}
	
	Group group(){return Group.PLAYER;}
	
	public Villager(double _x,double _y){
		super(_x,_y);
	}
	
	@Override
	void onKill(){
		dropItems();
		super.onKill();
	}

	@Override
	public void ai(){}
	
	public void onAttacked(game.entity.Attack a){
		super.onAttacked(a);
		if(a.src!=null)
		if(a.src.getSrc() instanceof Zombie){
			Zombie z0=(Zombie)a.src.getSrc();
			Zombie z=new Zombie(x,y);
			z.camp=z0.camp;
			z.hp=hp;
			z.dir=dir;
			z.add();
			hp=0;
		}
	}
}
