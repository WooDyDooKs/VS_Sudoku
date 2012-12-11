package ds.sudoku.gui;

import ds.sudoku.gui.service.SudokuService;
import ds.sudoku.gui.service.SudokuService.SudokuServerBinder;
import ds.sudoku.logic.CellInfo;
import ds.sudoku.logic.SampleSudokuFactory;
import ds.sudoku.logic.SudokuChangeListener;
import ds.sudoku.logic.SudokuChangedEvent;
import ds.sudoku.logic.SudokuChangedEvent.Change;
import ds.sudoku.logic.SudokuHandler;
import ds.sudoku.logic.SudokuTemplate;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;


public class GameActivity extends Activity {
	GridView gv_sudoku, gv_numPad;
	TextView[] cells;
	
	TextViewAdapter va_sudoku;
	int screenWidth, screenHeight;
	int numPadSize;
	
	int currentNumber = 0;
	boolean pencilMarksOn = false;
	boolean insertActive = true, pencilActive = false, deleteActive = false;
	
	//colors
	final static int COLOR_BG_WIN = Color.rgb(0x78, 0xD2, 0xFF);
	final static int COLOR_BG_LOSE = Color.rgb(0xFF, 0xB2, 0xB2);
	final static int COLOR_BG_5 = Color.WHITE;
	final static int COLOR_BG_4 = Color.rgb(200, 200, 200);
	final static int COLOR_PM_BG_GREY = Color.rgb(225, 180, 125);	
	final static int COLOR_PM_BG_WHITE = Color.rgb(255, 210, 155);
	final static int COLOR_PM = Color.BLACK;	
	final static int COLOR_D_BG_GREY = Color.rgb(220, 220, 0);
	final static int COLOR_D_BG_WHITE = Color.YELLOW;
	final static int COLOR_D_CLUE = Color.BLACK;
	final static int COLOR_D_HIDDEN = Color.rgb(0x78, 0xD2, 0xFF);
	final static int COLOR_D_OWN = Color.BLUE;
	final static int COLOR_D_OPPONENT = Color.RED;
	
	
	////LOGIC
	SudokuHandler sudoku;
	
