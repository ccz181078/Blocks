package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import game.block.Block;
import game.world.World;
import graphics.Canvas;

public class FallingBlock extends Entity{
private static final long serialVersionUID=1844677L;
	Block block;
	public double width(){return 0.5;}
	public double height(){return 0.5;}
	public FallingBlock(int _x,int _y,Block _b){
		x=_x+0.5;
		y=_y+0.5;
		xv=0;
		yv=0;
		hp=10;
		block=_b;
	}
	void onKill(){
		new DroppedItem(x+rnd(-0.3,0.3),y+rnd(-0.3,0.3),block.asItem().setAmount(1)).add();
	}
	public void update(){
		super.update();
		hp-=0.1f;
		if(!removed&&ydep<0&&World._.get(x,y).isCoverable()&&!World._.get(x,y-1).isCoverable()){
			double xp=x-floor(x);
			if(abs(xp-0.5)<0.1){
				remove();
				World._.place(f2i(x),f2i(y),block);
			}else{
				if(abs(xv)<0.05){
					xa+=xp<0.5?0.01:-0.01;
				}
			}
		}
	}
	public void draw(Canvas cv){
		block.draw(cv);
	}
};

