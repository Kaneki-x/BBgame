package bb.thread;

import bb.model.Bullet;
import bb.utils.AppStatic;


//子弹运动线程
public class BulletThread extends Thread{
	public boolean flag=false;//循环标记
	private Bullet blt;//子弹对象
	private int time_out;  //子弹存在时间
	public int sleep_time;  //线程休眠时间
	
	public BulletThread(Bullet blt) {
		super();
		this.blt=blt;
		this.flag=true;
		this.time_out = 150;
		this.sleep_time = AppStatic.Sleep_time;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(flag){
			if(!AppStatic.pause)
			{
				try{
					blt.move();
					time_out--;
					if(blt.bulletState == 1)
						time_out = 0;
					if(time_out <= 0){
						AppStatic.bulletarr.remove(this.blt);
						this.flag = false;
					}
					BulletThread.sleep(this.sleep_time);
				}catch(Exception e){
					
				}
			}
		}
		AppStatic.max_b++;
	}

}
