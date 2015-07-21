package bb.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.ViewFlipper;
import bb.model.ImageAdapter;
import bb.service.MusicService;
import bb.utils.AppStatic;
import bb.view.AboutView;
import bb.view.GameOverDialog;
import bb.view.GamePauseDialog;
import bb.view.GameView;
import bb.view.LoadView;
import bb.view.OptionDialog;
import bb.view.SuccessDialog;
import bb.view.WelcomeView;

public class GameActivity extends Activity implements OnLoadCompleteListener {
	
	//视图的引用 
	View currView; //记录当前显示的View
	public GameView gv; //游戏视图的引用
	WelcomeView wv; //欢迎视图的引用
	AboutView av; //关于视图的引用
	LoadView lov; //加载视图的引用
	OptionDialog od;//游戏设置对话框
	ViewFlipper viewFlipper;//滑动界面
	GameOverDialog go;//游戏结束对话框
	SuccessDialog sd;//游戏成功对话框
	Gallery g;//相册控件
	public SoundPool sndPool;//游戏音效播放引用
	private Button buttons [] = new Button[24];
	private Button back;
	private Button main_back;
	private TextView page1;
	private TextView page2;
	private TextView main_page1;
	private TextView main_page2;
	private TextView main_page3;
	private TextView main_page4;
	float my=0;  
	private int level=0;
	public int click;
	public boolean levelview=false;//选关界面激活标记
	public boolean mainlevelview=false;//选关界面激活标记
	public boolean help=false;//选关界面激活标记
	float startX; //屏幕滑动标记
	int x=0;//滑动页面个数
	private final String PREFERENCE_NAME = "SaveSetting";
	private int MODE = Context.MODE_PRIVATE  + Context.MODE_PRIVATE;
	
	//传感器
	boolean isSensor = true;  //操作方式，默认使用传感器操作
	SensorManager mySensorManager; //传感器管理
	//SensorManagerSimulator SensorManager; //连接SensorSimulator
	