    private ServiceConnection serviceConnection = new ServiceConnection() {	
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			SudokuServerBinder sudokuService = (SudokuServerBinder) service;
			sudoku = sudokuService.getSudokuHandler();
			if(sudoku == null) {
				finish();
				return;
			}
	        initGUI();
			// register sudoku change listener
			sudoku.addSudokuChangeListener(scl);
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
			sudoku = null;
			finish();
		}
	};
	
	private SudokuChangeListener  scl = new SudokuChangeListener() {
    	
		public void onSudokuChanged(SudokuChangedEvent e) {
			int row = e.row;
			int column = e.column;
			int pos = calculatePosition(row-1, column-1);
			Change change = e.change;
			TextView tv = (TextView)gv_sudoku.findViewWithTag(pos);	//glocal variable
			switch(change) {
			case digitSet: 
				tv.setTextAppearance(GameActivity.this, android.R.style.TextAppearance_Large);
				tv.setText(Integer.toString(sudoku.getValue(row, column)));
				if (grey[pos]) {	tv.setBackgroundColor(COLOR_D_BG_GREY); 	}
				else {	tv.setBackgroundColor(COLOR_D_BG_WHITE);	}	
				break;
			case digitRemoved:
				tv.setText("");
				if (grey[pos]) {	tv.setBackgroundColor(COLOR_BG_4);	}
				else {	tv.setBackgroundColor(COLOR_BG_5);	}	
				break;
			case candidateAdded:
				if (pencilMarksOn) {
					tv.setTextSize(10);
					tv.setTextColor(COLOR_PM);
					tv.setText(pmToString(sudoku.getCandidatesString(row, column)));
					if (grey[pos]) {	tv.setBackgroundColor(COLOR_PM_BG_GREY);	}
					else {	tv.setBackgroundColor(COLOR_PM_BG_WHITE);	}	
				}
				break;
			case candidateRemoved:
				if (pencilMarksOn) {
					tv.setTextSize(10);
					tv.setTextColor(COLOR_PM);
					tv.setText(pmToString(sudoku.getCandidatesString(row, column)));
					if (grey[pos]) {	tv.setBackgroundColor(COLOR_BG_4);	}
					else {	tv.setBackgroundColor(COLOR_BG_5);	}
				}
				break;
			case candidateToggled:
				tv.setTextSize(10);
				tv.setTextColor(COLOR_PM);
				tv.setText(pmToString(sudoku.getCandidatesString(row, column)));
				break;
			default:
				break;
			}
		}
		
		public void onLeaderChanged(boolean youAreWinning) {
			if (youAreWinning) {
				changeBackgroundColor(COLOR_BG_WIN);
				setTitle("You are winning!");
			} else {
				changeBackgroundColor(COLOR_BG_LOSE);
				setTitle("You are losing!");
			}
		}
		
		public void onGameFinished(String winner, int score) {
			// TODO Auto-generated method stub				
		}

		@Override
		public void onPlayerLeft(String username) {
			// TODO Auto-generated method stub
			
		}
	};;
	
	
	//wenn man digit l�scht sollen alle pm in zeile, spalte und kasten geupdatet werden

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setTitle("Start!");
        
        String username = getIntent().getExtras().getString("username");
        int[][] template = new int[9][9];
        for (int i=0; i<9; i++) {
        	template[i] = getIntent().getExtras().getIntArray("template"+Integer.toString(i));
        }
            
        // connect to sudoku service
        Intent service = new Intent(this, SudokuService.class);
        bindService(service, serviceConnection, Context.BIND_AUTO_CREATE);
    }

	private void initGUI() {
		//zum testen wir in jedem 3. feld pencilmarks(global) angezeigt und ansonsten number 5
        //TODO feld unterteilen in 3x3 (mit backgroundimage? oder entsprechende abst�nde vergr�ssern?)
        cells = new TextView[81];
        for (int pos=0; pos<81; pos++) {
        	TextView tv = new TextView(this);
        	if (grey[pos]) {
        		tv.setBackgroundColor(COLOR_BG_4);
        	} else {
        		tv.setBackgroundColor(COLOR_BG_5);
        	}
        	String text = "";
        	int[] coords = calculateCoordinates(pos);
        	int value = sudoku.getValue(coords[0]+1, coords[1]+1);
        	if (value != 0) {
        		text = Integer.toString(value);
        		tv.setTextAppearance(this, android.R.style.TextAppearance_Large);
        	}    		
        	tv.setText(text);
        	cells[pos] = tv;
        }
       
        Display display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        numPadSize = (screenHeight-screenWidth)/5;
        
        gv_sudoku = (GridView)findViewById(R.id.masterSudoku);
        gv_sudoku.setColumnWidth((int)(screenWidth/9 - 10));
        va_sudoku = new TextViewAdapter(GameActivity.this, cells, (int)(screenWidth/9));
        gv_sudoku.setAdapter(va_sudoku);
        gv_sudoku.setPadding(5, 5, 5, 5);
        gv_sudoku.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> av, View target, int position, long arg) {
				TextView tv = (TextView)target;
				Log.d("clicked", Integer.toString(position));
				if (insertActive && currentNumber != 0) {
					sendSetDigitRequest(position);
				} else if (deleteActive && tv.getText().length() == 1) {
					sendRemoveDigitRequest(position);
				} else if (pencilActive && tv.getText().length() != 1) {
					sendCandidateToggledRequest(position);
				}
			}
        });
        
        
        
        
        //TODO was ist mit button um pencilmarks zu togglen? evtl noch andere funktionen?
        gv_numPad = (GridView)findViewById(R.id.numPad);
        gv_numPad.setAdapter(new ImageViewAdapter(GameActivity.this, numPadSize, 0, insertActive, deleteActive, pencilActive));
        gv_numPad.setPadding((screenWidth-4*numPadSize)/2, 10, (screenWidth-4*numPadSize)/2, 10);
        gv_numPad.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> av, View target, int position, long arg3) {
				switch(position) {
					case 0: currentNumber = 1; highlight(); break;
					case 1: currentNumber = 2; highlight(); break;
					case 2: currentNumber = 3; highlight(); break;
					case 3: insertActive = true; pencilActive = false; deleteActive = false; break;
					case 4: currentNumber = 4; highlight(); break;
					case 5: currentNumber = 5; highlight(); break;
					case 6: currentNumber = 6; highlight(); break;
					case 7: deleteActive = true; pencilActive = false; insertActive = false; break;
					case 8: currentNumber = 7; highlight(); break;
					case 9: currentNumber = 8; highlight(); break;
					case 10: currentNumber = 9; highlight(); break;
					case 11: pencilActive = true; deleteActive = false; insertActive = false; break;
					default: break;
				}
				
				// after a click set the new images according to the given values
				gv_numPad.setAdapter(new ImageViewAdapter(GameActivity.this, numPadSize, currentNumber, insertActive, deleteActive, pencilActive));
				
				Log.d("############","------------------------------");
				Log.d("currentNumber", Integer.toString(currentNumber));
				Log.d("InsertActive",Boolean.toString(insertActive));
				Log.d("DeleteActive",Boolean.toString(deleteActive));
				Log.d("PencilActive",Boolean.toString(pencilActive));
				Log.d("###########","-------------------------------");
			}
        });
        
        //TODO backgroundcolor dem f�hrer anpassen
        changeBackgroundColor(COLOR_BG_LOSE);
	}
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(serviceConnection);
	}
    
    private void highlight() {
		String number = Integer.toString(currentNumber);
		TextView tv;
		for (int i = 0; i < 81; i++) {
			tv = (TextView)cells[i];
			if (tv.getText().toString().contains(number)) {
				if (tv.getText().toString().length() == 1) {
					// Number highlight
					if (grey[i]) {	tv.setBackgroundColor(COLOR_D_BG_GREY);
					} else {	tv.setBackgroundColor(COLOR_D_BG_WHITE);	}
				} else if (pencilMarksOn){
					// PencilMark highlight
					if (grey[i]) {	tv.setBackgroundColor(COLOR_PM_BG_GREY);
					} else {	tv.setBackgroundColor(COLOR_PM_BG_WHITE);	}
				}
			} else { 
				// default backgroundcolors
				if (grey[i]) {	tv.setBackgroundColor(COLOR_BG_4);
				} else {	tv.setBackgroundColor(COLOR_BG_5);	}
			}
		}
	}
    
    //wandelt boolean array der pencilmarks in string um -> string array w�re besser!!!
    public String pmToString(String[] pms) {
    	StringBuilder sb = new StringBuilder();		
		sb.append(pms[1]+"  ");
		sb.append(pms[2]+"  ");
		sb.append(pms[3]+"\n");
		sb.append(pms[4]+"  ");
		sb.append(pms[5]+"  ");
		sb.append(pms[6]+"\n");
		sb.append(pms[7]+"  ");
		sb.append(pms[8]+"  ");
		sb.append(pms[9]);
		return sb.toString();
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_game, menu);
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    pencilMarksOn = !pencilMarksOn;
	    TextView tv;
		if (pencilMarksOn) {
			for (int i = 0; i < 81; i++) {
				tv = (TextView)cells[i];
				if (tv.getText().length() != 1) {
					tv.setTextSize(10);
					int[] coords = calculateCoordinates(i);
					tv.setText(pmToString(sudoku.getCandidatesString(coords[0]+1, coords[1]+1)));
				}
			}
		} else {
			for (int i = 0; i < 81; i++) {
				tv = (TextView)cells[i];
				if (tv.getText().length() != 1) {
					tv.setText("");
				}
			}
		}
		highlight();
	    return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_MENU) {
	        event.startTracking();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_MENU) {
	        // Override default handling so soft keyboard doesn't pop up.
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_MENU) {
	        openOptionsMenu();
	        return true;
	    }
	    return super.onKeyUp(keyCode, event);
	}
	
	//zero based
	public int calculatePosition(int x, int y) {
		return x + y*9;
	}
	
	//zero based
	public int[] calculateCoordinates(int pos) {
		int x = pos % 9;
		int y = (pos - x) / 9;
		return new int[] {x,y}; 
	}
	
	private void changeBackgroundColor(int color) {
		gv_sudoku.setBackgroundColor(color);
		gv_numPad.setBackgroundColor(color);
	}
	
	private void sendAddCandidateRequest(int pos) {
		Message msg = new Message();
		msg.what = SudokuHandler.GUIRequestAddCandidate;
		int[] coords = calculateCoordinates(pos);
		int x = coords[0];
		int y = coords[1];
		msg.obj = new CellInfo(x+1, y+1, currentNumber);
		sudoku.sendMessage(msg);
	}
	
	private void sendSetDigitRequest(int pos) {
		Message msg = new Message();
		msg.what = SudokuHandler.GUIRequestSetDigit;
		int[] coords = calculateCoordinates(pos);
		int x = coords[0];
		int y = coords[1];
		msg.obj = new CellInfo(x+1, y+1, currentNumber);
		sudoku.sendMessage(msg);
	}
	
	private void sendRemoveCandidateRequest(int pos) {
		Message msg = new Message();
		msg.what = SudokuHandler.GUIRequestRemoveCandidate;
		int[] coords = calculateCoordinates(pos);
		int x = coords[0];
		int y = coords[1];
		msg.obj = new CellInfo(x+1, y+1, currentNumber);
		sudoku.sendMessage(msg);
	}
	
	private void sendRemoveDigitRequest(int pos) {
		Message msg = new Message();
		msg.what = SudokuHandler.GUIRequestRemoveDigit;
		int[] coords = calculateCoordinates(pos);
		int x = coords[0];
		int y = coords[1];
		msg.obj = new CellInfo(x+1, y+1, currentNumber);
		sudoku.sendMessage(msg);
	}
	
	private void sendCandidateToggledRequest(int pos) {
		Message msg = new Message();
		msg.what = SudokuHandler.GUIRequestToggleCandidate;
		int[] coords = calculateCoordinates(pos);
		int x = coords[0];
		int y = coords[1];
		msg.obj = new CellInfo(x+1, y+1, currentNumber);
		sudoku.sendMessage(msg);
	}
	
	boolean[] grey = {  false, false, false,  true,  true,  true, false, false, false,
						false, false, false,  true,  true,  true, false, false, false,
						false, false, false,  true,  true,  true, false, false, false,
						 true,  true,  true, false, false, false,  true,  true,  true, 
						 true,  true,  true, false, false, false,  true,  true,  true,
						 true,  true,  true, false, false, false,  true,  true,  true,
						false, false, false,  true,  true,  true, false, false, false,
						false, false, false,  true,  true,  true, false, false, false,
						false, false, false,  true,  true,  true, false, false, false, };
}
