package game.item;

import util.BmpRes;
import static util.MathUtil.*;
import static java.lang.Math.*;
import game.entity.*;

public class EnergyPipeline extends Pipeline_5 implements DefaultItemContainer{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Armor/EnergyPipeline");
	public BmpRes getBmp(){return bmp;}
	SpecialItem<EnergyLauncher> es[];
	public EnergyPipeline(){
		es=new SpecialItem[5];
		for(int i=0;i<5;++i)es[i]=new SpecialItem<>(EnergyLauncher.class);
	}
	@Override
	public ShowableItemContainer getItems(){
		return ItemList.create(es);
	}
	
	@Override
	public void shoot(final Human hu,final double a,final double b,Shilka ec){
		if(fire<3)return;

		if(!ec.hasEnergy(5))return;
		ec.loseEnergy(2);
		
		double x=ec.getShootX(hu,a,cnt);
		double y=ec.getShootY(hu,a,cnt);
		final EnergyLauncher ebl=es[cnt-1].get();
		if(ebl==null||!ebl.ready()||!ebl.shootCond())return;
		ebl.clickAt(x+cos(a),y+sin(a),Agent.temp(x,y,0.01,0.01,abs(a)>PI/2?-1:1,hu));
		if(ebl.isBroken()){
			ebl.onBroken(hu.x,hu.y);
			es[cnt-1].clear();
		}
		
		nextGun();
	}
	@Override
	public void test_shoot(final Human hu,final double a,final double b,Shilka ec,Item ammo){
		final double x=hu.x+1.6*cos(a)-0.15*(cnt-3)*sin(a),y=hu.y+0.15*(cnt-3)*cos(a)+1.6*sin(a)+0.23;
		final EnergyLauncher ebl=es[cnt-1].get();
		if(ebl==null||!ebl.ready()||!ebl.shootCond())return;
		ebl.clickAt(x+cos(a),y+sin(a),Agent.temp(x,y,0.01,0.01,a>PI/2?-1:1,hu));
	}
	
	@Override
	public void onUpdate(Human w,Shilka ec){
		++fire;
		int cnt=0;
		for(int i=0;i<5;++i){
			EnergyLauncher ebl=es[i].get();
			if(ebl==null)continue;
			ebl.onCarried(w);
			if(ebl.ready())cnt+=1;
		}
		ec.reload=ec.maxReload()*(cnt/5f);
	}
}

