package game.entity;

import util.BmpRes;
import static util.MathUtil.*;
import game.block.Block;

public class LavaBallMonster extends SimpleAgent{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/LavaBallMonster");
	public BmpRes getBmp(){return bmp;}
	public double width(){return size;}
	public double height(){return size;}
	public double light(){return 0.5;}

	@Override
	public double mass(){
		return size*2;
	}
	Group group(){return Group.FIRE;}
	
	public AttackFilter getAttackFilter(){return fire_filter;}
	
	public LavaBallMonster(double _x,double _y){this(_x,_y,0.6f);}
	private LavaBallMonster(double _x,double _y,float _size){
		super(_x,_y);
		size=_size;
		hp=maxHp();
	}
	float size;

	@Override
	public double maxHp(){
		return size*50;
	}
	
	@Override
	void touchBlock(int px,int py,Block b){
		if(!removed&&(b.rootBlock() instanceof game.block.WaterBlock)){
			Fragment.gen(x,y,width(),height(),4,4,6,new game.item.StoneBall().getBmp());
			remove();
		}else super.touchBlock(px,py,b);
	}

	void touchAgent(Agent ent){
		if(ent.group()!=group()){
			ent.onAttackedByFire(size,this);
			target=ent;
		}
		super.touchAgent(ent);
	}

	void onKill(){
		if(size>0.25f){
			size*=0.7f;
			new LavaBallMonster(x+rnd_gaussion()*0.001,y,size).add();
			new LavaBallMonster(x,y+rnd_gaussion()*0.001,size).add();
		}else{
			if(rnd()<0.3)new game.item.FireBall().drop(x,y,1);			
		}
		super.onKill();
	}

}
