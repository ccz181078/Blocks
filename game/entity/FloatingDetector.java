package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import game.item.*;
import util.BmpRes;

public final class FloatingDetector extends Agent implements DroppedItem.Picker{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/FloatingDetector");
	public BmpRes getBmp(){return bmp;}
	public double width(){return 0.2;}
	public double height(){return 0.2;}
	public double maxHp(){return 20;}
	Group group(){return Group.ENERGY;}
	NonOverlapItem item=new NonOverlapItem();
	public void clickAt(double tx,double ty){
		if(item.get()==null||abs(tx-x)<1e-8)return;
		double xd=tx-x,yd=ty-y;
		Item it=item.popItem();
		dir=tx>x?1:-1;
		it.onLaunch(this,(ty-y)/(tx-x),0.01);
	}
	
	@Override
	public SingleItem getCarriedItem(){
		return item;
	}
	
	@Override
	public void initUI(game.ui.UI_MultiPage ui){
		ui.addPage(new game.item.AgentMaker(FloatingDetector.class),new game.ui.UI_ItemList(item,null));
	}
	
	public double gA(){return 0;}
	
	@Override
	public void action(){
		climbable=true;
		xf+=0.02;
		yf+=0.02;
		super.action();
	}
	
	@Override
	public void pick(DroppedItem item){
		this.item.insert(item.item);
		if(item.item.isEmpty())item.remove();
	}
	
	public FloatingDetector(double _x,double _y){super(_x,_y);}
	
	void onKill(){
		DroppedItem.dropItems(item,x,y);
		super.onKill();
	}

}

