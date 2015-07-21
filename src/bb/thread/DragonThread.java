package bb.thread;

import bb.model.Dragon;
import bb.utils.AppStatic;

public class DragonThread extends Thread{
	public boolean flag=false;  //线程状态标识
	//public boolean t_flag;  //时间标识，用于更改龙图片
	public int mt_time;  //计时，用于更改龙图片
	public boolean is_show;  //是否显示
	public float t=AppStatic.Sleep_time/1000f;//速度计算时间
	public Dragon dragon;
	public int sleep_time;  //线程休眠时间
	
	public DragonThread(Dragon dragon){
		this.dragon = dragon;
		this.flag = true;
		this.mt_time = 200;
		this.is_show = true;
		this.sleep_time = AppStatic.Sleep_time;
	}
	
	public void run() {
		while(flag){
			if(!AppStatic.pause && !dragon.is_stop)
			{
				try{
					if(AppStatic.sensor==0)
						dragon.move();
					else
						dragon.vrMove();
					
					if(AppStatic.JumpState){
						if(dragon.jforward)
							dragon.jump();
						else
							dragon.drop();
					}
					if(dragon.is_NB){  //处于NB状态
						if(mt_time%5 == 0){
							is_show = !is_show;
						}
						mt_time--;
						if(mt_time <= 0){  //复活并减去生命
							dragon.is_NB = false;
							dragon.is_stop = false;
							mt_time = 200;
							is_show = true;
						}
						if(!is_show)
							dragon.setstatus(10);  //更改显示图片
					}else{
						dragon.collision();
						if(AppStatic.ghost != null)
							dragon.D_Gcollision();
					}
					MonsterThread.sleep(this.sleep_time);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
}