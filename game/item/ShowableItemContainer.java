package game.item;

public interface ShowableItemContainer extends ItemContainer{
	public void draw(graphics.Canvas c,SingleItem si,int flag);
	public void onClick(game.entity.Player pl,SingleItem s,ItemContainer ic);
	public boolean dragable();
}
