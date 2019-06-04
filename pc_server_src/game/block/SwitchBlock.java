package game.block;

import static util.MathUtil.*;
import game.entity.Agent;
import game.world.World;
import game.item.Switch;
import graphics.Canvas;

public class SwitchBlock extends CircuitBlock{
	private static final long serialVersionUID=1844677L;
	int tp;
	public SwitchBlock(Block b){super(b);}
	public int energyValX(){return tp*32;}
	public int energyValY(){return tp*32;}

	public boolean onClick(int x,int y,Agent agent){
		tp^=1;
		World.cur.check4(x,y);
		return super.onClick(x,y,agent);
	}
	public void onCircuitDestroy(int x,int y){
		new Switch().drop(x,y);
	}

	public void draw(Canvas cv){
		super.draw(cv);
		Switch.bmp[tp].draw(cv,0,0,.5f,.5f);
	}
	
};
