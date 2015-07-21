package bb.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import bb.activity.GameActivity;
import bb.activity.R;
import bb.thread.ScoreThread;

public class Score {
	public int score; //分数
	public int x;  //坐标x
	public int y;  //坐标y
	public Bitmap bitscore;  //图片
	public ScoreThread s_th;  //线程
	GameActivity ga;
	
	public Score(int score, int x, int y,GameActivity ga){
		this.x = x;
		this.y = y;
		this.ga=ga;
		this.score = score;
		initBitMap(score);
		s_th = new ScoreThread(this);
		s_th.start();
	}
	
	private void initBitMap(int score){
		switch(score)
		{
		case 100:bitscore=BitmapFactory.decodeResource(ga.getResources(), R.drawable.score_100);break;
		case 50:bitscore=BitmapFactory.decodeResource(ga.getResources(), R.drawable.score_50);break;
		case 150:bitscore=BitmapFactory.decodeResource(ga.getResources(), R.drawable.score_150);break;
		case 200:bitscore=BitmapFactory.decodeResource(ga.getResources(), R.drawable.score_200);break;
		case 1000:bitscore=BitmapFactory.decodeResource(ga.getResources(), R.drawable.score_1000);break;
		}
	}
	
	@Override
	protected void finalize(){
		
	}
	
	public void drawSelf(Canvas canvas){
		canvas.drawBitmap(bitscore, x, y,null);
	}
}
