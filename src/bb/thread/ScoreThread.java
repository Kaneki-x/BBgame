package bb.thread;

import bb.model.Score;
import bb.utils.AppStatic;

public class ScoreThread extends Thread{
	private Score score;
	private boolean flag=false;
	private float speed=3;
	private float time=0;
	private float scoretime=0;
	public ScoreThread(Score score) {
		super();
		this.score=score;
		this.flag=true;
		// TODO Auto-generated constructor stub
		 
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(flag)
		{	
			scoretime+=10;
			try{
			if(scoretime%50==0)
			{
					if(score.x-195>10)
					{
						score.x-=speed;
					}
					if(score.x-195<-10)
					{
						score.x+=speed;
					}
					if(score.y-5>15)
					{
						score.y-=speed;
					}
					if(score.y-5<-15)
					{
						score.y+=speed;
					}
					if(Math.abs(score.x-195)<20&&Math.abs(score.y-5)<20)
					{
						flag=false;
						AppStatic.gamepoints+=score.score;
						AppStatic.scorearr.remove(score);
					}
					time+=10;
					if(time%50==0)
					{
						speed+=2;
					}
			}
				Thread.sleep(10);
			}catch(Exception e){
				
			}
		}
	}

}
