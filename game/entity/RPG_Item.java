package game.entity;

import static java.lang.Math.*;
import static util.MathUtil.*;
import graphics.Canvas;
import game.world.World;
import game.block.Block;
import game.item.NonOverlapSpecialItem;
import util.BmpRes;
import game.item.Item;

public class RPG_Item extends RPG_Small_Guided{
	private static final long serialVersionUID=1844677L;
	public BmpRes getBmp(){return rpg.getBmp();}
	NonOverlapSpecialItem<Item> item=new NonOverlapSpecialItem<>(Item.class);
	public RPG_Item(game.item.RPG_Item a){
		super(a);
		if(!Entity.is_test)item.insert(a.item);
	}
	@Override
	double _fc(){return 0.01;}
	@Override
	void touchEnt(Entity ent){}
	@Override
	public void update(){
		super.update();
		if(!item.isEmpty()){
			ThrowedItem e=new ThrowedItem(0,0,item.get());
			e.initPos(x+ax*0.75,y+ay*0.75,xv,yv,SourceTool.make(this,"携带的"));
			e.des_flag=true;
			e.attacking=true;
			e.update0();
			e.update();
			e.anti_g=1;
			e.move();
			impulseMerge(e);
			if(!e.shadowed)shadowed=false;
			if(item.get().isBroken()){
				item.popItem().onBroken(x+ax*0.75,y+ay*0.75);
			}
		}
	}
	@Override
	public void draw(graphics.Canvas cv){
		super.draw(cv);
		if(!item.isEmpty()){
			cv.save();
			cv.translate((float)(ax*0.75),(float)(ay*0.75));
			new ThrowedItem(x,y,item.get()).draw(cv);
			cv.restore();
		}
	}
	@Override
	public void onKill(){
		super.onKill();
		DroppedItem.dropItems(item,x,y);
	}
	@Override
	public void explode(){
		Spark.explode_adhesive(x,y,xv,yv,fuel/16,0.1,4,this);
		kill();
	}
}
