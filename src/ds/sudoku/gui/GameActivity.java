package ds.sudoku.gui;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

//	spielbeginn: sudoku wird komplett neu gezeichnet: drawSudoku
//	immer wenn man selber eine zahl einfügt, zeichne diese: setOwnNumber
//	immer wenn gegner eine zahl einfügt, zeichne diese: setOpponentNumber
//	immer wenn eine zahl gelöscht wird, wird alles neu gezeichnet: deleteNumber
//	immer wenn ein pencilmark gelöscht wird, wird alles neu gezeichnet: changePencilMark
//	immer wenn ein pencilmark eingefügt wird, zeichne diesen: changePencilMark


public class GameActivity extends Activity {
	ImageButton ib_sudoku;
	ImageView iv_sudoku;
	GridView numPad;
	
	Paint p;
	
	int screenWidth, screenHeight;
	float cellSize, numberPosOffsetX, numberPosOffsetY;;
	float pencilMarkSize;
	
	int currentNumber = 0;
	boolean numberActive, deleteActive, pencilActive;	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        
        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        int minSize = Math.min(screenWidth, screenHeight)-10;
        int maxSize = Math.max(screenWidth, screenHeight)-10;
        cellSize = minSize/9 - 1;
        numberPosOffsetX = cellSize/1.33f;
        numberPosOffsetY = cellSize/7.5f;
        pencilMarkSize = cellSize/9 - 1;
        
        setupSudokuGrid(minSize);
        setupNumberPad(minSize, maxSize);
        
