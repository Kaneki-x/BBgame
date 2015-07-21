package bb.thread;

import bb.model.Monster;
import bb.model.Score;
import bb.utils.AppStatic;

//怪物运动线程
public class MonsterThread extends Thread{
	public boolean flag=false;  //线程状态标识
	public Monster m;  //怪物对象
	public boolean t_flag;  //时间标识，用于更改怪物图片
	public int mt_time;  //计时更改怪物图片，修改t_flag
	public int revive_time;  //怪物复活时间
	public int sleep_time;  //线程休眠时间
	public int time_out;  //怪频闪时间
	
	public MonsterThread(Monster m) {
		this.m = m;
		this.flag = true;
		this.t_flag = true;
		this.mt_time = 0;
		this.revive_time = 100;
		this.sleep_time = AppStatic.Sleep_time;
		this.time_out = 1000;
	}

	public void run(){
		int time_num = 300;
		while(flag){
			if(!AppStatic.pause)
			{
				try{
					if(m.is_die){
						m.move();
						revive_time = 100;
						if(t_flag)
							m.m_flag = 0;
						else
							m.m_flag = 1;
						time_num--;
						if(m.monster_flag == 3 && time_num <= 0){
							m.is_show = !m.is_show;
							if(m.is_show)
								time_num = 300;
							else
								time_num = 100;
						}
					}else{
						m.is_show = true;
						m.moveup();
						if(m.m_y <= 0)
							revive_time--;
						if(revive_time <= 0){
							m.move_flag = 10;
							m.is_die = true;
							time_num = 300;
						}
						if(t_flag)
							m.m_flag = 2;
						else
							m.m_flag = 3;
						if(m.killed()){  //怪物被搞掉
							AppStatic.monsterarr.remove(this.m);
							Score s=new Score(100, m.m_x,m.m_y, AppStatic.ga);
							AppStatic.scorearr.add(s);
							this.flag = false;
						}
					}
					mt_time++;
					if(mt_time == 12){
						mt_time = 0;
						t_flag = !t_flag;
					}
					MonsterThread.sleep(this.sleep_time);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
}
