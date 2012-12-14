package ds.sudoku.gui;

import ds.sudoku.communication.InviteMessage;
import ds.sudoku.gui.service.SudokuService;
import ds.sudoku.gui.service.UserStateListener;
import ds.sudoku.gui.service.SudokuService.SudokuServerBinder;
import ds.sudoku.logic.SudokuTemplate;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
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
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	int mProgressStatus = 0;

	SudokuServerBinder sudokuService;
	InviteMessage inviteMessage;
	
	String username;
	String opponentUsername;
	boolean registered;
	
	SpModi spModi;
	MpModi mpModi;
	
	RadioButton radio_sp, radio_mp;
	Spinner spinner;
	SpinnerAdapter adapter_sp, adapter_mp;
	
	TextView textView_invite;
	Button button_search, button_register, button_accept, button_decline;
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
        
        textView_invite = (TextView)findViewById(R.id.textView_invite);
        textView_invite.setVisibility(View.INVISIBLE);
        button_accept = (Button)findViewById(R.id.button_accept);
        button_accept.setVisibility(View.INVISIBLE);
        button_accept.setEnabled(false);
        button_decline = (Button)findViewById(R.id.button_decline);
        button_decline.setVisibility(View.INVISIBLE);
        button_decline.setEnabled(false);
        
        spinner = (Spinner)findViewById(R.id.spinner_modi);
        adapter_sp = ArrayAdapter.createFromResource(this, R.array.singleplayer_array, android.R.layout.simple_spinner_item);
        adapter_mp = ArrayAdapter.createFromResource(this, R.array.multiplayer_array, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter_mp);
        spinner.setEnabled(true);
        
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
    					spModi = SpModi.Very_Easy;
    					break;
    				case 1:
    					spModi = SpModi.Easy;
    					break;
    				case 2:
    					spModi = SpModi.Medium;
    					break;
    				case 3:
    					spModi = SpModi.Hard;
    					break;
    				case 4:
    					spModi = SpModi.Very_Hard;
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
    					mpModi = MpModi.Very_Easy;
    					break;
    				case 1:
    					mpModi = MpModi.Easy;
    					break;
    				case 2:
    					mpModi = MpModi.Medium;
    					break;
    				case 3:
    					mpModi = MpModi.Hard;
    					break;
    				case 4:
    					mpModi = MpModi.FIENDISH;
    					break;
    				default:
    					mpModi = MpModi.Easy;
    					break;
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
    	InputMethodManager imm = (InputMethodManager)getSystemService( Context.INPUT_METHOD_SERVICE); 
    	imm.hideSoftInputFromWindow(editText_search.getWindowToken(), 0);
    	imm.hideSoftInputFromWindow(editText_register.getWindowToken(), 0);
    }    

    public void register() {
    	String username = editText_register.getText().toString();
    	sudokuService.getServer().register(username);
    }
    
    public void deregister() {
    	sudokuService.getServer().deregister(null);
    }
    
    private ServiceConnection serviceConnection = new ServiceConnection() {	
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			sudokuService = (SudokuServerBinder) service;
			sudokuService.setUserStateListener(userStateListener);
			if(sudokuService.getServer() != null) {
				button_register.setEnabled(true);
			} else {
				Toast.makeText(MainActivity.this, "Could not connect to server.", Toast.LENGTH_LONG).show();
			}
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
	    	progressBar.setVisibility(View.INVISIBLE);
			
			Toast t = Toast.makeText(MainActivity.this, "The invitation was rejected", Toast.LENGTH_LONG);
			t.show();
		}
		
		@Override
		public void onInviteRequest(InviteMessage msg) {
	        textView_invite.setVisibility(View.VISIBLE);
	        textView_invite.setText(msg.getName() + " invites you for an epic battle!");
	        button_accept.setVisibility(View.VISIBLE);
	        button_accept.setEnabled(true);
	        button_decline.setVisibility(View.VISIBLE);
	        button_decline.setEnabled(true);
	        
	        // Der request wird erst in den buttonlistener beantwortet
	        inviteMessage = msg;
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
			Toast.makeText(MainActivity.this, "Disconnected.", Toast.LENGTH_LONG).show();
			finish();
		}

		@Override
		public void onError(String message) {
			Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();		
		}
	};
    
	public void onClickAcceptButton(View v) {
		sudokuService.getServer().ACK(inviteMessage);
		
		textView_invite.setVisibility(View.INVISIBLE);
        button_accept.setVisibility(View.INVISIBLE);
        button_accept.setEnabled(false);
        button_decline.setVisibility(View.INVISIBLE);
        button_decline.setEnabled(false);
	}
	
	public void onClickDeclineButton(View v) {
		sudokuService.getServer().NACK(inviteMessage);
		
        textView_invite.setVisibility(View.INVISIBLE);
        button_accept.setVisibility(View.INVISIBLE);
        button_accept.setEnabled(false);
        button_decline.setVisibility(View.INVISIBLE);
        button_decline.setEnabled(false);
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
    	if(opponentUsername.isEmpty()) {
    		sudokuService.getServer().requestRandomMatch(mpModi.toString());
    	} else {
    		sudokuService.getServer().invite(opponentUsername, mpModi.toString());
    	}
    }
    
    enum SpModi {
    	Very_Easy,
    	Easy,
    	Medium,
    	Hard,
    	Very_Hard
    }
    
    enum MpModi {
    	Very_Easy,
    	Easy,
    	Medium,
    	Hard,
    	FIENDISH
    }
}