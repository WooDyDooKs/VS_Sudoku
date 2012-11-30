package ds.sudoku.gui;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class MainActivity extends Activity {

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
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        button_register = (Button)findViewById(R.id.button_register);
        button_search = (Button)findViewById(R.id.button_search);
        button_search.setEnabled(false);										// is only true if a user is registered
        editText_register = (EditText)findViewById(R.id.editText_register);
        editText_search = (EditText)findViewById(R.id.editText_search);
               
        radio_sp = (RadioButton)findViewById(R.id.radio_singleplayer);
        radio_mp = (RadioButton)findViewById(R.id.radio_multiplayer);
        radio_sp.setChecked(true);
        
        spinner = (Spinner)findViewById(R.id.spinner_modi);
        adapter_sp = ArrayAdapter.createFromResource(this, R.array.singleplayer_array, android.R.layout.simple_spinner_item);
        adapter_mp = ArrayAdapter.createFromResource(this, R.array.multiplayer_array, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter_sp);
        
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
    
    public void register() {
    	username = editText_register.getText().toString();
//TODO			if (receive != null && receive.getString("success").equals("reg_ok")) //registration successful
		registered = true;
    	button_register.setText(getString(R.string.deregister_button));
    	if(!radio_mp.isChecked()) {
    		editText_search.setHint(R.string.multiplayerfirst_hint);
    	} else {
    		editText_search.setHint(R.string.search_hint);
    		button_search.setEnabled(true);
    	}
    }
    
    public void deregister() {
	
//TODO			if (receive.getString("success").equals("dreg_ok")) //deregistration successful
		registered = false;
		button_register.setText(getString(R.string.register_button));
		editText_search.setHint(R.string.registerfirst_hint);
		button_search.setEnabled(false);
    }
    
    public void onClickSearchButton(View v) {
    	opponentUsername = editText_search.getText().toString();
    	
    	// TODO do some searching
    	
    	// when found opponent, start game
    	Intent sensorInfo = new Intent(this, GameActivity.class);
    	sensorInfo.putExtra("username", username);
    	// TODO add extras for game
		this.startActivity(sensorInfo);
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