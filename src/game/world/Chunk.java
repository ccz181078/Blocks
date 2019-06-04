package game.world;

import static java.lang.Math.*;
import static util.MathUtil.*;
import static game.world.World.*;
import game.block.*;
import java.util.*;
import java.io.*;
import game.entity.*;

class Chunk implements Serializable{
	private static final long serialVersionUID=1844677L;
	//描述世界中的一个块，除World以外一般不应该使用这个类
	
	Block blocks[]=new Block[World_Height<<log_Chunk_Width];
	ArrayList<Entity> ents=new ArrayList<>();
	ArrayList<Agent> agents=new ArrayList<>();
	long last_update_time=0;
	int id;
	CompressedBlocks cbs=null;
	transient boolean gen_ent;
	private void setX(int x,Block[] block){//unchecked
		for(int y=0;y<World_Height;++y)blocks[y<<log_Chunk_Width|x]=block[y];
	}
	void init(int _id,WorldGenerator gen,int dir){
		id=_id;
		gen.init();
		boolean ps[]=new boolean[Chunk_Width];
		if(dir==1){
			for(int x=0;x<Chunk_Width;++x){
				setX(x,gen.nxt());
				ps[x]=gen.nxt_plant==0;
			}
		}else{
			for(int x=Chunk_Width-1;x>=0;--x){
				setX(x,gen.nxt());
				ps[x]=gen.nxt_plant==0;
			}
		}
		for(int x=3;x<Chunk_Width-3;++x)if(ps[x])World._.genPlant(minX()+x);
	}
	private void readObject(ObjectInputStream in)throws IOException,ClassNotFoundException{ 
		in.defaultReadObject();
		if(blocks==null){
			blocks=cbs.toBlockArray();
			cbs=null;
		}
	}
	private void writeObject(ObjectOutputStream out)throws IOException{
		Block[]b0=blocks;
		blocks=null;
		cbs=new CompressedBlocks(b0);
		out.defaultWriteObject();
		blocks=b0;
		cbs=null;
	}
	void moveEnts(){
		for(Entity e:ents)if(!e.removed)e.move();
		for(Agent a:agents)if(!a.removed)a.move();
	}
	void updateEnts0(){
		for(Entity e:ents)if(!e.removed)e.update0();
		for(Agent a:agents)if(!a.removed)a.update0();
		genEnt();
	}
	void genEnt(){
		if(!gen_ent)return;
		gen_ent=false;
		if(rnd()>0.0002)return;
		if(agents.size()>16)return;
		int x=rndi(minX(),maxX()-1);
		int y=World._.getGroundY(x);
		//World._.showText(minX()+"~"+(maxX()-1)+" time:"+World._.time+" agents:"+agents.size());
		for(Player p:World._.getPlayers()){
			if(abs(p.x-x)<16&&abs(p.y-y)<8)return;
		}
		//World.showText("-2-");
		if(World._.get(x,y).rootBlock() instanceof LiquidType)return;
		//World.showText("-3-");
		Class c=World._.get(x,y-1).rootBlock().getClass();
		//World.showText("-4-"+c.getName());
		if(World._.weather==Weather._fire)new LavaMonster(x+0.5,y+0.5).cadd();
		else if(c==DirtBlock.class){
			if(rnd()<0.02&&World._.weather==Weather._energystone)new EnergyMonster(x+0.5,y+1).cadd();
			else if(rnd()<0.05&&World._.weather==Weather._dark)new Zombie(x+0.5,y+1).cadd();
			else new GreenMonster(x+0.5,y+0.5).cadd();
		}else if(c==SandBlock.class){
			new CactusMonster(x+0.5,y+0.5).cadd();
		}else if(c==DarkSandBlock.class){
			if(rnd()<0.1&&World._.weather==Weather._dark)new DarkMonster(x+0.5,y+0.5).cadd();
		}
	}
	void updateEnts(){
		for(Entity e:ents)if(!e.removed)e.update();
		for(Agent a:agents)if(!a.removed)a.update();
	}
	void updateAgents(){
		for(Agent a:agents)if(!a.removed)a.action();
	}
	void randomUpdateBlocks(){
		double K=120;
		int T=rf2i(World_Height*Chunk_Width/K);
		for(int t=0;t<T;++t){
			int x=rndi(minX(),maxX()-1);
			int y=rndi(0,World_Height-1);
			World._.get(x,y).onUpdate(x,y);
		}
		if(World._.weather==Weather._dark&&rnd()<0.8)return;
		T=rf2i(Chunk_Width/K/3);
		for(int t=0;t<T;++t){
			int x=rndi(minX(),maxX()-1);
			double v=1;
			for(int y=World_Height-1;y>=0&&v>0;--y){
				Block w=World._.get(x,y);
				if(w instanceof PlantType){((PlantType)w).onLight(x,y,v);}
				v-=w.transparency();
			}
		}
	}
	void getEnts(NearbyInfo ni){
		double lx=ni.mx-ni.xd-1;
		double rx=ni.mx+ni.xd+1;
		int L=0,R=ents.size(),R0=R;
		while(L<R){
			int M=(L+R)>>1;
			if(ents.get(M).x<lx)L=M+1;
			else R=M;
		}
		for(int i=L;i<R0;++i){
			Entity e=ents.get(i);
			if(e.x>rx)break;
			if(ni.hitTest(e))ni.ents.add(e);
		}
	}
	void getAgents(NearbyInfo ni){
		double lx=ni.mx-ni.xd-1;
		double rx=ni.mx+ni.xd+1;
		int L=0,R=agents.size(),R0=R;
		//Log.i("getAgents",minX()+"~"+maxX()+"  sz="+R);
		while(L<R){
			int M=(L+R)>>1;
			if(agents.get(M).x<lx)L=M+1;
			else R=M;
		}
		for(int i=L;i<R0;++i){
			Agent e=agents.get(i);
			//Log.i(e.getClass().getName(),e.x+","+e.y);
			if(e.x>rx)break;
			if(ni.hitTest(e))ni.agents.add(e);
		}
	}
	void sortEnts(){
		Comparator<Entity> cmp=new Comparator<Entity>(){
			public int compare(Entity p1,Entity p2){
				if(p1.x==p2.x)return 0;
				return p1.x<p2.x?-1:1;
			}
		};
		Collections.sort(ents,cmp);
		Collections.sort(agents,cmp);
	}
	int minX(){return id<<log_Chunk_Width;}
	int maxX(){return (id+1)<<log_Chunk_Width;}
	void removeDeadEnts(){
		for(int i=0;i<ents.size();++i){
			Entity e=ents.get(i);

			if(e.isDead())e.onDestroy();
			else if(e.x<minX()||e.x>=maxX())e.add();
			else continue;

			ents.set(i,ents.get(ents.size()-1));
			ents.remove(ents.size()-1);
			--i;
		}
		for(int i=0;i<agents.size();++i){
			Agent a=agents.get(i);

			if(a.isDead()){
				a.onDestroy();
				//debug.Log.i("remove:"+World._.RX(a.x)+","+World._.RY(a.y)+":"+a.getClass().getSimpleName()+":"+World._.time+"/"+a.last_seen_time+"/"+a.spawn_time);
			}else if(a.x<minX()||a.x>=maxX())a.add();
			else continue;

			agents.set(i,agents.get(agents.size()-1));
			agents.remove(agents.size()-1);
			--i;
		}
	}
};

