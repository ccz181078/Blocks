package game.block;

import static java.lang.Math.*;
import util.BmpRes;
import game.world.World;
import game.entity.*;

public class CactusBlock extends PlantType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/CactusBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 30;}
	public int foodVal(){return 12;}
	public int eatTime(){return 150;}
	public double hardness(){return game.entity.NormalAttacker.CACTUS;}
	public void using(UsingItem ent){
		if(ent.hp>120){
			Human hu=(Human)ent.ent;
			hu.loseHp(0.3,SourceTool.item(hu,this));
		}
		super.using(ent);
	}
	public void touchEnt(int x,int y,Entity ent){
		double k=intersection(x,y,ent);
		/*ent.f+=k*0.5;
		ent.inblock+=k*0.5;
		ent.anti_g+=k*4;*/
		super.touchEnt(x,y,ent);
		if(!(ent instanceof game.entity.CactusMonster))ent.onAttacked((0.1+hypot(ent.xv,ent.yv)*2)*k,SourceTool.block(x,y,this),this);
	}
	public boolean onUpdate(int x,int y){
		if(super.onUpdate(x,y))return true;
		if(World.cur.get(x,y-1).isCoverable()){
			World.cur.setAir(x,y);
			new FallingBlock(x,y,this).add();
			return true;
		}
		Block d=World.cur.get(x,y-1);
		dirt_v=max(0,dirt_v-0.01f);
		light_v=max(0,light_v-0.1f);
		repair(0.3,0.3);
		Class tp=d.rootBlock().getClass();
		if(tp==CactusBlock.class)spread((PlantType)d,0.2f);
		else if(tp==SandBlock.class&&light_v>2){
			light_v-=2;
			dirt_v=min(5f,dirt_v+1);
		}
		if(dirt_v>3&&World.cur.get(x,y+1).isCoverable()){
			CactusBlock w=new CactusBlock();
			spread(w,0.2f);
			World.cur.place(x,y+1,w);
		}
		return false;
	}
	@Override
	protected int crackType(){return 0;}
}
