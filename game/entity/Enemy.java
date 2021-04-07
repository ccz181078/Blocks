package game.entity;

import game.world.World;

import static util.MathUtil.*;

public class Enemy implements java.io.Serializable{
private static final long serialVersionUID=1844677L;
	Agent w;
	double sum_v;
	long last_att_time;
	private Enemy(double _v,Source _w){
		sum_v=_v;w=_w.getSrc();
		last_att_time=World.cur.time;
	}
	Enemy(double _v,Source _w,Agent x){
		this(_v,_w);
		if(w==x)w=null;
	}
	public static void ins(Enemy e[],Enemy w){
		if(e==null)return;
		if(w==null)return;
		if(w.w==null)return;
		if(w.sum_v<1e-8)return;
		for(int i=0;i<e.length;++i){
			if(e[i]==null){
				e[i]=w;
				return;
			}
			if(e[i].w==w.w){
				w.sum_v+=e[i].sum_v;
				e[i]=w;
				return;
			}
		}
		e[rndi(0,e.length-1)]=w;
	}
	public static void check(Enemy e[]){
		if(e==null)return;
		for(int i=0;i<e.length;++i)if(e[i]!=null){
			if(e[i].w.isRemoved()){
				e[i].w=e[i].w.getSrc();
				if(e[i].w==null)e[i]=null;
			}
		}
	}
	public static Agent weightedSelect(Enemy es[]){
		Agent ret=null;
		double sum=0;
		for(Enemy e:es){
			if(e==null)continue;
			sum+=e.sum_v;
			if(rnd()*sum<=e.sum_v){
				ret=e.w;
			}
		}
		return ret;
	}
}

