package game.world;

import static java.lang.Math.*;
import static util.MathUtil.*;
import game.block.*;
import game.entity.Entity;
import game.entity.Agent;
import game.entity.Player;
import graphics.Canvas;
import java.util.*;
import util.BmpRes;
import game.GameSetting;
import game.entity.Entity.Pose;

public class NearbyInfo implements java.io.Serializable{
	private static final long serialVersionUID=1844677L;
	//描述世界中一个矩形内的全部物体
	
	public Block[][] blocks;//方块
	public ArrayList<Entity> ents=null;//非生物实体
	public ArrayList<Agent> agents=null;//生物
	public int xl,yl;//最左下的方块坐标
	public double mx,my,xd,yd;//矩形中心和半长半宽
	public Player pl=null;//需要被绘制ui的玩家
	public static float BW=12;//屏幕高度相当于BW个方块
	
	boolean hitTest(Entity e){
		return
			e.active()&&
			abs(mx-e.x)<=xd+e.width()&&
			abs(my-e.y)<=yd+e.height();
	}
	
	private static final int C1=8; //light transform constant
	
	//compute light decay
	private double getLight(double light,double x,double y){
		final double decay=0.25;
		double k=max(abs(x-mx)/2,abs(y-my))/(BW/2); //[0,1]
		k=k*k;
		light*=max(decay,1-k*(1-decay)); //[decay,1]*light
		light=C1-light*C1;
		return light;
	}
	
	//compute light for player
	int[][] calcLight(){
		final int C=2;
		int xl=(int)floor(mx-xd)-C,xr=(int)floor(mx+xd)+C;
		int yl=(int)floor(my-yd)-C,yr=(int)floor(my+yd)+C;
		double light0[]=new double[xr-xl+1];
		double light00=0;
		switch(World.cur.weather){
			case Weather._fire:light00=1;break;
			case Weather._plant:light00=1;break;
			case Weather._energystone:light00=1;break;
			case Weather._blood:light00=0.7;break;
			case Weather._dark:light00=0.4;break;
		}
		light00=getLight(light00,mx,my);
		for(int x=xl;x<=xr;++x){
			light0[x-xl]=light00;
		}
		for(int y=World.World_Height-1;y>yr;--y){
			for(int x=xl;x<=xr;++x){
				light0[x-xl]+=World.cur.get(x,y).transparency();
			}
		}
		
		
		double light[][]=new double[yr-yl+1][xr-xl+1];
		int y1=max(yl,min(yr,World.World_Height-1));
		for(int y=yr;y>=yl;--y){
			for(int x=xl;x<=xr;++x){
				light[y-yl][x-xl]=light0[x-xl];
			}
			if(y<=y1){
				for(int x=xl;x<=xr;++x){
					light0[x-xl]+=World.cur.get(x,y).transparency();
				}
			}
		}
		for(Entity e:ents){
			if(e.shadowed)continue;
			double x0=e.x-xl-0.5,y0=e.y-yl-0.5;
			int x=f2i(x0),y=f2i(y0);
			double a=getLight(e.light(),e.x,e.y);
			if(x<0||x>=xr-xl||y<0||y>=yr-yl)continue;
			for(int ya=0;ya<=1;++ya)
				for(int xa=0;xa<=1;++xa)
					light[y+ya][x+xa]=min(light[y+ya][x+xa],a+abs(x0-x-xa)+abs(y0-y-ya));
		}
		for(Agent e:agents){
			if(e.shadowed)continue;
			double x0=e.x-xl-0.5,y0=e.y-yl-0.5;
			int x=f2i(x0),y=f2i(y0);
			double a=getLight(e.light(),e.x,e.y);
			if(x<0||x>=xr-xl||y<0||y>=yr-yl)continue;
			for(int ya=0;ya<=1;++ya)
				for(int xa=0;xa<=1;++xa)
					light[y+ya][x+xa]=min(light[y+ya][x+xa],a+abs(x0-x-xa)+abs(y0-y-ya));
		}
		for(int y=yr;y>=yl;--y){
			for(int x=xl;x<=xr;++x){
				light[y-yl][x-xl]=min(light[y-yl][x-xl],getLight(World.cur.get(x,y).light(),x+0.5,y+0.5));
			}
			if(y<yr){
				for(int x=xl;x<=xr;++x){
					light[y-yl][x-xl]=min(light[y-yl][x-xl],light[y-yl+1][x-xl]+1);
				}
			}
			for(int x=xl+1;x<=xr;++x){
				light[y-yl][x-xl]=min(light[y-yl][x-xl],light[y-yl][x-xl-1]+1);
			}
			for(int x=xr-1;x>=xl;--x){
				light[y-yl][x-xl]=min(light[y-yl][x-xl],light[y-yl][x-xl+1]+1);
			}
		}
		for(int y=yl+1;y<=yr;++y){
			for(int x=xl;x<=xr;++x){
				light[y-yl][x-xl]=min(light[y-yl][x-xl],light[y-yl-1][x-xl]+1);
			}
		}
		int int_light[][]=new int[yr-yl+1-C*2][xr-xl+1-C*2];
		for(int y=yl+C;y<=yr-C;++y){
			for(int x=xl+C;x<=xr-C;++x){
				int c=max(0,min(255,f2i(light[y-yl][x-xl]/(C1)*256)));
				int_light[y-yl-C][x-xl-C]=c;
			}
		}
		return int_light;
	}
	
