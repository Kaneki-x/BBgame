package bb.utils;

import java.util.ArrayList;

import bb.activity.GameActivity;
import bb.model.Bullet;
import bb.model.Dragon;
import bb.model.GameMap;
import bb.model.Ghost;
import bb.model.Monster;
import bb.model.Props;
import bb.model.Score;

public class AppStatic {
	
	//系统参数
	public static boolean is_lock = true;  //是否锁定
	public static boolean stop = false;  //停止检测死亡或通关
	public static boolean gv_flag = false; //gameview线程是否开始
	public static int help = 0; //gameview线程是否开始


	public static GameMap old_gv = null;
	public static int screen_x = 800;  //屏幕宽度
	public static int screen_y = 480;  //屏幕高度
	public static int sensor = 1;  //游戏控制方式：0、传感器控制；1、模拟摇杆控制
	public static int vr_x = 100;  //虚拟摇杆x坐标
	public static int vr_y = 330;  //虚拟摇杆y坐标
	public static int vr_bx = 720;  //虚拟按钮x坐标
	public static int vr_by = 330;  //虚拟按钮y坐标
	public static int music=0; //背景音乐状态：0、开启；1、关闭
	public static int level=11; //游戏当前开启关卡数
	public static boolean weclome=false;//欢迎界面载入
	public static boolean pause=false;  //游戏是否暂停
	public static final int PLAY_MSG=1; //开启背景音乐
	public static final int STOP_MSG=2; //关闭背景音乐
	public static final int PLAY_SEN=1;  //开启传感器
	public static final int STOP_SEN=2;  //关闭传感器
	public static GameActivity ga=null;  //主activity
	public static int Sleep_time = 30;  //线程休眠时间
	//public static GameView gv=null;
	
	//地图参数
	public static int gamemapnum = 0;  //地图标识
	public static int gamepoints=0;
	public static GameMap gamemap = null;  //当前使用地图
	public static final int map_width = 800;  //地图宽度
	public static final int map_height = 455;  //地图高度
	public static final int border_w = 5;  //地图边缘厚度
	public static int baffle_xw = 10;  //挡板单体宽度横，用于绘图
	public static int baffle_yw = 15;  //挡板单体厚度纵，用于绘图
	public static int level_stars=1;
	
	
	//龙参数
	public static Dragon dragon;  //龙
	public static int pNum = 3;  //龙生命数
	public static int MoveSpeed = 5;  //龙运动速度
	public static boolean moveState = false;  //移动状态标识
	public static boolean JumpState = false;  //跳跃状态标识
	public static boolean DropState = false;  //掉落状态标识
	public static boolean forward = true;   //方向标识：true、右移；false、左移
	public static boolean jforward = true;   //方向标识：true、上；false、下
	public static float pX = 20;  //龙开始X坐标
	public static float pY = 0;  //龙开始Y坐标
	public static int pW = 60;  //龙宽度
	public static int pH = 65;  //龙高度
	public static int pR = 30;  //龙直径
	public static float g = (float)0.9;  //重力加速度
	public static int start_V = 17;  //龙起跳初速度
	
	//怪物参数
	public static ArrayList<Monster> monsterarr = new ArrayList<Monster>();  //怪物数组
	public static int monster_num;
	public static int m_width = 45;  //怪物宽度
	public static int m_height =45;  //怪物高度
	public static int m_r = 23;  //怪物半径
	public static int m_speed = 3;  //怪物速度
	public static int m_xspeed = 3;  //怪物x速度
	public static int m_yspeed = 4;  //怪物y速度
	public static boolean m_is_stop = false;  //怪物是否停止
	
	public static Ghost ghost = null;  //幽灵
	
	
	//子弹参数
	public static ArrayList<Bullet> bulletarr = new ArrayList<Bullet>();//子弹数组
	public static int BulletSpeed = 10;  //子弹速度
	public static int max_b = 4;  //子弹最大数量
	
	//道具参数
	public static Props props = null;  //道具数组
	
	//计分参数
	public static ArrayList<Score> scorearr = new ArrayList<Score>();  //怪物数组
	
	public static void clear(){
		AppStatic.pNum=3;
		AppStatic.old_gv = AppStatic.gamemap;
		AppStatic.gamemap = null;
		AppStatic.pause = true;
		//线程停止有顺序，否则出错
		for(int i=0; i<AppStatic.monsterarr.size(); i++)  //停止怪物线程
			AppStatic.monsterarr.get(i).monsterthread.flag = false;
		for(int i=0; i<AppStatic.bulletarr.size(); i++)  //停止子弹线程
			AppStatic.bulletarr.get(i).bt.flag = false;
		if(AppStatic.props != null)
			AppStatic.props.propsthread.flag = false;
		//for(int i=0; i<AppStatic.scorearr.size(); i++)  //停止分数线程
			//AppStatic.scorearr.get(i)
		if(AppStatic.dragon != null)
			AppStatic.dragon.dragonthread.flag = false;  //停止龙线程
		if(AppStatic.ghost != null)
			AppStatic.ghost.ghostthread.flag = false;
		AppStatic.props = null;
		AppStatic.ghost = null;
		AppStatic.bulletarr.clear();
		AppStatic.monsterarr.clear();
		AppStatic.scorearr.clear();
		AppStatic.dragon = null;
		AppStatic.m_is_stop = false;
	}
}
