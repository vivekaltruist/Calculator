package com.example.slidecal;

import java.math.BigInteger;
import java.text.DecimalFormat;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainCal extends Activity {
	private EditText display ;
	private float numBef;
	private String operation;
	private ButtonClickListener btnClick;
	private boolean operationClicked;
	private boolean equalClicked;
	private boolean resultDisplayed;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_cal);
		
		display = (EditText) findViewById(R.id.display);
		display.setEnabled(false);
		display.setText("0");
		
		int idList[] = {R.id.button0, R.id.button1,R.id.button2,R.id.button3,R.id.button4,
				R.id.button5,R.id.button6,R.id.button7,R.id.button8,R.id.button9,R.id.buttonDot,
				R.id.buttonAdd,R.id.buttonSub,R.id.buttonMul,R.id.buttonDiv,R.id.buttonCan,R.id.buttonEq,
				R.id.buttonCanE,R.id.buttonSin,R.id.buttonCos,R.id.buttonTan,R.id.buttonPi,R.id.buttonFact,
				R.id.buttonLn,R.id.buttonLog,R.id.buttonE,R.id.buttonRoot,R.id.buttonPow};
		
		btnClick = new ButtonClickListener();
		for(int id: idList){
			View v = findViewById(id);
			v.setOnClickListener(btnClick);
		}
		operationClicked=false;
		equalClicked = true;
		resultDisplayed=true;
	}
	
	private class ButtonClickListener implements OnClickListener{

		@Override
		public void onClick(final View v) {
			final int colorId = ((ColorDrawable) v.getBackground()).getColor();
			v.setBackgroundColor(Color.GREEN); // Choose whichever color

	        new Handler().postDelayed(new Runnable() {

	            public void run() {
	            	
	                v.setBackgroundColor(colorId);
	                
	                switch(v.getId()){
	    			
					case R.id.buttonCan: //clear screen
						display.setText("0");
						numBef=0;
						operation="";
						operationClicked=false;
						equalClicked = true;
						resultDisplayed=true;
						break;
						
					case R.id.buttonSin:
						eMath("Sin");
						break;
						
					case R.id.buttonCos:
						eMath("Cos");
						break;
						
					case R.id.buttonTan:
						eMath("Tan");
						break;
						
					case R.id.buttonLn:
						eMath("Ln");
						break;

					case R.id.buttonLog:
						eMath("Log");
						break;

					case R.id.buttonPow:
						mMath("Pow");
						break;

					case R.id.buttonRoot:
						eMath("Root");
						break;

					case R.id.buttonFact:
						eMath("Fact");
						break;
						
					case R.id.buttonPi:
						eKeyboard("Pi");
						break;
						
					case R.id.buttonE:
						eKeyboard("E");
						break;
						
					case R.id.buttonCanE:
						if(!operationClicked)
							display.setText("0");
						break;
						
					case R.id.buttonAdd:
						mMath("+");
						break;

					case R.id.buttonSub:
						mMath("-");
						break;

					case R.id.buttonMul:
						mMath("*");
						break;

					case R.id.buttonDiv:
						mMath("/");
						break;
						
					case R.id.buttonEq:
						mResult();
						break;
						
					default:
						String num = ((Button) v).getText().toString();
						getKeyboard(num);
						break;
				}
	            }

	        }, 100L);
			
		}

		private void mResult() {
			equalClicked=true;
			resultDisplayed=true;
			
			String scr = display.getText().toString();
			if(checkError())
				return;
			
			float numAf = Float.parseFloat(scr);
			double result=0;
			Boolean done = true;
			if(operation.equals("+")){
				result = numBef + numAf;
			}
			
			else if(operation.equals("-")){
				result = numBef - numAf;
			}
			
			else if(operation.equals("*")){
				result = numBef * numAf;
			}
			
			else if(operation.equals("/")){
				if(numAf==0){
					display.setText("Error");
					return;
				}
				else
					result = numBef / numAf;
				
			}
			else if(operation.equals("Sin")){
				result = (float) Math.sin(Math.toRadians(numBef));
			}
			else if(operation.equals("Cos")){
				result = (float) Math.cos(Math.toRadians(numBef));
			}
			else if(operation.equals("Tan")){
				result = (float) Math.tan(Math.toRadians(numBef));
			}
			else if(operation.equals("Ln")){
				result = (float) Math.log(numBef);
			}
			else if(operation.equals("Log")){
				result = (float) Math.log10(numBef);
			}
			else if(operation.equals("Pow")){
				result = (float) Math.pow(numBef, numAf);
			}
			else if(operation.equals("Root")){
				result = (float) Math.sqrt(numBef);
			}
			else if(operation.equals("Fact")){
				if(!(numBef == Math.floor(numBef)) || Math.round(numBef)>25){
					display.setText("Error");
					operation="";
					return;
				}
				BigInteger fact = BigInteger.valueOf(1);
			    for (int i = 1; i <= Math.round(numBef); i++)
			        fact = fact.multiply(BigInteger.valueOf(i));
			    result = fact.doubleValue();
			}
			else{
				done=false;
			}
				
			if(done){
				DecimalFormat decimalFormat=new DecimalFormat("#.######");
				display.setText(String.valueOf(decimalFormat.format(result)));	
			}
			operation="";
		}

		private void getKeyboard(String num) {
			String curDisplay = display.getText().toString();
			if(!operationClicked && (curDisplay.length() >10))
				return;
			if(operationClicked){
				curDisplay="";
				operationClicked=false;
				equalClicked=false;
			}
			if(resultDisplayed){
				curDisplay="";
				resultDisplayed=false;
			}
			if(num.equals(".")){
				if(curDisplay.contains("."))
					return;
				if(curDisplay.equals(""))
					num = "0.";
			}
			if(curDisplay.equals("0") && !num.equals("."))
				curDisplay="";
			
			curDisplay+=num;
			display.setText(curDisplay);
		}
		
		private void eKeyboard(String str){
			if(operationClicked){
				operationClicked=false;
				equalClicked=false;
			}
			if(resultDisplayed){
				resultDisplayed=false;
			}
			if(str.equals("Pi")){
				display.setText(String.valueOf(new DecimalFormat("#.######").format(Math.PI)));
			}
			else if(str.equals("E")){
				display.setText(String.valueOf(new DecimalFormat("#.######").format(Math.E)));
			}
		}

		private void mMath(String str) {
			if(checkError())
				return;
			
			if(!equalClicked && !operation.equals("")){
				mResult();
			}
			operationClicked=true;
			operation = str;
			
			if(checkError())
				return;
			numBef = Float.parseFloat(display.getText().toString());
		}
		
		private void eMath(String opr){
			if(checkError())
				return;
			numBef = Float.parseFloat(display.getText().toString());
			operation = opr;
			mResult();
		}
		
		public boolean checkError(){
			if(!MainCal.isNumeric(display.getText().toString())){
				display.setText("0");
				numBef=0;
				operation="";
				operationClicked=false;
				equalClicked = true;
				resultDisplayed=true;
				return true;
			}
			return false;
		}
	}
	
	
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    Float d = Float.parseFloat(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
}
