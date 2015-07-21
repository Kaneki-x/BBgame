package bb.model;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Welcome {

	public int start_x = 0;
	public int start_y = 0;
	public int width = 800;
	public int height = 480;
	ArrayList<Bitmap> wp;
	
	public Welcome(ArrayList<Bitmap> wp, int start_x, int start_y, int width, int height){
		this.wp = wp;
		this.start_x = start_x;
		this.start_y = start_y;
		this.width = width;
		this.height = height;
	}
	
	public void drewself(Canvas canvas){
		//canvas.drawBitmap( start_x, start_y, null);
	}
	
}
