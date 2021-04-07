package game.entity;

import util.BmpRes;
import static java.lang.Math.*;
import static util.MathUtil.*;
import game.world.World;

public class IronBall_HDX extends Agent implements AttackFilter{
private static final long serialVersionUID=1844677L;
	public IronBall_HDX(double x,double y){
		super(x,y);
	}
	private static BmpRes bmp=new BmpRes("Entity/IronBall_HDX");
	public double maxHp(){return 30000;}
	public double maxXp(){return 30000;}
	public double width(){return 0.4;}
	public double height(){return 0.4;}
	Group group(){return Group.STONE;}
	public boolean chkBlock(){return false;}//是否与方块接触
	public boolean chkRigidBody(){return false;}//是否与方块进行刚体碰撞检测
	public boolean chkAgent(){return true;}//是否与Agent接触
	public boolean chkEnt(){return true;}//是否与Entity接触
	public double mass(){return 40;}
	public double light(){return 2;}
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
	public Attack transform(Attack a){
		int t = 1;
		if(a instanceof FireAttack){
			a.val*=0.01;
		}
		return a;
	}
	int d=15,wait=0;
	Agent target=null;
	@Override
	public void ai(){
		if(target==null){
			if(wait>0){
				--wait;
				if(abs(xv)<0.1)xdir=(xv>0?1:-1);
				if(abs(yv)<0.1)ydir=(yv>0?1:-1);
				return;
			}
			d+=1;
			if(d>100)d=15;
			for(Agent a:World.cur.getNearby(x,y,d,d,false,false,true).agents){
				if(a instanceof IronBall_HDX)break;
				if(a instanceof Airship_Flank){
					Airship_Flank af=(Airship_Flank)a;
					if(af.airship!=null)a=af.airship;
				}
				//if(a==source.getSrc())continue;
				double d=distL2(a);
				if(d<8)continue;
				if(target!=null){
					if(d>distL2(target))continue;
				}
				target=a;
			}
			if(target==null){
				xdir=(xv>rnd(-0.03,0.03)?-1:1);
				ydir=(yv>rnd(-0.03,0.03)?-1:1);
			}
		}else{
			if(target.isRemoved()){
				target=null;
				d=15;
				return;
			}
			double d=hypot(x-target.x,y-target.y)+1e-8;
			if(d<5){
				target=null;
				this.d=15;
				wait=30;
				return;
			}
			double txv=target.xv,tyv=target.yv,tc=d/max(1,hypot(txv,tyv)+1e-8);
			double c=d/max(1,hypot(xv,yv)+1e-8);
			double xd=target.x+txv*tc-(x+xv*c);
			double yd=target.y+tyv*tc-(y+yv*c);
			d=hypot(xd,yd)+1e-8;
			xdir=ydir=0;
			Line.gen(x,y,x+xd,y+yd);
			if(abs(xd)>rnd(d))xdir=(xd>0?1:-1);
			if(abs(yd)>rnd(d))ydir=(yd>0?1:-1);
		}
	}
	@Override
	public double touchVal(){return 1/(1+3*(xv*xv+yv*yv));}
	@Override
	public void update(){
		super.update();
		anti_g=1;
		if(xdir!=0&&xp>=1){
			xa+=xdir*0.03;
			--xp;
		}
		if(ydir!=0&&xp>=1){
			ya+=ydir*0.03;
			--xp;
		}
		if(xv*xv+yv*yv>0.01)
		for(int i=0;i<5;++i){
			int x1=f2i(x+rnd_gaussion()),y1=f2i(y+rnd_gaussion());
			game.block.Block b=game.world.World.cur.get(x1,y1);
			if(!b.isCoverable()){
				b.fall(x1,y1,xv,yv);
				impulse(xv,yv,-1);
				//b.des(x1,y1,rf2i(Ek*30));
				//f+=0.006;
				hp-=1;
			}
		}
	}
	@Override
	void touchEnt(Entity ent){
		double k=intersection(ent)*(max(0.,v2rel(ent)-0.01)*20000);
		ent.onAttacked(k,this);
		hp-=1;
		super.touchEnt(ent);
	}
	public AttackFilter getAttackFilter(){return this;}//攻击过滤器
	public BmpRes getBmp(){return bmp;}
}
