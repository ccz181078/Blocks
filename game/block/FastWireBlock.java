package game.block;

import game.world.World;
import graphics.Canvas;
import static java.lang.Math.*;
import static game.block.WireBlock.getColor;

public abstract class FastWireBlock extends CircuitBlock{
	private static final long serialVersionUID=1844677L;
	static final class Energy implements java.io.Serializable{
		int l=1,r=0,e=0;
	}
	public static FastWireBlock make(Block b,int tp){
		if(tp==0)return new FastWireBlockH(b);
		return new FastWireBlockV(b);
	}
	Energy e;
	public FastWireBlock(Block b){super(b);e=new Energy();}
	
	
	public int energyValL(){return 0;}
	public int energyValR(){return 0;}
	public int energyValD(){return 0;}
	public int energyValU(){return 0;}
	
	public boolean onCheck(int x,int y){
		if(super.onCheck(x,y))return true;
		checkCir(x,y);
		return false;
	}
	
	public void onCircuitDestroy(int x,int y){
		new game.item.Wire().drop(x,y);
		World.cur.check4(x,y);
	}

	public final boolean ins(int x,int y,int v,int vs){
		WireBlock w=new WireBlock(block);
		if(this instanceof FastWireBlockH){
			if(v==vs&&v==WireBlock.ud_in){
				World.cur.setCircuit(x,y,new FastWireBlockC(block));
				return true;
			}
			w.tp=WireBlock.lr_in;
			w.xe=e.e;
			w.ye=0;
		}else{
			if(v==vs&&v==WireBlock.lr_in){
				World.cur.setCircuit(x,y,new FastWireBlockC(block));
				return true;
			}
			w.tp=WireBlock.ud_in;
			w.ye=e.e;
			w.xe=0;
		}
		if(w.ins(x,y,v,vs)){
			World.cur.setCircuit(x,y,w);
			return true;
		}
		return false;
	}
	abstract void checkCir(int x,int y);
	
};
interface FastWireBlockHInterface{
	FastWireBlockH getFastWireBlockH();
}

