package game.world;

import game.item.*;
import game.block.*;
import game.entity.*;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class ZombieMode extends GameMode{
	private static double[] prob_of_energy={
		0.300,
		0.500,
		0.100,
		0.001,
		0.100,
	};
	
	@Override
	public boolean forceOnline(){return true;}
	
	@Override
	int getWorldWidth(){return 512;}

	@Override
	int resourceRate(){
		return 4;
	}
	
	@Override
	double[] getWeatherProb(){
		return prob_of_energy;
	}

	@Override
	void initChunk(Chunk chunk,WorldGenerator gen,int dir){
		super.initChunk(chunk,gen,dir);
		int x=rndi(chunk.minX()+4,chunk.maxX()-4);
		int y=min(World.cur.getGroundY(x)+rndi(7,20),World.cur.World_Height-2);
		new ZombieBase(x,y).add();
	}
	
	@Override
	void genEnt(Chunk chunk){
		if(!chunk.gen_ent)return;
		chunk.gen_ent=false;
		if(rnd()>0.0002)return;
		if(chunk.agents.size()>32)return;
		defaultGenEnt(chunk);
	}

	@Override
	void initPlayer(Player player){
		super.initPlayer(player);
		player.items.insert(new StoneBlock().setAmount(20));
		player.items.insert(new WoodenBlock().setAmount(20));
		player.items.insert(new CactusBlock().setAmount(5));
		player.items.insert(new TorchBlock().setAmount(40));
		player.items.insert(new BloodSword().setAmount(1));
		player.items.insert(new BloodEssence().setAmount(10));
		player.items.insert(new PlantEssence().setAmount(40));
		player.items.insert(new RPGLauncher().setAmount(1));
		player.items.insert(new game.item.RPG_HE().setAmount(2));
		player.items.insert(new game.item.RPG_HEAT().setAmount(2));
		player.items.insert(HeatEnergyCellGroup.full().setAmount(1));
		player.items.insert(HeatEnergyCellGroup.full().setAmount(1));
	}

	/*@Override
	public void onPlayerDead(Player player){
		super.onPlayerDead(player);
		for(int i=0;i<360;i+=10){
			HE_EnergyBall b=new HE_EnergyBall();
			player.throwEntFromCenter(b,player.x+cos(toRadians(i)),player.y+sin(toRadians(i)),0.1);
			b.src=null;
		}
	}*/

	@Override
	public void onPlayerRespawn(Player player){
		super.onPlayerRespawn(player);
		int x=rndi(-64,64);
		int y=World.cur.getGroundY(x)+1;
		new SetRelPos(player,null,x+0.5,y+1);
		player.items.insert(new StoneBlock().setAmount(max(4,20-player.death_cnt)));
		player.items.insert(new BloodSword().setAmount(1));
	}
}
