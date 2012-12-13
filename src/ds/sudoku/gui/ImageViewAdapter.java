package ds.sudoku.gui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageViewAdapter extends BaseAdapter{
	Context context;
	int size;
	
	public ImageViewAdapter(Context context, int size, int activeNumber, boolean insertActive, boolean deleteActive, boolean pencilMarks) {
		this.context = context;
		this.size = size;
		if (pencilMarks) {
			pics[11] = R.drawable.btn_pen_p;
		}
		if (deleteActive) {
			pics[7] = R.drawable.btn_del_p;
		}
		if (insertActive) {
			pics[3] = R.drawable.btn_ins_p;
		}
		switch (activeNumber) {
			case 0: break;
			case 1: pics[0] = R.drawable.btn_1_p; break;
			case 2: pics[1] = R.drawable.btn_2_p; break;
			case 3: pics[2] = R.drawable.btn_3_p; break;
			case 4: pics[4] = R.drawable.btn_4_p; break;
			case 5: pics[5] = R.drawable.btn_5_p; break;
			case 6: pics[6] = R.drawable.btn_6_p; break;
			case 7: pics[8] = R.drawable.btn_7_p; break;
			case 8: pics[9] = R.drawable.btn_8_p; break;
			case 9: pics[10] = R.drawable.btn_9_p; break;
			default: break;
		}
	}

	public int getCount() {
		return 12;
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public ImageView getView(int position, View target, ViewGroup parent) {
		ImageView imageView;
        if (target == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
//            imageView.getLayoutParams().height = size;
//            imageView.getLayoutParams().width = LayoutParams.MATCH_PARENT;            
            imageView.setLayoutParams(new GridView.LayoutParams(size, size));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            
        } else {
            imageView = (ImageView) target;
        }

        imageView.setImageResource(pics[position]);
        return imageView;
	}
	
	private Integer[] pics = {
            R.drawable.btn_1, R.drawable.btn_2, R.drawable.btn_3, R.drawable.btn_ins,
            R.drawable.btn_4, R.drawable.btn_5, R.drawable.btn_6, R.drawable.btn_del,
            R.drawable.btn_7, R.drawable.btn_8, R.drawable.btn_9, R.drawable.btn_pen,
    };

}
