package debug.script;

import android.widget.*;
import android.content.*;
import android.app.AlertDialog;
import android.text.style.*;
import android.text.*;
import static java.lang.Math.*;
import android.graphics.Typeface;
import debug.ObjectNode;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.*;
import com.ccz.blocks.MainActivity;
import android.view.View.OnClickListener;
import com.ccz.blocks.R;
import android.text.method.KeyListener;

public final class ScriptEditor{
	private int upd=0,upd_t=0;
	private TextView tv;
	private EditText et;
	private Button run_btn,save_btn,par_btn,exit_btn;
	private void setClickable(Button b,boolean t){
		b.setClickable(t);
		b.setTextColor(t?0xffffffff:0xffaaaaaa);
	}
	private void update(){
		if(upd>0)return;
		++upd;
		++upd_t;
		int sel_l=et.getSelectionStart(),sel_r=et.getSelectionEnd();
		ScriptException se=null;
		try{
			Editable ea=et.getEditableText();
			ea.clearSpans();
			try{
				Script.compile("(\n"+ea.toString()+"\n)\n");
				setClickable(run_btn,true);
			}catch(ScriptException _se){
				se=_se;
				setClickable(run_btn,false);
			}catch(Exception e){
				se=new ScriptException(0,0,"compile time exception:"+e);
				debug.Log.i(e);
				setClickable(run_btn,false);
			}
			int L=2,R=Script.script_src.length-3;
			//com.ccz.blocks.MainActivity.showText(sel_l+","+sel_r);
			for(int i=L,j=L;i<R;i=j){
				int tp=Script.script_type[i];
				for(++j;j<R&&Script.script_type[j]==tp;++j);
				ea.setSpan(new ForegroundColorSpan(Script.text_type_2_color[tp]),max(i,L)-L,min(j,R)-L,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			if(se!=null){
				int seL=se.L,seR=se.R+1;
				if(seL>=seR)seL=seR-1;
				if(seL>=R-1)seL=R-2;
				if(seR<=L+1)seR=L+2;
				ea.setSpan(new BackgroundColorSpan(0x5fff0000),max(seL,L)-L,min(seR,R)-L,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				String str=upd_t+":";
				if(se.e!=null)str+=se.e;
				else str+=se.info;
				tv.setTextColor(0xffff0000);
				tv.setText(str);
			}else{
				tv.setTextColor(0xff00a000);
				tv.setText(upd_t+":"+"no error");
			}
			et.setText(ea);
			et.setSelection(sel_l,sel_r);
			setClickable(save_btn,true);
		}catch(Exception e){
			debug.Log.i(e);
		}
		--upd;
	}
	private void save(){
		String s=et.getText().toString();
		try{
			util.SerializeUtil.writeStringToFile(s,debug.Log.log_path+"script.txt");
			setClickable(save_btn,false);
		}catch(Exception e){debug.Log.i(e);}
	}
	public void show(){
		final MainActivity ctx=MainActivity._this;
		if(ctx==null)return;
		//Display di=ctx.getWindowManager().getDefaultDisplay();
		//int W=di.getWidth(),H=di.getHeight();
		//final PopupWindow pw=new PopupWindow(ctx);
		ctx.setContentView(R.layout.main);
		//View main=LayoutInflater.from(ctx).inflate(R.layout.main,lo);
		tv=(TextView)ctx.findViewById(R.id.mainTextView);
		et=(EditText)ctx.findViewById(R.id.mainEditText);
		run_btn=(Button)ctx.findViewById(R.id.runButton);
		save_btn=(Button)ctx.findViewById(R.id.saveButton);
		par_btn=(Button)ctx.findViewById(R.id.parenthesisButton);
		exit_btn=(Button)ctx.findViewById(R.id.exitButton);
		
		setClickable(run_btn,true);
		setClickable(save_btn,false);
		setClickable(par_btn,true);
		setClickable(exit_btn,true);
		
		StringBuffer sb=new StringBuffer();
		try{
			util.SerializeUtil.readStringFromFile(sb,debug.Log.log_path+"script.txt");
		}catch(Exception e){debug.Log.i(e);}
		et.addTextChangedListener(new TextWatcher(){
			public void beforeTextChanged(CharSequence p1,int p2,int p3,int p4){}
			public void onTextChanged(CharSequence p1,int p2,int p3,int p4){}
			public void afterTextChanged(Editable p1){
				update();
			}
		});
		et.setText(sb.toString());
		
		run_btn.setOnClickListener(new OnClickListener(){
			public void onClick(View p1){
				save();
				ctx.setContentView(ctx.rl);
				Script.run();
			}
		});
		save_btn.setOnClickListener(new OnClickListener(){
			public void onClick(View p1){
				save();
			}
		});
		par_btn.setOnClickListener(new OnClickListener(){
			public void onClick(View p1){
				Editable ea=et.getEditableText();
				int L=et.getSelectionStart(),R=et.getSelectionEnd();
				ea.replace(L,R,"()");
				et.setSelection(L+1);
			}
		});
		exit_btn.setOnClickListener(new OnClickListener(){
			public void onClick(View p1){
				ctx.setContentView(ctx.rl);
			}
		});
	}
}
