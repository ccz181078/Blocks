package game.block;

import static java.lang.Math.*;
import util.BmpRes;
import game.world.World;
import game.entity.Entity;

public class CactusBlock extends PlantType{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Block/CactusBlock");
	public BmpRes getBmp(){return bmp;}
	int maxDamage(){return 30;}
	public void touchEnt(int x,int y,Entity ent){
		double k=intersection(x,y,ent);
		ent.f+=k*0.5;
		ent.inblock+=k*0.5;
		ent.anti_g+=k*4;
		if(!(ent instanceof game.entity.CactusMonster))ent.onAttacked(0.1*k,null);
	}
	public boolean onUpdate(int x,int y){
		if(super.onUpdate(x,y))return true;
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
}
