package bb.model;

import java.util.ArrayList;

import bb.thread.MonsterThread;
import bb.utils.AppStatic;

import android.graphics.Bitmap;
import android.graphics.Canvas;

//怪物模型类
public class Monster {
	public int m_x;  //x坐标
	public int m_y;  //y坐标
	public int m_xspeed;  //怪物x方向速度
	public int m_yspeed;  //怪物y方向速度
	public int width;  //怪物宽度
	public int height;  //怪物高度
	public int monster_flag;  //1.普通怪2.独眼怪3.隐身怪
	public int move_flag;  //运动标识，分为10种：1、长左；2、短左；3、长右；
						   //4、短右；5、左斜上；6、右斜上；7、直跳上；
						   //8、左斜下；9、右斜下；10、直跳下
	public boolean forward_x;  //运动方向标识x
	public boolean forward_y;  //运动方向标识y
	public boolean is_x;  //x方向是否移动
	public boolean is_y;  //y方向是否移动
	public boolean flag;  //运动状态，是否处于临界时刻
	public int bafflenum;  //当前所在挡板编号
	public int x_longth;  //x运动长度
	public int y_longth;  //y运动长度
	public int m_flag;  //怪物状态，用于选择显示图片及其他
	public boolean is_die;  //怪物是否存活
	public int mr;  //怪物半径
	public ArrayList<Bitmap> m_p = new ArrayList<Bitmap>();  //怪物图片
	//public Bitmap m_picture;  //当前怪物图片
	public MonsterThread monsterthread;  //怪物运动线程
	public GameMap gamemap;  //当前使用地图
	public boolean[] m_farr = new boolean[3];  //移动方向缓存
	public boolean is_show;
	
	//初始化怪物
	public Monster(int monster_flag, int m_x, int m_y, ArrayList<Bitmap> m_p){
		this.monster_flag = monster_flag;
		this.m_x = m_x;
		this.m_y = m_y;
		this.is_x = false;
		this.is_y = false;
		this.is_show = true;
		this.width = AppStatic.m_width;
		this.height = AppStatic.m_height;
		this.m_p = m_p;
		this.move_flag = 10;
		this.m_xspeed = AppStatic.m_xspeed;
		this.m_yspeed = AppStatic.m_yspeed;
		this.is_die = true;
		this.flag = true;
		this.mr = AppStatic.m_r;
		gamemap = AppStatic.gamemap;
		m_flag = 0;
		monsterthread = new MonsterThread(this);
		//monsterthread.start();
	}
	
	public void move(){  //怪物存活移动
		if(flag){
			setforward();  //确定移动方向属性
			if(!is_y){
				for(int i=0; i<3; i++){
					if(i<2)
						m_farr[i] = m_farr[i+1];
					else
						m_farr[i] = forward_x;
				}
				if(m_farr[0] != m_farr[1]){
					forward_x = m_farr[1];
					m_farr[2] = m_farr[1];
				}
			}
		}
		hitdetection();  //碰撞检测
		if(!AppStatic.m_is_stop){
			if(is_x){
				if(forward_x)
					m_x -= m_xspeed;
				else 
					m_x += m_xspeed;
				x_longth -= m_xspeed;
				if(x_longth<=0 && !is_y)
					move_random();
			}
			if(is_y){
				if(forward_y){
					m_y -= m_yspeed;
					y_longth -= m_yspeed;
				}
				else{
					m_y += m_yspeed;
					if(m_y >= AppStatic.screen_y + height)
						m_y = 0 - height;
				}
				if(y_longth<=0 && forward_y){
					flag = true;
					move_flag += 3;
				}
			}
		}
	}

	public void moveup(){  //怪物死亡移动
		m_y -= m_yspeed;
		if(m_y <= 0){
			m_y = 0;
			flag = true;
			move_flag = 10;
		}
	}
	
