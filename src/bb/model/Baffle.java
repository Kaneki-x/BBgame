package bb.model;

import java.util.ArrayList;

import bb.thread.BaffleThread;
import bb.utils.AppStatic;
import android.graphics.Bitmap;
import android.graphics.Canvas;

//挡板模型类
public class Baffle {

	public int start_x;  //挡板起始x坐标
	public int start_y;  //挡板起始y坐标
	public int move_lx;  //挡板起始y坐标
	public int move_rx;  //挡板起始y坐标
	public boolean forward=false;//挡板左右移动标识
	public int speed=1;//挡板左右移动标识
	public int flag;  //挡板类型：1、横向；2、纵向 ;
	public int length;  //挡板长度/宽度
	public int thickness;  //挡板厚度
	public BaffleThread bt;  //挡板厚度
	Bitmap bmpWood;  //挡板背景
	
	//初始化参数
	public Baffle(int flag, int start_x, int start_y, int length, ArrayList<Bitmap> bmp_arr,int move_lx,int move_rx){
		this.flag = flag;
		this.move_lx=move_lx;
		this.move_rx=move_rx;
		this.start_x = start_x;
		this.start_y = start_y;
		this.length = length;
		switch(this.flag){
		case 1:
			this.thickness = 25; 
			this.bmpWood = bmp_arr.get(0);
			break;
		case 2:
			this.thickness = 15; 
			this.bmpWood = bmp_arr.get(1);
			break;
		case 3:
			this.thickness = 25;
			this.bmpWood = bmp_arr.get(2);
			break;
		case 4:
			this.thickness = 25;
			this.bmpWood = bmp_arr.get(0);
			bt=new BaffleThread(this);
			bt.start();
			break;
		}
	}
	
	//挡板绘制
	public void drewself(Canvas canvas){
		int i;
		if(flag == 1 || flag == 3 || flag == 4){
			for(i=0;i<length;i+=AppStatic.baffle_xw){
					canvas.drawBitmap(this.bmpWood, start_x+i, start_y, null);
			}
		}else if(flag == 2){
			for(i=0; i<length; i+=AppStatic.baffle_yw){
					canvas.drawBitmap(this.bmpWood, start_x, start_y+i, null);
			}
		}
	}
	
	public void drewself(Canvas canvas, int up_y){
		int i;
		if(flag == 1 || flag == 3||flag == 4){
			for(i=0;i<length;i+=AppStatic.baffle_xw){
					canvas.drawBitmap(this.bmpWood, start_x+i, start_y+up_y, null);
			}
		}
		else{
			for(i=0;i<length;i+=AppStatic.baffle_yw){
					canvas.drawBitmap(this.bmpWood, start_x, start_y+i+up_y, null);
			}
		}
	}

}
