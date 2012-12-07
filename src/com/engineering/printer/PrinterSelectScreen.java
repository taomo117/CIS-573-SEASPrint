package com.engineering.printer;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class PrinterSelectScreen extends Activity{
	
	public static String printer;

	public static boolean duplex;
	boolean dTemp;
	public static Integer number;
	public static final String PRINTER_PREF = "SEASPrintingFavorite";
	public static final String PRINTER_KEY = "printerpreference";
	public static String mFavored;
	public static boolean timedPrinting = false;
	private CheckBox mDuplexCheckbox;
	private Spinner mSpinner;
	private Button mPrintbutton;
	private NumberPicker mNumberPicker;
	private ArrayAdapter<CharSequence> mAdapter;
	private CheckBox mTimedPrinting;
	private CheckBox mSavePrintOptions;
	private boolean eniac = false;
	private String filePath = null;
	
	
	//public static Integer pps;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater infl = new MenuInflater(this);
		infl.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.connection_config) {
       	 Intent myIntent = new Intent(this, EngineeringPrinter.class);
         startActivityForResult(myIntent, 0);
		return true;
		}
		return super.onOptionsItemSelected(item);
		
	}
	
	 @Override
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        System.out.println("In onCreate of PrinterSelectScreen");
	        Intent mt = getIntent();
	        eniac = mt.getBooleanExtra("eniac", false);
	        filePath = mt.getStringExtra("filePath");
	        System.out.println("eniac? "+eniac);
	        System.out.println(filePath);
