package game.block;

import game.entity.Entity;
import game.world.World;
import game.item.*;
import game.entity.*;
import graphics.Canvas;
import static util.MathUtil.*;

public class MineBlock extends CircuitBlock{
	private static final long serialVersionUID=1844677L;
	Mine mine;
	Warhead wh;
	long time;
	public MineBlock(Block b,Mine mine){
		super(b);
		this.mine=mine;
		time=World.cur.time+40;
	}
	
	public String getName(){return "埋在"+super.getName()+"中的地雷";}//获取名字
	
	public boolean onSpannerClick(int x,int y){
		return false;
	}
	
	public Item setWarhead(Warhead w,Agent src){
		if(wh!=null)return w;
		wh=w;
		this.src=src;
		time=World.cur.time+80;
		return null;
	}

	public void touchEnt(int x,int y,Entity ent){
		super.touchEnt(x,y,ent);
		if(wh!=null&&World.cur.time>=time&&rnd()<0.3&&ent.mass()>=0.8){
			if(World.cur.get(x,y)==this){
				World.cur.set(x,y,rootBlock());
				mine.explode(x+0.5,y+0.5,ent,wh,SourceTool.carry(SourceTool.block(x,y,this)));
			}
		}
	}
	
	public void onCircuitDestroy(int x,int y){
		mine.drop(x,y);
		if(wh!=null)wh.drop(x,y);
	}
	
	public void draw(Canvas cv){
		super.draw(cv);
		if(World.cur.time<time){
			if(wh!=null){
				wh.getBmp().draw(cv,0,0,0.3f,0.3f);
			}else{
				mine.getBmp().draw(cv,0,0,0.3f,0.3f);
			}
		}
	}
}
