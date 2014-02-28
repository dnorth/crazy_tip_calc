package com.parentlink.crazytipcalc;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class CrazyTipCalc extends Activity {
	
	//defining constants in the case that they exit the app, these will be saved when they return
	private static final String TOTAL_BILL= "TOTAL_BILL";
	private static final String CURRENT_TIP= "CURRENT_TIP";
	private static final String BILL_WITHOUT_TIP= "BILL_WITHOUT_TIP";
	
	private double billBeforeTip;
	private double tipAmount;
	private double finalBill;
	
	EditText billBeforeTipET;
	EditText tipAmountET;
	EditText finalBillET;
	
	SeekBar tipSeekBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crazy_tip_calc);
		
		//checking to see if there is anything saved. if not then we are initializing these variables
		if(savedInstanceState == null)
		{
			billBeforeTip = 0.0;
			tipAmount = .15;
			finalBill = 0.0;
		
		}
		//If there is something state then we want to get them with the variables previously defined
		else
		{
			billBeforeTip= savedInstanceState.getDouble(BILL_WITHOUT_TIP); 
			tipAmount= savedInstanceState.getDouble(CURRENT_TIP) *1.0; 
			finalBill= savedInstanceState.getDouble(TOTAL_BILL); 
		}
		//It's going to find the view by ID and set the variable equal to whatever is inputted by the user
		billBeforeTipET = (EditText) findViewById(R.id.billEditText);
		tipAmountET = (EditText) findViewById(R.id.tipEditText);
		finalBillET = (EditText) findViewById(R.id.finalBillEditText);
		
		tipSeekBar = (SeekBar) findViewById(R.id.changeTipSeekBar);
		
		//Sets the Bar based on the changes we make in the "tipSeekBarListener" object
		tipSeekBar.setOnSeekBarChangeListener(tipSeekBarListener);
		
		
		billBeforeTipET.addTextChangedListener(billBeforeTipListener);
		
	}
	
	//Imported a library and clicked "add unimplemented methods"
	private TextWatcher billBeforeTipListener = new TextWatcher(){

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
			try{
				
				billBeforeTip = Double.parseDouble(s.toString());
			}
			
			catch(NumberFormatException e){
				billBeforeTip = 0.0;
			}
			
			updateTipAndFinalBill();
			
		}
		
	};
	
	private OnSeekBarChangeListener tipSeekBarListener = new OnSeekBarChangeListener(){

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {

			//Turning the tipAmount into a decimal value
			tipAmount = ((tipSeekBar.getProgress()) * .01);
			
			tipAmountET.setText(String.valueOf(tipSeekBar.getProgress()) + "%");
		
			updateTipAndFinalBill();
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	//Our method to update the tip and final bill
	private void updateTipAndFinalBill(){
		
		double tipAmount;
		
		//Taking the tip amount inputted from the Edit Text box and turning it to a string, which is then parsed to a double
		try{
			tipAmount = (Double.parseDouble(tipAmountET.getText().toString().replace("%", "")) *.01);
		}
		catch(NumberFormatException e){
			tipAmount = .15;
		}
		
		
		double finalBill =billBeforeTip + (billBeforeTip * tipAmount);
		
		//Setting the Edit Text box of final bill to the "finalBill" variable with a format of 2 decimal places
		finalBillET.setText("$"+ String.format("%.02f", finalBill));
	}
	
	//This will be called whenever something interrupts the app (leaving the app, etc.) so we save the information we need
	//whenever the person opens the app again
	protected void onSaveInstanceState(Bundle outState){
		
		super.onSaveInstanceState(outState);
		
		//These variables are saved in the private static variables
		outState.putDouble(TOTAL_BILL, finalBill);
		outState.putDouble(CURRENT_TIP, tipAmount);
		outState.putDouble(BILL_WITHOUT_TIP, billBeforeTip);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.crazy_tip_calc, menu);
		return true;
	}

}