//	        InputStream is = null;
//	        try {
//	        	if (null != getIntent().getData()) {
//		            is = getContentResolver().openInputStream(getIntent().getData());
//			        Document.load(is);
//			        Document.setDescriptor(getIntent().getData());
//			          EngineeringPrinter.Microsoft = MicrosoftSink.Filter(getIntent().getType());
//			          EngineeringPrinter.type = getIntent().getType();
//	        	}
//
//	        }
//	        catch  (FileNotFoundException fnf){
//	            Log.e("Connection","File Not Found");
//	        }

	        
//	       SharedPreferences settings = getSharedPreferences(PRINTER_PREF, 0);
//	       mFavored = settings.getString(PRINTER_KEY, null);
//	       if (mFavored == null) {
//	           mFavored = "169";
//	       }
//	       
//	       SharedPreferences conn_set = getSharedPreferences(EngineeringPrinter.PREFS_NAME, MODE_PRIVATE);
//	        if (conn_set.contains(EngineeringPrinter.PASSWORD_KEY)) {
//	        	System.out.println("Password_key exists");
//	        	if (conn_set.contains(EngineeringPrinter.KEY_KEY) &&
//	 	    		conn_set.contains(EngineeringPrinter.HOST_KEY) &&
//		    		conn_set.contains(EngineeringPrinter.PORT_KEY)) {
//	        			String key = (new AuthSetup(conn_set.getString(EngineeringPrinter.USER_KEY, ""),
//	    			   conn_set.getString(EngineeringPrinter.PASSWORD_KEY, ""), 
//	    			   conn_set.getString(EngineeringPrinter.HOST_KEY, ""), 
//	    			   Integer.valueOf(conn_set.getString(EngineeringPrinter.PORT_KEY,EngineeringPrinter.PORT_FAIL)))).keyGen();
//	        			Log.d("key", key);
//	        			conn_set.edit().putString(EngineeringPrinter.KEY_KEY, key).commit();
//	        	}
//	        	conn_set.edit().remove(EngineeringPrinter.PASSWORD_KEY).commit();
//	        }
//	       if (!conn_set.contains(EngineeringPrinter.USER_KEY) ||
//	    		   !conn_set.contains(EngineeringPrinter.KEY_KEY) || 
//	    		   !conn_set.contains(EngineeringPrinter.HOST_KEY) ||
//	    		   !conn_set.contains(EngineeringPrinter.PORT_KEY)){
//	    	   System.out.println("Going to take input");
//	         	 Intent myIntent = new Intent(this, EngineeringPrinter.class);
//	             startActivity(myIntent);
//	       }
//	       else {
//	    	   try {
//	    	   EngineeringPrinter.connect = (new ConnectionFactory()).MakeConnectionKey(
//	    			   conn_set.getString(EngineeringPrinter.USER_KEY, ""),
//	    			   conn_set.getString(EngineeringPrinter.KEY_KEY, ""), 
//	    			   conn_set.getString(EngineeringPrinter.HOST_KEY, ""), 
//	    			   Integer.valueOf(conn_set.getString(EngineeringPrinter.PORT_KEY,EngineeringPrinter.PORT_FAIL)));
//	    	   System.out.println("12134");
//	    	   }
//	    	   catch (IOException e) {
//		         	 Intent myIntent = new Intent(this, EngineeringPrinter.class);
//		             startActivityForResult(myIntent, 0);
//	    	   }
//	       }
	 }
	 
	 @Override
	public void onStop() {
	     super.onStop();

	 }
	 
	 @Override
	public void onResume()
	 {
		 super.onResume();
		 setContentView(R.layout.printers);
		 
		 
		 SharedPreferences settings = getSharedPreferences(PRINTER_PREF, 0);
	    
		 //set printer dropdown list.
		 mFavored = settings.getString(PRINTER_KEY, null);
	     if (mFavored == null) {
	         mFavored = "169";
	     }	     
	   	mSpinner = (Spinner) findViewById(R.id.printer_spinner);
        String printers[] = null;
        boolean has_favored = false;
        try {
        	Log.d("Connection", "Start Connecting");
            PrintCaller pc = new PrintCaller(new CommandConnection(EngineeringPrinter.connect));
            List<String> ps = pc.getPrinters();
            printers = new String[ps.size()];
            // yes I know this is stupid and could be done much easier but ps.toArray was trippin ballz
            int i = 0;
            for(Iterator<String> iter = ps.iterator(); iter.hasNext(); i++) {
            	printers[i] = iter.next();
            }
            has_favored = ps.contains(mFavored);
        }
        
        catch (IOException ioe){
            new AlertDialog.Builder(getApplicationContext()).setMessage("Could not connect to server! Verify login information and network status.").create().show();
            Log.d("Connection", "Failed to connect or send");
        }
        mAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, printers);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);
       if (has_favored) {
            int pos = mAdapter.getPosition(mFavored);
            mSpinner.setSelection(pos);
        }
        mSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
        
        
        boolean mDuplex = settings.getBoolean("DUPLEX_KEY", false);
        mDuplexCheckbox = (CheckBox) findViewById(R.id.duplex_toggle_checkbox);
        if(mDuplex)
        {
        	dTemp=true;
        	mDuplexCheckbox.setChecked(true);
        }
        mDuplexCheckbox.setOnClickListener(new OnClickListener() {
            @Override
			public void onClick(View v) {
                // Perform action on clicks
                if (mDuplexCheckbox.isChecked()) {
                    dTemp=true;
                } else {
                    dTemp=false;
                }
            }
        });
        
        mTimedPrinting = (CheckBox) findViewById(R.id.timed_printing);
        boolean mTimePrinting = settings.getBoolean("TIMEPRINT_KEY", false);
        if(mTimePrinting)
        {
        	mTimedPrinting.setChecked(true);
        }
        
        mNumberPicker= (NumberPicker) findViewById(R.id.number_picker);
        int mNumber = settings.getInt("NUMBER_PICKER",1);
        mNumberPicker.setValue(mNumber);
        
        //TextView t1 = (TextView) findViewById(R.id.duplex_label);
        TextView t2 = (TextView) findViewById(R.id.number_label);
        TextView t3 = (TextView) findViewById(R.id.select_file_name);
        Document.descriptor = filePath;	       
        //Document.addToHistory(Document.descriptor.startsWith
        		//("/")?Document.descriptor.substring(1):Document.descriptor+"   "+Calendar.getInstance().getTime());
        //final NumberPicker ppspicker= (NumberPicker) findViewById(R.id.pps_picker);
        
        if (EngineeringPrinter.Microsoft) {
           // t1.setVisibility(View.GONE);
            t2.setVisibility(View.GONE);
            mNumberPicker.setVisibility(View.GONE);
            mDuplexCheckbox.setVisibility(View.GONE);
        }
        
        
        mSavePrintOptions = (CheckBox)findViewById(R.id.save_print_options);
        mSavePrintOptions.setChecked(true);
        mPrintbutton = (Button) findViewById(R.id.print_button);
        mPrintbutton.setOnClickListener(new View.OnClickListener() {
            @Override
			public void onClick(View v) {
            	 number=mNumberPicker.value;
            	// pps=ppspicker.value;
            	 duplex=dTemp;
            	 timedPrinting = mTimedPrinting.isChecked();       	 
                 
            	 if(mSavePrintOptions.isChecked())
            	 {
            		 SharedPreferences settings = getSharedPreferences(PRINTER_PREF, 0);
                     SharedPreferences.Editor ed = settings.edit();
                     ed.putBoolean("TIMEPRINT_KEY", timedPrinting);
                     ed.putBoolean("DUPLEX_KEY", duplex);
                     ed.putString(PRINTER_KEY, (String)mSpinner.getSelectedItem());
                     ed.putInt("NUMBER_PICKER", number);
                     ed.commit();
            		 
            	 }
            	 //PRINT
            	 Intent myIntent = new Intent(v.getContext(), LoadingStatusScreen.class);
            	 myIntent.putExtra("eniac", eniac);
            	 myIntent.putExtra("filePath", filePath);
            	 startActivityForResult(myIntent, 0);
            	 
             }
         });
		 
	 }
	 
	 
	 public static class MyOnItemSelectedListener implements OnItemSelectedListener {
		 public static String printer;
		 	
		    @Override
			public void onItemSelected(AdapterView<?> parent,
		        View view, int pos, long id) {
		    	//PRINTER WAS SELECTED
		    	printer=parent.getItemAtPosition(pos).toString();
		    	mFavored = printer;
		    }

		    @Override
			public void onNothingSelected(AdapterView parent) {
		      // Do nothing.
		    }
		}
}
