package bb.model;

import bb.utils.AppStatic;
import android.graphics.Canvas;
import android.graphics.Paint;

//模拟控制器模型类
public class VirtualRocker {
	//固定摇杆背景圆形的X,Y坐标以及半径
	public int RockerCircleX = AppStatic.vr_x;
	public int RockerCircleY = AppStatic.vr_y;
	public int RockerCircleR = 50;
	//摇杆的X,Y坐标以及摇杆的半径
	public float SmallRockerCircleX = AppStatic.vr_x;
	public float SmallRockerCircleY = AppStatic.vr_y;
	public float SmallRockerCircleR = 20;
	//发射背景的X,Y坐标以及半径
	public int shotCircleX = AppStatic.vr_bx;
	public int shotCircleY = AppStatic.vr_by;
	public int shotCircleR = 40;
	private Paint paint;
	/***
	 * 虚拟摇杆类的构造方法
	 */
	public VirtualRocker() {
		paint=new Paint();
	}
	/***
	 * 得到两点之间的弧度
	 */
	public double getRad(float px1, float py1, float px2, float py2) {
		//得到两点X的距离
		float x = px2 - px1;
		//得到两点Y的距离
		float y = py1 - py2;
		//算出斜边长
		float xie = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		//得到这个角度的余弦值（通过三角函数中的定理 ：邻边/斜边=角度余弦值）
		float cosAngle = x / xie;
		//通过反余弦定理获取到其角度的弧度
		float rad = (float) Math.acos(cosAngle);
		//注意：当触屏的位置Y坐标<摇杆的Y坐标我们要取反值-0~-180
		if (py2 < py1) {
			rad = -rad;
		}
		return rad;
	}
	
	/**
	 * 
	 * @param R
	 *            圆周运动的旋转点
	 * @param centerX
	 *            旋转点X
	 * @param centerY
	 *            旋转点Y
	 * @param rad
	 *            旋转的弧度
	 */
	public void getXY(float centerX, float centerY, float R, double rad) {
		//获取圆周运动的X坐标 
		SmallRockerCircleX = (float) (R * Math.cos(rad)) + centerX;
		//获取圆周运动的Y坐标
		SmallRockerCircleY = (float) (R * Math.sin(rad)) + centerY;
	}
	public void drawSelf(Canvas canvas) {
		try {
			//设置透明度
			paint.setColor(0x70000000);
			//绘制摇杆背景
			canvas.drawCircle(RockerCircleX, RockerCircleY, RockerCircleR, paint);
			canvas.drawCircle(shotCircleX, shotCircleY, shotCircleR, paint);
			paint.setColor(0x70ff0000);
			//绘制摇杆
			canvas.drawCircle(SmallRockerCircleX, SmallRockerCircleY, SmallRockerCircleR, paint);
		} catch (Exception e) {
			// TODO: handle exception
		} 
	}
	
}
