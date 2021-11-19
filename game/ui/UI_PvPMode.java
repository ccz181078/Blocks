package game.ui;

import game.entity.Player;
import graphics.Canvas;
import static game.item.Craft.*;
import game.item.ItemList_PvPMode;

public class UI_PvPMode extends UI_AllItem{
	public UI_PvPMode(){
		super(x->new ItemList_PvPMode(x));
	}
}