	public void move_random(){  //随机方向
		int move_flag;
		flag = true;
		//(int)(Math.random()*(end-start+1))+start;
		if(monster_flag == 1 || monster_flag == 3){
			if(Math.random()>0.35){
				move_flag = (int)(Math.random()*4)+1;
				if((this.move_flag==2 && move_flag==4) || (this.move_flag==4 && move_flag==2))
					move_random();
			}else{
				move_flag = (int)(Math.random()*3)+5;
				if(this.move_flag == move_flag)
					move_random();
			}
			this.move_flag = move_flag;
		}else if(monster_flag == 2){
			move_flag = (int)(Math.random()*3)+5;
			if(this.move_flag == move_flag)
				move_random();
			this.move_flag = move_flag;
		}
	}
	
	public void move_random(int start, int end){  //随机方向
		flag = true;
		move_flag = (int)(Math.random()*(end-start+1))+start;
	}
	
 	public void setforward(){  //确定移动方向,根据移动方向确定移动属性
		switch(move_flag){  //设置运动方向
		case 1:  //长左
			is_y = false;
			is_x = true;
			forward_x = true;
			x_longth = 100;
			break;
		case 2:  //短左
			is_y = false;
			is_x = true;
			forward_x = true;
			x_longth = 70;
			break;
		case 3:  //长右
			is_y = false;
			is_x = true;
			forward_x = false;
			x_longth = 100;
			break;
		case 4:  //短右
			is_y = false;
			is_x = true;
			forward_x = false;
			x_longth = 70;
			break;
		case 5:  //左斜上
			is_y = true;
			is_x = true;
			forward_x = true;
			forward_y = true;
			x_longth = 150;
			y_longth = 150;
			break;
		case 6:  //右斜上
			is_y = true;
			is_x = true;
			forward_x = false;
			forward_y = true;
			x_longth = 150;
			y_longth = 150;
			break;
		case 7:  //直跳上
			is_y = true;
			is_x = false;
			forward_y = true;
			y_longth = 150;
			break;
		case 8:  //左斜下
			is_y = true;
			is_x = true;
			forward_x = true;
			forward_y = false;
			x_longth = 150;
			break;
		case 9:  //右斜下
			is_y = true;
			is_x = true;
			forward_x = false;
			forward_y = false;
			x_longth = 150;
			break;
		case 10:  //直跳下
			is_y = true;
			is_x = false;
			forward_y = false;
			break;
		}
		flag = false;  //锁定运动状态
	}
	
