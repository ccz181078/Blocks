package game.entity;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;

public class IronBall_HD extends IronBall{
private static final long serialVersionUID=1844677L;
	public IronBall_HD(){
		super();
		hp=3000;
	}
	protected void drop(){
		new game.item.IronBall_HD().drop(x,y);
	}
	public boolean chkBlock(){return false;}//是否与方块接触
	public boolean chkRigidBody(){return sqrt(xv*xv+yv*yv)<0.2;}//是否与方块进行刚体碰撞检测
	public boolean chkAgent(){return true;}//是否与Agent接触
	public boolean chkEnt(){return true;}//是否与Entity接触
	public double mass(){return 40;}
	public double hardness(){return game.entity.NormalAttacker.HD;}
	void onKill(){
		Source ex=SourceTool.explode(this);
		for(int i=0;i<10;++i){
			double xv=rnd(-1,1),yv=rnd(-1,1),v=sqrt(xv*xv+yv*yv)+1e-8;
			xv/=v;yv/=v;
			if(v<1){
				new Bullet(new game.item.Bullet_HD())
				.initPos(x+xv*0.2,y+yv*0.2,this.xv/5+xv,this.yv/5+yv,ex)
				.add();
			}else{
				--i;
				continue;
			}
		}
		ShockWave.explode(x,y,0,0,40,0.2,1,this);
	}
	@Override
	public void update(){
		super.update();
		if(xv*xv+yv*yv>0.04)
		for(int i=0;i<5;++i){
			int x1=f2i(x+rnd_gaussion()),y1=f2i(y+rnd_gaussion());
			game.block.Block b=game.world.World.cur.get(x1,y1);
			if(!b.isCoverable()){
				b.fall(x1,y1,xv,yv);
				impulse(xv,yv,-1);
				//b.des(x1,y1,rf2i(Ek*30));
				//f+=0.06;
				hp-=30;
			}
		}
	}
	@Override
	void touchEnt(Entity ent){
		double k=intersection(ent)*(max(0.,v2rel(ent)-0.04)*10000);
		ent.onAttacked(k,this);
		hp-=1;
		super.touchEnt(ent);
	}
	public BmpRes getBmp(){return game.item.IronBall_HD.bmp;}
}
