package bb.model;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import bb.thread.DragonThread;
import bb.utils.AppStatic;

//龙模型类
public class Dragon {
	public boolean is_stop;  //是否停止
	public int MoveSpeed;  //龙运动速度
	public boolean moveState;  //移动标识
	public boolean JumpState;  //跳跃标识
	public boolean forward;  //方向标识
	public boolean jforward;  //跳跃上下标识
	public int status; //龙状态，用于绘图
	public boolean is_NB;  //是否处于NB状态
	public float pX;  //龙X坐标
	public float pY;  //龙Y坐标
	public int pW;  //龙宽度
	public int pH;  //龙高度
	public int pR;  //龙半径
	public float pV_y;  //龙纵向速度
	public int jstart_y;  //龙起跳Y坐标
	public boolean down_die;  //掉出界面是否死亡
	public ArrayList<Bitmap> bitDragonarr =new ArrayList<Bitmap>();  //不同状态下龙的图片
	private VirtualRocker vr;  //模拟摇杆
	public Bitmap bitDragon;  //龙图片
	public GameMap gamemap;  //当前使用地图
	public DragonThread dragonthread;  //龙运动线程
	
	//初始化
	public Dragon(ArrayList<Bitmap> bitDragonarr,VirtualRocker vr){
		this.vr=vr;
		this.MoveSpeed = AppStatic.MoveSpeed;
		this.moveState = AppStatic.moveState;
		this.JumpState = AppStatic.JumpState;
		this.forward = AppStatic.forward;
		this.pX = AppStatic.pX;
		this.pY = AppStatic.pY;
		this.pW = AppStatic.pW;
		this.pH = AppStatic.pH;
		this.pR = AppStatic.pR;
		this.pV_y = 0;
		this.is_stop = false;
		this.is_NB = true;
		this.down_die = true;
		this.bitDragonarr = bitDragonarr;
		this.bitDragon = bitDragonarr.get(0);
		this.gamemap = AppStatic.gamemap;
		jstart_y = (int) pY;
		this.dragonthread = new DragonThread(this);
		//this.dragonthread.start();
		AppStatic.JumpState = true;
		drop();
	}
	
	//通过计算获取龙实时状态
	public void setstatus(){
		status = AppStatic.forward?100:200;  //用来确定龙的图片
		switch(status){
		case 100:bitDragon = bitDragonarr.get(0);break;
		case 200:bitDragon = bitDragonarr.get(1);break;
		case 300:bitDragon = bitDragonarr.get(2);break;
		case 400:bitDragon = bitDragonarr.get(3);break;
		default:bitDragon = null;break;
		}
	}
	
	//手动设置龙实时状态
	public void setstatus(int status){
		switch(status){
		case 100:bitDragon = bitDragonarr.get(0);break;
		case 200:bitDragon = bitDragonarr.get(1);break;
		case 300:bitDragon = bitDragonarr.get(2);break;
		case 400:bitDragon = bitDragonarr.get(3);break;
		default:bitDragon = null;break;
		}
	}
	
	//重力传感器操作时的移动
	public void move(){
		setstatus();  //设置龙图片
		if(AppStatic.moveState){
			if(pX>gamemap.width-pW)//右边界判断
				pX=gamemap.width-pW;
			if(pX<0)//左边界判断
				pX=0;
			if(pY<(AppStatic.screen_x-AppStatic.pH-15)&&AppStatic.JumpState==false)//在挡板上判断
			{
				if(detection())//挡板边界状态判断
				{
					AppStatic.JumpState=true;
					drop();
				}
			}
			if(!baffleBorder())  //检测竖挡板
			{
				pX = AppStatic.forward ? pX+MoveSpeed : pX-MoveSpeed;  //左右移动龙
			}
		}
		else{//挡板移动，龙静止的掉落判断
			if(pY<(AppStatic.screen_x-AppStatic.pH-15)&&AppStatic.JumpState==false)//在挡板上判断
			{
				if(detection())//挡板边界状态判断
				{
					AppStatic.JumpState=true;
					drop();
				}
			}
		}
	}
	
