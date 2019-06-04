package game.block;
import util.BmpRes;
import game.entity.Entity;
import game.world.World;
import game.entity.Agent;
import game.item.Button;
import graphics.Canvas;

public class ButtonBlock extends CircuitBlock{
	private static final long serialVersionUID=1844677L;
	int tp=0;

	public ButtonBlock(Block b){super(b);}
	public int energyValX(){return tp>0?32:0;}
	public int energyValY(){return tp>0?32:0;}

	public boolean onCheck(int x,int y){
		if(super.onCheck(x,y))return true;
		if(tp>0){
			--tp;
			if(tp==0)World.cur.check4(x,y);
			else World.cur.checkBlock(x,y);
		}
		return false;
	}

	public boolean onClick(int x,int y,Agent agent){
		if(tp==0)World.cur.check4(x,y);
		tp=10;
		return super.onClick(x,y,agent);
	}
	
	public void onCircuitDestroy(int x,int y){
		new Button().drop(x,y);
	}

	public void draw(Canvas cv){
		super.draw(cv);
		Button.bmp[tp>0?1:0].draw(cv,0,0,.5f,.5f);
	}
}
