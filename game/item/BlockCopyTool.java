package game.item;

import static util.MathUtil.*;
import util.BmpRes;
import game.entity.*;
import game.world.World;
import game.block.*;

public class BlockCopyTool extends Item{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp[]=BmpRes.load("Item/BlockCopyTool_",3);
	public BmpRes getBmp(){return bmp[state];}
	public boolean disableRecover(){return true;}
	
	int state=0;
	
	@Override
	public int maxAmount(){
		return 1;
	}
	int x1,y1,xn,yn;
	Block blocks[][];
	
	public Item clickAt(double tx,double ty,Agent a){
		if(!(a instanceof Player))return this;
		if(!((Player)a).creative_mode)return this;
		int px=f2i(tx),py=f2i(ty);
		if(state!=1){
			x1=px;y1=py;
			state=1;
		}else{
			int x2=px,y2=py;
			state=2;
			if(x1>x2){int t=x1;x1=x2;x2=t;}
			if(y1>y2){int t=y1;y1=y2;y2=t;}
			xn=x2-x1+1;
			yn=y2-y1+1;
			if(xn*yn>128*128*10){
				state=0;
				blocks=null;
			}else{
				Block tmp[][]=new Block[yn][xn];
				for(int y=0;y<yn;++y){
					for(int x=0;x<xn;++x){
						tmp[y][x]=World.cur.get(x1+x,y1+y);
					}
				}
				blocks=util.SerializeUtil.deepCopy(tmp);
			}
		}
		return this;
	}
	public boolean onLongPress(Agent a,double tx,double ty){
		if(!(a instanceof Player))return true;
		if(!((Player)a).creative_mode)return true;
		int px=f2i(tx),py=f2i(ty);
		if(state==2){
			for(int y=0;y<yn;++y){
				for(int x=0;x<xn;++x){
					if(!(blocks[y][x] instanceof AirBlock)){
						World.cur.set(px+x,py+y,blocks[y][x]);
					}
				}
			}
			blocks=util.SerializeUtil.deepCopy(blocks);
			for(int y=-1;y<=yn;++y){
				for(int x=-1;x<=xn;++x){
					World.cur.checkBlock(px+x,py+y);
				}
			}
		}
		return true;
	}
	public String getAmountString(int cnt){
		if(state==2)return xn+"x"+yn;
		return "";
	}

	@Override
	public boolean isCreative(){return true;}

}
