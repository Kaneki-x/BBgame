package bb.model;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import bb.thread.PropsThread;
import bb.utils.AppStatic;

//道具类
public class Props {
	public boolean is_valid;  //道具是否有效
	public int Pr_flag;  //道具标识
	public boolean Pr_mflag;  //道具运动标识
	public int Pr_speed;  //道具下降速度
	public int Pr_r;  //道具半径
	public int Pr_x;  //坐标x
	public int Pr_y;  //坐标y
	public int Pr_num;  //道具过几个挡板
	public boolean is_KD;  //是否坑爹
	public GameMap gamemap;  //当前使用地图
	public Bitmap bitProps;  //当前道具图片
	public ArrayList<Bitmap> bitPropsarr = new ArrayList<Bitmap>();  //道具图片数组
	public PropsThread propsthread;  //道具线程
	
	public Props(ArrayList<Bitmap> bitPropsarr){
		this.is_valid = true;
		this.Pr_flag = (int)(Math.random()*8);
		this.Pr_mflag = true;
		this.bitPropsarr = bitPropsarr;
		this.Pr_x = (int)(Math.random()*(AppStatic.screen_x-4*Pr_r+1))+Pr_r;
		this.Pr_y = 0;
		this.Pr_speed = 4;
		this.is_valid = true;
		this.Pr_num = (int)(Math.random()*3);
		this.gamemap = AppStatic.gamemap;
		this.propsthread = new PropsThread(this);
		this.propsthread.start();
	}
	
	public void move(){  //道具运动
		boolean is_down = false;  //是否快到最底层
		if(Pr_mflag){
			Pr_y += Pr_speed;
			if(Pr_y+2*Pr_r > 0.8*AppStatic.screen_y){
				is_down = true;
			}
			for(int i=0; i<gamemap.bafflearr.size(); i++){
				if(gamemap.bafflearr.get(i).flag == 1 || gamemap.bafflearr.get(i).flag == 3){
						if(Pr_y+Pr_r*2-Pr_speed<gamemap.bafflearr.get(i).start_y
							&& Pr_y+Pr_r*2+Pr_speed>gamemap.bafflearr.get(i).start_y
							&& Pr_x+Pr_r*2>gamemap.bafflearr.get(i).start_x
							&& Pr_x<gamemap.bafflearr.get(i).start_x+gamemap.bafflearr.get(i).length){
							if(Pr_num<0 || is_down){  //碰撞检测
								Pr_mflag = false;
								Pr_y = gamemap.bafflearr.get(i).start_y - Pr_r*2;
							}
							Pr_num--;
						}
				}
			}
			if(Pr_y > AppStatic.screen_y){
				this.propsthread.flag = false;
				AppStatic.props = null;
			}
		}
		if(!is_valid){
			Pr_y -= 3;
		}
	}
	
	public void setPinfo(int Pr_flag){  //设置道具信息
		switch(Pr_flag){
		case 0:  //NB状态
			Pr_r = 15;
			bitProps = bitPropsarr.get(0);
			break;
		case 1:  //加速
			Pr_r = 15;
			bitProps = bitPropsarr.get(1);
			break;
		case 2:  //减速
			Pr_r = 15;
			bitProps = bitPropsarr.get(2);
			break;
		case 3:  //停止龙
			Pr_r = 15;
			bitProps = bitPropsarr.get(3);
			break;
		case 4:  //停止怪
			Pr_r = 15;
			bitProps = bitPropsarr.get(3);
			break;
		case 5:  //加分
			Pr_r = 15;
			bitProps = bitPropsarr.get(0);
			break;
		case 6:  //死亡
			Pr_r = 15;
			bitProps = bitPropsarr.get(0);
			break;
		case 7:  //加生命
			Pr_r = 15;
			bitProps = bitPropsarr.get(0);
			break;
		default:
			bitProps = bitPropsarr.get(100);
			break;
		}
	}
	
	public void recover(int Pr_flag){  //恢复道具效果
		switch(Pr_flag){  
		case 0:
			if(AppStatic.dragon != null)
				AppStatic.dragon.is_NB = false;
			break;
		case 1:
			if(AppStatic.dragon != null)
				AppStatic.dragon.MoveSpeed = AppStatic.MoveSpeed;
			break;
		case 2:
			if(AppStatic.dragon != null)
				AppStatic.dragon.MoveSpeed = AppStatic.MoveSpeed;
			break;
		case 3:
			if(AppStatic.dragon != null)
				AppStatic.dragon.is_stop = false;
			break;
		case 4:
			AppStatic.m_is_stop = false;
			break;
		default:break;
		}
	}
	
	public boolean eatProps(){  //龙吃道具
		int pX = (int)(AppStatic.dragon.pX + AppStatic.dragon.pR);  //龙中心点x
		int pY = (int)(AppStatic.dragon.pY + AppStatic.dragon.pR);  //龙中心点y
		int Pr_x = (int)(this.Pr_x + Pr_r);  //道具中心点x
		int Pr_y = (int)(this.Pr_y + Pr_r);  //道具中心点y
		
		if(Math.sqrt((pX-Pr_x)^2+(pY-Pr_y)^2) < AppStatic.dragon.pR+Pr_r){
			if(Math.abs(pX-Pr_x) < AppStatic.dragon.pR+Pr_r
					&& Math.abs(pY-Pr_y) < AppStatic.dragon.pR+Pr_r
					&& is_valid){  //碰撞判断
				AppStatic.dragon.is_NB = true;
				//Score score=new Score(50,Pr_x, Pr_y, AppStatic.ga);
				//AppStatic.scorearr.add(score);
				this.Pr_y = (int)AppStatic.dragon.pY;
				switch(Pr_flag){  //执行道具效果
				case 0:
					AppStatic.dragon.is_NB = true;
					AppStatic.dragon.dragonthread.mt_time = 200;
					break;
				case 1:
					AppStatic.dragon.MoveSpeed = 8;
					break;
				case 2:
					AppStatic.dragon.MoveSpeed = 3;
					break;
				case 3:
					AppStatic.dragon.is_stop = true;
					break;
				case 4:
					AppStatic.m_is_stop = true;
					break;
				case 5:
					Score s=new Score(200, Pr_x, Pr_y, AppStatic.ga);
					AppStatic.scorearr.add(s);
					break;
				case 6:
					AppStatic.pNum--;
					AppStatic.dragon.is_NB = true;
					AppStatic.dragon.pX = AppStatic.pX;
					AppStatic.dragon.pY = AppStatic.pY;
					AppStatic.dragon.drop();
					break;
				case 7:
					if(AppStatic.pNum < 3)
						AppStatic.pNum++;
					else{
						Score s1=new Score(200, Pr_x, Pr_y, AppStatic.ga);
						AppStatic.scorearr.add(s1);
					}
					break;
				default:break;
				}
				this.Pr_y = (int)AppStatic.dragon.pY; //指定y坐标，上移消失
				is_valid = false;
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	protected void finalize(){
		this.propsthread.flag = false;
	}
	
	public void drawself(Canvas canvas){
		try {
			canvas.drawBitmap(bitProps, Pr_x, Pr_y, null);
		}catch (Exception e) {
			// TODO: handle exception
		} 
	}
}