	//虚拟摇杆操作时的移动
	public void vrMove(){
		if(vr.SmallRockerCircleX>100&&vr.SmallRockerCircleY<360&&vr.SmallRockerCircleY>300){//向右移动龙
			if(pX>gamemap.width-pW)
				pX=gamemap.width-pW;
			if(pX<gamemap.width-pW&&!baffleBorder()){
				AppStatic.forward=true;
				pX+=MoveSpeed;
				if(pY<(AppStatic.screen_x-AppStatic.pH-15)&&AppStatic.JumpState==false)
				{
					if(detection())//挡板边界状态判断
					{
						AppStatic.JumpState=true;
						drop();
					}
				}
			}
		}
		if(vr.SmallRockerCircleX<100&&vr.SmallRockerCircleY<360&&vr.SmallRockerCircleY>300){//向左移动龙
			if(pX<0)
				pX=0;
			if(pX>0&&!baffleBorder()){
				AppStatic.forward=false;
				pX-=MoveSpeed;
				if(pY<(AppStatic.screen_x-AppStatic.pH-15)&&AppStatic.JumpState==false)
				{
					if(detection())//挡板边界状态判断
					{
						AppStatic.JumpState=true;
						drop();
					}
				}
			}
		}
		if(vr.SmallRockerCircleY<320&&vr.SmallRockerCircleX<150&&vr.SmallRockerCircleX>50){//龙跳跃
			if(AppStatic.JumpState==false){
				AppStatic.JumpState=true;
				jump();
			}
		}
		setstatus();  //设置龙图片
	}
	
	//跳跃
	public void jump(){
		AppStatic.JumpState = true;
		jforward = true;
		pV_y -= AppStatic.g;
		if(pV_y > 0 && pY > 0-pH/2)
			pY = pY - pV_y;
		else
			drop();
	}
	
	//掉落
	public void drop(){
		jforward = false;
		pV_y += AppStatic.g;
		pV_y = pV_y<AppStatic.start_V ? pV_y : AppStatic.start_V;
		pY = pY + pV_y;
		for(int i=0; i<AppStatic.gamemap.bafflearr.size(); i++){
			if((AppStatic.gamemap.bafflearr.get(i).flag == 1||AppStatic.gamemap.bafflearr.get(i).flag == 4)
					&& pY+pH >= AppStatic.gamemap.bafflearr.get(i).start_y
					&& pY+pH-pV_y <= AppStatic.gamemap.bafflearr.get(i).start_y
					&& pX+pW*2/3 >= AppStatic.gamemap.bafflearr.get(i).start_x
					&& pX+pW/3 <= AppStatic.gamemap.bafflearr.get(i).start_x + AppStatic.gamemap.bafflearr.get(i).length){
				pY = AppStatic.gamemap.bafflearr.get(i).start_y-pH;
				pV_y = AppStatic.start_V;
				AppStatic.JumpState = false;
			}
			if(AppStatic.gamemap.bafflearr.get(i).flag == 3
					&& pY+pH >= AppStatic.gamemap.bafflearr.get(i).start_y
					&& pY+pH-pV_y <= AppStatic.gamemap.bafflearr.get(i).start_y
					&& pX+pW*2/3 >= AppStatic.gamemap.bafflearr.get(i).start_x
					&& pX+pW/3 <= AppStatic.gamemap.bafflearr.get(i).start_x + AppStatic.gamemap.bafflearr.get(i).length){
				down_die = false;
			}
		}
		if(pY > AppStatic.screen_y){
			if(down_die){
				this.dragonthread.flag = false;
				AppStatic.pNum = 0;
			}else{
				pY = 0 - pH;
			}
		}else if(pY + pH + 20 < AppStatic.screen_y)
			down_die = true;
	}
	
