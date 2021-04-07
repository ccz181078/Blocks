package game.item;

import util.BmpRes;

public class ItemSlot extends Item{
private static final long serialVersionUID=1844677L;
	static BmpRes bmp=new BmpRes("Item/ItemSlot");
	public BmpRes getBmp(){return bmp;}
	public void onUse(game.entity.Human a){//按下使用按钮
		if(a.items.unlock());
		else if(a.bag_items.unlock());
		else a.items.getSelected().insert(this);
	}
	public BmpRes getUseBmp(){//获取使用按钮的贴图
		return eat_btn;
	}
};
