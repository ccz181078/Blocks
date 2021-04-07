package game.item;

import game.entity.Human;
import game.entity.Player;
import util.BmpRes;

public interface DefaultItemContainer extends ItemContainer{
    default public SingleItem[] toArray(){return getItems().toArray();}
    default public void insert(SingleItem it){getItems().insert(it);}
    public ShowableItemContainer getItems();
    default public BmpRes getUseBmp(){return Item.use_btn;}
    default public void onUse(Human a){
        a.items.getSelected().insert((Item)this);
        if((a instanceof Player)){
            ((Player)a).openDialog(new game.ui.UI_Item((Item)this,getItems()));
        }
    }
}