package bb.view;

import bb.activity.GameActivity;
import bb.activity.R;
import bb.service.MusicService;
import bb.utils.AppStatic;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

//游戏选项设置界面
public class OptionDialog extends Dialog{
	private GameActivity ga;
	private RadioGroup sensor_group=null;
    private RadioButton sensor_1=null;
    private RadioButton sensor_2=null;
    private RadioGroup music_group=null;
    private RadioButton music_1=null;
    private RadioButton music_2=null;
    private RadioGroup level_group=null;
    private RadioButton level_1=null;
    private RadioButton level_2=null;
    private RadioButton level_3=null;
    
	public OptionDialog(GameActivity ga) {
		super(ga);
		this.ga=ga;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.option);
		setTitle("游戏设置");//设置标题
		setCanceledOnTouchOutside(true);
		sensor_group=(RadioGroup)findViewById(R.id.sensor_radiogroup);
		sensor_1=(RadioButton)findViewById(R.id.sensor_1);
		sensor_2=(RadioButton)findViewById(R.id.sensor_2);
		music_group=(RadioGroup)findViewById(R.id.music_radiogroup);
		music_1=(RadioButton)findViewById(R.id.music_1);
		music_2=(RadioButton)findViewById(R.id.music_2);
		level_group=(RadioGroup)findViewById(R.id.level_radiogroup);
		level_1=(RadioButton)findViewById(R.id.level_1);
		level_2=(RadioButton)findViewById(R.id.level_2);
		level_3=(RadioButton)findViewById(R.id.level_3);
		switch(AppStatic.sensor)
		{
			case 0:sensor_1.setChecked(true);break;
			case 1:sensor_2.setChecked(true);break;
		}
		switch(AppStatic.music)
		{
			case 0:music_1.setChecked(true);break;
			case 1:music_2.setChecked(true);break;
		}
		switch(AppStatic.Sleep_time)
		{
			case 50:level_1.setChecked(true);break;
			case 40:level_2.setChecked(true);break;
			case 30:level_3.setChecked(true);break;
		}
		setListener();//为单选按钮绑定监听器
		
	}
	private void setListener(){
		sensor_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(sensor_1.getId()==checkedId)
				{
					AppStatic.sensor=0;
				}
				if(sensor_2.getId()==checkedId)
				{
					AppStatic.sensor=1;
				}
			}
		});
		music_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(music_1.getId()==checkedId)
				{
					AppStatic.music=0;
					Intent intent=new Intent();
					intent.setClass(ga, MusicService.class);
					intent.putExtra("MSG", AppStatic.PLAY_MSG);
					ga.startService(intent);
				}
				if(music_2.getId()==checkedId)
				{
					AppStatic.music=1;
					Intent intent=new Intent();
					intent.setClass(ga, MusicService.class);
					intent.putExtra("MSG", AppStatic.STOP_MSG);
					ga.startService(intent);
				}
			}
		});
		level_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(level_1.getId()==checkedId)
				{
					AppStatic.Sleep_time=50;
				}
				if(level_2.getId()==checkedId)
				{
					AppStatic.Sleep_time=40;
				}
				if(level_3.getId()==checkedId)
				{
					AppStatic.Sleep_time=30;
				}
			}
		});
	}

}
