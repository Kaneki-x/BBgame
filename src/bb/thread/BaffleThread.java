package bb.thread;

import bb.model.Baffle;
import bb.utils.AppStatic;

public class BaffleThread extends Thread{
	Baffle baffle;
	boolean flag;
	public BaffleThread(Baffle baffle) {
		super();
		this.baffle=baffle;
		flag=true;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(flag)
		{
			if(baffle.start_x+baffle.length>=(AppStatic.map_width-baffle.move_rx))
			{
				baffle.forward=false;
			}
			if(baffle.start_x<=baffle.move_lx)
			{
				baffle.forward=true;
			}
			if(baffle.forward)
			{
				baffle.start_x+=baffle.speed;
			}
			else{
				baffle.start_x-=baffle.speed;
			}
			try{
				Thread.sleep(20);
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
