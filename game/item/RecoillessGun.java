package game.item;

import static java.lang.Math.*;
import static util.MathUtil.*;
import util.BmpRes;
import game.entity.*;
import game.block.Block;

public class RecoillessGun extends RPGLauncher{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/RecoillessGun");
	public BmpRes getBmp(){return bmp;}
	SpecialItem<Block> block=new SpecialItem<>(Block.class);
	
	int extra_energy=0;
	public double mv2(){return E0*(5+extra_energy);}
	public boolean onLongPress(Agent a,double tx,double ty){
		if(hasEnergy(energyCost()+10)&&extra_energy<=10000){
			extra_energy+=5;
			loseEnergy(10);
		}
		return true;
	}
	public void drawInfo(graphics.Canvas cv){
		super.drawInfo(cv);
		double c=sqrt(extra_energy/10000.);
		game.ui.UI.drawProgressBar(cv,0xff00ffff,0,(float)c,-0.4f,-0.2f,0.4f,-0.13f);
	}
	
	@Override
	public Item clickAt(double x,double y,Agent a){
		double xm=a.xa,ym=a.ya,m=a.mass();
		super.clickAt(x,y,a);
		damage+=f2i(extra_energy/10.);
		extra_energy=0;
		xm-=a.xa;ym-=a.ya;
		if(abs(xm)>1e-16){
			double p=hypot(xm,ym)*m,mv2=p*p*(1+1/m);
			int cost=rf2i(mv2)+1;
			if(hasEnergy(cost)){
				loseEnergy(cost);
				Block b=block.popItem();
				if(b!=null){
					a.dir*=-1;
					b.onLaunch(a,ym/xm,mv2);
					a.dir*=-1;
				}
			}
		}
		return this;
	}

	@Override
	public boolean autoUse(Human h,Agent a){
		if(block.isEmpty()){
			SingleItem ss[]=h.items.toArray();
			for(int t=0;t<30;++t){
				int id=rndi(0,ss.length-1);
				SingleItem si=ss[id];
				if(si.get() instanceof Block){
					block.insert(si);
					break;
				}
			}
		}
		return super.autoUse(h,a);
	}
	
	/*@Override
	public void drawTip(graphics.Canvas cv,Player pl){
		if(!hasEnergy(energyCost())||rpg.isEmpty())return;
		float tx=pl.action.tx,ty=pl.action.ty;
		cv.drawLines(new float[]{(float)(pl.width()*pl.dir),0.2f,tx,ty},0xff|pl.press_t<<24);
	}*/
	public ShowableItemContainer getItems(){
		return ItemList.create(ec,block,rpg);
	}
};
