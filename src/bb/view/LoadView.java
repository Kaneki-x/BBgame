package bb.view;

import java.util.ArrayList;

import bb.activity.GameActivity;
import bb.activity.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

//加载界面
public class LoadView extends  SurfaceView implements SurfaceHolder.Callback,Runnable{
	private SurfaceHolder sfh;
	private Canvas canvas;
	private Paint paint;
	private Thread th;
	private GameActivity father;
	public boolean flag = false;  //视图运行标识
	public boolean flow = false;  //气球上下飘动标记
	public int y=30 ;  //最大飘动高度
	public int present=0 ;  //载入进度
	public int time=0 ;  //载入辅助时间
	private ArrayList<Bitmap> lp = new ArrayList<Bitmap>();
	
	
	public LoadView(GameActivity father) {
		super(father);
		// TODO Auto-generated constructor stub
		this.father=father;
		this.setKeepScreenOn(true);
		sfh = this.getHolder();
		sfh.addCallback(this);
		paint = new Paint();
		paint.setColor(Color.RED);
		paint.setTextSize(50);
		paint.setFakeBoldText(true); 
		paint.setAntiAlias(true);
		initBitmap();
	}

	public void initBitmap(){//载入图片资源
		lp.add(0,BitmapFactory.decodeResource(getResources(), R.drawable.load_back));
		lp.add(1,BitmapFactory.decodeResource(getResources(), R.drawable.load_ball_1));
		lp.add(2,BitmapFactory.decodeResource(getResources(), R.drawable.load_ball_2));
		lp.add(3,BitmapFactory.decodeResource(getResources(), R.drawable.loading));
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(flag)
		{
			canvas = sfh.lockCanvas();//获得画布
			canvas.drawBitmap(lp.get(0), 0,0,null);//画背景
			if(y>=0&&!flow)//气球上飘
			{
				y--;
				canvas.drawBitmap(lp.get(2),250,y,null);
				canvas.drawBitmap(lp.get(3),380,y+180,null);
				time++;
				if(time%2==0||time%3==0||time%5==0)
				{
					present+=8;
					canvas.drawText(present+"%",450, y+115, paint);
				}
				else{
					present+=3;
					canvas.drawText(present+"%",450, y+115, paint);
				}
				if(y==0)
				{
					flow=true;
				}
			}
			if(flow)//气球下飘
			{
				y++;
				canvas.drawBitmap(lp.get(2),250,y,null);
				canvas.drawBitmap(lp.get(3),380,y+180,null);
				if(y==30)
				{
					flow=false;
				}
			}
			if(present>=100)//载入完成
			{
				flag=false;//暂停线程
				father.Handler.sendEmptyMessage(5);	//回调开始游戏
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
