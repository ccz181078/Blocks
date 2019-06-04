package game.world;

import static java.lang.Math.*;
import static util.MathUtil.*;
import game.block.*;
import game.entity.Entity;
import game.entity.Agent;
import game.entity.Player;
import graphics.Canvas;
import java.util.*;
import graphics.MyCanvas;
import util.BmpRes;

public class NearbyInfo implements java.io.Serializable{
	private static final long serialVersionUID=1844677L;
	//描述世界中一个矩形内的全部物体
	
	public Block[][] blocks;//方块
	public ArrayList<Entity> ents=null;//非生物实体
	public ArrayList<Agent> agents=null;//生物
	public int xl,yl;//最左下的方块坐标
	public double mx,my,xd,yd;//矩形中心和半长半宽
	public Player pl=null;//需要被绘制ui的玩家
	public static float BW=10f;//屏幕高度相当于BW个方块
	
	boolean hitTest(Entity e){
		return
			!e.removed&&
			abs(mx-e.x)<=xd+e.width()&&
			abs(my-e.y)<=yd+e.height();
	}
	
	public void setPlayer(Player _pl){
		pl=_pl;
	}
	
	public byte[] getBytes(BitSet known_id,game.GameSetting gs,float HdW){
		game.ui.UI.pl=pl;
		game.ui.UI.H_div_W=max(0.5f,min(1,HdW));
		Canvas cv=new Canvas(known_id,gs);
		cv.drawColor(World._.sky_color);
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
				if(abs(yl+y+0.5-pl.y)>4.5)cols[y][x]=false;
				else if(abs(xl+x+0.5-pl.x)>4.5)cols[y][x]=false;
				else if(!World._.nextToBlock(xl+x,yl+y))cols[y][x]=false;
			}
		}
		
		
		for(int y=0;y<y_max;++y){
			Block[] b=blocks[y];
			for(int x=0;x<x_max;++x){
				if(!(pl.creative_mode&&pl.suspend_mode))
				if(b[x].rootBlock() instanceof StoneBlock){
					if(!World._.destroyable(xl+x,yl+y)){
						b[x]=((StoneBlock)(b[x].rootBlock())).toStone();
					}
				}
				b[x].draw(cv);
				if(cols[y][x])cv.drawRect(0x3fffffff);
				//if(blocks[y][x].last_chk_time>=World._.time-5)cv.drawRect(0x3fff0000);
				cv.gridNext();
			}
			cv.gridNewLine();
		}
		cv.end();
		
		pl.drawTip(cv);
		
		for(Agent a:agents){
			float xd=(float)(a.x-mx),yd=(float)(a.y-my);
			cv.save();
			cv.translate(xd,yd);
			a.before_draw(cv);
			a.draw(cv);
			a.after_draw(cv);
			cv.restore();
		}
		
		
		for(Entity e:ents){
			float xd=(float)(e.x-mx),yd=(float)(e.y-my);
			cv.save();
			cv.translate(xd,yd);
			e.draw(cv);
			cv.restore();
		}
		cv.end();
		pl.drawLeftUI(cv);
		cv.end();
		pl.drawRightUI(cv);
		cv.end();
		return cv.getBytes();
	}
	
	public static void draw(android.graphics.Canvas cv,byte[] bytes,float W,float H){
		MyCanvas mc=new MyCanvas(cv,bytes,BmpRes.int2bmp);
		try{
			cv.save();
			cv.translate(W/2,H/2);
			cv.scale(H/BW,-H/BW);
			mc.draw();
			cv.restore();

			cv.save();
			cv.scale(H/8,H/8);
			mc.draw();
			cv.translate(W*8/H,0);
			mc.draw();
			cv.restore();
		}catch(Exception e){e.printStackTrace();}
	}
}
