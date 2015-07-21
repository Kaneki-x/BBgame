package bb.view;

import java.util.ArrayList;
import bb.activity.GameActivity;
import bb.activity.R;
import bb.utils.AppStatic;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

//欢迎界面
public class WelcomeView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

	private SurfaceHolder sfh;
	private Canvas canvas;
	private Paint paint;
	private Thread th;
	private GameActivity father;
	public boolean flag = false;  //视图运行标识
	private int welcometime=2000;//欢迎界面等待时间
	private int press=0;//点击的图标编号
	
	private ArrayList<Bitmap> wp = new ArrayList<Bitmap>();
	
	public WelcomeView(GameActivity father) {
		super(father);
		// TODO Auto-generated constructor stub
		this.father = father;
		this.setKeepScreenOn(true);
		sfh = this.getHolder();
		sfh.addCallback(this);
		paint = new Paint();
		paint.setAntiAlias(true);
		initBitmap();
	}

	public void initBitmap(){//载入图片资源
		wp.add(0,BitmapFactory.decodeResource(getResources(), R.drawable.welcome));
		wp.add(1,BitmapFactory.decodeResource(getResources(), R.drawable.mainback));
		wp.add(2,BitmapFactory.decodeResource(getResources(), R.drawable.play));
		wp.add(3,BitmapFactory.decodeResource(getResources(), R.drawable.option));
		wp.add(4,BitmapFactory.decodeResource(getResources(), R.drawable.help));
		wp.add(5,BitmapFactory.decodeResource(getResources(), R.drawable.about));
		wp.add(6,BitmapFactory.decodeResource(getResources(), R.drawable.play1));
		wp.add(7,BitmapFactory.decodeResource(getResources(), R.drawable.option1));
		wp.add(8,BitmapFactory.decodeResource(getResources(), R.drawable.help1));
		wp.add(9,BitmapFactory.decodeResource(getResources(), R.drawable.about1));
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		th = new Thread(this);
		flag = true;
		th.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		flag = false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN&&AppStatic.weclome){
			int x = (int)event.getX();
			int y = (int)event.getY();
			if(x>450&&x<728){
				if(y>170&&y<232){
					press=6;
					if(AppStatic.music==0)
					{
						father.sndPool.play(father.click,1,1,0,0,(float)1);
					}
				}
				if(y>240&&y<302){
					if(AppStatic.music==0)
					{
						father.sndPool.play(father.click,1,1,0,0,(float)1);
					}
					press=7;
				}
				if(y>310&&y<372){
					press=8;
					if(AppStatic.music==0)
					{
						father.sndPool.play(father.click,1,1,0,0,(float)1);
					}
				}
				if(y>380&&y<442){
					press=9;
					if(AppStatic.music==0)
					{
						father.sndPool.play(father.click,1,1,0,0,(float)1);
					}
				}
			}
		}
		return true;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int i;
		
		while(flag){
			canvas = sfh.lockCanvas();
			if(welcometime!=0&&!AppStatic.weclome)
			{
				canvas.drawBitmap(wp.get(0), 0, 0, paint);
				welcometime-=50;
			}
			else{
				AppStatic.weclome=true;
				if(press==0)
				{
					canvas.drawBitmap(wp.get(1), 0, 0, paint);
					for(i=2; i<=5; i++){
						canvas.drawBitmap(wp.get(i),450,70*(i-1)+100,paint);
					}
				}
				else{
					canvas.drawBitmap(wp.get(1), 0, 0, paint);
					switch(press)
					{
					case 6:for(i=2; i<=5; i++){
							if(i==2)
							{
								canvas.drawBitmap(wp.get(6),450,70*(2-1)+100, paint);
							}
							else{
								canvas.drawBitmap(wp.get(i),450,70*(i-1)+100, paint);
							}
							};try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}press=0;AppStatic.weclome=true;father.Handler.sendEmptyMessage(0);flag=false;break;
					case 7:for(i=2; i<=5; i++){
						if(i==3)
						{
							canvas.drawBitmap(wp.get(7),450,70*(3-1)+100,paint);
						}
						else{
							canvas.drawBitmap(wp.get(i),450,70*(i-1)+100,paint);
						}
						};try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}press=0;father.Handler.sendEmptyMessage(1);break;
					case 8:for(i=2; i<=5; i++){
						if(i==4)
						{
							canvas.drawBitmap(wp.get(8),450,70*(4-1)+100, paint);
						}
						else{
							canvas.drawBitmap(wp.get(i),450,70*(i-1)+100, paint);
						}
						};try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}press=0;AppStatic.weclome=true;father.Handler.sendEmptyMessage(9);break;
					case 9:for(i=2; i<=5; i++){
						if(i==5)
						{
							canvas.drawBitmap(wp.get(9),450,70*(5-1)+100,paint);
						}
						else{
							canvas.drawBitmap(wp.get(i),450,70*(i-1)+100,paint);
						}
						};try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}press=0;AppStatic.weclome=true;father.Handler.sendEmptyMessage(7);break;
					
					}
				}
			}
			try {
				Thread.sleep(50);
			} catch (Exception ex) {
			}finally {
				try {
					if (canvas != null)
						sfh.unlockCanvasAndPost(canvas);
				} catch (Exception e2) {

				}
			}
		}
	}

}