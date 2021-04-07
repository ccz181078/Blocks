package game.ui;

public interface BlockWithUI{
	//一个带有用户的方块如果实现这个接口，在onClick时将自动打开用户界面
	public UI getUI(game.block.BlockAt ba);
}