class CompressedBlocks implements Serializable{
	private static final long serialVersionUID=1844677L;
	static byte[]_as=new byte[World_Height<<log_Chunk_Width];
	static Block[]_bs=new Block[World_Height<<log_Chunk_Width];
	byte[]as;
	Block[]bs;
	CompressedBlocks(Block[]b){
		int ap=0,bp=0;
		for(int l=0,r=0;l<b.length;l=r){
			for(++r;r<min(b.length,l+120)&&b[l]==b[r];++r);
			if(r-l==1){
				if(ap==0||_as[ap-1]>0||_as[ap-1]<-120)_as[ap++]=0;
				--_as[ap-1];
			}else _as[ap++]=(byte)(r-l);
			_bs[bp++]=b[l];
		}
		as=Arrays.copyOfRange(_as,0,ap);
		bs=Arrays.copyOfRange(_bs,0,bp);
		Arrays.fill(_bs,0,bp,null);
	}
	Block[] toBlockArray(){
		Block[]b=new Block[World_Height<<log_Chunk_Width];
		int p=0,bp=0;
		for(int t0:as){
			if(t0>0){
				Arrays.fill(b,p,p+t0,bs[bp++]);
				p+=t0;
			}else{
				for(;t0<0;++t0)b[p++]=bs[bp++];
			}
		}
		if(p!=b.length||bp!=bs.length)p/=0;
		return b;
	}
}
