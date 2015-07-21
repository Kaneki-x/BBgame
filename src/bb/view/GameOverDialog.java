package bb.view;


import bb.activity.GameActivity;
import bb.activity.R;
import bb.utils.AppStatic;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class GameOverDialog extends Dialog{
	private GameView gv;
	public Button button_game_over_level;
	public Button button_game_over_restart;
	public GameActivity father;
	
	public GameOverDialog(GameActivity ga,GameView gv) {
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
		setContentView(R.layout.over);//设置对话框布局
		button_game_over_level=(Button)findViewById(R.id.button_game_over_level);//获得按钮对象
		button_game_over_restart=(Button)findViewById(R.id.button_game_over_restart);
		
		button_game_over_level.setOnClickListener(new View.OnClickListener() {  //绑定监听器，返回选关
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(AppStatic.music==0)
				{
					father.sndPool.play(father.click,1,1,0,0,(float)1);
				}
				button_game_over_level.setBackgroundResource(R.drawable.game_over_level_off);
			    GameOverDialog.this.dismiss();//对话框消失
			    AppStatic.gv_flag = false;  //停止gameview线程
				father.Handler.sendEmptyMessage(2);
			}
		});
		
		button_game_over_restart.setOnClickListener(new View.OnClickListener() {  //重新开始
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(AppStatic.music==0)
				{
					father.sndPool.play(father.click,1,1,0,0,(float)1);
				}
				button_game_over_restart.setBackgroundResource(R.drawable.game_over_restart_off);
				GameOverDialog.this.dismiss();
				AppStatic.is_lock = false;
				GameOverDialog.this.gv.initcp();
			}
		});
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
	}
}
