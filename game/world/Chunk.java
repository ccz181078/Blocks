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
	ArrayList<Entity> ni_ents=new ArrayList<>();
	ArrayList<Entity> ents=new ArrayList<>();
	ArrayList<Agent> agents=new ArrayList<>();
	
	static <T extends Entity> void getEnts(NearbyInfo ni,ArrayList<T> src,ArrayList<T> dst){
		double lx=ni.mx-ni.xd-1;
		double rx=ni.mx+ni.xd+1;
		int L=0,R=src.size(),R0=R;
		while(L<R){
			int M=(L+R)>>1;
			if(src.get(M).x<lx)L=M+1;
			else R=M;
		}
		for(int i=L;i<R0;++i){
			T e=src.get(i);
			if(e.x>rx)break;
			if(ni.hitTest(e))dst.add(e);
		}
	}
	
	private static class RectSearchDS{
		private static final long serialVersionUID=1844677L;
		ArrayList<Entity> es[];
		RectSearchDS(){
			es=new ArrayList[32];
			for(int i=0;i<32;++i)es[i]=new ArrayList<>();
		}
		int mapY(double y){
			return max(0,min(127,(int)y))/4;
		}
		void update(ArrayList es0){
			for(ArrayList<Entity> e:es)e.clear();
			for(Entity e:(ArrayList<Entity>)es0){
				if(e.locked)continue;
				es[mapY(e.y)].add(e);
			}
		}
		void query(NearbyInfo ni,ArrayList dst){
			int ly=mapY(ni.my-ni.yd-1);
			int ry=mapY(ni.my+ni.yd+1);
			for(int i=ly;i<=ry;++i)getEnts(ni,es[i],dst);
		}
	}
	private transient RectSearchDS 
		ni_ents_ds=new RectSearchDS(),
		ents_ds=new RectSearchDS(),
		agents_ds=new RectSearchDS();
	
	long last_update_time=0;
	int id;
	CompressedBlocks cbs=null;
	transient boolean gen_ent;
	void setX(int x,Block[] block){//unchecked
		for(int y=0;y<World_Height;++y)blocks[y<<log_Chunk_Width|x]=block[y];
	}
	void init(int _id,WorldGenerator gen,int dir){
		id=_id;
		World.cur.getMode().initChunk(this,gen,dir);
	}
	private void readObject(ObjectInputStream in)throws IOException,ClassNotFoundException{ 
		in.defaultReadObject();
		if(blocks==null){
			blocks=cbs.toBlockArray();
			cbs=null;
		}
		ni_ents=new ArrayList<>();
		
		ni_ents_ds=new RectSearchDS();
		ents_ds=new RectSearchDS();
		agents_ds=new RectSearchDS();
	}
	private void writeObject(ObjectOutputStream out)throws IOException{
		ArrayList<Entity> tmp_ni_ents=ni_ents;
		ni_ents=null;
		Block[]b0=blocks;
		blocks=null;
		cbs=new CompressedBlocks(b0);
		out.defaultWriteObject();
		blocks=b0;
		cbs=null;
		ni_ents=tmp_ni_ents;
	}
	void moveEnts(){
		for(Entity e:ni_ents)if(e.active())e.move();
		for(Entity e:ents)if(e.active())e.move();
		for(Agent a:agents)if(a.active())a.move();
	}
	void updateEnts0(){
		for(Entity e:ni_ents)if(e.active())e.update0();
		for(Entity e:ents)if(e.active())e.update0();
		for(Agent a:agents)if(a.active())a.update0();
		genEnt();
	}
	void genEnt(){
		World.cur.getMode().genEnt(this);
	}
	void updateEnts(){
		for(Entity e:ni_ents)if(e.active())e.update();
		for(Entity e:ents)if(e.active())e.update();
		for(Agent a:agents)if(a.active())a.update();
	}
	void updateAgents0(){
		for(Agent a:agents)if(!a.isRemoved()){
			if(!a.is_ctrled||a instanceof Player)a.ai();
		}
	}
	void updateAgents1(){
		for(Agent a:agents)if(!a.isRemoved()){
			a.action();
		}
	}
	void randomUpdateBlocks(){
		double K=120;
		int T=rf2i(World_Height*Chunk_Width/K);
		for(int t=0;t<T;++t){
			int x=rndi(minX(),maxX()-1);
			int y=rndi(0,World_Height-1);
			World.cur.get(x,y).onUpdate(x,y);
		}
		
		T=rf2i(World_Height*Chunk_Width/K/10);
		for(int t=0;t<T;++t){
			int x=rndi(minX(),maxX()-1);
			int y=rndi(0,World_Height-1);
			GroupFall.apply(x,y);
		}
		
		if(World.cur.weather==Weather._dark&&rnd()<0.8)return;
		T=rf2i(Chunk_Width/K/3);
		for(int t=0;t<T;++t){
			int x=rndi(minX(),maxX()-1);
			double v=1;
			for(int y=World_Height-1;y>=0&&v>0;--y){
				Block w=World.cur.get(x,y);
				w.onLight(x,y,v);
				v-=w.transparency();
			}
		}
	}
	void getNonInteractiveEnts(NearbyInfo ni){
		ni_ents_ds.query(ni,ni.ents);
	}
	void getEnts(NearbyInfo ni){
		ents_ds.query(ni,ni.ents);
	}
	void getAgents(NearbyInfo ni){
		agents_ds.query(ni,ni.agents);
	}
	void sortEnts(){
		Comparator<Entity> cmp=new Comparator<Entity>(){
			public int compare(Entity p1,Entity p2){
				if(p1.x==p2.x)return 0;
				return p1.x<p2.x?-1:1;
			}
		};
		Collections.sort(ni_ents,cmp);
		Collections.sort(ents,cmp);
		Collections.sort(agents,cmp);
		
		ni_ents_ds.update(ni_ents);
		ents_ds.update(ents);
		agents_ds.update(agents);
	}
	int minX(){return id<<log_Chunk_Width;}
	int maxX(){return (id+1)<<log_Chunk_Width;}
	void removeDeadEnts(ArrayList<Entity> ents){
		for(int i=0;i<ents.size();++i){
			Entity e=ents.get(i);

			if(e.isDead())e.onDestroy();
			else if(e.x<minX()||e.x>=maxX())e.add();
			else continue;

			ents.set(i,ents.get(ents.size()-1));
			ents.remove(ents.size()-1);
			--i;
		}		
	}
	void removeDeadEnts(){
		removeDeadEnts(ni_ents);
		removeDeadEnts(ents);
		for(int i=0;i<agents.size();++i){
			Agent a=agents.get(i);

			if(a.isDead()){
				a.onDestroy();
				//debug.Log.i("remove:"+World.cur.RX(a.x)+","+World.cur.RY(a.y)+":"+a.getClass().getSimpleName()+":"+World.cur.time+"/"+a.last_seen_time+"/"+a.spawn_time);
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
