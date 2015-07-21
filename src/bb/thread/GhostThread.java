package bb.thread;

import bb.model.Ghost;
import bb.utils.AppStatic;

public class GhostThread extends Thread{
	public boolean flag;  //线程状态标识
	public Ghost ghost;  //怪物对象
	public int change_t;  //图片切换时间

	public GhostThread(Ghost ghost){
		this.flag = true;
		this.ghost = ghost;
		this.change_t = 10;
	}
	
	public void run(){
		while(flag){
			if(!AppStatic.pause)
			{
				change_t--;
				if(change_t < 0){
					ghost.bitnum++;
					if(ghost.bitnum > 1)
						ghost.bitnum = 0;
					change_t = 10;
				}
				ghost.move();
				try{
					MonsterThread.sleep(AppStatic.Sleep_time);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
}