	public Handler Handler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case 0:  //0为收到来着WelcomView的开始游戏命令
//				levelview=true;
//				initLevel();
				initMainlevel();
				mainlevelview=true;
				break;
			case 1:
				od =new OptionDialog(GameActivity.this);
				od.show();
				break;
			case 2:
				cleargv();
				levelview=true;
				initLevel();
				break;
			case 3:
				go=new GameOverDialog(GameActivity.this,gv);
			    go.show();
				break;
			case 4:
				startGame(AppStatic.gamemapnum);
				break;
			case 5:
				gv=new GameView(GameActivity.this);
				setContentView(gv);
				currView=gv;
				break;
			case 6:
				sd=new SuccessDialog(GameActivity.this,gv);
			    sd.show();
				break;
			case 7:
				wv.flag=false;
				wv=null;
				av=new AboutView(GameActivity.this);
				setContentView(av);
				currView=av;
				break;
			case 8:
				initLevel();
				levelview=true;
				mainlevelview=false;
				break;
			case 9:
				wv.flag=false;
				wv=null;
				setContentView(R.layout.help);
				help=true;
				break;
			}
		}
	};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		loadSharedPreferences();
		sndPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 0);
		sndPool.setOnLoadCompleteListener(this);
		click=sndPool.load(GameActivity.this,R.raw.click,1);
		AppStatic.ga=this;
		wv = new WelcomeView(this);
		setContentView(wv);
		currView = wv;
		if(isSensor)
			mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mySensorManager.unregisterListener(mySensorListener);
		mySensorManager.registerListener(			//注册监听器
				mySensorListener, 					//监听器对象
				SensorManager.SENSOR_ORIENTATION,	//传感器类型
				SensorManager.SENSOR_DELAY_UI		//传感器事件传递的频度
				);
		Intent intent=new Intent();
		intent.setClass(this, MusicService.class);
		intent.putExtra("MSG", AppStatic.PLAY_MSG);
		startService(intent);
    }
    
	@Override
	protected void onPause() {									//重写onPause方法
		super.onPause();	
		Intent intent=new Intent();
		intent.setClass(GameActivity.this, MusicService.class);
		stopService(intent);//停止音乐服务
		saveSharedPreferences();//存储游戏相关信息到本地
		System.exit(0);//退出游戏
	}
	
	@Override	
	public void onBackPressed() {								//重写onBackPressed方法
		// TODO Auto-generated method stub
		if(currView==wv&&mainlevelview==false)
		{
				AlertDialog.Builder bulider=new AlertDialog.Builder(this);
				bulider.setMessage("确定要退出游戏吗？");
				bulider.setTitle("退出");
				bulider.setPositiveButton("确定",new DialogInterface.OnClickListener() {		
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent=new Intent();
						intent.setClass(GameActivity.this, MusicService.class);
						stopService(intent);//停止音乐服务
						saveSharedPreferences();//存储游戏相关信息到本地
						System.exit(0);//退出游戏
					}
				});
				bulider.setNegativeButton("取消",new DialogInterface.OnClickListener() {		
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();//对话框消失
					}
				});
				AlertDialog alert=bulider.create();
				alert.show();
		}
		if(mainlevelview)
		{
			wv=new WelcomeView(this);
			setContentView(wv);
			currView=wv;
			mainlevelview=false;
		}
		if(levelview)
		{
			levelview=false;
			initMainlevel();
			mainlevelview=true;
		}
		if(currView==gv)
		{
			AppStatic.pause=true;
			GamePauseDialog gpd=new GamePauseDialog(this, gv);
			gpd.show();
		}
		if(currView==av)
		{
			av.flag=false;
			av=null;
			wv = new WelcomeView(this);
			setContentView(wv);
			currView = wv;
		}
		if(help)
		{
			help=false;
			wv = new WelcomeView(this);
			setContentView(wv);
			currView = wv;
		}
	}
	
	//开发实现了SensorEventListener接口的传感器监听器
	@SuppressWarnings("deprecation")
	private SensorListener mySensorListener = new SensorListener(){
		@Override
		public void onAccuracyChanged(int sensor, int accuracy) {	//重写onAccuracyChanged方法
		}
		@Override
		public void onSensorChanged(int sensor, float[] values) {	//重写onSensorChanged方法
			if(sensor == SensorManager.SENSOR_ORIENTATION){		//判断是否为姿态传感器变化产生的数据
				if(AppStatic.sensor==0&&gv!=null)//启用重力感应操作
				{
					if(values[2]<4&&values[2]>-4) AppStatic.moveState = false;
					else if(values[2]<=-4){
						AppStatic.moveState = true;
						AppStatic.forward = true;
					}
					else if(values[2]>=4){
						AppStatic.moveState = true;
						AppStatic.forward = false;
					}
				}
			}
		}
	};
	
	//滑动触摸检测
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(levelview)
		{
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startX = event.getX();
				break;
			case MotionEvent.ACTION_UP:
				if (event.getX()-startX>50) { // 向右滑动
					if(x>0)
					{
						viewFlipper.setInAnimation(this, R.anim.in_leftright);
						viewFlipper.setOutAnimation(this, R.anim.out_leftright);
						viewFlipper.showNext();
						x--;
						page1.setBackgroundResource(R.drawable.page_on);
						page2.setBackgroundResource(R.drawable.page_off);
					}
				} else if (event.getX() < startX) { // 向左滑动
					if(x<1)
					{
						viewFlipper.setInAnimation(this, R.anim.in_rightleft);
						viewFlipper.setOutAnimation(this, R.anim.out_rightleft);
						viewFlipper.showPrevious();
						x++;
						page2.setBackgroundResource(R.drawable.page_on);
						page1.setBackgroundResource(R.drawable.page_off);
					}
				}
				break;
			}
		}
		return super.onTouchEvent(event);
	}

	//初始化level界面
	public void initLevel(){
		setContentView(R.layout.level);
		page1=(TextView)findViewById(R.id.page1);
		page2=(TextView)findViewById(R.id.page2);
		x=0;
		findViews();
		unlockLevel();
		setListener();
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(AppStatic.music==0)
				{
					GameActivity.this.sndPool.play(GameActivity.this.click,1,1,0,0,(float)1);
				}
				onBackPressed();
			}
		});
		viewFlipper =(ViewFlipper) this.findViewById(R.id.viewFlipper);
	}
	private void findViews() {
		back=(Button)findViewById(R.id.back);
		buttons[0] = (Button) findViewById(R.id.button1);
		buttons[1] = (Button) findViewById(R.id.button2);
		buttons[2] = (Button) findViewById(R.id.button3);
		buttons[3] = (Button) findViewById(R.id.button4);
		buttons[4] = (Button) findViewById(R.id.button5);
		buttons[5] = (Button) findViewById(R.id.button6);
		buttons[6] = (Button) findViewById(R.id.button7);
		buttons[7] = (Button) findViewById(R.id.button8);
		buttons[8] = (Button) findViewById(R.id.button9);
		buttons[9] = (Button) findViewById(R.id.button10);
		buttons[10] = (Button) findViewById(R.id.button11);
		buttons[11] = (Button) findViewById(R.id.button12);
		buttons[12] = (Button) findViewById(R.id.button13);
		buttons[13] = (Button) findViewById(R.id.button14);
		buttons[14] = (Button) findViewById(R.id.button15);
		buttons[15] = (Button) findViewById(R.id.button16);
		buttons[16] = (Button) findViewById(R.id.button17);
		buttons[17] = (Button) findViewById(R.id.button18);
		buttons[18] = (Button) findViewById(R.id.button19);
		buttons[19] = (Button) findViewById(R.id.button20);
		buttons[20] = (Button) findViewById(R.id.button21);
		buttons[21] = (Button) findViewById(R.id.button22);
		buttons[22] = (Button) findViewById(R.id.button23);
		buttons[23] = (Button) findViewById(R.id.button24);
	}
	private void setListener(){
		for(level=0;level<=23;level++)
		{
			final int t = level;
			buttons[level].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(AppStatic.music==0)
					{
						sndPool.play(click,10,10,0,0,(float)1);
					}
					startGame(t);
				}
			});
		}
	}
	
	private void cleargv(){

		if(gv != null){
			gv.surfaceDestroyed(null);
			gv = null;
			AppStatic.clear();
		}
	}
	
	private void startGame(int i)
	{
		if(i <= AppStatic.level)
		{
			levelview=false;
			AppStatic.gamemapnum = i;
			AppStatic.gamepoints = 0;
			cleargv();
			lov=new LoadView(this);
			setContentView(lov);
			currView = lov;
		}
	}
	private void unlockLevel(){
		for(int i=0;i<=AppStatic.level;i++)
		{
			loadLevelStars(i);
			buttons[i].setTextColor(Color.RED);
			buttons[i].setText(i+1+"");
		}
	}
	public void initMainlevel(){
		setContentView(R.layout.main_level);
		wv=null;
		main_back=(Button)findViewById(R.id.main_back);
		main_page1=(TextView)findViewById(R.id.main_page1);
		main_page2=(TextView)findViewById(R.id.main_page2);
		main_page3=(TextView)findViewById(R.id.main_page3);
		main_page4=(TextView)findViewById(R.id.main_page4);
		g = (Gallery) findViewById(R.id.gallery);
		g.setAdapter(new ImageAdapter(this));
		g.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				switch(arg2)
            	{
            	case 0:
            		main_page1.setBackgroundResource(R.drawable.page_on);
            		main_page2.setBackgroundResource(R.drawable.page_off);
            		main_page3.setBackgroundResource(R.drawable.page_off);
            		main_page4.setBackgroundResource(R.drawable.page_off);
            		break;
            	case 1:
            		main_page1.setBackgroundResource(R.drawable.page_off);
            		main_page2.setBackgroundResource(R.drawable.page_on);
            		main_page3.setBackgroundResource(R.drawable.page_off);
            		main_page4.setBackgroundResource(R.drawable.page_off);
            		break;
            	case 2:
            		main_page1.setBackgroundResource(R.drawable.page_off);
            		main_page2.setBackgroundResource(R.drawable.page_off);
            		main_page3.setBackgroundResource(R.drawable.page_on);
            		main_page4.setBackgroundResource(R.drawable.page_off);
            		break;
            	case 3:
            		main_page1.setBackgroundResource(R.drawable.page_off);
            		main_page2.setBackgroundResource(R.drawable.page_off);
            		main_page3.setBackgroundResource(R.drawable.page_off);
            		main_page4.setBackgroundResource(R.drawable.page_on);
            		break;
            	}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		g.setOnItemClickListener(new  AdapterView.OnItemClickListener() {	 
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                  //just a test,u can start a game activity
            	switch(position)
            	{
            	case 0:
            		if(AppStatic.music==0)
					{
						sndPool.play(click,10,10,0,0,(float)1);
					}
            		Handler.sendEmptyMessage(8);
            		break;
            	case 1:
            		break;
            	case 2:
            		break;
            	case 3:
            		break;
            	}
            }
        });
		main_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(AppStatic.music==0)
				{
					sndPool.play(click,10,10,0,0,(float)1);
				}
				onBackPressed();
			}
		});
	}
	
	private void loadSharedPreferences(){//游戏信息从本地加载
	    SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE);
		AppStatic.level=sharedPreferences.getInt("gamelevel",0);//读取当前总关卡数
		AppStatic.music=sharedPreferences.getInt("gamemusic",0);//读取音乐是否开启标识
		AppStatic.sensor=sharedPreferences.getInt("gamesensor",0);//读取游戏操作模式
		AppStatic.help=sharedPreferences.getInt("help",0);//读取游戏操作模式
		AppStatic.Sleep_time=sharedPreferences.getInt("sleeptime",40);//读取游戏难度
	}
	
	private void loadLevelStars(int i){
		SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE);
		int level_stars=sharedPreferences.getInt(i+"level_stars",0);//读取当前总关卡数
		switch(level_stars)
		{
		case 0:buttons[i].setBackgroundResource(R.drawable.level_1);break;
		case 1:buttons[i].setBackgroundResource(R.drawable.level_1);break;
		case 2:buttons[i].setBackgroundResource(R.drawable.level_2);break;
		case 3:buttons[i].setBackgroundResource(R.drawable.level_3);break;
		}
	}

	@Override
	public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
		// TODO Auto-generated method stub);
	}
	
	 private void saveSharedPreferences(){//游戏信息本地存储
	 		SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE);
	 		SharedPreferences.Editor editor = sharedPreferences.edit();
	 		editor.putInt("gamemusic",AppStatic.music);//存储游戏音乐是否开启标识
	 		editor.putInt("gamesensor",AppStatic.sensor);//存储游戏操作方式
	 		editor.putInt("sleeptime",AppStatic.Sleep_time);//存储游戏难度
	 		editor.putInt("help",AppStatic.help);//存储游戏难度
	 		editor.commit();//写入本地
	 	}
}