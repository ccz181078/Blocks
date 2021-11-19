package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import game.block.Block;
import game.world.World;
import graphics.Canvas;

public class FallingBlock extends Entity implements AttackFilter{
private static final long serialVersionUID=1844677L;
	Block block;
	public double hardness(){return block.hardness();}
	public double width(){return 0.5;}
	public double height(){return 0.5;}
	public double light(){return block.light();}
	public boolean chkEnt(){return true;}
	public double mass(){return block.mass();}
	@Override
	public boolean harmless(){return hypot(xv,yv)<0.1;}
	int t_new=0;
	@Override
	public String getName(){
		String s=block.getName();
		return s;
	}

	
	public FallingBlock(int _x,int _y,Block _b){
		x=_x+0.5;
		y=_y+0.5;
		xv=0;
		yv=0;
		hp=_b.getFallHp();
		block=_b;
	}
	void onKill(){
		while(rnd()<0.7)new Smoke().initPos(x+rnd_gaussion()*0.2,y+rnd_gaussion()*0.2,rnd_gaussion()*0.2,rnd_gaussion()*0.2,null);
		if(source!=null)block.src=source.getSrc();
		block.onDestroy(x,y);
		//new DroppedItem(x+rnd(-0.3,0.3),y+rnd(-0.3,0.3),block.asItem().setAmount(1)).add();
	}
	private static double rnd_e(){
		if(rnd()<0.5)return rnd(-0.5,0.5);
		return rnd()<0.5?-0.5:0.5;
	}
	public void update(){
		super.update();
		if(removed)return;
		hp-=0.1f;
		//if(f>0)ya+=gA()*1.3;
		block.onFall(this);
		double x_=x+rnd_e(),y_=y+rnd_e();
		t_new+=1;
		if(block.rootBlock() instanceof game.block.DirtType){
			y_=y-0.5;
		}
		if(t_new>10)
		if((!World.cur.get(x_,y_).isCoverable()||hp<5)&&World.cur.get(x,y).isCoverable()){
			double xp=x-floor(x);
			if(abs(xp-0.5)<0.1){
				block.src=getSrc();
				remove();
				int px=f2i(x),py=f2i(y);
				World.cur.get(px,py).onDestroy(px,py);
				World.cur.set(px,py,block);
				World.cur.check4(px,py);
			}else{
				xf+=0.1;
				if(abs(xv)<0.05){
					xa+=xp<0.5?0.01:-0.01;
					ya-=0.03;
				}
			}
		}
		if(!removed){
			double xs=0,ys=0;
			for(int xd=0;xd<=1;xd+=1){
				for(int yd=0;yd<=1;yd+=1){
					int px=f2i(x-0.5)+xd,py=f2i(y-0.5)+yd;
					Block b=World.cur.get(px,py);
					if(b.isCoverable())continue;
					double c=b.intersection(px,py,this);
					double x1=max(-0.5,px-x);
					double y1=max(-0.5,py-y);
					double x2=min(0.5,px+1-x);
					double y2=min(0.5,py+1-y);
					if(x1>=x2||y1>=y2)return;
					f+=c*0.5;
					hp-=c*0.2;
					if(b.isSolid()){
						in_wall=true;
						hp-=0.2;
					}
					c=(b.isSolid()||b instanceof game.block.SmokeBlock)?1:0.5;
					xs+=((x1*x1-x2*x2)*(y2-y1))*c;
					ys+=((y1*y1-y2*y2)*(x2-x1))*c;
				}
			}
			//if(abs(xs)<0.01&&abs(xs)>1e-8)xs=xs>0?0.01:-0.01;
			//if(abs(ys)<0.01&&abs(ys)>1e-8)ys=ys>0?0.01:-0.01;
			new SetRelPos(this,this,xs,ys);
			/*int xs=0,ys=0;
			for(int xd=-1;xd<=1;++xd){
				for(int yd=-1;yd<=1;++yd){
					Block b=World.cur.get(x+xd*0.5,y+yd*0.5);
					if(!b.isCoverable()){
						xs+=xd;ys+=yd;
					}
				}
			}
			x-=xs*rnd(0.02);
			y-=ys*rnd(0.02);*/
		}
	}
	public void draw(Canvas cv){
		block.draw(cv);
	}
	
	@Override
	public AttackFilter getAttackFilter(){return this;}//攻击过滤器
	public Attack transform(Attack a){
		if(a!=null)a.val*=0.005;
		return a;
	}
	
	@Override
	void touchEnt(Entity ent){
		double k=intersection(ent)*(max(v2rel(ent)-0.04,0)*block.impactValue());
		ent.onAttacked(k,SourceTool.impact(this),this);
		hp-=k;
		super.touchEnt(ent);
	}
	@Override
	void touchBlock(int x,int y,Block b){
		if(!b.isSolid())b.des(x,y,1,this);
	}
};

