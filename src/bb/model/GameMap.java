package bb.model;

import java.util.ArrayList;
import bb.utils.AppStatic;
import android.graphics.Bitmap;
import android.graphics.Canvas;

//地图模型类
public class GameMap {
	public int start_x;  //地图起始x坐标
	public int start_y;  //地图起始x坐标
	public int height;  //地图高度
	public int width;  //地图宽度
	public int border_w;  //地图边界宽度
	public int score;  //游戏分数
	public Bitmap mapback;  //地图背景
	public ArrayList<Baffle> bafflearr = new ArrayList<Baffle>();  //挡板数组
	
	//地图初始化1
	public GameMap(GameMap gamemap){
		this.score=0;
		this.start_x = gamemap.start_x;
		this.start_y = gamemap.start_y;
		this.width = AppStatic.map_width;
		this.height = AppStatic.map_height;
		this.mapback = gamemap.mapback;
		this.bafflearr = gamemap.bafflearr;
	}
	
	//地图初始化2
	public GameMap(Bitmap mapback, int start_x, int start_y, ArrayList<Baffle> bafflearr){
		this.start_x = start_x;
		this.start_y = start_y;
		this.width = AppStatic.map_width;
		this.height = AppStatic.map_height;
		this.border_w = AppStatic.border_w;
		this.mapback = mapback;
		this.bafflearr = bafflearr;
	}
	
	public void drewself(Canvas canvas){
		int i;
		canvas.drawBitmap(this.mapback, start_x, start_y, null);
		for(i=0;i<bafflearr.size();i++){
			if(bafflearr.get(i).flag == 2)
				bafflearr.get(i).drewself(canvas);
		}
		for(i=0;i<bafflearr.size();i++){
			if(bafflearr.get(i).flag != 2)
				bafflearr.get(i).drewself(canvas);
		}
	}
	
	public void drewself(Canvas canvas, int up_y){
		int i;
		canvas.drawBitmap(this.mapback, start_x, start_y+up_y, null);

		for(i=0;i<bafflearr.size();i++){
			if(bafflearr.get(i).flag == 2)
				bafflearr.get(i).drewself(canvas,up_y);
		}
		for(i=0;i<bafflearr.size();i++){
			if(bafflearr.get(i).flag != 2)
				bafflearr.get(i).drewself(canvas,up_y);
		}
	}
}