final class FastWireBlockH extends FastWireBlock implements FastWireBlockHInterface{
	private static final long serialVersionUID=1844677L;
	public FastWireBlockH(Block b){super(b);}
	public FastWireBlockH getFastWireBlockH(){return this;}
	void checkCir(int x,int y){
		int e0=e.e;
		boolean bad=(e.l>x||e.r<x);
		if(!bad){
			Block bl=World.cur.get(x-1,y);
			Block br=World.cur.get(x+1,y);
			boolean ls=(bl instanceof FastWireBlockHInterface);
			boolean rs=(br instanceof FastWireBlockHInterface);
			bad|=(ls!=(e.l<x));
			bad|=(rs!=(e.r>x));
			if(ls)bad|=(((FastWireBlockHInterface)bl).getFastWireBlockH().e!=e);
			if(rs)bad|=(((FastWireBlockHInterface)br).getFastWireBlockH().e!=e);
		}
		if(bad){
			e0=-1;
			int l=x,r=x;
			e=new Energy();
			for(;;){
				Block b=World.cur.get(l-1,y);
				if(!(b instanceof FastWireBlockHInterface))break;
				((FastWireBlockHInterface)b).getFastWireBlockH().e=e;
				--l;
			}
			for(;;){
				Block b=World.cur.get(r+1,y);
				if(!(b instanceof FastWireBlockHInterface))break;
				((FastWireBlockHInterface)b).getFastWireBlockH().e=e;
				++r;
			}
			e.l=l;e.r=r;e.e=0;
		}
		e.e=max(0,max(World.cur.get(e.l-1,y).energyValR(),World.cur.get(e.r+1,y).energyValL()));
		if(e0!=e.e){
			World.cur.checkBlock(e.l-1,y);
			World.cur.checkBlock(e.r+1,y);
		}
	}
	public int energyValL(){return e.e;}
	public int energyValR(){return e.e;}
	public void draw(Canvas cv){
		super.draw(cv);
		int col=getColor(e.e);
		cv.drawRect(-1/2f,-1/8f,1/2f,1/8f,col);
	}
}
interface FastWireBlockVInterface{
	FastWireBlockV getFastWireBlockV();
}
final class FastWireBlockV extends FastWireBlock implements FastWireBlockVInterface{
	private static final long serialVersionUID=1844677L;
	public FastWireBlockV(Block b){super(b);}
	public FastWireBlockV getFastWireBlockV(){return this;}
	void checkCir(int x,int y){
		int e0=e.e;
		boolean bad=(e.l>y||e.r<y);
		if(!bad){
			Block bl=World.cur.get(x,y-1);
			Block br=World.cur.get(x,y+1);
			boolean ls=(bl instanceof FastWireBlockVInterface);
			boolean rs=(br instanceof FastWireBlockVInterface);
			bad|=(ls!=(e.l<y));
			bad|=(rs!=(e.r>y));
			if(ls)bad|=(((FastWireBlockVInterface)bl).getFastWireBlockV().e!=e);
			if(rs)bad|=(((FastWireBlockVInterface)br).getFastWireBlockV().e!=e);
		}
		if(bad){
			e0=-1;
			int l=y,r=y;
			e=new Energy();
			for(;;){
				Block b=World.cur.get(x,l-1);
				if(!(b instanceof FastWireBlockVInterface))break;
				((FastWireBlockVInterface)b).getFastWireBlockV().e=e;
				--l;
			}
			for(;;){
				Block b=World.cur.get(x,r+1);
				if(!(b instanceof FastWireBlockVInterface))break;
				((FastWireBlockVInterface)b).getFastWireBlockV().e=e;
				++r;
			}
			e.l=l;e.r=r;
		}
		e.e=max(0,max(World.cur.get(x,e.l-1).energyValU(),World.cur.get(x,e.r+1).energyValD())-1);
		if(e0!=e.e){
			World.cur.checkBlock(x,e.l-1);
			World.cur.checkBlock(x,e.r+1);
		}
	}
	public int energyValD(){return e.e;}
	public int energyValU(){return e.e;}
	public void draw(Canvas cv){
		super.draw(cv);
		int col=getColor(e.e);
		cv.drawRect(-1/8f,-1/2f,1/8f,1/2f,col);
	}
}
final class FastWireBlockC extends CircuitBlock implements FastWireBlockHInterface,FastWireBlockVInterface{
	private static final long serialVersionUID=1844677L;
	FastWireBlockH lr;
	FastWireBlockV ud;
	public FastWireBlockH getFastWireBlockH(){return lr;}
	public FastWireBlockV getFastWireBlockV(){return ud;}
	public FastWireBlockC(Block b){
		super(b);
		lr=new FastWireBlockH(b);
		ud=new FastWireBlockV(b);
	}
	public boolean onCheck(int x,int y){
		if(super.onCheck(x,y))return true;
		checkCir(x,y);
		return false;
	}
	void checkCir(int x,int y){
		lr.checkCir(x,y);
		ud.checkCir(x,y);
	}
	public int energyValL(){return lr.e.e;}
	public int energyValR(){return lr.e.e;}
	public int energyValD(){return ud.e.e;}
	public int energyValU(){return ud.e.e;}
	public void draw(Canvas cv){
		super.draw(cv);
		cv.drawRect(-1/2f,-1/8f,1/2f,1/8f,getColor(lr.e.e));
		cv.drawRect(-1/8f,-1/2f,1/8f,1/2f,getColor(ud.e.e));
	}
	public void onCircuitDestroy(int x,int y){
		new game.item.Wire().drop(x,y,2);
		World.cur.check4(x,y);
	}
	public final boolean ins(int x,int y,int v,int vs){
		WireBlock w=new WireBlock(block);
		
		w.tp=WireBlock.lr_in|WireBlock.ud_in;
		w.xe=lr.e.e;
		w.ye=ud.e.e;
		
		if(w.ins(x,y,v,vs)){
			World.cur.setCircuit(x,y,w);
			return true;
		}
		return false;
	}
}