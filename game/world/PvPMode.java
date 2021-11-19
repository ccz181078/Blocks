package game.world;

import static java.lang.Math.*;
import static util.MathUtil.*;
import static game.world.World.*;
import game.block.*;
import java.util.*;
import game.entity.*;
import game.item.*;
import game.world.StatMode.StatResult;

public class PvPMode extends GameMode{
	private static final long serialVersionUID=1844677L;
	private static double[] prob_of_energy={
		0.30,
		0.20,
		0.20,
		0.20,
		0.10,
	};
	
	@Override
	public boolean forceOnline(){return true;}
	
	private transient StatResult result=null;
	
	public StatResult getStat(){
		if(result==null){
			result=StatMode.restore("v2.dat");
		}
		return result;
	}
	
	int getWorldWidth(){return 256;}
	

	double[] getWeatherProb(){
		return prob_of_energy;
	}
	
	int resourceRate(){
		return 8;
	}
	
	void newWorld(World world){}
	long time=0;
	void update(Chunk chunk){
		if(World.cur.time>time){
			time=World.cur.time;
			int online_cnt=0;
			for(Player pl:World.cur.getPlayers()){
				if(pl.online)++online_cnt;
			}
			if(online_cnt>0)
			for(Player pl:World.cur.getPlayers()){
				if(pl.online)
				if(rnd()<0.005/online_cnt){
					double rx=World.cur.getRelX(pl.x);
					if(rx>0&&rx<1){
						new GoldParticle().drop(pl.x+rnd_gaussion(),rnd(pl.y-pl.height(),World.cur.getMaxY()+1));
					}
				}
			}
		}
	}
	
	@Override
	void initPlayer(Player player){
		super.initPlayer(player);
		player.money=0;
	}
	public void onPlayerDead(Player w){
		w.dropArmor();
		
		double x=Math.max(0,w.getPrice(getStat()));
		x=((x/1e3)/(1+x/1e3))*(0.3*x);
		Item i1=new GoldParticle();
		Item i2=new Gold();
		Item i3=new GoldBlock();
		double p1=i1.getPrice(getStat(),false);
		double p2=i2.getPrice(getStat(),false);
		double p3=i3.getPrice(getStat(),false);
		int n3=Math.max(0,Math.min(99,(int)(x/p3)));
		x-=p3*n3;
		int n2=Math.max(0,Math.min(99,(int)(x/p2)));
		x-=p2*n2;
		int n1=Math.max(0,Math.min(99,(int)(x/p1)));
		x-=p1*n1;
		w.money-=p1*n1+p2*n2+p3*n3;
		if(n1>0)i1.drop(w.x,w.y,n1);
		if(n2>0)i2.drop(w.x,w.y,n2);
		if(n3>0)i3.drop(w.x,w.y,n3);
		if(w.money<0){
			w.forceSell(getStat());
		}
	}
	public void onZombieDead(Zombie w){}
	public void onPlayerRespawn(Player player){
		super.onPlayerRespawn(player);
		randomRespawn(player);
	}
}
