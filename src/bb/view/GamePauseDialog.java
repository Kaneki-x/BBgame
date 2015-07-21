package bb.view;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import bb.activity.GameActivity;
import bb.activity.R;
import bb.utils.AppStatic;

public class GamePauseDialog extends Dialog{
	private GameView gv;
	public Button button_jixu;
	public Button button_restart;
	public Button button_level;
	public GameActivity father;
	
	public GamePauseDialog(GameActivity ga,GameView gv) {
		super(ga);
		this.father = ga;
		this.gv=gv;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//设置对话框无标题
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pause);//设置对话框布局
		button_jixu=(Button)findViewById(R.id.button_jixu);//获得按钮对象
		button_level=(Button)findViewById(R.id.button_level);
		button_restart=(Button)findViewById(R.id.button_restart);
		button_jixu.setOnClickListener(new View.OnClickListener() {//绑定监听器
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(AppStatic.music==0)
				{
					father.sndPool.play(father.click,1,1,0,0,(float)1);
				}
				button_jixu.setBackgroundResource(R.drawable.button_jixu_off);
			    AppStatic.pause=false;//开始游戏
			    GamePauseDialog.this.dismiss();//对话框消失
			}
		});
		button_level.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(AppStatic.music==0)
				{
					father.sndPool.play(father.click,1,1,0,0,(float)1);
				}
				button_level.setBackgroundResource(R.drawable.button_level_off);
			    GamePauseDialog.this.dismiss();
			    AppStatic.gv_flag = false;  //停止gameview线程
				father.Handler.sendEmptyMessage(2);
			}
		});
		button_restart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(AppStatic.music==0)
				{
					father.sndPool.play(father.click,1,1,0,0,(float)1);
				}
				button_restart.setBackgroundResource(R.drawable.button_restart_off);
		    	GamePauseDialog.this.dismiss();
				AppStatic.stop = true;
				AppStatic.clear();
				GamePauseDialog.this.gv.initcp();
				AppStatic.is_lock = false;
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.dismiss();
		AppStatic.pause=false;
	}
}
