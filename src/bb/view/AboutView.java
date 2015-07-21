package bb.view;


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

//关于界面
public class AboutView extends SurfaceView implements SurfaceHolder.Callback,Runnable{
	private SurfaceHolder sfh;
	private Canvas canvas;
	private Paint paint;
	private Thread th;
	private GameActivity father;
	public int spandtime =10;//滚动间隔时间
	public boolean flag = false;  //视图运行标识
	public int y=0;  //一级页面初始偏移量
	public int ty=480;  //二级页面初始偏移量
	private Bitmap about;
	private Bitmap back;
	
	public AboutView(GameActivity father) {
		super(father);
		this.father=father;
		this.setKeepScreenOn(true);
		sfh = this.getHolder();
		sfh.addCallback(this);
		paint = new Paint();
		paint.setAntiAlias(true);
		about=BitmapFactory.decodeResource(getResources(), R.drawable.about_back);
		back=BitmapFactory.decodeResource(getResources(), R.drawable.back);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		System.out.println(event.getX()+"  "+event.getY());
		if(event.getX()>=750&&event.getX()<=800&&event.getY()>=420&&event.getY()<=480)
		{
			if(AppStatic.music==0)
			{
				father.sndPool.play(father.click,1,1,0,0,(float)1);
			}
			father.onBackPressed();
		}
		return super.onTouchEvent(event);
	}


	@Override
	public void run() {
		boolean wait = true;
		// TODO Auto-generated method stub
		while(flag)
		{
			canvas = sfh.lockCanvas();//获得画布
			if(y==-1202)//该阶段降低滚动速度
			{
				spandtime=3000;//提高线程休眠时间,减慢速度
				wait=false;
			}
			if(y<=-1567)//一级页面滚动到底
			{
				canvas.drawBitmap(about,0,ty,null);//开始画二级页面，实现循环
				ty--;
			}
			if(ty==0)//二级页面置顶
			{
				ty=480;//重置二级页面初始偏移量
				y=0;//重置一级页面初始偏移量
			}
			if(y<=0&&wait)
			{
				canvas.drawBitmap(about,0,y,null);//开始画一级页面，实现循环
				y--;
			}
			canvas.drawBitmap(back, 750, 430, null);
			try {
				Thread.sleep(spandtime);
				if(spandtime==3000)
				{
					wait=true;
					spandtime=10;
					y--;
				}
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

}
