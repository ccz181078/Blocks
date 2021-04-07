package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import game.item.*;
import util.BmpRes;

public final class FloatingDetector_5 extends Agent implements DroppedItem.Picker{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/FloatingDetector_5");
	public BmpRes getBmp(){return bmp;}
	public double width(){return 0.6;}
	public double height(){return 0.6;}
	public double maxHp(){return 100;}
	Group group(){return Group.ENERGY;}
	ItemList item=ItemList.emptyNonOverlapList(5);
	private static double xs[]={-1,-1,1,1,0},ys[]={-1,1,-1,1,0};
	public void clickAt(double tx,double ty){
		if(abs(tx-x)<1e-8)return;
		double xd=tx-x,yd=ty-y;
		int i=0;
		for(SingleItem si:item.toArray()){
			Item it=si.popItem();
			if(it!=null){
				dir=tx>x?1:-1;
				it.onLaunch(Agent.temp(x+xs[i],y+ys[i],0.01,0.01,dir,this),(ty-y)/(tx-x),0.01);
			}
			++i;
		}
		kill();
	}
	
	@Override
	public void initUI(game.ui.UI_MultiPage ui){
		ui.addPage(new game.item.AgentMaker(FloatingDetector_5.class),new game.ui.UI_ItemList(item,null));
	}
	
	@Override
	public void pick(DroppedItem item){
		this.item.insert(item.item);
		if(item.item.isEmpty())item.remove();
	}
	
	public double gA(){return 0;}
	
	@Override
	public void action(){
		climbable=true;
		xf+=0.03;
		yf+=0.03;
		super.action();
	}
	
	public FloatingDetector_5(double _x,double _y){super(_x,_y);}
	
	void onKill(){
		DroppedItem.dropItems(item,x,y);
		super.onKill();
	}

}