        p = new Paint(Color.BLACK);
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_game, menu);
        return true;
    }
    
    private void setupSudokuGrid(int size) {
    	
    }
    
    private void setupNumberPad(int width, int height) {

	}
    
    private void drawSudoku() {
    	for (int i = 1; i < 10; i++) {
    		for (int j = 1; j < 10; j++) {
    			BitmapDrawable bmd = (BitmapDrawable)iv_sudoku.getDrawable();
    	        Bitmap bm = bmd.getBitmap();
    	        Bitmap bm_copy = bm.copy(bm.getConfig(), true);
    	    	Canvas c = new Canvas(bm_copy);
    			// Cell cell = Sudoku.getCell(i,j);
    			// get for each cell the values
    			// drawNumber(c,i,j,Sudoku.getNumber(i,j));
    			// drawPencilMarks(c,i,j,Sudoku.getPencilMarks(i,j));
    			// iv_sudoku.setBackgroundColor(Sudoku.getBackground(i,j));
    	    	iv_sudoku.setImageBitmap(bm_copy);
    		}
    	}
    }
    
    private void drawPencilMarks(Canvas c, int x, int y, boolean[] pm) {//TODO fertig machen
    	Log.d("pencilmarks", x+","+y+","+pencilMarkSize);
    	for (int i=0; i<pm.length; i++) {
    		if (pm[i]) {
    	    	p.setColor(Color.BLACK);
    	    	p.setTextSize(pencilMarkSize);
    	    	switch(i) {
    	    	case 1:
    	    		c.drawText(Integer.toString(i), (int)(x*cellSize), (int)(y*cellSize), p);
    	    		break;
    	    	case 2:
    	    		c.drawText(Integer.toString(i), (int)(x*cellSize), (int)(y*cellSize), p);
    	    		break;
    	    	case 3:
    	    		c.drawText(Integer.toString(i), (int)(x*cellSize), (int)(y*cellSize), p);
    	    		break;
    	    	case 4:
    	    		c.drawText(Integer.toString(i), (int)(x*cellSize), (int)(y*cellSize), p);
    	    		break;
    	    	case 5:
    	    		c.drawText(Integer.toString(i), (int)(x*cellSize), (int)(y*cellSize), p);
    	    		break;
    	    	case 6:
    	    		c.drawText(Integer.toString(i), (int)(x*cellSize), (int)(y*cellSize), p);
    	    		break;
    	    	case 7:
    	    		c.drawText(Integer.toString(i), (int)(x*cellSize), (int)(y*cellSize), p);
    	    		break;
    	    	case 8:
    	    		c.drawText(Integer.toString(i), (int)(x*cellSize), (int)(y*cellSize), p);
    	    		break;
    	    	case 9:
    	    		c.drawText(Integer.toString(i), (int)(x*cellSize), (int)(y*cellSize), p);
    	    		break;
    	    	default:
    	    		break;
    	    	}
    		}
    	}
    }
    
    private void drawPencilMark(int x, int y, int pm) {
    	Log.d("pencilmarks", x+","+y+","+pencilMarkSize);
    	BitmapDrawable bmd = (BitmapDrawable)iv_sudoku.getDrawable();
        Bitmap bm = bmd.getBitmap();
        Bitmap bm_copy = bm.copy(bm.getConfig(), true);
    	Canvas c = new Canvas(bm_copy);
    	p.setColor(Color.BLACK);
    	p.setTextSize(pencilMarkSize);
    	switch(pm) {
    	case 1:
    		c.drawText(Integer.toString(pm), (int)(x*cellSize), (int)(y*cellSize), p);
    		break;
    	case 2:
    		c.drawText(Integer.toString(pm), (int)(x*cellSize), (int)(y*cellSize), p);
    		break;
    	case 3:
    		c.drawText(Integer.toString(pm), (int)(x*cellSize), (int)(y*cellSize), p);
    		break;
    	case 4:
    		c.drawText(Integer.toString(pm), (int)(x*cellSize), (int)(y*cellSize), p);
    		break;
    	case 5:
    		c.drawText(Integer.toString(pm), (int)(x*cellSize), (int)(y*cellSize), p);
    		break;
    	case 6:
    		c.drawText(Integer.toString(pm), (int)(x*cellSize), (int)(y*cellSize), p);
    		break;
    	case 7:
    		c.drawText(Integer.toString(pm), (int)(x*cellSize), (int)(y*cellSize), p);
    		break;
    	case 8:
    		c.drawText(Integer.toString(pm), (int)(x*cellSize), (int)(y*cellSize), p);
    		break;
    	case 9:
    		c.drawText(Integer.toString(pm), (int)(x*cellSize), (int)(y*cellSize), p);
    		break;
    	default:
    		break;
    	}
    }
    
    private void drawNumber(Canvas c, int x, int y, int number, int color) {
		Log.d("clicked in grid", x+","+y+","+cellSize);
    	Paint p = new Paint();
    	p.setColor(color);
    	p.setTextSize(cellSize*5/5);
    	c.drawText(Integer.toString(number), (int)(x*cellSize-numberPosOffsetX), (int)(y*cellSize-numberPosOffsetY), p);    	
    }
    
    private void drawNumber(int x, int y, int number, int color) {
		Log.d("clicked in grid", x+","+y+","+cellSize);
    	BitmapDrawable bmd = (BitmapDrawable)iv_sudoku.getDrawable();
        Bitmap bm = bmd.getBitmap();
        Bitmap bm_copy = bm.copy(bm.getConfig(), true);
    	Canvas c = new Canvas(bm_copy);
    	Paint p = new Paint();
    	highlight(c, x, y);
    	p.setColor(color);
    	p.setTextSize(cellSize*5/5);
    	c.drawText(Integer.toString(number), (int)(x*cellSize-numberPosOffsetX), (int)(y*cellSize-numberPosOffsetY), p);
    	iv_sudoku.setImageBitmap(bm_copy);	
    }
    
    private void highlight(Canvas c, int x, int y) {
    	p.setColor(Color.YELLOW);
    	p.setAlpha(75);
    	c.drawRect((x-1)*cellSize+1, (y-1)*cellSize+1, x*cellSize-1, y*cellSize-1, p);
    	p.setAlpha(255);
    }
    
    private void setOpponentNumber(int x, int y, int number) {
    	drawNumber(x, y, number, Color.RED);
    }
    
    private void setOwnNumber(int x, int y) {
    	//Sudoku.setNumber(x,y,currentNumber);
    	drawNumber(x, y, currentNumber, Color.GREEN);
    }
    
    private void deleteNumber(int x, int y) {
    	//Sudoku.deleteNumber(x,y);
    	drawSudoku();
    }
    
    private void changePencilMark(int x, int y, int number) {
    	//if(Sudoku.getPencilMarks(x,y).get(number-1)) {
    	//	Sudoku.deletePencilMark(x,y,number);
    		drawSudoku();
    	//} else {
    	//	Sudoku.setPencilMark(x,y,number);
    	//}
    }
}
