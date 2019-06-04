package game;

import java.io.Serializable;

public class GameSetting implements Serializable{
	//游戏设定中，在联机时需要发送给服务器的部分
	public boolean
		tip_des_block=true,//提示破坏方块
		tip_place_block=true,//提示放置方块
		tip_click_range=true;//提示点击范围
	public float text_size=0.2f;//字体相对于方块的大小
}
