package ds.sudoku.gui;

import ds.sudoku.communication.InviteMessage;
import ds.sudoku.gui.service.SudokuService;
import ds.sudoku.gui.service.UserStateListener;
import ds.sudoku.gui.service.SudokuService.SudokuServerBinder;
import ds.sudoku.logic.SampleSudokuFactory;
import ds.sudoku.logic.SudokuTemplate;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class MainActivity extends Activity {

	int mProgressStatus = 0;

	SudokuServerBinder sudokuService;
	
	String username;
	String opponentUsername;
	boolean registered;
	
	SpModi spModi;
	MpModi mpModi;
	
	RadioButton radio_sp, radio_mp;
	Spinner spinner;
	SpinnerAdapter adapter_sp, adapter_mp;
	
	Button button_search, button_register;
	EditText editText_search, editText_register;
	
	ProgressBar progressBar;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Sudoku Beta");
        
        button_register = (Button)findViewById(R.id.button_register);
        button_search = (Button)findViewById(R.id.button_search);
        button_search.setEnabled(false);										// is only true if a user is registered
        editText_register = (EditText)findViewById(R.id.editText_register);
        editText_search = (EditText)findViewById(R.id.editText_search);
               
        radio_sp = (RadioButton)findViewById(R.id.radio_singleplayer);
        radio_sp.setEnabled(false);	// false because we only support multiplayer
        radio_mp = (RadioButton)findViewById(R.id.radio_multiplayer);
        radio_mp.setChecked(true);
        
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        
        spinner = (Spinner)findViewById(R.id.spinner_modi);
        adapter_sp = ArrayAdapter.createFromResource(this, R.array.singleplayer_array, android.R.layout.simple_spinner_item);
        adapter_mp = ArrayAdapter.createFromResource(this, R.array.multiplayer_array, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter_mp);
        spinner.setEnabled(false);
        
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
        	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        		if (radio_sp.isChecked()) {
        			button_search.setEnabled(false);
        			if(!registered) {
        				editText_search.setHint(R.string.registerfirst_hint);
        	    	} else if(!radio_mp.isChecked()) {
        	    		editText_search.setHint(R.string.multiplayerfirst_hint);
        	    	}
        			switch ((int)id) {
        				case 0:
        					spModi = SpModi.Easy;
        					break;
        				case 1:
        					spModi = SpModi.Medium;
        					break;
        				case 2:
        					spModi = SpModi.Hard;
        					break;
        				default:
        					spModi = SpModi.Easy;
        					break;
        			}
        		} else if (radio_mp.isChecked()){
        			if(!registered) {
        	    		editText_search.setHint(R.string.registerfirst_hint);
        	    	} else {
        	    		editText_search.setHint(R.string.search_hint);
        	    		button_search.setEnabled(true);
        	    	}
        			switch ((int)id) {
	    				case 0:
	    					mpModi = MpModi._1on1;
	    					break;
	    				case 1:
	    					mpModi = MpModi._2on2;
	    					break;
	    				case 2:
	    					mpModi = MpModi.Deathmatch;
	    					break;
	    				default:
	    					mpModi = MpModi.Deathmatch;
	    			}
        		}
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // no use...
            }
        });
        
        // disable registration until service is connected
        button_register.setEnabled(false);
        
        // connect to sudoku service
        Intent service = new Intent(this, SudokuService.class);
        bindService(service, serviceConnection, Context.BIND_AUTO_CREATE);
    }
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(serviceConnection);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    
    public void onClickSingleplayerRadio(View v) {
    	spinner.setAdapter(adapter_sp);
    }
    
    public void onClickMultiplayerRadio(View v) {
    	spinner.setAdapter(adapter_mp);
    }
    
    public void onClickRegisterButton(View v) {
    	if (!registered) {
    		register();
    	} else {
    		deregister();
    	}
    	
    	// only enable search function if registering was successful
    	
    }
    
    private ServiceConnection serviceConnection = new ServiceConnection() {	
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			sudokuService = (SudokuServerBinder) service;
			sudokuService.setUserStateListener(userStateListener);
			button_register.setEnabled(true);
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
			sudokuService = null;
		}
	};

    private UserStateListener userStateListener = new UserStateListener() {
		@Override
		public void onRegistered(String user) {
			username = user;
			registered = true;
			
	    	button_register.setText(getString(R.string.deregister_button));
	    	if(!radio_mp.isChecked()) {
	    		editText_search.setHint(R.string.multiplayerfirst_hint);
	    	} else {
	    		editText_search.setHint(R.string.search_hint);
	    		button_search.setEnabled(true);
	    	}
		}
		
		@Override
		public void onInviteRequestRejected(InviteMessage msg) {
			// TODO: show to user that invite request was rejected
		}
		
		@Override
		public void onInviteRequest(InviteMessage msg) {
			// accept invite			
			sudokuService.getServer().ACK(msg);	
			
			// or reject invite
			// sudokuService.getServer().NACK(msg);
		}
		
		@Override
		public void onGameStarted(SudokuTemplate sudokuTemplate) {
	    	progressBar.setVisibility(View.INVISIBLE);
	    	
	    	// when found opponent, start game
	    	//int[][] template = SampleSudokuFactory.getSampleSudoku().getTemplate();
	    	int[][] template = sudokuTemplate.getTemplate();
	    	Intent intent = new Intent(MainActivity.this, GameActivity.class);
	    	intent.putExtra("username", username);
	    	for (int i=0; i<9; i++) {
	    		intent.putExtra("template"+Integer.toString(i), template[i]);
	    	}
	    	
	    	// TODO add extras for game
			startActivity(intent);
			
		}
		
		@Override
		public void onDeregistered() {
			registered = false;
			button_register.setText(getString(R.string.register_button));
			editText_search.setHint(R.string.registerfirst_hint);
			button_search.setEnabled(false);
		}
		
		@Override
		public void onDeath(String message) {
									
		}
	};

    public void register() {
    	String username = editText_register.getText().toString();
    	sudokuService.getServer().register(username);
    }
    
    public void deregister() {
    	sudokuService.getServer().deregister(null);
    }
    

    
    public void onClickSearchButton(View v) {
    	opponentUsername = editText_search.getText().toString();
    	
    	// manually disable the softkeyboard
    	InputMethodManager imm = (InputMethodManager)getSystemService( Context.INPUT_METHOD_SERVICE); 
    	imm.hideSoftInputFromWindow(editText_search.getWindowToken(), 0);
    	imm.hideSoftInputFromWindow(editText_register.getWindowToken(), 0);
    	
    	// TODO do some searching
    	progressBar.setVisibility(View.VISIBLE);
    	
    	// find Player
	    sudokuService.getServer().invite(opponentUsername);
    }
    
    enum SpModi {
    	Easy,
    	Medium,
    	Hard
    }
    
    enum MpModi {
    	_1on1,
    	_2on2,
    	Deathmatch
    }
}