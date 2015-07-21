package bb.model;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import bb.thread.BulletThread;
import bb.utils.AppStatic;

//子弹模型类
public class Bullet {
	public int MoveSpeed;//子弹移动速度
	public float StartX;//子弹初始x坐标
	public float StartY;//子弹初始y坐标
	public boolean forward;//子弹左右方向
	public boolean movexflag;  //子弹上下方向
	public int bulletState;  //子弹状态：0，未打中；1，打中1；2，打中2；3，打中3...
	public ArrayList<Bitmap> bitmaps=new ArrayList<Bitmap>();
	public boolean is_top;  //子弹是否停在顶部
	public int br = 30; //子弹直径
	public int distance = 200;//子弹飞行距离
	public BulletThread bt;//	子弹移动线程
	public GameMap gamemap = null;
	public ArrayList<Monster> monsterarr = new ArrayList<Monster>();//怪物数组
	//子弹构造方法
	public Bullet(ArrayList<Bitmap> bitmaps,GameMap gamemap, ArrayList<Monster> monsterarr) {
		this.StartX=AppStatic.dragon.pX+br;
		this.StartY=AppStatic.dragon.pY+br;
		this.forward=AppStatic.forward;
		this.bitmaps=bitmaps;
		this.gamemap = gamemap;
		this.movexflag = true;
		this.MoveSpeed = AppStatic.BulletSpeed;
		this.monsterarr = monsterarr;
		this.is_top = false;
		bt=new BulletThread(this);
		bt.start();
	}
	
	//子弹运动方法
	public void move(){
		int i;
		if(movexflag){
			if(forward){  //向右
				if(distance <= 0)
					movexflag = false;
				else{
					StartX += MoveSpeed;
					distance -= MoveSpeed;
					if(StartX >= gamemap.start_y+gamemap.width-br){
						movexflag = false;
						StartX = gamemap.start_y+gamemap.width-br;
					}
					for(i = 0; i<gamemap.bafflearr.size(); i++){
						if(gamemap.bafflearr.get(i).flag == 2){
							if(StartX <= gamemap.bafflearr.get(i).start_x-br
									&& StartX+MoveSpeed >= gamemap.bafflearr.get(i).start_x-br
									&& StartY < gamemap.bafflearr.get(i).start_y+gamemap.bafflearr.get(i).length+br
									&& StartY > gamemap.bafflearr.get(i).start_y){
								movexflag = false;
								StartX = gamemap.bafflearr.get(i).start_x-br;
								break;
							}
						}
					}
				}
			}else{
				if(distance <= 0)
					movexflag = false;
				else{
					StartX -= MoveSpeed;
					distance -= MoveSpeed;
					if(StartX <= gamemap.start_x){
						movexflag = false;
						StartX = gamemap.start_x;
					}
					for(i = 0; i<gamemap.bafflearr.size(); i++){
						if(gamemap.bafflearr.get(i).flag == 2){
							if(StartX >= gamemap.bafflearr.get(i).start_x+gamemap.bafflearr.get(i).thickness
									&&StartX-MoveSpeed <= gamemap.bafflearr.get(i).start_x+gamemap.bafflearr.get(i).thickness
									&&StartY < gamemap.bafflearr.get(i).start_y+gamemap.bafflearr.get(i).length+br
									&&StartY > gamemap.bafflearr.get(i).start_y){
								movexflag = false;
								StartX = gamemap.bafflearr.get(i).start_x+gamemap.bafflearr.get(i).thickness;
								break;
							}
						}
					}
				}
			}
			if(collision()){  //碰撞检测，检测是否碰到怪物
				bulletState = 1;
			}
		}else{
			if(StartY <= gamemap.start_x+br){
				StartY = gamemap.start_x;
				is_top = !is_top;
			}else{
				StartY -= MoveSpeed;
				if(collision()){  //碰撞检测，检测是否碰到怪物
					bulletState = 1;
				}
			}
		}
	}

	public boolean collision(){  //子弹与怪兽碰撞检测
		int b_x = (int)(StartX + br);  //子弹中心点x
		int b_y = (int)(StartY + br);  //子弹中心点y
		int m_x;  //怪物中心点x
		int m_y;  //怪物中心点y
		for(int i=0; i<AppStatic.monsterarr.size(); i++){
			m_x = (int)(AppStatic.monsterarr.get(i).m_x + AppStatic.monsterarr.get(i).width/2);
			m_y = (int)(AppStatic.monsterarr.get(i).m_y + AppStatic.monsterarr.get(i).height/2);
			if(Math.sqrt((b_x-m_x)^2+(b_y-m_y)^2) < br+AppStatic.monsterarr.get(i).mr-5){
				if(Math.abs(b_x-m_x) < br+AppStatic.monsterarr.get(i).mr-5
						&& Math.abs(b_y-m_y) < br+AppStatic.monsterarr.get(i).mr-5
						&& AppStatic.monsterarr.get(i).is_die){
					AppStatic.monsterarr.get(i).is_die = false;
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	protected void finalize(){
		this.bt.flag = false;
	}
	
	public void drawSelf(Canvas canvas){
		canvas.drawBitmap(bitmaps.get(bulletState), StartX, StartY, null);
	}
}
