package game.entity;

import game.world.World;

public class Enemy implements java.io.Serializable{
private static final long serialVersionUID=1844677L;
	Agent w;
	double sum_v;
	long last_att_time;
	Enemy(double _v,Agent _w){
		sum_v=_v;w=_w;
		last_att_time=World._.time;
	}
	public static void ins(Enemy e[],Enemy w){
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
		e[util.MathUtil.rndi(0,e.length-1)]=w;
	}
	public static void check(Enemy e[]){
		for(int i=0;i<e.length;++i)if(e[i]!=null){
			if(e[i].w.removed)e[i]=null;
		}
	}
}

