package game.world;

import static java.lang.Math.*;
import static util.MathUtil.*;
import static game.world.World.*;
import game.block.*;
import game.item.*;
import java.util.*;
import game.entity.*;
import java.io.*;

public class StatMode extends GameMode{
	private static final long serialVersionUID=1844677L;
	private static double[] prob_of_energy={
		0.60,
		0.30,
		0.03,
		0.05,
		0.02,
	};
	
	public static final class ItemInfo implements Serializable,Cloneable{
		private static final long serialVersionUID=1844677L;
		Item item;
		int cnt=0;
		double cost=1e40;
		ItemInfo(Item item){
			this.item=item;
		}
		void inc(int c){
			cnt+=c;
			cost=1./cnt;
		}
		void mul(double c){
			cost/=c;
		}
		boolean update(double c){
			if(cost>c){
				cost=c;
				return true;
			}
			return false;
		}
	};
	
	public static final class StatResult implements Serializable{
		private static final long serialVersionUID=1844677L;
		
		private HashMap<Class,ArrayList<ItemInfo>> mp=new HashMap<>();
		
		public ItemInfo get(Item item,boolean ins){
			Class c=item.getClass();
			if(!mp.containsKey(c))mp.put(c,new ArrayList<ItemInfo>());
			ArrayList<ItemInfo> ls=mp.get(c);
			for(ItemInfo ii:ls){
				if(ii.item.cmpType(item))return ii;
			}
			if(ins){
				ItemInfo ii=new ItemInfo(item);
				ls.add(ii);
				return ii;
			}
			return null;
		}
		public void insert(SingleItem item){
			if(item.isEmpty())return;
			int cnt=item.getAmount();
			Item it=item.popItem();
			if(it instanceof PlantType)cnt*=10;
			get(it,true).inc(cnt);
		}
		public double cost(SingleItem item){
			if(item.isEmpty())return 0;
			int cnt=item.getAmount();
			Item it=item.get();
			return cnt*get(it,true).cost;
		}
		public boolean update(Craft c){
			double s=getEnergyPrice()*c.cost.energy+getTimePrice()*c.cost.time;
			for(SingleItem si:c.in)s+=cost(si);
			boolean flag=false;
			for(SingleItem si:c.out)flag|=get(si.get(),true).update(s/si.getAmount());
			return flag;
		}
		public void update(){
			Craft cs[]=Craft.getAll(0xffffffff);
			get(new Grass(),true).mul(1000);
			get(new CactusBlock(),true).mul(0.3);
			get(new LeafBlock(0),true).mul(1000);
			get(new Algae(),true).mul(100);
			get(new AquaticGrass(),true).mul(100);
			get(WaterBlock.getInstance(),true).mul(1000);
			get(LavaBlock.getInstance(),true).mul(1000);
			
			double unit=get(new EnergyStone(),true).cost;
			for(Map.Entry<Class,ArrayList<ItemInfo>> entry:new HashMap<>(mp).entrySet()){
				for(ItemInfo ii:entry.getValue()){
					ii.cost/=unit;
				}
			}
			
			for(int T=1;;++T){
				System.out.println("Round "+T);
				boolean flag=false;
				for(Map.Entry<Class,ArrayList<ItemInfo>> entry:new HashMap<>(mp).entrySet()){
					for(ItemInfo ii:entry.getValue()){
						Item it=ii.item.heatingProduct(true);
						if(it!=null)flag|=get(it,true).update(ii.cost);
					}
				}
				for(Craft c:cs)flag|=update(c);
				if(!flag)break;
			}
		}
		public double getTimePrice(){
			return 0.001;
		}
		public double getEnergyPrice(){
			return 0.172/(30*2*4*1);
			//cactus_Price / ((cacuts_fuelVal * fuelVal->furnaceTime * furnaceSlotNum * (energy gain per frame))
		}
		public double getPrice0(Item i){
			if(i==null)return 1e40;
			ItemInfo ii=get(i,false);
			if(ii==null)return 1e40;
			return ii.cost;
		}
		public double getPrice(Item i){
			if(i==null)return 1e40;
			return i.getPrice(this);
		}
		public static String getPriceString(double cost){
			if(cost>1e10)return "inf";
			return String.format("%.3g",cost);
		}
		public void print(Item[] items){
			System.out.println("---------------------");
			for(Item i:items){
				System.out.println(i.getName()+":\t"+getPriceString(getPrice(i)));
			}
		}
		public void print(){
			print(Craft._normal_item);
			print(Craft._throwable_item);
			print(Craft._normal_tool);
			print(Craft._energy_tool);
			
			print(Craft._normal_block);
			print(Craft._functional_block);
			print(Craft._circuit);
		}
	};
	StatResult result=new StatResult();
	
	int getWorldWidth(){return 4096;}
	
	double[] getWeatherProb(){
		return prob_of_energy;
	}
	
	void newWorld(World world){
		while(world.max_chunk_id-world.min_chunk_id<getWorldWidth()/Chunk_Width){
			if(rnd()<0.5)world.addChunkL();
			else world.addChunkR();
		}
	}
	void save(String fn){
		try{
			OutputStream os=new FileOutputStream(debug.Log.ASSETS_DIR+fn);
			BufferedOutputStream bos=new BufferedOutputStream(os,32768);
			ObjectOutputStream oos=new ObjectOutputStream(bos);
			oos.writeObject(result);
			oos.close();
			bos.close();
			os.close();
		}catch(Exception e){debug.Log.i(e);}
	}
	
	public static StatResult restore(String fn){
		StatResult result=null;
		try{
			InputStream os=util.AssetLoader.load(fn);
			BufferedInputStream bos=new BufferedInputStream(os,32768);
			ObjectInputStream oos=new util.UncheckedObjectInputStream(bos);
			result=(StatResult)oos.readObject();
			oos.close();
			bos.close();
			os.close();
		}catch(Exception e){debug.Log.i(e);}
		return result;
	}
	
	int resourceRate(){
		return 8;
	}
	long time=-1;
	
	void update(Chunk chunk){
		if(World.cur.time!=time){
			time=World.cur.time;
			if(time%20==0)System.out.println("time:"+time);
			if(time==200){
				//result=restore("v1.dat");
				save("v1.dat");
				result.update();
				save("v2.dat");
				result.print();
				System.exit(0);
			}
		}

		World.cur.weather=Weather.randomWeather();
		
		for(Entity e:chunk.ents){
			if(e instanceof DroppedItem){
				result.insert(((DroppedItem)e).item);
			}
			e.kill();
		}
		for(Agent a:chunk.agents){
			if(a.hp>0)a.kill();
		}
		for(int i=0;i<10;++i){
			int x=rndi(chunk.minX(),chunk.maxX()-1);
			int y=rndi(0,World_Height-1);
			if(i<2){
				Item item;
				if(rnd()<0.5)item=new Bucket();
				else item=new Scissors();
				World.cur.get(x,y).onPress(x,y,item);
			}else World.cur.get(x,y).des(x,y,10000,new Bullet_HD());
		}
	}
	
	
	void genEnt(Chunk chunk){
		defaultGenEnt(chunk);
	}
}
