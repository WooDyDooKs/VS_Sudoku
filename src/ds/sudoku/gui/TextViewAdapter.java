package ds.sudoku.gui;

import android.content.Context;
import android.opengl.Visibility;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;

public class TextViewAdapter extends BaseAdapter{
	Context context;
	View[] cells;
	int size;
	
	public TextViewAdapter(Context context, View[] cells, int size) {
		this.context = context;
		this.cells = cells;
		this.size = size;
	}
	
	public int getCount() {
		return 81;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View target, ViewGroup parent) {
	    if ( target == null ) {
	    	TextView tv = (TextView)cells[position];
        	tv.setGravity(Gravity.CENTER);
        	tv.setTag(position);
	        tv.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT, size));    
	        return tv;
	    } else {
	    	return target;
	    }
	}
}