	//挡板边缘掉落判断
	private boolean detection(){
		for(int i=0;i<gamemap.bafflearr.size();i++)
		{
			if(!AppStatic.forward && (gamemap.bafflearr.get(i).flag==1 || gamemap.bafflearr.get(i).flag==4))
			{
				if((pX-pW)<=gamemap.bafflearr.get(i).start_x||pX>=(gamemap.bafflearr.get(i).start_x+gamemap.bafflearr.get(i).length))
				{
					return true;
				}
			}
			if(AppStatic.forward && gamemap.bafflearr.get(i).flag==1 || gamemap.bafflearr.get(i).flag==4){
				if((pX-pW)<=gamemap.bafflearr.get(i).start_x||pX>=(gamemap.bafflearr.get(i).start_x+gamemap.bafflearr.get(i).length))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	//竖直挡板的边界判断
	public boolean baffleBorder(){
		for(int i=0;i<gamemap.bafflearr.size();i++)  //迭代挡板
		{
			if(gamemap.bafflearr.get(i).flag==2)  //检测竖挡板
			{
				if(pY+pH > gamemap.bafflearr.get(i).start_y
						&& pY < (gamemap.bafflearr.get(i).start_y+gamemap.bafflearr.get(i).length)){  //纵向检测
					
					if(pX+pW <= gamemap.bafflearr.get(i).start_x
							&& pX+pW+MoveSpeed >= gamemap.bafflearr.get(i).start_x  //横向检测，右移
							&& AppStatic.forward){
						pX = gamemap.bafflearr.get(i).start_x-pW;
						return true;
					}
					
					if(pX >= gamemap.bafflearr.get(i).start_x+gamemap.bafflearr.get(i).thickness
							&& pX-MoveSpeed <= gamemap.bafflearr.get(i).start_x+gamemap.bafflearr.get(i).thickness
							&& !AppStatic.forward){  //横向检测，左移
						pX = gamemap.bafflearr.get(i).start_x+gamemap.bafflearr.get(i).thickness;
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean collision(){  //龙与怪兽碰撞检测
		int pX = (int)(this.pX + pR);  //龙中心点x
		int pY = (int)(this.pY + pR);  //龙中心点y
		int m_x;  //怪物中心点x
		int m_y;  //怪物中心点y
		for(int i=0; i<AppStatic.monsterarr.size(); i++){
			m_x = (int)(AppStatic.monsterarr.get(i).m_x + AppStatic.monsterarr.get(i).width/2);
			m_y = (int)(AppStatic.monsterarr.get(i).m_y + AppStatic.monsterarr.get(i).height/2);
			if(Math.abs(pX-m_x) < pR+AppStatic.monsterarr.get(i).mr-5
					&& Math.abs(pY-m_y) < pR+AppStatic.monsterarr.get(i).mr-5
					&& AppStatic.monsterarr.get(i).is_die){  //碰撞判断
				AppStatic.pNum--;
				is_NB = true;
				this.pX = AppStatic.pX;
				this.pY = AppStatic.pY;
				drop();
				return true;
			}
		}
		return false;
	}
	
	public boolean D_Gcollision(){  //龙与幽灵碰撞检测
		if(Math.abs(AppStatic.ghost.g_x-pX) < pR + AppStatic.ghost.g_w/2
				&& Math.abs(AppStatic.ghost.g_y-pY) < pR + AppStatic.ghost.g_h/2
				&& !is_NB){
			AppStatic.pNum--;
			is_NB = true;
			this.pX = AppStatic.pX;
			this.pY = AppStatic.pY;
			drop();
			return true;
		}
		return false;
	}
	
	@Override
	protected void finalize(){
		this.dragonthread.flag = false;
	}

	//绘制龙
	public void drawSelf(Canvas canvas){
		try {
			if(!dragonthread.is_show)
				setstatus(status+200);
			if(AppStatic.sensor==0)//重力传感器时画图
			{
				canvas.drawBitmap(bitDragon, pX, pY, null);
			}else{//虚拟摇杆时画图
				vrMove();
				canvas.drawBitmap(bitDragon, pX, pY, null);
			}
		}catch (Exception e) {
			// TODO: handle exception
		} 
	}
	
	//绘制龙
	public void drawSelf(Canvas canvas, int up_y){
		try {
			if(!dragonthread.is_show)
				setstatus(status+200);
			if(AppStatic.sensor==0)//重力传感器时画图
			{
				canvas.drawBitmap(bitDragon, pX, pY+up_y, null);
			}else{//虚拟摇杆时画图
				vrMove();
				canvas.drawBitmap(bitDragon, pX, pY+up_y, null);
			}
		}catch (Exception e) {
			// TODO: handle exception
		} 
	}
}
