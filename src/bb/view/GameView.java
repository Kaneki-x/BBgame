﻿package bb.view;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import bb.activity.GameActivity;
import bb.activity.R;
import bb.model.Bullet;
import bb.model.ChackPoints;
import bb.model.Dragon;
import bb.model.Ghost;
import bb.model.Props;
import bb.model.VirtualRocker;
import bb.service.MusicService;
import bb.utils.AppStatic;

//游戏界面
public class GameView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

	private SurfaceHolder sfh;
	private Canvas canvas;
	private Paint paint;
	private Thread th;
	public GameActivity father;
	public VirtualRocker vr;//虚拟摇杆对象
	public ChackPoints cp;
	private GamePauseDialog gp;//虚拟摇杆对象
	private int j = 0;  //子弹数量
	private int shot;
	private int jump;
	private ArrayList<Bitmap> game_p = new ArrayList<Bitmap>();  //音乐图片数组
	private ArrayList<Bitmap> bulletarr_p = new ArrayList<Bitmap>();  //子弹图片数组
	public ArrayList<Bitmap> monsterarr_p = new ArrayList<Bitmap>();  //怪物图片数组
	public ArrayList<Bitmap> propsarr_p = new ArrayList<Bitmap>();  //道具图片数组
	public ArrayList<Bitmap> ghost_p = new ArrayList<Bitmap>();  //幽灵图片数组
	
	public ArrayList<Bitmap> bitDragonarr =new ArrayList<Bitmap>();  //龙图片
	
	
	long current = System.currentTimeMillis();  //去抖动
	
	public GameView(GameActivity father) {
		super(father);
		this.father=father;
		this.setKeepScreenOn(true);
		sfh = this.getHolder();
		sfh.addCallback(this);
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(30);
		paint.setColor(Color.YELLOW);
		paint.setFakeBoldText(true);
		shot=father.sndPool.load(father, R.raw.shot, 1);
		jump=father.sndPool.load(father, R.raw.jump, 1);
		vr=new VirtualRocker();
		initBitmap();  //初始化所需图片资源
		initcp(); //初始化关卡
		AppStatic.pause = true;
		startgame();
	}
	
	public void initcp(){  //初始化关卡
		cp=new ChackPoints(this);
	}
	
	public void startgame(){  //开始游戏
		AppStatic.dragon = new Dragon(bitDragonarr,vr);  //初始化龙
		AppStatic.pNum = 3;
		AppStatic.dragon.dragonthread.start();
		for(int i=0; i<AppStatic.monsterarr.size(); i++)
			AppStatic.monsterarr.get(i).monsterthread.start();
		AppStatic.pause = false;
		AppStatic.stop = false;
	}
	
	public void initBitmap(){  //初始化所需图片资源
		bulletarr_p.add(0,BitmapFactory.decodeResource(getResources(), R.drawable.p2));
		bitDragonarr.add(0,BitmapFactory.decodeResource(getResources(), R.drawable.b1));
		bitDragonarr.add(1,BitmapFactory.decodeResource(getResources(), R.drawable.b2));
		bitDragonarr.add(2,BitmapFactory.decodeResource(getResources(), R.drawable.b3));
		bitDragonarr.add(3,BitmapFactory.decodeResource(getResources(), R.drawable.b4));
		game_p.add(0,BitmapFactory.decodeResource(getResources(), R.drawable.music_on));
		game_p.add(1,BitmapFactory.decodeResource(getResources(), R.drawable.music_off));
		game_p.add(2,BitmapFactory.decodeResource(getResources(), R.drawable.pause));
		game_p.add(3,BitmapFactory.decodeResource(getResources(), R.drawable.life));
		game_p.add(4,BitmapFactory.decodeResource(getResources(), R.drawable.points));
		game_p.add(5,BitmapFactory.decodeResource(getResources(), R.drawable.game_over));
		game_p.add(6,BitmapFactory.decodeResource(getResources(), R.drawable.success));
		game_p.add(7,BitmapFactory.decodeResource(getResources(), R.drawable.help_move));
		game_p.add(8,BitmapFactory.decodeResource(getResources(), R.drawable.help_jump));
		game_p.add(9,BitmapFactory.decodeResource(getResources(), R.drawable.help_shot));
		monsterarr_p.add(0,BitmapFactory.decodeResource(getResources(), R.drawable.monster0));
		monsterarr_p.add(1,BitmapFactory.decodeResource(getResources(), R.drawable.monster1));
		monsterarr_p.add(2,BitmapFactory.decodeResource(getResources(), R.drawable.monster2));
		monsterarr_p.add(3,BitmapFactory.decodeResource(getResources(), R.drawable.monster3));
		monsterarr_p.add(4,BitmapFactory.decodeResource(getResources(), R.drawable.monster5));
		monsterarr_p.add(5,BitmapFactory.decodeResource(getResources(), R.drawable.monster6));
		monsterarr_p.add(6,BitmapFactory.decodeResource(getResources(), R.drawable.monster7));
		monsterarr_p.add(7,BitmapFactory.decodeResource(getResources(), R.drawable.monster8));
		monsterarr_p.add(8,BitmapFactory.decodeResource(getResources(), R.drawable.monster9));
		monsterarr_p.add(9,BitmapFactory.decodeResource(getResources(), R.drawable.monster11));
		monsterarr_p.add(10,BitmapFactory.decodeResource(getResources(), R.drawable.monster12));
		monsterarr_p.add(11,BitmapFactory.decodeResource(getResources(), R.drawable.monster13));
		propsarr_p.add(0,BitmapFactory.decodeResource(getResources(), R.drawable.lifeadd));
		propsarr_p.add(1,BitmapFactory.decodeResource(getResources(), R.drawable.speedup));
		propsarr_p.add(2,BitmapFactory.decodeResource(getResources(), R.drawable.speeddown));
		propsarr_p.add(3,BitmapFactory.decodeResource(getResources(), R.drawable.stop));
		ghost_p.add(0,BitmapFactory.decodeResource(getResources(), R.drawable.ghost1));
		ghost_p.add(1,BitmapFactory.decodeResource(getResources(), R.drawable.ghost2));
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {  //屏幕触摸检测
		if(!AppStatic.pause)
		{
			if(event.getX()>700&&event.getX()<744&&event.getY()>0&&event.getY()<44&&event.getAction()==MotionEvent.ACTION_UP)//游戏音效图标点击
			{
				if(AppStatic.music==0)
				{
					father.sndPool.play(father.click,1,1,0,0,(float)1);
					Intent intent=new Intent();
					intent.setClass(father, MusicService.class);
					intent.putExtra("MSG", AppStatic.STOP_MSG);
					father.startService(intent);
					AppStatic.music=1;
				}else{
					Intent intent=new Intent();
					intent.setClass(father, MusicService.class);
					intent.putExtra("MSG", AppStatic.PLAY_MSG);
					father.startService(intent);
					AppStatic.music=0;
				}
			}
			if(event.getX()>744&&event.getX()<800&&event.getY()>0&&event.getY()<44&&event.getAction()==MotionEvent.ACTION_UP)//游戏暂停图标点击
			{
				if(!AppStatic.pause)
				{
					if(AppStatic.music==0)
					{
						father.sndPool.play(father.click,1,1,0,0,(float)1);
					}
					AppStatic.pause=true;
					gp =new GamePauseDialog(father,this);
					gp.show();	
				}	
			}
			if(AppStatic.sensor==0)//重力传感器控制
			{
				int i;
				for(i=0; i<event.getPointerCount(); i++){
					if(System.currentTimeMillis()-current>800){//去抖动
						current = System.currentTimeMillis();
						if(event.getX(i) < AppStatic.map_width/2){
							if(AppStatic.JumpState==false)
							{
								if(AppStatic.music==0)
									father.sndPool.play(jump, 1,1, 0, 0, (float)1);
								AppStatic.dragon.jump();
							}
						}else{
							if((event.getX()<700)||(event.getY()>44&&event.getX()<800))
							{
								if(AppStatic.max_b > 0){
									Bullet b=new Bullet(bulletarr_p,AppStatic.gamemap, AppStatic.monsterarr);
									if(AppStatic.music==0)
									{
										father.sndPool.play(shot, 1,1, 0, 0, (float)1);
									}
									j = AppStatic.bulletarr.size();
									AppStatic.bulletarr.add(j, b);
									j++;
									AppStatic.max_b--;
								}
							}
						}
					}
				}
				
				return true;
			}
			else{//虚拟摇杆控制
				int i;
				for(i=0; i<event.getPointerCount(); i++){
					if(Math.sqrt(Math.pow((vr.shotCircleX - (int) event.getX(i)), 2) + Math.pow((vr.shotCircleY - (int) event.getY(i)), 2)) <= vr.shotCircleR&&event.getAction()==MotionEvent.ACTION_DOWN)
					{
						System.out.println("11");
						if(System.currentTimeMillis()-current > 200){//去抖动
							if(AppStatic.max_b > 0){
								Bullet b=new Bullet(bulletarr_p,AppStatic.gamemap, AppStatic.monsterarr);
								if(AppStatic.music==0)
									father.sndPool.play(shot, 1,1, 0, 0, (float)1);
								j = AppStatic.bulletarr.size();
								AppStatic.bulletarr.add(j, b);
								j++;
								AppStatic.max_b--;
							}
						}
					}
					if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
						// 当触屏区域不在一定活动范围内
						if(Math.sqrt(Math.pow((vr.RockerCircleX - (int) event.getX(i)), 2) + Math.pow((vr.RockerCircleY - (int) event.getY(i)), 2))-vr.RockerCircleR<=60&&Math.sqrt(Math.pow((vr.RockerCircleX - (int) event.getX(i)), 2) + Math.pow((vr.RockerCircleY - (int) event.getY(i)), 2))-vr.RockerCircleR>0)
						{
							float z=(float) Math.sqrt(event.getX(i)*event.getX(i)+event.getY(i)*event.getY(i));
							float sin=event.getY(i)/z;
							float cos=event.getX(i)/z;
							float x= (z+30)*cos;
							float y =(z+30)*sin;
							//得到摇杆与触屏点所形成的角度
							double tempRad = vr.getRad(vr.RockerCircleX,vr.RockerCircleY,x,y);
							//保证内部小圆运动的长度限制
							vr.getXY(vr.RockerCircleX, vr.RockerCircleY, vr.RockerCircleR, tempRad);
						} 
						else {
							//如果小球中心点小于活动区域则随着用户触屏点移动即可
							if(Math.sqrt(Math.pow((vr.RockerCircleX - (int) event.getX(i)), 2) + Math.pow((vr.RockerCircleY - (int) event.getY(i)), 2)) <=vr.RockerCircleR)
							{
								vr.SmallRockerCircleX = (int)event.getX(i);
								vr.SmallRockerCircleY = (int)event.getY(i);
							}
							//超出移动范围
							if(Math.sqrt(Math.pow((vr.RockerCircleX - (int) event.getX(i)), 2) + Math.pow((vr.RockerCircleY - (int) event.getY(i)), 2))-vr.RockerCircleR>60)
							{
								vr.SmallRockerCircleX = AppStatic.vr_x;
								vr.SmallRockerCircleY = AppStatic.vr_y;
							}
							}
						} else
							{
								if (event.getAction() == MotionEvent.ACTION_UP) {
								//当释放按键时摇杆要恢复摇杆的位置为初始位置
									vr.SmallRockerCircleX = AppStatic.vr_x;
									vr.SmallRockerCircleY = AppStatic.vr_y;
								}
							}
				}
				return true;
			}
		}
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		th = new Thread(this);
		AppStatic.gv_flag = true;
		AppStatic.pause = false;
		th.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		AppStatic.gv_flag = false;
	}

	public void drawSelf(Canvas canvas)//游戏界面图标画图
	{
		if(AppStatic.music==0)
		{
			for(int i=0;i<AppStatic.pNum;i++)
			{
				canvas.drawBitmap(game_p.get(3),i*40,0,null);
			}
			canvas.drawBitmap(game_p.get(4),125,5,null);
			canvas.drawText(AppStatic.gamepoints+"",195,30,paint);
			canvas.drawBitmap(game_p.get(0),700,0,null);
			canvas.drawBitmap(game_p.get(2),750,0,null);
		}else{
			for(int i=0;i<AppStatic.pNum;i++)
			{
				canvas.drawBitmap(game_p.get(3),i*40,0,null);
			}
			canvas.drawText(AppStatic.gamepoints+"",195,30, paint);
			canvas.drawBitmap(game_p.get(4),125,5,null);
			canvas.drawBitmap(game_p.get(1),700,0,null);
			canvas.drawBitmap(game_p.get(2),750,0,null);
		}
	}
	
	public void drawSelf(Canvas canvas, int up_y)//游戏界面图标画图
	{
		if(AppStatic.music==0)
		{
			for(int i=0;i<AppStatic.pNum;i++)
			{
				canvas.drawBitmap(game_p.get(3),i*40,0+up_y,null);
			}
			canvas.drawBitmap(game_p.get(4),125,5+up_y,null);
			canvas.drawBitmap(game_p.get(0),700,0+up_y,null);
			canvas.drawBitmap(game_p.get(2),750,0+up_y,null);
		}else{
			for(int i=0;i<AppStatic.pNum;i++)
			{
				canvas.drawBitmap(game_p.get(3),i*40,0+up_y,null);
			}
			canvas.drawBitmap(game_p.get(4),125,5+up_y,null);
			canvas.drawBitmap(game_p.get(1),700,0+up_y,null);
			canvas.drawBitmap(game_p.get(2),750,0+up_y,null);
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int up_y = 0;  //地图上移速度
		int bmpnum = 1000;  //通关或失败图片标识，1000表示无
		int props_t = 200;  //生成道具间隔
		int time_out = 1000;  //游戏超时，用来生成幽灵
		int count=0;
		boolean next = false;  //是否通关或死亡

		while(AppStatic.gv_flag){
			try {
				count++;
				canvas = sfh.lockCanvas();  //锁定
				if(AppStatic.is_lock){
					
					//道具生成
					if(props_t == 0){
						if(AppStatic.props == null){
							AppStatic.props = new Props(propsarr_p);
							props_t = 100;
						}
					}else{
						if(AppStatic.props == null)
							props_t--;
					}
					

					canvas.drawColor(Color.BLACK);  //清屏
					if(next){
						AppStatic.old_gv.drewself(canvas, up_y);   //绘制地图
						//画出游戏界面图标
						drawSelf(canvas);
					}

					
					if(!next){
						if(time_out < 0 && AppStatic.ghost == null)  //超时出现幽灵
							AppStatic.ghost = new Ghost(ghost_p);
						else
							time_out--;
						
						//绘制地图
						if(AppStatic.gamemap != null)
							AppStatic.gamemap.drewself(canvas);
						
						//画出游戏界面图标
						drawSelf(canvas);
						
						//绘制龙
						if(AppStatic.dragon != null)
							AppStatic.dragon.drawSelf(canvas);
						
						//绘制幽灵
						if(AppStatic.ghost != null)
							AppStatic.ghost.drawSelf(canvas);
						
						//绘制子弹
						for(int i=0; i<AppStatic.bulletarr.size(); i++)
							AppStatic.bulletarr.get(i).drawSelf(canvas);
						
						//绘制怪兽
						for(int j=0; j<AppStatic.monsterarr.size(); j++)
							AppStatic.monsterarr.get(j).drawSelf(canvas);
						
						//绘制道具
						if(AppStatic.props != null)
							AppStatic.props.drawself(canvas);
						
						//绘制分数
						for(int f=0; f<AppStatic.scorearr.size(); f++)
							AppStatic.scorearr.get(f).drawSelf(canvas);
					}
					if(!AppStatic.stop){
						if(AppStatic.pNum<=0){  //游戏结束
							//flag=false;
							AppStatic.stop = true;
							next = true;
							AppStatic.clear();
							bmpnum = 5;
							Timer timer=new Timer();//实例化Timer类
							timer.schedule(new TimerTask(){
							public void run(){
								   father.Handler.sendEmptyMessage(3);
							this.cancel();}},1500);//五百毫秒
						}else if(AppStatic.monsterarr.size() == 0 && AppStatic.scorearr.size()==0){  //游戏通关
							//flag=false;
							AppStatic.stop = true;
							next = true;
							AppStatic.clear();
							bmpnum = 6;
							if(count>=12000&&AppStatic.gamepoints==(AppStatic.monster_num*100))
							{
								count=0;
								AppStatic.level_stars=1;
							}
							if(count<=12000||(AppStatic.gamepoints-(AppStatic.monster_num*100)>=400))
							{
								count=0;
								AppStatic.level_stars=2;
							}
							if(count<=12000&&(AppStatic.gamepoints-(AppStatic.monster_num*100)>=400)){
								count=0;
								AppStatic.level_stars=3;
							}
							if(AppStatic.gamemapnum==AppStatic.level)
							{
								AppStatic.level++;
							}
							Timer timer=new Timer();//实例化Timer类
							timer.schedule(new TimerTask(){
							public void run(){
								   father.Handler.sendEmptyMessage(6);
							this.cancel();}},1500);//五百毫秒
						}
						if(AppStatic.help==0)
						{
							if(count>=12000/AppStatic.Sleep_time)
							{
								AppStatic.help=1;
							}
							if(count>=(1000/AppStatic.Sleep_time)&&count<=(5000/AppStatic.Sleep_time))
							{
								canvas.drawBitmap(game_p.get(7),0,0,null);
							}
							if(count>=(5500/AppStatic.Sleep_time)&&count<=(8000/AppStatic.Sleep_time))
							{
								canvas.drawBitmap(game_p.get(8),0,0,null);
							}
							if(count>=(8000/AppStatic.Sleep_time)&&count<=(11000/AppStatic.Sleep_time))
							{
								canvas.drawBitmap(game_p.get(9),420,0,null);
							}
						}
					}
					if(AppStatic.sensor==1)
						vr.drawSelf(canvas);
					//绘制游戏结束
					switch(bmpnum){
					case 5:
						canvas.drawBitmap(game_p.get(5),150,210,null);
						break;
					case 6:
						canvas.drawBitmap(game_p.get(6),155,140,null);
						break;
					default:
						break;
					}
					Thread.sleep(AppStatic.Sleep_time);
				}else{  //切换地图
					canvas.drawColor(Color.BLACK);
					AppStatic.old_gv.drewself(canvas, up_y);
					drawSelf(canvas, up_y);
					AppStatic.gamemap.drewself(canvas, up_y+AppStatic.screen_y);
					drawSelf(canvas, up_y+AppStatic.screen_y);
					up_y -= 20;
					if(0-up_y <= AppStatic.screen_y
							&& 0-up_y+20 >= AppStatic.screen_y){
						AppStatic.stop = false;
						up_y = 0;
						AppStatic.is_lock = true;
						AppStatic.pause = false;
						startgame();
						props_t = 100;
						bmpnum = 1000;
						time_out = 1000;
						next = false;
					}
					switch(bmpnum){
					case 5:
						canvas.drawBitmap(game_p.get(5),150,210+up_y,null);
						break;
					case 6:
						canvas.drawBitmap(game_p.get(6),155,170+up_y,null);
						break;
					default:
						break;
					}
					Thread.sleep(5);
				}
			}catch (Exception ex){
			}finally {
				try {
					if (canvas != null)
						sfh.unlockCanvasAndPost(canvas);
				} catch (Exception e2) {

				}
			}
		}
	}

}
