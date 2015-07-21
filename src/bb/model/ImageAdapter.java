package bb.model;

import bb.activity.R;
import android.content.Context;
 
import android.view.View;
import android.view.ViewGroup; 
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
 

 
public class ImageAdapter extends BaseAdapter {
 
        private Context mContext; //define Context 
 

 
    private Integer[] mImageIds = { //picture source 
            R.drawable.model_1,
            R.drawable.model_2,
            R.drawable.model_3,
            R.drawable.model_4
 
    };
 
    public ImageAdapter(Context c) { //define ImageAdapter
 
        mContext = c;
 
    }
    //get the picture number
 
    public int getCount() { 
 
        return mImageIds.length;
 
    }
 
    public Object getItem(int position) {
 
        return position;
 
    }
 
    public long getItemId(int position) {
 
        return position;
 
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
 
        ImageView i = new ImageView(mContext);
        i.setAlpha(250);
        i.setImageResource(mImageIds[position]);//set resource for the imageView
        i.setLayoutParams(new Gallery.LayoutParams(300, 300));//layout
        i.setScaleType(ImageView.ScaleType.FIT_XY);//set scale type
        return i;
 
    }
 
}
