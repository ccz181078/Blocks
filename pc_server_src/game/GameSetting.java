package game;

import java.io.*;

public class GameSetting implements Serializable{
	//private static final long serialVersionUID=1844677L;
	//游戏设定中，在联机时需要发送给服务器的部分
	public boolean
		show_cursor=true,//显示光标
		show_ui=true,//显示用户界面
		tip_des_block=true,//提示破坏方块
		tip_place_block=true,//提示放置方块
		tip_click_range=true;//提示点击范围
	public float text_size=0.2f;//字体相对于方块的大小
	public String player_name="player";
	private void readObject(ObjectInputStream in) throws IOException,ClassNotFoundException{ 
		in.defaultReadObject();
		if(text_size==0)text_size=0.2f;
		if(player_name==null)player_name="player";
	}
}
