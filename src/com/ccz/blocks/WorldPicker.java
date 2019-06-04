package com.ccz.blocks;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.util.*;
import android.graphics.drawable.*;
import java.io.*;
import java.util.*;
import game.GlobalSetting;
import game.GameSetting;

public class WorldPicker extends View{
private static final long serialVersionUID=1844677L;
	Bitmap main,set;
	int w,h,ch,x,y,chd;long tt;
	FirstActivity ctx;
	boolean chs=false;
	WorldPicker(FirstActivity con){
		super(con);
		setBackgroundColor(0);
		w=con.getWindowManager().getDefaultDisplay().getWidth();
		h=con.getWindowManager().getDefaultDisplay().getHeight();
		ctx=con;
		setKeepScreenOn(true);
		try{
			main=BitmapFactory.decodeStream(con.getAssets().open("UI/main.png"));
			set=BitmapFactory.decodeStream(con.getAssets().open("UI/set.png"));
		}catch (IOException e){e.printStackTrace();}
	}
	public boolean onTouchEvent(MotionEvent e){
		float w2=h/6f;
		switch(e.getAction()){
			case 2:
			case 0:
				if(Math.abs(e.getX()-w/2)<w2&&Math.abs(e.getY()-w2*3.3f)<w2*0.3f){
					chd=1;
				}else if(Math.abs(e.getX()-w/2)<w2&&Math.abs(e.getY()-w2*4.2f)<w2*0.3f){
					chd=2;
				}else if(Math.abs(e.getX()-w/2)<w2&&Math.abs(e.getY()-w2*5.1f)<w2*0.3f){
					chd=-1;
				}else if(e.getX()>w-w2*1.5f&&e.getX()<w-w2/2f&&e.getY()>w2*0.5f&&e.getY()<w2*1.5f){
					chd=3;
				}else chd=0;
				break;
			case 1:
				tt=0;
				chd=0;
				if(Math.abs(e.getX()-w/2)<w2&&Math.abs(e.getY()-w2*3.3f)<w2*0.3f){
					wlist();
				}else if(Math.abs(e.getX()-w/2)<w2&&Math.abs(e.getY()-w2*4.2f)<w2*0.3f){
					neww();
				}else if(Math.abs(e.getX()-w/2)<w2&&Math.abs(e.getY()-w2*5.1f)<w2*0.3f){
					more();
				}else if(e.getX()>w-w2*1.5f&&e.getX()<w-w2/2f&&e.getY()>w2*0.5f&&e.getY()<w2*1.5f){
					setting();
				}
				break;
		}
		invalidate();
		return true;
	}
	void playScreenRecord(){
		game.world.World._=null;
		GlobalSetting.playing_screen_record=true;
		ctx.startgame();
	}
	void more(){
		new AlertDialog.Builder(ctx)
		.setTitle("更多")
		.setItems(new String[]{"多人游戏","查看录像"},
		new DialogInterface.OnClickListener(){public void onClick(DialogInterface d,int w){
			switch(w){
				case 0:
					joinServer();
					break;
				case 1:
					playScreenRecord();
					break;
			}
		}})
		.setNegativeButton("取消",null)
		.show();
	}
	void setting(){
		final GlobalSetting gs=GlobalSetting.getInstance();
		final GameSetting gs2=gs.getGameSetting();
		
		final TextView tv_ui=new TextView(ctx);
		tv_ui.setText("界面设定:");
		
		final CheckBox cb_tip_des_block=new CheckBox(ctx);
		cb_tip_des_block.setText("提示破坏方块");
		cb_tip_des_block.setChecked(gs2.tip_des_block);

		final CheckBox cb_tip_place_block=new CheckBox(ctx);
		cb_tip_place_block.setText("提示放置方块");
		cb_tip_place_block.setChecked(gs2.tip_place_block);
		
		final CheckBox cb_tip_click_range=new CheckBox(ctx);
		cb_tip_click_range.setText("提示点击范围");
		cb_tip_click_range.setChecked(gs2.tip_click_range);
		
		final TextView tv_text_size=new TextView(ctx);
		tv_text_size.setText("字体大小");
		
		final SeekBar sb_text_size=new SeekBar(ctx);
		sb_text_size.setMax(100);
		sb_text_size.setProgress(Math.max(0,Math.min(100,(int)((gs2.text_size-0.1f)/(1-0.1f)*100))));

		final CheckBox cb_screen_record=new CheckBox(ctx);
		cb_screen_record.setText("开启屏幕录制");
		cb_screen_record.setChecked(gs.screen_record);
		
		final TextView tv_server=new TextView(ctx);
		tv_server.setText("服务器设定:");
		
		final CheckBox cb=new CheckBox(ctx);
		cb.setText("开启服务器");
		cb.setChecked(gs.server_on);
		
		final EditText et=new EditText(ctx);
		et.setHint("服务器默认ip");
		et.setText(gs.default_server_ip);
		
		LinearLayout lo=new LinearLayout(ctx);
		lo.setOrientation(1);
		lo.addView(tv_ui);
		lo.addView(cb_tip_des_block);
		lo.addView(cb_tip_place_block);
		lo.addView(cb_tip_click_range);
		lo.addView(tv_text_size);
		lo.addView(sb_text_size);
		
		lo.addView(cb_screen_record);
		
		lo.addView(tv_server);
		lo.addView(cb);
		lo.addView(et);
		
		ScrollView sv=new ScrollView(ctx);
		sv.addView(lo);
		new AlertDialog.Builder(ctx)
		.setTitle("设置")
		.setNegativeButton("取消",null)
		.setPositiveButton("保存",new DialogInterface.OnClickListener(){public void onClick(DialogInterface d,int w){
			gs2.tip_des_block=cb_tip_des_block.isChecked();
			gs2.tip_place_block=cb_tip_place_block.isChecked();
			gs2.tip_click_range=cb_tip_click_range.isChecked();
			gs2.text_size=sb_text_size.getProgress()/100f*(1-0.1f)+0.1f;
			gs.screen_record=cb_screen_record.isChecked();
			gs.server_on=cb.isChecked();
			gs.default_server_ip=et.getText().toString();
			gs.save();
		}})
		.setView(sv)
		.show();
	}
	void neww(){
		LinearLayout lo=new LinearLayout(ctx);
		lo.setOrientation(1);
		TextView tv=new TextView(ctx);
		tv.setText("世界名称:");
		lo.addView(tv);
		
		final EditText et=new EditText(ctx);
		int i=1;
		File f;
		for(;;){
			f=new File("/sdcard/Blocks/世界"+i+"/World.dat");
			if(!f.isFile())break;
			i++;
		}
		et.setText("世界"+i);
		lo.addView(et);
		
		final RadioGroup rg=new RadioGroup(ctx);
		final RadioButton rb1=new RadioButton(ctx);
		rb1.setText("创造模式");
		rg.addView(rb1);
		final RadioButton rb2=new RadioButton(ctx);
		rb2.setText("生存模式");
		rg.addView(rb2);
		lo.addView(rg);
		rb2.setChecked(true);
		
		new AlertDialog.Builder(ctx)
		.setTitle("新建世界")
		.setNegativeButton("取消",null)
		.setPositiveButton("创建",new DialogInterface.OnClickListener(){public void onClick(DialogInterface d,int w){
			String s=et.getText().toString();
			if(s.length()==0||s.indexOf('.')>-1||s.indexOf('/')>-1||s.indexOf("\n")>-1){
				Toast.makeText(ctx,"名称不能为空或含\"/\",\".\"或换行",0).show();
				neww();
				return;
			}
			File f=new File("/sdcard/Blocks/"+s+"/World.dat");
			if(f.isFile()){
				Toast.makeText(ctx,"已经存在",0).show();
				neww();
				return;
			}
			try{
				game.world.World.init("/sdcard/Blocks/"+s+"/",rg.getCheckedRadioButtonId()==rb1.getId());
				GlobalSetting.playing_screen_record=false;
				ctx.startgame();
			}catch(Exception e){
				Toast.makeText(ctx,"failed to init new world",0).show();
				e.printStackTrace();
			}
		}})
		.setView(lo)
		.show();
	}
	void delw(final String s){
		new AlertDialog.Builder(ctx)
		.setTitle("删除世界")
		.setMessage("确定删除世界\""+s+"\"?此操作不可恢复，删除后将丢失这个世界的所有信息。")
		.setNegativeButton("取消",null)
		.setPositiveButton("删除",new DialogInterface.OnClickListener(){public void onClick(DialogInterface d,int w){
			File f=new File("/sdcard/Blocks/"+s+"/World.dat");
			f.delete();
			wlist();
		}})
		.show();
	}
	void joinServer(){
		final EditText ip_addr=new EditText(ctx);
		ip_addr.setHint("ip");
		final EditText user_name=new EditText(ctx);
		user_name.setHint("用户名");
		user_name.setText("user0");
		final EditText password=new EditText(ctx);
		password.setHint("密码");
		password.setText("123456");
		LinearLayout lo=new LinearLayout(ctx);
		lo.setOrientation(1);
		lo.addView(ip_addr);
		lo.addView(user_name);
		lo.addView(password);
		
		new AlertDialog.Builder(ctx)
		.setTitle("加入多人游戏")
		.setView(lo)
		.setNegativeButton("取消",null)
		.setPositiveButton("连接",new DialogInterface.OnClickListener(){public void onClick(DialogInterface d,int w){
			String ip=ip_addr.getText().toString();
			String un=user_name.getText().toString();
			String pw=password.getText().toString();
			try{
				game.socket.Client.cur=new game.socket.Client(ip,un,pw);
			}catch(Exception e){
				e.printStackTrace();
				Toast.makeText(ctx,"输入有误",0).show();
				return;
			}
			game.world.World._=null;
			GameView.ni=null;
			GlobalSetting.playing_screen_record=false;
			ctx.startgame();
		}})
		.show();
	}
	PopupWindow pw=null;
	void wlist(){
		try{
		File f1=new File("/sdcard/Blocks/");
		if(!f1.isDirectory())f1.mkdirs();
		File[] fs=f1.listFiles();
		final ArrayList<WorldInfo> ws=new ArrayList<>();
		for(File f:fs){
			if(f.isDirectory()){
				String name=f.getName();
				f=new File("/sdcard/Blocks/"+name+"/World.dat");
				if(f.isFile())ws.add(new WorldInfo(name,f.lastModified()));
			}
		}
		if(ws.size()==0){
			neww();
			return;
		}
		Collections.sort(ws);
		ArrayList<String> s=new ArrayList<>();
		for(WorldInfo w:ws)s.add(w.toString());
		ArrayAdapter aa=new ArrayAdapter(ctx,android.R.layout.simple_list_item_1,s);
		ListView lv=new ListView(ctx);
		lv.setAdapter(aa);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			public void onItemClick(AdapterView<?> a,View v,int w,long l){
				pw.dismiss();
				try{
					game.world.World.restore("/sdcard/Blocks/"+ws.get(w).name+"/");
					GlobalSetting.playing_screen_record=false;
					ctx.startgame();
				}catch(Exception e){
					Toast.makeText(ctx,"读取存档失败",0).show();
					debug.Log.i(e);
				}
			}
		});
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
			public boolean onItemLongClick(AdapterView<?> a,View v,int w,long l){
				pw.dismiss();
				delw(ws.get(w).name);
				return true;
			}
		});
		pw=new PopupWindow(ctx);
		pw.setWidth((int)(w*0.8f));
		pw.setHeight((int)(h*0.8f));
		pw.setBackgroundDrawable(new ColorDrawable(0xff444444));
		pw.setContentView(lv);
		pw.setFocusable(true);
		pw.showAtLocation(this,Gravity.CENTER,0,0);
		}catch(Exception err){err.printStackTrace();}
	}
	protected void onDraw(Canvas c){
		Paint p=new Paint();
		p.setTextSize(h/16);
		p.setColor(0xffffffff);
		Rect r=new Rect();
		r.set(0,0,main.getWidth(),main.getHeight());
		c.drawBitmap(main,r,new RectF(0,0,w,h),p);
		//c.drawText(new String(new char[]{'1','.','0','.','0'}),h/16f,h/10f,p);
		//c.drawText(new String(new char[]{'b','y',' ','c','c','z','1','8','1','0','7','8'}),h/16f,h/6f,p);
		r.set(0,0,48,16);
		float w2=h/6f,w1=(w-h)/2f;
		p.setColor(chd==1?0xffaaaaaa:0xff969696);
		c.drawRect(w/2-w2,w2*3f,w/2+w2,w2*3.6f,p);
		p.setColor(chd==2?0xffaaaaaa:0xff969696);
		c.drawRect(w/2-w2,w2*3.9f,w/2+w2,w2*4.5f,p);
		p.setColor(chd==-1?0xffaaaaaa:0xff969696);
		c.drawRect(w/2-w2,w2*4.8f,w/2+w2,w2*5.4f,p);
		p.setColor(0xffffffff);
		p.setTextSize(w2*0.3f);
		p.setTextAlign(Paint.Align.CENTER);
		c.drawText("开始",w/2,w2*3.4f,p);
		c.drawText("新建世界",w/2,w2*4.3f,p);
		c.drawText("更多",w/2,w2*5.2f,p);
		r.set(0,0,16,16);//多人游戏
		c.drawBitmap(set,r,new RectF(w-w2*1.5f,w2*0.5f,w-w2/2f,w2*1.5f),p);
		if(chd==3){
			p.setColor(0x22ffffff);
			c.drawRect(new RectF(w-w2*1.5f,w2*0.5f,w-w2/2f,w2*1.5f),p);
		}
	}
}
class WorldInfo implements Comparable{
	private static final long serialVersionUID=1844677L;
	String name;
	long time;
	WorldInfo(String s,long t){
		name=s;
		time=t;
	}
	public int compareTo(Object o){
		long wtime=((WorldInfo)o).time;
		if(time==wtime)return 0;
		return time>wtime?-1:1;
	}
	public String toString(){
		Calendar c=Calendar.getInstance();
		c.setTimeInMillis(time);
		return name+"\n"+c.getTime().toLocaleString();
	}
}
