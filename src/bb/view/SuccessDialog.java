package bb.view;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import bb.activity.GameActivity;
import bb.activity.R;
import bb.utils.AppStatic;

public class SuccessDialog extends Dialog{
	private GameView gv;
	public Button game_success_next;
	public Button game_success_restart;
	public Button game_success_level;
	public Button button[] = new Button[3];
	public TextView game_points;
	public GameActivity father;
	private final String PREFERENCE_NAME = "SaveSetting";
	private int MODE = Context.MODE_PRIVATE  + Context.MODE_PRIVATE;

	public SuccessDialog(GameActivity ga,GameView gv) {
		super(ga);
		this.father = ga;
		this.gv=gv;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//设置对话框无标题
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.success);//设置对话框布局
		saveSharedPreferences();
		game_success_next=(Button)findViewById(R.id.game_success_next);//获得按钮对象
		game_success_restart=(Button)findViewById(R.id.game_success_restart);
		game_success_level=(Button)findViewById(R.id.game_success_level);
		button[0]=(Button)findViewById(R.id.button1);
		button[1]=(Button)findViewById(R.id.button2);
		button[2]=(Button)findViewById(R.id.Button01);
		switch(AppStatic.level_stars)
		{
		case 1:
			button[1].setBackgroundResource(R.drawable.dark_ball);
			button[2].setBackgroundResource(R.drawable.dark_ball);
			break;
		case 2:
			button[2].setBackgroundResource(R.drawable.dark_ball);
			break;
		}
		game_points=(TextView)findViewById(R.id.points);
		game_points.setTextColor(Color.RED);
		game_points.setTextSize(25);
		game_points.setText(AppStatic.gamepoints+"");
		game_success_next.setOnClickListener(new View.OnClickListener() {//绑定监听器
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(AppStatic.music==0)
				{
					father.sndPool.play(father.click,1,1,0,0,(float)1);
				}
				game_success_next.setBackgroundResource(R.drawable.game_success_next_off);//更改按钮背景
				SuccessDialog.this.dismiss();//对话框消失
				AppStatic.gamemapnum++;
				AppStatic.gamepoints=0;
				SuccessDialog.this.gv.initcp();
				AppStatic.is_lock = false;
			}
		});
		game_success_level.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(AppStatic.music==0)
				{
					father.sndPool.play(father.click,1,1,0,0,(float)1);
				}
				game_success_level.setBackgroundResource(R.drawable.game_success_level_off);
			    SuccessDialog.this.dismiss();
			    AppStatic.gv_flag = false;  //停止gameview线程
			    father.Handler.sendEmptyMessage(2);
			}
		});
		game_success_restart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(AppStatic.music==0)
				{
					father.sndPool.play(father.click,1,1,0,0,(float)1);
				}
				game_success_restart.setBackgroundResource(R.drawable.game_success_restart_off);
				AppStatic.gamepoints=0;
				SuccessDialog.this.dismiss();
				SuccessDialog.this.gv.initcp();
				AppStatic.is_lock = false;
			}
		});
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	}
	 private void saveSharedPreferences(){
 		SharedPreferences sharedPreferences = AppStatic.ga.getSharedPreferences(PREFERENCE_NAME, MODE);
 		SharedPreferences.Editor editor = sharedPreferences.edit();
 		editor.putInt(AppStatic.gamemapnum+"",AppStatic.gamemapnum);
 		editor.putInt("gamelevel",AppStatic.level);
 		editor.putInt(AppStatic.gamemapnum+"points",AppStatic.gamepoints);
 		editor.putInt(AppStatic.gamemapnum+"level_stars",AppStatic.level_stars);
 		editor.commit();
 	}
}
