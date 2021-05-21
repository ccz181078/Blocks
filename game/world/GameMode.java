package game.world;

import static java.lang.Math.*;
import static util.MathUtil.*;
import static game.world.World.*;
import game.block.*;
import java.util.*;
import game.entity.*;

public abstract class GameMode implements java.io.Serializable{
	private static final long serialVersionUID=1844677L;
	private static double[] prob_of_energy={
		0.660,
		0.300,
		0.020,
		0.019,
		0.001,
	};
	public boolean enable_group_fall=true;
	public boolean forceOnline(){return false;}
	
	int getWorldWidth(){return 4096;}
	
	double[] getWeatherProb(){
		return prob_of_energy;
	}
	
	int resourceRate(){
		return 1;
	}
	
	void newWorld(World world){}
	void update(Chunk chunk){}
	void genEnt(Chunk chunk){
		//if(!chunk.gen_ent)return;
		chunk.gen_ent=false;
		if(rnd()>0.0002)return;
		if(chunk.agents.size()>16)return;
		defaultGenEnt(chunk);
	}
	void defaultGenEnt(Chunk chunk){
		int x=rndi(chunk.minX(),chunk.maxX()-1);
		int y=World.cur.getGroundY(x);
		//World.cur.showText(minX()+"~"+(maxX()-1)+" time:"+World.cur.time+" agents:"+agents.size());
		for(Player p:World.cur.getPlayers()){
			if(abs(p.x-x)<16&&abs(p.y-y)<8)return;
		}
		//World.showText("-2-");
		if(World.cur.get(x,y).rootBlock() instanceof LiquidType)return;
		//World.showText("-3-");
		Class c=World.cur.get(x,y-1).rootBlock().getClass();
		//World.showText("-4-"+c.getName());
		if(World.cur.weather==Weather._fire)new LavaMonster(x+0.5,y+0.5).cadd();
		else if(c==DirtBlock.class){
			if(World.cur.weather==Weather._blood)new BloodMonster(x+0.5,y+0.5).cadd();
			else if(rnd()<0.1&&World.cur.weather==Weather._energystone)new EnergyMonster(x+0.5,y+1).cadd();
			else if(rnd()<0.05&&World.cur.weather==Weather._dark)new Zombie(x+0.5,y+1).cadd();
			else new GreenMonster(x+0.5,y+0.5).cadd();
		}else if(c==SandBlock.class){
			if(rnd()<0.3)new CactusMonster(x+0.5,y+0.5).cadd();
		}else if(c==DarkSandBlock.class){
			if(rnd()<0.1&&World.cur.weather==Weather._dark)new DarkMonster(x+0.5,y+0.5).cadd();
			else if(rnd()<0.3)new DarkWorm(x+0.5,y+0.5).cadd();
			else if(rnd()<0.2)new BigDarkWorm(x+0.5,y+0.5).cadd();
		}else if(c==StoneBlock.class){
			if(y<20&&rnd()<0.1)new LavaBallMonster(x+0.5,y+0.5).cadd();
			else if(rnd()<0.3)new RotatorMonster(x+0.5,y+0.5).cadd();
			else if(rnd()<0.1)new AbsorberMonster(x+0.5,y+0.5).cadd();
			else if(rnd()<0.2)new QuartzMonster(x+0.5,y+0.5).cadd();
			else new StoneMonster(x+0.5,y+0.5).cadd();
		}
		
	}
	
	void initChunk(Chunk chunk,WorldGenerator gen,int dir){
		gen.init();
		boolean ps[]=new boolean[Chunk_Width];
		if(dir==1){
			for(int x=0;x<Chunk_Width;++x){
				chunk.setX(x,gen.nxt());
				ps[x]=gen.nxt_plant==0;
			}
		}else{
			for(int x=Chunk_Width-1;x>=0;--x){
				chunk.setX(x,gen.nxt());
				ps[x]=gen.nxt_plant==0;
			}
		}
		for(int x=3;x<Chunk_Width-3;++x)if(ps[x])World.cur.genPlant(chunk.minX()+x);
	}
	
	void initPlayer(Player player){}
	public void onZombieDead(Zombie w){
		w.dropItems();
		new game.item.ZombieCrystal().drop(w.x,w.y,2);
	}
	public void onPlayerDead(Player player){
		player.dropItems();
		
		Zombie z=new Zombie(player.x,player.y);
		z.dir=player.dir;
		z.hp*=0.1;
		z.add();
	}
	public void onPlayerRespawn(Player player){
		player.dead=false;
		ProtectionZone.setPlayer(player);
	}
	public void touchLevelEnd(LevelEnd e,Player pl){
		e.kill();
	}
}
