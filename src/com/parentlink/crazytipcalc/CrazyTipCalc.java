package com.parentlink.crazytipcalc;

import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

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
	
	CheckBox friendlyCheckBox;
	CheckBox specialsCheckBox;
	CheckBox opinionCheckBox;
	
	RadioGroup availableRadioGroup;
	RadioButton availableBadRadio;
	RadioButton availableOKRadio;
	RadioButton availableGoodRadio;
	
	Spinner problemsSpinner;
	
	Button startChronometerButton;
	Button pauseChronometerButton;
	Button resetChronometerButton;
	
	Chronometer timeWaitingChronometer;
	
	private int[] checklistValues = new int[12];

	long secondsYouWaited = 0;
	
	TextView timeWaitingTextView;
	
	
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
			tipAmount= savedInstanceState.getDouble(CURRENT_TIP); 
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
		
		//Finding the Checkbox Views by ID
		friendlyCheckBox = (CheckBox) findViewById(R.id.friendlyCheckBox);
		specialsCheckBox = (CheckBox) findViewById(R.id.specialsCheckBox);
		opinionCheckBox = (CheckBox) findViewById(R.id.opinionCheckBox);
		
		setUpIntroCheckBoxes();
		
		//Finding Checkbox Views By ID
		
		availableRadioGroup = (RadioGroup) findViewById(R.id.availableRadioGroup);
		availableBadRadio = (RadioButton) findViewById(R.id.availableBadRadio);
		availableOKRadio = (RadioButton) findViewById(R.id.availableOKRadio);
		availableGoodRadio = (RadioButton) findViewById(R.id.availableGoodRadio);
		
		addChangeListenersToRadios();
		
		problemsSpinner = (Spinner) findViewById(R.id.problemsSpinner);
		
		addItemSelectedListenerToSpinner();
		
		startChronometerButton = (Button) findViewById(R.id.startChronometerButton);
		pauseChronometerButton = (Button) findViewById(R.id.pauseChronometerButton);
		resetChronometerButton = (Button) findViewById(R.id.resetChronometerButton);
		
		setButtonOnClickListeners();
		
		timeWaitingChronometer = (Chronometer) findViewById(R.id.timeWaitingChronometer);
		
		timeWaitingTextView = (TextView) findViewById(R.id.timeWaiting);
		
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
			tipAmount = (tipSeekBar.getProgress()) * .01;
			
			tipAmountET.setText(String.format("%.02f", tipAmount));
			
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
		
		//Taking the tip amount inputted from the Edit Text box and turning it to a string, which is then parsed to a double
		double tipAmount = Double.parseDouble(tipAmountET.getText().toString());
		
		double finalBill = billBeforeTip + (billBeforeTip * tipAmount);
		
		//Setting the Edit Text box of final bill to the "finalBill" variable with a format of 2 decimal places
		finalBillET.setText(String.format("%.02f", finalBill));
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
	
	private void setUpIntroCheckBoxes(){
		
		friendlyCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				
				checklistValues[0] = (friendlyCheckBox.isChecked())?4:0;
				
				setTipFromWaitressChecklist();
				
				updateTipAndFinalBill();
				
			}
			
		});
		
		specialsCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				
				checklistValues[1] = (specialsCheckBox.isChecked())?1:0;
				
				setTipFromWaitressChecklist();
				
				updateTipAndFinalBill();
				
			}
			
		});
		
		opinionCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				
				checklistValues[2] = (opinionCheckBox.isChecked())?2:0;
				
				setTipFromWaitressChecklist();
				
				updateTipAndFinalBill();
				
			}
			
		});
		
	}
	
	private void setTipFromWaitressChecklist(){
		
		int checklistTotal = 0;
		
		for(int item : checklistValues){
			
			checklistTotal += item;
		}
		
		tipAmountET.setText(String.format("%.02f", checklistTotal * .01));
	}
	
	private void addChangeListenersToRadios(){
		
		availableRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				
				checklistValues[3] = (availableBadRadio.isChecked())?-1:0;
				checklistValues[4] = (availableOKRadio.isChecked())?2:0;
				checklistValues[5] = (availableGoodRadio.isChecked())?4:0;
				
				setTipFromWaitressChecklist();
				
				updateTipAndFinalBill();
			}
			
		});
	}
	
	private void addItemSelectedListenerToSpinner(){
		
		problemsSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				checklistValues[6] = (problemsSpinner.getSelectedItem()).equals("Bad")?-1:0;
				checklistValues[7] = (problemsSpinner.getSelectedItem()).equals("OK")?3:0;
				checklistValues[8] = (problemsSpinner.getSelectedItem()).equals("Good")?6:0;
				
				setTipFromWaitressChecklist();
				
				updateTipAndFinalBill();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
	}
	
	private void setButtonOnClickListeners(){
		
		startChronometerButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				int stoppedMilliseconds = 0;
				
				String chronoText = timeWaitingChronometer.getText().toString();
				String array[] = chronoText.split(":");
				
				if(array.length == 2){
					
					stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000 + Integer.parseInt(array[1]) * 1000;
				}
				else if (array.length == 3){
					
					stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000 + Integer.parseInt(array[1]) * 60 * 1000
							+ Integer.parseInt(array[2]) * 1000;
					
				}
				
				timeWaitingChronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
				
				secondsYouWaited = Long.parseLong(array[1]);
				
				updateTipBasedOnTimeWaited(secondsYouWaited);
				
				timeWaitingChronometer.start();
				
			}
			
		});
		
		pauseChronometerButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				timeWaitingChronometer.stop();
				
				updateTipBasedOnTimeWaited(secondsYouWaited);
			
			}
			
		});
		
		resetChronometerButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				timeWaitingChronometer.setBase(SystemClock.elapsedRealtime());
				
				secondsYouWaited= 0;
			}
			
		});
		
	}
	
	private void updateTipBasedOnTimeWaited( long secondsYouWaited){
		
		checklistValues[9] = ( secondsYouWaited > 10)?-2:2;
		
		setTipFromWaitressChecklist();
		
		updateTipAndFinalBill();
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.crazy_tip_calc, menu);
		return true;
	}

}
