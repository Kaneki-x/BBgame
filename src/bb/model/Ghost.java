package bb.model;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import bb.thread.GhostThread;
import bb.utils.AppStatic;

//幽灵类
public class Ghost {
	public int g_x;  //幽灵x坐标  
	public int g_y;  //幽灵y坐标
	public int g_xspeed = 2;  //怪物x方向速度
	public int g_yspeed = 1;  //怪物y方向速度
	public int g_w;  //怪物宽度
	public int g_h;  //怪物高度
	public GhostThread ghostthread;
	public int bitnum;  //幽灵图片
	public ArrayList<Bitmap> gbitarr = new ArrayList<Bitmap>();
	
	public Ghost(ArrayList<Bitmap> gbitarr){
		this.g_w = 40;
		this.g_h = 54;
		this.g_x = 0+g_w/2;
		this.g_y = 0+g_h/2;
		this.ghostthread = new GhostThread(this);
		this.ghostthread.start();
		this.gbitarr = gbitarr;
		this.bitnum = 0;
	}
	
	//怪物移动
	public void move(){
		if(AppStatic.dragon != null){
			if(g_x < AppStatic.dragon.pX)
				g_x += g_xspeed;
			else
				g_x -= g_xspeed;
			if(g_y < AppStatic.dragon.pY)
				g_y += g_yspeed;
			else
				g_y -= g_yspeed;
		}
	}
	
	public void drawSelf(Canvas canvas){  //绘制怪物
		try {
			canvas.drawBitmap(gbitarr.get(bitnum), g_x, g_y, null);
		}catch (Exception e) {
			// TODO: handle exception
		} 
	}
}
