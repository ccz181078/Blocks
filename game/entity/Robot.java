package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import game.item.*;
import util.BmpRes;

public final class Robot extends Agent implements DroppedItem.Picker{
	private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Entity/Robot");
	public BmpRes getBmp(){return bmp;}
	public double width(){return 0.5;}
	public double height(){return 0.5;}
	public double maxHp(){return 240;}
	Group group(){return Group.PLAYER;}
	public SelectableItemList items=ItemList.emptySelectableList(16);
	public double hardness(){return game.entity.NormalAttacker.IRON;}
	
	public void clickAt(double tx,double ty){
		int px=f2i(tx),py=f2i(ty);
		Item it=items.getSelected().popItem();
		if(it!=null)it=it.clickAt(tx,ty,this);
		items.getSelected().insert(it);
	}
	
	@Override
	public SingleItem getCarriedItem(){
		return items.getSelected();
	}
	
	@Override
	public void initUI(game.ui.UI_MultiPage ui){
		ui.addPage(new game.item.AgentMaker(Robot.class),new game.ui.UI_ItemList(items,null));
	}
	
	@Override
	public AttackFilter getAttackFilter(){
		return new AttackFilter(){public Attack transform(Attack a){
			if(a!=null){
				if(a instanceof NormalAttack)a.val*=0.2;
				Item it=items.popItem();
				if(it!=null&&it instanceof Shield)a=((Shield)it).transform(a);
				items.getSelected().insert(it);
			}
			return a;
		}};
	}
	
	@Override
	public void pick(DroppedItem item){
		items.insert(item.item);
		if(item.item.isEmpty())item.remove();
	}
	
	@Override
	public double getJumpAcc(){
		return super.getJumpAcc()*1.3;
	}
	@Override
	double friction(){return 0.4;}
	
	@Override
	public void update(){
		super.update();
		
		addHp(0.005);
		
		Item w=items.popItem();
		if(w!=null)w.onCarried(this);
		if(w instanceof Tool){
			Tool u=(Tool)w;
			if(u.isBroken()){
				u.onBroken(x+dir*width(),y);
				w=null;
			}
		}
		items.getSelected().insert(w);
	}
	
	@Override
	public double onImpact(double v){
		return max(0,v-5)*0.2;
	}
	
	public Robot(double _x,double _y){super(_x,_y);}
	
	void onKill(){
		DroppedItem.dropItems(items,x,y);
		super.onKill();
	}
	
	@Override
	public void touchAgent(Agent a){
		a.onAttacked(max(0,v2rel(a)-0.04)*30,this);
	}
}