	public void setPlayer(Player _pl){
		pl=_pl;
	}
	
	public byte[] getBytes(BitSet known_id,float HdW){
		game.ui.UI.pl=pl;
		game.ui.UI.H_div_W=max(0.5f,min(1,HdW));
		GameSetting gs=pl.game_setting;
		Canvas cv=new Canvas(known_id,gs);
		if(rnd()<0.3&&!pl.action.unknown_texture.isEmpty()){
			int size=pl.action.unknown_texture.size();
			short id=pl.action.unknown_texture.get(0);
			cv.wBmp(id);
			pl.action.unknown_texture.remove(0);
		}
		
		cv.save();
		cv.rotate(pl.getRotation());

		cv.drawColor(World.cur.sky_color);
		
		int[][] light=calcLight();
		
		Comparator<Entity> cmp=new Comparator<Entity>(){
			public int compare(Entity p1,Entity p2){
				if(p1.id==p2.id)return 0;
				return p1.id<p2.id?-1:1;
			}
		};
		Collections.sort(ents,cmp);
		Collections.sort(agents,cmp);
		
		if(World.cur.getMode() instanceof ECPvPMode){
			ECPvPMode mode=(ECPvPMode)World.cur.getMode();
			float x0=(float)(mode.cur_l-mx);
			float x1=(float)(mode.cur_r-mx);
			cv.drawRect(x0-4000,-128,x0,128,0xffff0000);
			cv.drawRect(x1,-128,x1+4000,128,0xffff0000);
		}
		
		for(Agent a:agents){
			if(a.shadowed)continue;
			Pose p=a.getPose(mx,my);
			if(p==null)continue;
			if(abs(p.x-mx)>BW+a.width())continue;
			if(abs(p.y-my)>BW/2+a.height())continue;
			float xd=(float)(p.x-mx),yd=(float)(p.y-my);
			cv.save();
			cv.translate(xd,yd);
			a.before_draw(cv);
			a.draw(cv);
			a.after_draw(cv);
			cv.restore();
		}
		
		
		for(Entity e:ents){
			if(e.shadowed)continue;
			Pose p=e.getPose(mx,my);
			if(p==null)continue;
			if(abs(p.x-mx)>BW+e.width())continue;
			if(abs(p.y-my)>BW/2+e.height())continue;
			float xd=(float)(p.x-mx),yd=(float)(p.y-my);
			cv.save();
			cv.translate(xd,yd);
			e.draw(cv);
			cv.restore();
		}
		
		
		cv.gridBegin((float)(xl+0.5-mx),(float)(yl+0.5-my));
		
		final int y_max=blocks.length,x_max=blocks[0].length;
		boolean[][] cols=new boolean[y_max][x_max];
		
		if(gs.tip_des_block&&pl.des_flag){
			if(pl.destroyable(pl.des_x,pl.des_y)){
				for(int y=0;y<y_max;++y)
				for(int x=0;x<x_max;++x)
					if(yl+y==pl.des_y||xl+x==pl.des_x)cols[y][x]=true;
			}else{
				for(int y=0;y<y_max;++y)
				for(int x=0;x<x_max;++x)
					cols[y][x]=pl.destroyable(xl+x,yl+y);
			}
		}else if(gs.tip_place_block&&pl.fail_to_place_block>0){
			for(int y=0;y<y_max;++y)
			for(int x=0;x<x_max;++x)
				cols[y][x]=true;
			for(Agent a:agents){
				int x1=max(0,f2i(a.left-xl)),y1=max(0,f2i(a.bottom-yl));
				int x2=min(x_max-1,f2i(a.right-xl)),y2=min(y_max-1,f2i(a.top-yl));
				for(int y=y1;y<=y2;++y)
				for(int x=x1;x<=x2;++x)cols[y][x]=false;
			}
			for(int y=0;y<y_max;++y)
			for(int x=0;x<x_max;++x){
				if(abs(yl+y+0.5-my)>4.5)cols[y][x]=false;
				else if(abs(xl+x+0.5-mx)>4.5)cols[y][x]=false;
				else if(!World.cur.nextToBlock(xl+x,yl+y))cols[y][x]=false;
			}
		}
		
		for(int y=0;y<y_max;++y){
			Block[] b=blocks[y];
			for(int x=0;x<x_max;++x){
				if(pl.suspend_mode&&!gs.light_in_suspend_mode)light[y][x]=0;
				if(!(pl.creative_mode&&pl.suspend_mode))
				if(b[x].rootBlock() instanceof StoneBlock&&light[y][x]>127){
					if(!World.cur.destroyable(xl+x,yl+y)){
						b[x]=((StoneBlock)(b[x].rootBlock())).toStone();
					}
				}
				if(light[y][x]<255)b[x].draw(cv);
				//cv.drawRect(light[y][x]<<24);
				if(cols[y][x])cv.drawRect(0x3fffffff);
				cv.gridNext();
			}
			cv.gridNewLine();
		}
		cv.end();
		
		cv.drawLight(light,y_max,x_max,(float)(xl-mx),(float)(yl-my));
		
		pl.drawTip(cv);
		
		cv.restore();
		
		cv.end();
		if(gs.show_ui)pl.drawLeftUI(cv);
		cv.end();
		if(gs.show_ui&&pl.respawn_time==0)pl.drawRightUI(cv);
		cv.end();
		return cv.getBytes();
	}
}
