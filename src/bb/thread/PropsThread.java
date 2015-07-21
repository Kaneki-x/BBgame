package bb.thread;

import bb.model.Props;
import bb.utils.AppStatic;

public class PropsThread extends Thread {
	public boolean flag = false;  //线程运行标识
	public Props Pr;  //道具对象
	public int time_out;  //道具存在时间
	public boolean is_show;  //是否显示
	public int sleep_time;  //线程休眠时间
	public boolean is_use;  //道具效果是否启动
	public int use_time;  //道具效果有效时间
	
	public PropsThread(Props Pr){
		this.flag = true;
		this.Pr = Pr;
		this.Pr.setPinfo(Pr.Pr_flag);
		this.time_out = 500;
		this.is_show = true;
		this.sleep_time = AppStatic.Sleep_time;
		this.is_use = false;
		this.use_time = 200;
	}
	
	@Override
	public void run(){
		while(flag){
			if(!AppStatic.pause)
			{
				try{
					if(Pr.is_valid){  //道具存在且未被吃掉
						time_out--;
						if(time_out <= 100){
							if(time_out%10 == 0){
								is_show = !is_show;
							}
							if(time_out<=0){  //道具消失
								flag = false;
								AppStatic.props = null;
							}
						}
						if(is_show)
							Pr.setPinfo(Pr.Pr_flag);
						else
							Pr.setPinfo(10);
						if(Pr.eatProps())
							time_out = 20;
					}else{  //道具被吃掉，启动道具效果
						time_out--;
						if(time_out <= 0){
							is_show = false;
						}
						use_time--;
						if(use_time < 0){
							Pr.recover(Pr.Pr_flag);
							flag = false;
							AppStatic.props = null;
						}
					}
					Pr.move();
					BulletThread.sleep(this.sleep_time);
				}catch(Exception e){
					
				}
			}
		}
	}

}
