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
	public MineBlock(Block b,Mine mine){
		super(b);
		this.mine=mine;
	}
	
	public String getName(){return "埋在"+super.getName()+"中的地雷";}//获取名字
	
	public boolean onSpannerClick(int x,int y){
		return false;
	}
	
	public Item setWarhead(Warhead w,Agent src){
		if(wh!=null)return w;
		wh=w;
		this.src=src;
		return null;
	}

	public void touchEnt(int x,int y,Entity ent){
		super.touchEnt(x,y,ent);
		if(rnd()<0.1){
			if(World.cur.get(x,y)==this){
				World.cur.set(x,y,rootBlock());
				mine.explode(x+0.5,y+0.5,ent,wh,SourceTool.make(SourceTool.block(x,y,this),"携带的"));
			}
		}
	}
	
	public void onCircuitDestroy(int x,int y){
		mine.drop(x,y);
		if(wh!=null)wh.drop(x,y);
	}
	
}
