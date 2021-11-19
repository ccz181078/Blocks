package game.world;

import game.item.*;
import game.block.*;
import game.entity.*;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.StatMode.StatResult;

public class InfiniteZombieMode extends GameMode{
	private static double[] prob_of_energy={
		0.500,
		0.300,
		0.100,
		0.050,
		0.050,
	};
	
	private StatResult result=null;
	
	public StatResult getStat(){
		if(result==null){
			result=StatMode.restore("v2.dat");
		}
		return result;
	}
	
	@Override
	public boolean forceOnline(){return true;}
	
	@Override
	int getWorldWidth(){return 512;}

	@Override
	int resourceRate(){
		return 8;
	}
	
	@Override
	double[] getWeatherProb(){
		return prob_of_energy;
	}
	
	int level=0,cnt=0;
	
	private void give(Zombie z,Item item,int cnt){
		item=util.SerializeUtil.deepCopy(item);
		cnt=min(cnt,item.maxAmount());
		z.getItems().insert(item.setAmount(cnt));
	}
	private void give(Zombie z,Item item){
		give(z,item,1);
	}
	
	private void give(Zombie z,Item item[],int cnt){
		double s=0;
		Item w=null;
		for(Item i:item){
			double c0=getStat().cost(i.setAmount(1));
			if(c0>1e5)continue;
			double c=1/pow(c0,1.0/(1+level/64.));
			s+=c;
			if(rnd()*s<c)w=i;
		}
		if(w!=null)give(z,w,cnt);
	}
	
	private void give(Zombie z,Item item[]){
		give(z,item,1);
	}
	
	private void genZombie(Player p){
		Zombie z;
		if(rnd()*320<level){
			if(rnd()*2560<level&&rnd()<0.5)z=new FloatingZombie(0,0);
			else z=new ZombieRobot(0,0);
		}else z=new Zombie(0,0);
		z.camp=1;
		++cnt;
		z.name="lv"+level+"_id"+cnt;
		z.setTarget(p);
		if(rnd()*5<level)give(z,new EnergyStone(),1);
		
		if(rnd()*20<level)give(z,new DirtBlock(),1+(int)rnd_exp(2));
		
		if(rnd()*40<level)give(z,new StoneBlock(),1+(int)rnd_exp(3));
		if(rnd()*40<level)give(z,Craft._physics_tool);
		if(rnd()*40<level)give(z,Craft._balls);
		if(rnd()*40<level)give(z,Craft._essence,1+(int)rnd_exp(2));
		
		if(rnd()*80<level)give(z,Craft._physics_tool);
		if(rnd()*80<level)give(z,Craft._bottle,1+(int)rnd_exp(2));
		if(rnd()*80<level)give(z,Craft._normal_shoes);
		if(rnd()*80<level)give(z,Craft._normal_shield);
		if(rnd()*80<level)give(z,Craft._balls,1+(int)rnd_exp(1));
		if(rnd()*80<level)give(z,new ZombieCrystal(),1+(int)rnd_exp(level/64.));
		if(rnd()*80<level)give(z,Craft._normal_armor);
		
		if(rnd()*160<level)give(z,Craft._essence,1+(int)rnd_exp(2));
		if(rnd()*160<level)give(z,Craft._balls);
		
		if(rnd()*160<level){
			give(z,Craft._bow);
			give(z,Craft._arrow,1+(int)rnd_exp(20));
		}
		if(rnd()*160<level)give(z,Craft._energy_sword);
		if(rnd()*160<level&&rnd()<0.3){
			give(z,Craft._energy_launcher);
			give(z,Craft._energy_cell);
		}
		if(rnd()*160<level&&rnd()<0.1){
			give(z,Craft._others_energy_tool);
			give(z,Craft._energy_cell);
		}
		
		if(rnd()*320<level){
			give(z,Craft._launcher);
			give(z,Craft._energy_cell);
			give(z,Craft._bullet,1+(int)rnd_exp(20));
		}
		if(rnd()*640<level){
			give(z,new RPGLauncher());
			give(z,Craft._energy_cell);
			if(rnd()<0.5)give(z,Craft._warhead);
			give(z,Craft._rpg_normal,1+(int)rnd_exp(1));
			if(rnd()<0.5)give(z,Craft._rpg_normal,1+(int)rnd_exp(1));
		}
		
		if(rnd()*1280<level){
			give(z,Craft._energy_armor);
			give(z,Craft._energy_cell);
		}
		
		if(rnd()*1280<level)give(z,Craft._field,1+(int)rnd_exp(1));
		if(rnd()*2560<level){
			give(z,Craft._vehicle);
			give(z,BlueCrystalEnergyCell.full());
			give(z,Craft._rpg_normal,1+(int)rnd_exp(5));
			give(z,Craft._rpg_normal,1+(int)rnd_exp(5));
			give(z,Craft._rpg_normal,1+(int)rnd_exp(5));
			give(z,Craft._rpg_normal,1+(int)rnd_exp(5));
			give(z,Craft._bullet,1+(int)rnd_exp(30));
			give(z,Craft._bullet,1+(int)rnd_exp(30));
			give(z,Craft._bullet,1+(int)rnd_exp(30));
			give(z,Craft._bullet,1+(int)rnd_exp(30));
		}
		double xy[]=randomPosFarFromPlayer();
		z.x=xy[0];
		z.y=xy[1];
		
		z.add();
	}
	
	void give(Player player){
		player.items.insert(new WoodenBlock().setAmount(20));
		player.items.insert(new Iron().setAmount(20));
		player.items.insert(new IronWorkBenchBlock().setAmount(1));
		player.items.insert(new BloodSword().setAmount(1));
		player.items.insert(new IronSword().setAmount(1));
		player.items.insert(new BloodEssence().setAmount(10));
		player.items.insert(new PlantEssence().setAmount(40));
		player.items.insert(new RPGLauncher().setAmount(1));
		player.items.insert(new Mine().setAmount(16));
		player.items.insert(new game.item.Warhead_HE().setAmount(16));
		player.items.insert(new game.item.RPG_HE().setAmount(16));
		player.items.insert(new game.item.RPG_HEAT().setAmount(16));
		player.items.insert(HeatEnergyCellGroup.full().setAmount(1));
		player.items.insert(HeatEnergyCellGroup.full().setAmount(1));
	}

	@Override
	void initPlayer(Player player){
		super.initPlayer(player);
		give(player);
	}
	
	
	public void onZombieDead(Zombie w){
		if(rnd()<0.4)level+=1;
		
		BoxShield box=new DeadHumanBoxShield();
		for(SingleItem si:w.getItems().toArray())box.insert(si);
		box.ent.initPos(w.x,w.y,w.xv,w.yv,SourceTool.dropOnDead(w)).add();

		//level+=4;
		super.onZombieDead(w);
	}
	
	private void genZombie(){
		for(Player p:World.cur.getPlayers()){
			if(p.locked)continue;
			genZombie(p);
		}
	}
	
	long time=-1;
	void update(Chunk chunk){
		if(World.cur.time<=time)return;
		time=World.cur.time;
		if(rnd()<1./(500.))genZombie();
		//if(rnd()<1./((1.+sqrt(level)/4.)*300.)&&time%30*120<30*60)genZombie();
	}
	/*public void onPlayerDead(Player player){
		player.dropItems();
		player.respawn_time=150;
		player.locked=true;
	}*/
	public void onPlayerRespawn(Player player){
		give(player);
		randomRespawn(player);
	}
}
