package bb.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import bb.activity.R;
import bb.utils.AppStatic;

//背景音乐服务
public class MusicService extends Service{
	MediaPlayer mediaPlayer=null;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		int Msg=intent.getIntExtra("MSG",0);
		if(Msg==AppStatic.PLAY_MSG&&AppStatic.music==0)
			play();
		else if(Msg==AppStatic.STOP_MSG&&AppStatic.music==1)
			stop();
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void play(){
		mediaPlayer=MediaPlayer.create(this, R.raw.background);
		mediaPlayer.setLooping(true);
		mediaPlayer.start();
	}
	
	private void stop(){
		mediaPlayer.stop();
		mediaPlayer.release();
	}
}
