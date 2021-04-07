package game.world;

import static java.lang.Math.*;
import static util.MathUtil.*;
import static game.world.World.*;
import game.block.*;
import game.item.*;
import java.util.*;
import game.entity.*;
import java.io.*;

/*public class TestMode extends GameMode{
	private static final long serialVersionUID=1844677L;
	private static double[] prob_of_energy={
		0.60,
		0.30,
		0.03,
		0.05,
		0.02,
	};
	abstract class TestCase{
		abstract boolean run();
	}
	
	class LaunchTest extends TestCase{
		int state=0;
		Human z1,z2;
		Item item;
		Armor ar;
		String name;
		int dhp;
		int ddmg;
		LaunchTest(Item item,Armor ar,String name){
			this.item=item;
			this.ar=ar;
			this.name=name;
		}
		boolean run(){
			++state;
			if(state==1){
				for(int i=-5;i<=5;++i){
					for(int j=30;j<=35;++j){
						if(i==-5||i==5||j==30||j==35){
							World.cur.set(i,j,new BedRockBlock());
						}else{
							World.cur.setAir(i,j);
						}
					}
				}
				z1=new Villager(rnd(-2,-1),32);
				z2=new Villager(rnd(1,2),32);
				z1.getCarriedItem().insert(item);
				z2.armor.insert(ar);
				z1.add();
				z2.add();
			}else if(state==5){
				z1.clickAt(z2.x,z2.y);
			}else if(state==20){
				dhp=(int)(z2.maxHp()-z2.hp);
				ddmg=ar.damage;
				System.out.println(dhp+"\t"+ddmg+"\t"+name);
				z1.kill();
				z2.kill();
				return true;
			}
			return false;
		}
	}
	
	int getWorldWidth(){return 64;}
	
	double[] getWeatherProb(){
		return prob_of_energy;
	}
	
	void newWorld(World world){
		for(Item it:Craft._bullet)
		for(Item ar:Craft._normal_armor){
			Armor a=util.SerializeUtil.deepCopy((Armor)ar);
			Item i=util.SerializeUtil.deepCopy(it);
			EnergyGun e=new EnergyGun();
			e.insert(EnergyCell.full().setAmount(1));
			e.insert(i.setAmount(1));
			cases.offer(new LaunchTest(e,a,e.getName()+"+"+i.getName()+"->"+a.getName()));
		}
	}
	
	transient LinkedList<TestCase> cases=new LinkedList<>();
	
	int resourceRate(){
		return 1;
	}
	long time=-1;
	TestCase cur=null;
	
	void update(){
		if(cur!=null){
			if(cur.run()){
				cur=null;
			}
		}else if(!cases.isEmpty()){
			cur=cases.poll();
		}
	}
	
	void update(Chunk chunk){
		if(World.cur.time!=time){
			time=World.cur.time;
			update();
		}
	}
	
	
	void genEnt(Chunk chunk){
		
	}
}
*/