	public void hitdetection(){  //碰撞检测--地图
		if(is_y){  //纵向检测
			if(forward_y){  //向上
				if(m_y-m_yspeed <= AppStatic.gamemap.start_y){  //检测是否碰到地图顶部
					is_y = false;
					m_y = AppStatic.gamemap.start_y;
					flag = true;
					is_x = false;
					move_flag += 3;
				}
			}else{
				for(int j=0; j<AppStatic.gamemap.bafflearr.size(); j++){
					if(AppStatic.gamemap.bafflearr.get(j).flag == 1||AppStatic.gamemap.bafflearr.get(j).flag == 4){  //检测是否碰到横向挡板
						if(m_y+height+m_yspeed >= AppStatic.gamemap.bafflearr.get(j).start_y
								&& m_y+height <= AppStatic.gamemap.bafflearr.get(j).start_y
								&& m_x+width*2/3 >= AppStatic.gamemap.bafflearr.get(j).start_x
								&& m_x-width/3 <= AppStatic.gamemap.bafflearr.get(j).start_x+AppStatic.gamemap.bafflearr.get(j).length){
							is_y = false;
							m_y = AppStatic.gamemap.bafflearr.get(j).start_y - height;
							bafflenum = j;
							move_random(1,4);
							break;
						}
					}
				}
			}
		}
		if(is_x){  //横向检测
			if(forward_x){  //向左
				if(!is_y){  //检测是否走到挡板末端
					if(m_x+width-m_xspeed-10 < AppStatic.gamemap.bafflearr.get(bafflenum).start_x){
						flag = true;
						move_flag = 8;
					}
				}
				if(m_x-m_xspeed <= AppStatic.gamemap.start_x){  //检测四壁
					is_x = false;
					m_x = AppStatic.gamemap.start_x;
					if(!is_y){  //解锁运动状态
						flag = true;
						move_flag = 3;
					}
				}
				for(int i=0; i<AppStatic.gamemap.bafflearr.size(); i++){
					if(AppStatic.gamemap.bafflearr.get(i).flag == 2){  //检测纵向挡板
						if(m_x-m_xspeed <= AppStatic.gamemap.bafflearr.get(i).start_x+AppStatic.gamemap.bafflearr.get(i).thickness
								&& m_x >= AppStatic.gamemap.bafflearr.get(i).start_x+AppStatic.gamemap.bafflearr.get(i).thickness
								&& m_y < AppStatic.gamemap.bafflearr.get(i).start_y+AppStatic.gamemap.bafflearr.get(i).length
								&& m_y+height >AppStatic.gamemap.bafflearr.get(i).start_y){
							is_x = false;
							m_x = AppStatic.gamemap.bafflearr.get(i).start_x + AppStatic.gamemap.bafflearr.get(i).thickness;
							if(!is_y){
								flag = true;
								move_flag = 4;
							}
							break;
						}
					}
				}
			}else{  //向右
				if(!is_y){  //检测是否走到挡板末端
					if(m_x+m_xspeed+10 > AppStatic.gamemap.bafflearr.get(bafflenum).start_x+AppStatic.gamemap.bafflearr.get(bafflenum).length){
						flag = true;
						move_flag = 9;
					}
				}
				if(m_x+width+m_xspeed >= AppStatic.gamemap.start_x+AppStatic.gamemap.width){  //检测四壁
					is_x = false;
					m_x = AppStatic.gamemap.start_x + AppStatic.gamemap.width - width;
					if(!is_y){
						flag = true;
						move_flag = 1;
					}
				}
				for(int i=0; i< AppStatic.gamemap.bafflearr.size(); i++){
					if(AppStatic.gamemap.bafflearr.get(i).flag == 2){  //检测纵向挡板
						if(m_x+width+m_xspeed >= AppStatic.gamemap.bafflearr.get(i).start_x
								&& m_x+width <= AppStatic.gamemap.bafflearr.get(i).start_x
								&& m_y < AppStatic.gamemap.bafflearr.get(i).start_y+AppStatic.gamemap.bafflearr.get(i).length
								&& m_y+height >AppStatic.gamemap.bafflearr.get(i).start_y){
							is_x = false;
							m_x = AppStatic.gamemap.bafflearr.get(i).start_x - width;
							if(!is_y){
								flag = true;
								move_flag = 2;
							}
							break;
						}
					}
				}
			}
		}
	}

	public boolean killed(){  //被杀死
		int pX = (int)(AppStatic.dragon.pX + AppStatic.dragon.pR);  //龙中心点x
		int pY = (int)(AppStatic.dragon.pY + AppStatic.dragon.pR);  //龙中心点y
		int m_x = (int)(this.m_x + this.width/2);;  //怪物中心点x
		int m_y = (int)(this.m_y + this.height/2);;  //怪物中心点y
		if(Math.abs(pX-m_x) < AppStatic.dragon.pR+this.mr 
				&& Math.abs(pY-m_y) < AppStatic.dragon.pR+this.mr){//碰撞判断
			return true;
		}
		
		return false;
	}
	
	@Override
	protected void finalize(){
		this.monsterthread.flag = false;
	}
	
	public void drawSelf(Canvas canvas){  //绘制怪物
		try {
			int m_flag;
			m_flag = this.m_flag;
			if(monster_flag == 2)
				m_flag += 4;
			if(monster_flag == 3)
				m_flag += 8;
			if(monster_flag != 3)
				canvas.drawBitmap(m_p.get(m_flag), m_x, m_y, null);
			else if(monster_flag == 3 && is_show)
				canvas.drawBitmap(m_p.get(m_flag), m_x, m_y, null);
				
		}catch (Exception e) {
			// TODO: handle exception
		} 
	}
}