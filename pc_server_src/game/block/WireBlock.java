package game.block;

import game.world.World;
import graphics.Canvas;
import static java.lang.Math.*;
import game.item.Wire;
import game.item.OneWayWire;
import game.item.EnergyStone;
import game.item.EnergyBlocker;
import game.item.AndGate;
import game.item.OneWayControler;

public class WireBlock extends CircuitBlock{
	private static final long serialVersionUID=1844677L;
	public static final int
	
		l_in=1,
		r_in=2,
		lr_in=l_in|r_in,
		
		u_in=4,
		d_in=8,
		ud_in=u_in|d_in,
		
		mid=16,
		
		lr_block=32,
		ud_block=64,
		_block=lr_block|ud_block,
		
		lr_and=128,
		ud_and=256,
		_and=lr_and|ud_and,
		
		lr_2_ud=512,
		ud_2_lr=1024,
		_one_way=lr_2_ud|ud_2_lr,
		
		_ctrl=_block|_and|_one_way;
	
	private int xe,ye,tp;
	public WireBlock(Block b){super(b);}
	private static int getColor(int v){
		return v>0?0xff3fffff:0xff00afaf;
	}
	
	public boolean onCheck(int x,int y){
		if(super.onCheck(x,y))return true;
		
		int xe1=0,ye1=0;
		
		if((tp&l_in)!=0)xe1=max(xe1,World.cur.get(x-1,y).energyValX()-1);
		if((tp&r_in)!=0)xe1=max(xe1,World.cur.get(x+1,y).energyValX()-1);
		if((tp&u_in)!=0)ye1=max(ye1,World.cur.get(x,y+1).energyValY()-1);
		if((tp&d_in)!=0)ye1=max(ye1,World.cur.get(x,y-1).energyValY()-1);
		
		if((tp&mid)!=0){
			int t=tp&_ctrl;
			if(t==lr_block){
				if(xe1>0)ye1=0;
			}else if(t==ud_block){
				if(ye1>0)xe1=0;
			}else if(t==lr_and){
				if(xe1==0)ye1=0;
			}else if(t==ud_and){
				if(ye1==0)xe1=0;
			}else if(t==lr_2_ud){
				ye1=xe1;
			}else if(t==ud_2_lr){
				xe1=ye1;
			}else{
				xe1=ye1=max(xe1,ye1);
			}
		}
		
		if(xe1!=xe||ye1!=ye)World.cur.check4(x,y);
		xe=xe1;ye=ye1;
		return false;
	}
	
	public int energyValX(){return xe;}
	public int energyValY(){return ye;}
	
	public boolean ins(int x,int y,int v,int vs){
		if((vs&tp)==0){
			int _tp=tp|v;
			if((_tp&mid)==0&&(_tp&_ctrl)!=0)return false;
			if((_tp&mid)!=0&&((_tp&lr_in)==0||(_tp&ud_in)==0))return false;
			tp=_tp;
			World.cur.check4(x,y);
			return true;
		}
		return false;
	}
	
	public void onCircuitDestroy(int x,int y){
		int lr=tp&lr_in;
		int ud=tp&ud_in;
		int wire_t=(lr==lr_in?1:0)+(ud==ud_in?1:0);
		int one_way_wire_t=(lr!=0?1:0)+(ud!=0?1:0)-wire_t;
		if(wire_t>0)new Wire().drop(x,y,wire_t);
		if(one_way_wire_t>0)new OneWayWire().drop(x,y,one_way_wire_t);
		if((tp&mid)!=0)new EnergyStone().drop(x,y);
		if((tp&_block)!=0)new EnergyBlocker().drop(x,y);
		if((tp&_and)!=0)new AndGate().drop(x,y);
		if((tp&_one_way)!=0)new OneWayControler().drop(x,y);
	}

	public void draw(Canvas cv){
		super.draw(cv);
		int lr=tp&lr_in;
		int ud=tp&ud_in;
		if(lr!=0){
			int col=getColor(xe);
			cv.drawRect(-1/2f,-1/16f,1/2f,1/16f,col);
			if(lr==l_in)cv.drawRect(1/4f,-1/8f,1/2f,1/8f,col);
			if(lr==r_in)cv.drawRect(-1/2f,-1/8f,-1/4f,1/8f,col);
		}
		if(ud!=0){
			int col=getColor(ye);
			cv.drawRect(-1/16f,-1/2f,1/16f,1/2f,col);
			if(ud==d_in)cv.drawRect(-1/8f,1/4f,1/8f,1/2f,col);
			if(ud==u_in)cv.drawRect(-1/8f,-1/2f,1/8f,-1/4f,col);
		}
		if((tp&mid)!=0){
			int col=getColor(max(xe,ye));
			cv.drawRect(-1/8f,-1/8f,1/8f,1/8f,col);
		}
		if((tp&lr_block)!=0)EnergyBlocker.bmp[0].draw(cv,0,0,.5f,.5f);
		if((tp&ud_block)!=0)EnergyBlocker.bmp[1].draw(cv,0,0,.5f,.5f);
		if((tp&lr_and)!=0)AndGate.bmp[0].draw(cv,0,0,.5f,.5f);
		if((tp&ud_and)!=0)AndGate.bmp[1].draw(cv,0,0,.5f,.5f);
		if((tp&lr_2_ud)!=0)OneWayControler.bmp[0].draw(cv,0,0,.5f,.5f);
		if((tp&ud_2_lr)!=0)OneWayControler.bmp[1].draw(cv,0,0,.5f,.5f);
	}
	
};
