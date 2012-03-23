/**
 * Copyright (C) 2012 Bernd Battes
 * free software
 * free to distribute, making changes and improvements
 */  
package sl4a.activity.chart;

import sl4a.activity.chart.ChartUtil;

import java.util.ArrayList;
import java.util.List;
import java.net.Socket;
import java.io.DataOutputStream;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYValueSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.tools.ZoomEvent;
import org.achartengine.tools.ZoomListener;
import org.achartengine.tools.PanListener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.CountDownTimer;

public class ChartActi extends ListActivity {
	
	String[]			xListIn=			{""};
	String[]			xList0In=			{""};
	String[]			yListIn=			{""};
	String[]			yList0In=			{""};
	String[]			xList=				{""};
	String[]			xList0=				{""};
	String[]			yList=				{""};
	String[]			yList0=				{""};
	String[]			axesValsIn=			{"", "x axes", "y axes", "\"\"","\"\"", "\"\"","\"\"",  "gray", "gray"};
	String[]			axesValsZoom=		{"", "", "", "","", "","",  "", ""};
	String[]			axesVals=			{"", "x axes", "y axes", "","", "","",  "gray", "gray"};
	String[]			noLabels=			{"4", "4"};
	String[]			panLimits=			{"","", "",""};
	String[]			zoomLimits;
	String[]			zoomEnabledIn=		{"true", "true"};
	String[]			zoomEnabledZoom=	{"", ""};
	String[]			zoomEnabled=		{"true", "true"};
	String[]			defaultColors= 		{"red", "blue", "green", "gray", "cyan", "magenta", "yellow", "lightgray", "darkgray", "black", "white"};
	String[]			Colors, Colors0;
	String[]			PointStyles, PointStyles0;
	String[]			LineWidths, LineWidths0;
	String[]			legends=			{""};
	String[]			ChartLayout=		{""};
	String[]			ChartLayout0=		{""};
	String[] 			legende;
	String[]			bubbleList=			{""};
	String[]			bubbleList0=		{""};
	
	float				PointSize=			5f;
	float				PointSize0=			5f;
	float				zoomVal	=			1f;
	String				defaultLineW=		"2";
	String				MENU=				null;//"|OK";
	String				subMenuChar=		null;
	String				barType=			"default";
	String[]			mnuZoom;
	String[]			mnuChart;
	String				listStringSep="\\s*,\\s*";
	
	int[] 				colors;
	int					seriesCnt =			0;
	int					seriesCnt0 =		0;
	int					menuItem=			0;
	boolean				haveMenu=			false;
	boolean     		sokOk = 			true; // Status  Socket-connection
	boolean				spPressed=			false;
	boolean 			butClc =    		false;
	boolean				showGrid=			false;
	boolean				appendData=			false;
	int					selInd=				0;
	int					selChart;
	public 				GraphicalView	 	mChartView;

	long 				xMax, yMax = 		Long.MIN_VALUE;
	long 				xMin, yMin = 		Long.MAX_VALUE;
	List 				<String[]> 			llegend;

	boolean[]			selMenueIt;
	int					nextMenuItem	=0;
	
	TextView			textTop=null, textBot=null;
	CountDownTimer      cdTimer;
	
	LinearLayout layout;

  /** Called when the activity is first created. */
  public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
		try	{requestWindowFeature(Window.FEATURE_NO_TITLE);}	catch(Exception e) {}
		Intent intent = getIntent();    
		newIntent(intent);
  }
  public void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		newIntent(intent);
	}

  public void newIntent(Intent intent)
	{
		if(intent.getExtras()!=null)
		{
			setContentView(R.layout.main);
			
			String		textT=null, textB=null;
			textTop=(TextView)findViewById(R.id.textView1);
			textBot=(TextView)findViewById(R.id.textView2);
			Button butSel = (Button) findViewById(R.id.button1);
			Button butExit = (Button) findViewById(R.id.button2);
			final Spinner spMenue = (Spinner)findViewById(R.id.spMenue);
			final Button butSp = (Button)findViewById(R.id.butSp);
			final Button butPause = (Button)findViewById(R.id.butPause);
			final Button butMnuChart = (Button)findViewById(R.id.butMnu);
			mnuZoom=getResources().getStringArray(R.array.default_menue);
			mnuChart=getResources().getStringArray(R.array.chart_menue);
			
//			TextView	text1=null, text2=null;
			
			String[]	lvmethods=
			{
				"setListSeparator", 	"setMenu",				"setSubMenuChar"
			,	"setXVals",				"setYVals",				"setChartLayout",		"setLineWidths"
			,	"setColors",			"setPointStyles",		"setPointSize",			"setBubbleSize"
			,	"setXVals0",			"setYVals0",			"setChartLayout0",		"setLineWidths0"
			,	"setColors0",			"setPointStyles0",		"setPointSize0",		"setBubbleSize0"
			,	"setLegends",			"setAxes"
			,	"setNoLabels",			"showGrid",				"setPanLimits",			"setZoomLimits"
			,	"setZoomEnabled",		"setTextTop",			"setTextBot",			"setCountdownTimer"
			,	"setAppendData"
			};
			
			for(int i=0; i<lvmethods.length; i++)
			{
				if(!intent.hasExtra(lvmethods[i]))	continue;
//				Toast.makeText(this, "switch  " +i, Toast.LENGTH_SHORT).show();
				switch(i)
				{
					case 0:		listStringSep=(intent.getStringExtra(lvmethods[i]));				break;
					case 1:		MENU=		intent.getStringExtra(lvmethods[i]);	haveMenu=true;	break;
					case 2:		subMenuChar =intent.getStringExtra(lvmethods[i]);					break;

					case 3:		if (intent.getStringExtra(lvmethods[i]) != null) {xListIn=intent.getStringExtra(lvmethods[i]).split(";");}					break;
					case 4:		if (intent.getStringExtra(lvmethods[i]) != null) {yListIn=intent.getStringExtra(lvmethods[i]).split(";"); seriesCnt = yListIn.length;}		break;
					case 5:		if (ChartLayout[0] == "") ChartLayout=intent.getStringExtra(lvmethods[i]).split(listStringSep); 	break;
					case 6:		LineWidths= intent.getStringExtra(lvmethods[i]).split(listStringSep);	break;
					case 7:		Colors=intent.getStringExtra(lvmethods[i]).split(listStringSep);		break;
					case 8:		PointStyles=intent.getStringExtra(lvmethods[i]).split(listStringSep);  break;
					case 9:		PointSize=Float.valueOf(intent.getStringExtra(lvmethods[i]));  break;
					case 10:	if (intent.getStringExtra(lvmethods[i]) != null) {bubbleList=intent.getStringExtra(lvmethods[i]).split(";");}				break;
					
					case 11:	if (intent.getStringExtra(lvmethods[i]) != null) {xList0In=intent.getStringExtra(lvmethods[i]).split(";");}					break;
					case 12:	if (intent.getStringExtra(lvmethods[i]) != null) {yList0In=intent.getStringExtra(lvmethods[i]).split(";"); seriesCnt0 = yList0In.length;}		break;
					case 13:	if (ChartLayout0[0] == "") ChartLayout0=intent.getStringExtra(lvmethods[i]).split(listStringSep); 	break;
					case 14:	LineWidths0= intent.getStringExtra(lvmethods[i]).split(listStringSep);	break;
					case 15:	Colors0=intent.getStringExtra(lvmethods[i]).split(listStringSep);		break;
					case 16:	PointStyles0=intent.getStringExtra(lvmethods[i]).split(listStringSep);  break;
					case 17:	PointSize0=Float.valueOf(intent.getStringExtra(lvmethods[i]));  break;
					case 18:	if (intent.getStringExtra(lvmethods[i]) != null) {bubbleList0=intent.getStringExtra(lvmethods[i]).split(";");}				break;

					case 19:	legends=intent.getStringExtra(lvmethods[i]).split(listStringSep);	break;
					case 20:	axesValsIn=intent.getStringExtra(lvmethods[i]).split(listStringSep);		break;
					case 21:	if (intent.getStringExtra(lvmethods[i]) != null) {noLabels=intent.getStringExtra(lvmethods[i]).split(listStringSep);}		break;
//					case 22:	showGrid=intent.getBooleanExtra(lvmethods[i],false);	break;
					case 22:	showGrid = Boolean.valueOf(intent.getStringExtra(lvmethods[i]).toLowerCase());	break;
					case 23:	if (intent.getStringExtra(lvmethods[i]) != null) {panLimits=intent.getStringExtra(lvmethods[i]).split(listStringSep);}		break;
					case 24:	if (intent.getStringExtra(lvmethods[i]) != null) {zoomLimits=intent.getStringExtra(lvmethods[i]).split(listStringSep);}		break;
					case 25:	if (intent.getStringExtra(lvmethods[i]) != null) {zoomEnabledIn=intent.getStringExtra(lvmethods[i]).split(listStringSep);}	break;
					
					case 26:	textT = intent.getStringExtra(lvmethods[i]);		break;
					case 27:	textB = intent.getStringExtra(lvmethods[i]);		break;
					case 28:	setCountDownTimer(intent.getStringExtra(lvmethods[i]));	break;
					case 29:	appendData = Boolean.valueOf(intent.getStringExtra(lvmethods[i]).toLowerCase());	break;
					
				}//switch
			}//for


			if(textT!=null) {textTop.setText(textT); textTop.setTextSize(18);}
			if(textB!=null) {textBot.setText(textB); textBot.setTextSize(18);}
			
						
//			Toast.makeText(this, "appendData " +appendData, Toast.LENGTH_SHORT).show();
			if (appendData){
				if(yListIn.length > yList.length ){
					yList = yListIn.clone();
				}
				else{
					if(yListIn[0] != ""){
						for (int i=0; i<yListIn.length; i++){
							yList[i] += ", " +yListIn[i];
						}
					}
				}
				
				if(yList0In.length > yList0.length ){
					yList0 = yList0In.clone();
				}
				else{
					if(yList0In[0] != ""){ 
						for (int i=0; i<yList0In.length; i++){
							yList0[i] += ", " +yList0In[i];
						}
					}
				}
				if(xListIn.length > xList.length ){
					xList = xListIn.clone();
				}
				else{
					if(xListIn[0] != ""){
						for (int i=0; i<xListIn.length; i++){
							xList[i] += ", " +xListIn[i];
						}
					}
				}
				
				if(xList0In.length > xList0.length ){
					xList0 = xList0In.clone();
				}
				else{
					if(xList0In[0] != ""){ 
						for (int i=0; i<xList0In.length; i++){
							xList0[i] += ", " +xList0In[i];
						}
					}
				}
			}
			else{
				yList = yListIn.clone();
				yList0 = yList0In.clone();
				xList = xListIn.clone();
				xList0 = xList0In.clone();
			}

			
			for (int i=0; i<yList0.length; i++){
				if (yList0[i].startsWith(",")) yList0[i] = yList0[i].replaceFirst(",", " ");
//				yList0[i] = yList0[i].replace(" ", "");
				yList0[i] = yList0[i].trim();
			}
			for (int i=0; i<yList.length; i++){
				if (yList[i].startsWith(",")) yList[i] = yList[i].replaceFirst(",", " ");		
				yList[i] = yList[i].trim();
			}
			for (int i=0; i<xList0.length; i++){
				if (xList0[i].startsWith(",")) xList0[i] = xList0[i].replaceFirst(",", " ");
				xList0[i] = xList0[i].trim();
			}
			for (int i=0; i<xList.length; i++){
				if (xList[i].startsWith(",")) xList[i] = xList[i].replaceFirst(",", " ");
				xList[i] = xList[i].trim();
			}
			
			
			try{
				layout.removeAllViews();		// improves stability for calls of activity in a loop
			}catch(NullPointerException e){}
			
			
//			UNDERLayer (if activated)
			if ((yList0.length > 0)&(yList0[0]!="")){
				makeChart(this, ChartLayout0, xList0, yList0, bubbleList0, Colors0, LineWidths0, PointStyles0,
						PointSize0, seriesCnt0, R.id.chart0);
			}
			
//			MAINLayer (TopLayer)  receives 'mouse' clicks
			if ((yList.length > 0)&(yList[0]!="")){
				makeChart(this, ChartLayout, xList, yList, bubbleList, Colors, LineWidths, PointStyles,
						PointSize, seriesCnt, R.id.chart);
			}

		
	
			butSel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					butClc = true;
					putResult(0);
					if (cdTimer != null) cdTimer.cancel();
					wrSocket("butSelClick noError");
				}
			});
			butExit.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					wrSocket("butExitClick");
					finish();
				}
			});
			butSp.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (cdTimer != null) cdTimer.cancel();
					callDialog1();	
					wrSocket("menuZoomClick noError");
				}
			});
			butMnuChart.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (cdTimer != null) cdTimer.cancel();
					callDialog2();	
					wrSocket("menuChartClick noError");
				}
			});
			
			
			butPause.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (cdTimer != null) cdTimer.cancel();
					wrSocket("butPauseClick noError");
					if ( butPause.getText() == "Pause"){
						butPause.setText(" Run ");
					}
					else {
						butPause.setText("Pause");
					}
				}
			});
			

			spMenue.setOnItemSelectedListener(new OnItemSelectedListener()
			{
//				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					if(spPressed){
					  selInd = spMenue.getSelectedItemPosition();
					  Toast.makeText(getBaseContext(), 
		                    "You have selected item : " + selInd, 
		                    Toast.LENGTH_SHORT).show(); 
					  spPressed = false;
					  if (cdTimer != null) cdTimer.cancel();
//					  wrSocket("itemClick a:" +selInd +"b:" +" noError");
					}
					else{
					  spMenue.setSelection(selInd);	
					}
				}

//				@Override
				public void onNothingSelected(AdapterView<?> arg0) {}
				
			});
			
//			wrSocket("dummy");
		}
	}

	int getIntentNumber(Intent intent, String option)	{return((int)Long.decode(intent.getStringExtra(option)).intValue());}
	int getIntentFloat(Intent intent, String option)	{return((int)Float.parseFloat(intent.getStringExtra(option)));}
	
	
	
	
// -------------------------------------------------------------------------
	
	
	public void makeChart(Context context, String[] ChartLayout, String[] xList, String[] yList,
			String[] bubbleList, String[] Colors, String[] LineWidths, String[] PointStyles,
			float PointSize, int seriesCnt, int RidChart ) {
		final XYMultipleSeriesRenderer 				renderer;
		DefaultRenderer							defrenderer;
		double[] 								yValues;
		List				<double[]> 			x;
		List				<double[]> 			values;
		List				<double[]> 			bubbleSiz;
		PointStyle[] 		styles;
		String[][]			xVals =				{{""},{""}};
		String[][]			yVals =				{{""},{""}};
		String[][]			bubVals =			{{""},{""}};
		
		renderer = new XYMultipleSeriesRenderer();
		
		layout = (LinearLayout) findViewById(RidChart);	
		
//		if necessary create x values and bubble size values according to number of y values
		int max =0;
		for (int i = 0; i < seriesCnt; i++) {
			if (yList[i].length() > max) max = yList[i].length();
		}
		yVals = new String[seriesCnt][max];
		xVals = new String[seriesCnt][max];
		bubVals = new String[seriesCnt][max];
		for (int i = 0; i < seriesCnt; i++) {
			yVals[i] = yList[i].split(listStringSep);
			try{ 
				xVals[i] = xList[i].split(listStringSep);
			}catch(NullPointerException e){
				xVals[i][0] = "";						// filled with meaningful values later on
			}catch(IndexOutOfBoundsException e){
				xVals[i] = xVals[i-1];
			}
			try{ 
				bubVals[i] = bubbleList[i].split(listStringSep);
			}catch(NullPointerException e){
				bubVals[i][0] = "";						// filled with meaningful values later on
			}catch(IndexOutOfBoundsException e){
//				bubVals[i] = xVals[i-1];
				bubVals[i] = bubVals[i-1];
			}
		}


//		Menu Zoom    enable / disable zoom
		selMenueIt = new boolean[mnuZoom.length];
		for (int i=0; i < mnuZoom.length; i++ ){
			selMenueIt[i] = false;
		}
		
		if (zoomEnabledZoom[0] == "") zoomEnabled[0] = zoomEnabledIn[0];
		else zoomEnabled[0] = zoomEnabledZoom[0];
		
		try{
			if (zoomEnabledZoom[1] == "") zoomEnabled[1] = zoomEnabledIn[1];
			else zoomEnabled[1] = zoomEnabledZoom[1];
		}catch(IndexOutOfBoundsException e){
			zoomEnabled[1] = zoomEnabledIn[0];
		}
		selMenueIt[0] = Boolean.valueOf(zoomEnabled[0].toLowerCase());
		selMenueIt[1] = Boolean.valueOf(zoomEnabled[1].toLowerCase());

		if (axesVals[5] == "0") selMenueIt[2] = true;	// yMin = 0
		else selMenueIt[2] = false;
		
		if (axesValsZoom[3] == ""& axesValsZoom[5] == "") axesVals = axesValsIn;
		else axesVals = axesValsZoom;	
		
		if (selMenueIt[2] == true) axesVals[5] = "0";	// yMin = 0
		
		
//		Menu Chart    which Chart selected
		if (ChartLayout[0] == "") ChartLayout[0] = "line";
		for (int i=0; i < mnuChart.length; i++ ){
			if (ChartLayout[0].equalsIgnoreCase(mnuChart[i]))  selChart = i;
		}

		if (ChartLayout[0].equalsIgnoreCase("Bar")){
			if (ChartLayout.length == 1){
				ChartLayout = new String[2];}
			ChartLayout[0] = "bar";
			ChartLayout[1] = "default";
		}
		if (ChartLayout[0].equalsIgnoreCase("Stack")){
			if (ChartLayout.length == 1){
				ChartLayout = new String[2];}
			ChartLayout[0] = "bar";
			ChartLayout[1] = "stacked";
		}
	
		
//		convert values for use in achartengine
		x = new ArrayList<double[]>();
		values = new ArrayList<double[]>();
		bubbleSiz = new ArrayList<double[]>();
		double dwert1;
		for (int i = 0; i < seriesCnt; i++) {	
		    int count = yVals[i].length;
			double[] xValues = new double[count];                      
	        yValues = new double[count];  
	        double[] bubSiz = new double[count];
	        for (int k = 0; k < count; k++) {
	        	if(xVals[i][0] == ""){ 
                	xValues[k] = k + 1;
                	if(k+1 < xMin){xMin = k+1;}
                	if(k+1 > xMax){xMax = k+1;}
                	}                   
                else{ 
                	try{
	//                	dwert1 = Double.valueOf(xVals[i][k]).doubleValue();
		                dwert1 = Double.valueOf(xVals[i][k]);  // seems to work              
	                }catch (IndexOutOfBoundsException e){
	                	dwert1 = xValues[k-1] +1;
	                	Toast.makeText(ChartActi.this, "Less X than Y values! " , Toast.LENGTH_SHORT).show();
	            	}
	                
	                xValues[k] = dwert1;
	                if(dwert1 < xMin){xMin = Math.round(dwert1);}
                	if(dwert1 > xMax){xMax = Math.round(dwert1);}
                }  

	        	if (ChartLayout[0].equalsIgnoreCase("bubble")){
		        	if(bubVals[i][0] == ""){ 
		        		bubSiz[k] = Double.valueOf(yVals[i][k]);
	                	}                   
	                else{
	                	try{
	                		dwert1 = Double.valueOf(bubVals[i][k]);             
	                		bubSiz[k] = dwert1;
	                	}catch (IndexOutOfBoundsException e){
	                		bubSiz[k] = Double.valueOf(yVals[i][k]);
	                		Toast.makeText(ChartActi.this, "Less bubble size than Y values! " , Toast.LENGTH_SHORT).show();
	                	}
	                }        
	        	}
	            double dwert2 = Double.valueOf(yVals[i][k]);
	            yValues[k] = dwert2;
	            if(dwert2 < yMin){yMin = Math.round(dwert2);}
            	if(dwert2 > yMax){yMax = Math.round(dwert2);}


	        }// for k
	      
		    x.add(xValues);
		    values.add(yValues);
		    bubbleSiz.add(bubSiz);
		}// for i

		
		Charts ChartType = Charts.valueOf(ChartLayout[0].toUpperCase());
		
		if  ((values.get(0)[0] != 0.0)& (values.size() > 0)){
			switch(ChartType)
			{
				case LINE:
				case AREA:
				case SCATTER:
				case BUBBLE:
				case BAR:
					legende = new String[seriesCnt];
					for (int i = 0; i < seriesCnt; i++) {
						try{
							legende[i] = legends[i];
						}catch(NullPointerException e){
							legende[i] = "";
						}catch(IndexOutOfBoundsException e){
							legende[i] = "";
						}
					}
					
					colors = new int[seriesCnt];
					
					for (int i = 0; i < seriesCnt ; i++) {
						try{
							colors[i] = Color.parseColor(Colors[i].toLowerCase());
						}catch(NullPointerException e){
							int c= i % defaultColors.length;
							colors[i] = Color.parseColor(defaultColors[c]);
						}catch(IndexOutOfBoundsException e){
							int c= i % defaultColors.length;
							colors[i] = Color.parseColor(defaultColors[c]);
						}catch(IllegalArgumentException e){                // unknown Color
							int c= i % defaultColors.length;
							colors[i] = Color.parseColor(defaultColors[c]);
						}
					}
					
					
					try{
						if (Colors[0].toLowerCase().contains("gradient")) {
							int startCol, endCol;
							try{
								startCol = Color.parseColor(Colors[1].toLowerCase());
							}catch(NullPointerException e){
								startCol = Color.parseColor(defaultColors[1]);
							}catch(IndexOutOfBoundsException e){
								startCol = Color.parseColor(defaultColors[1]);
							}
							try{
								endCol = Color.parseColor(Colors[2].toLowerCase());
							}catch(NullPointerException e){
								endCol = Color.parseColor(defaultColors[2]);
							}catch(IndexOutOfBoundsException e){
								endCol = Color.parseColor(defaultColors[2]);
							}
							int noCol = Colors.length;
							int noExtraCol = noCol - 3;			// with gradient, every color behind endColor (Colors[2]) is ExtraCol 
							if (noExtraCol < 0) noExtraCol = 0;
					
							for (int i = 0; i < seriesCnt -noExtraCol ; i++) {
							     float ratio = (float)i / (float)seriesCnt;
							     int red = (int)(Color.red(endCol) * ratio + Color.red(startCol) * (1 - ratio));
							     int green = (int)(Color.green(endCol) * ratio +Color.green(startCol) * (1 - ratio));
							     int blue = (int)(Color.blue(endCol) * ratio +Color.blue(startCol) * (1 - ratio));
							     int c = Color.rgb(red, green, blue);
							     colors[i] = c;
							}
							for (int i = 0 ; i < noExtraCol ; i++) {
								try{
									colors[seriesCnt -noExtraCol +i] = Color.parseColor(Colors[3+i].toLowerCase());
								}catch(NullPointerException e){
									int c= i % defaultColors.length;
									colors[seriesCnt -noExtraCol +i] = Color.parseColor(defaultColors[c]);
								}catch(IndexOutOfBoundsException e){
									int c= i % defaultColors.length;
									colors[seriesCnt -noExtraCol +i] = Color.parseColor(defaultColors[c]);
								}catch(IllegalArgumentException e){                // unknown Color
									int c= i % defaultColors.length;
									colors[seriesCnt -noExtraCol +i] = Color.parseColor(defaultColors[c]);
								}
							}
						}
					}catch(NullPointerException e){
					}catch(IndexOutOfBoundsException e){}
					
					if (axesVals[3].toLowerCase().contains("none")| axesVals[3].contains("\"\"")) axesVals[3] = String.valueOf(xMin-xMin/10);
				    if (axesVals[4].toLowerCase().contains("none")| axesVals[4].contains("\"\"")) axesVals[4] = String.valueOf(xMax+xMax/10);
				    if (axesVals[5].toLowerCase().contains("none")| axesVals[5].contains("\"\"")) axesVals[5] = String.valueOf(yMin-yMin/10);
				    if (axesVals[6].toLowerCase().contains("none")| axesVals[6].contains("\"\"")) axesVals[6] = String.valueOf(yMax+yMax/10);
				    if (axesVals[7].toLowerCase().contains("none")| axesVals[7].contains("\"\"")) axesVals[7] = "gray";
				    if (axesVals[8].toLowerCase().contains("none")| axesVals[8].contains("\"\"")) {axesVals[8] = "gray";}
					
					switch(ChartType){	
						case LINE:
						case AREA:
						case SCATTER:
							styles = new PointStyle[seriesCnt];
							PointStyle defaultStyle = PointStyle.POINT;
//							PointStyle defaultStyle = PointStyle.CIRCLE;
							if (ChartLayout[0].toUpperCase().contains("SCATTER")){
								defaultStyle = PointStyle.CIRCLE;
							}
							for (int i = 0; i < seriesCnt ; i++) {
								try{
									styles[i] = PointStyle.getPointStyleForName(PointStyles[i].toLowerCase());
								}catch(NullPointerException e){
									styles[i] = defaultStyle;
								}catch(IndexOutOfBoundsException e){
									styles[i] = styles[i-1];
								}
							}	
	
							String[] LW = LineWidths;
							LineWidths = new String[seriesCnt];
							for (int i = 0; i < seriesCnt ; i++) {
								try{
									LineWidths[i] =LW[i];
								}catch(NullPointerException e){
									LineWidths[i] = defaultLineW;
								}catch(IndexOutOfBoundsException e){
									LineWidths[i] = LineWidths[i-1];
								}
							}
							
						    renderer.setAxisTitleTextSize(16);
						    renderer.setChartTitleTextSize(20);
						    renderer.setLabelsTextSize(15);
						    renderer.setLegendTextSize(15);
						    renderer.setPointSize(PointSize);
						    renderer.setMargins(new int[] { 20, 30, 15, 20 });
						    
						    int length = colors.length;
						    for (int i = 0; i < length; i++) {
						      XYSeriesRenderer r = new XYSeriesRenderer();
						      if (ChartType == Charts.AREA){
						    	  r.setColor(Color.BLACK);
						      }else{
						    	  r.setColor(colors[i]);
						      }
						      r.setPointStyle(styles[i]);
						      renderer.addSeriesRenderer(r);
						    }  
						      
							length = renderer.getSeriesRendererCount();
							
						    for (int i = 0; i < length; i++) {
						    
						      XYSeriesRenderer seriesRenderer = (XYSeriesRenderer) renderer.getSeriesRendererAt(i);
						      if (ChartType == Charts.AREA){  
						          seriesRenderer.setFillBelowLine(true);
							      seriesRenderer.setFillBelowLineColor(colors[i]);
						      }
						      seriesRenderer.setFillPoints(true);
						      seriesRenderer.setLineWidth(Integer.valueOf(LineWidths[i]));
						      
						    }
			
						    /* axes: description  limits  axescolor  textcolor */
						    renderer.setChartTitle(axesVals[0]);
						    renderer.setXTitle(axesVals[1]);
						    renderer.setYTitle(axesVals[2]);
							
						    renderer.setXAxisMin(Double.valueOf(axesVals[3]));
						    renderer.setXAxisMax(Double.valueOf(axesVals[4]));
						    renderer.setYAxisMin(Double.valueOf(axesVals[5]));
						    renderer.setYAxisMax(Double.valueOf(axesVals[6]));
						    renderer.setAxesColor(Color.parseColor(axesVals[7]));
						    renderer.setLabelsColor(Color.parseColor(axesVals[8]));
						    
						    renderer.setXLabels(Integer.valueOf(noLabels[0]));
						    renderer.setYLabels(Integer.valueOf(noLabels[1]));
						    renderer.setShowGrid(showGrid);
						    renderer.setXLabelsAlign(Align.RIGHT);
						    renderer.setYLabelsAlign(Align.RIGHT);
						    renderer.setZoomButtonsVisible(true);
						    renderer.setPanLimits(new double[] { xMin-Math.abs(xMin)-2, xMax+xMax+2, yMin-Math.abs(yMin)-2, yMax+yMax+2 });
						    try{
						    	renderer.setZoomLimits(new double[] { Integer.valueOf(zoomLimits[0]), Integer.valueOf(zoomLimits[1]), Integer.valueOf(zoomLimits[2]), Integer.valueOf(zoomLimits[3]) });
						    }catch(IndexOutOfBoundsException e){
							}catch(NullPointerException e){}
						    
							try{
								renderer.setZoomEnabled(Boolean.valueOf(zoomEnabled[0].toLowerCase()), Boolean.valueOf(zoomEnabled[1].toLowerCase()));
							}catch(IndexOutOfBoundsException e){
								renderer.setZoomEnabled(Boolean.valueOf(zoomEnabled[0].toLowerCase()), Boolean.valueOf(zoomEnabled[0].toLowerCase()));
							}
							
							
							switch(ChartType){
							case LINE:
							case AREA:
								mChartView = ChartFactory.getLineChartView(context, ChartUtil.buildDataset(legende, x, values),renderer); break;
							case SCATTER:
								mChartView = ChartFactory.getScatterChartView(context, ChartUtil.buildDataset(legende, x, values),renderer); break;
							} // switch 2
						
						break;
						case BUBBLE:
							 XYMultipleSeriesDataset seriesB = new XYMultipleSeriesDataset();
							 for (int i = 0; i < seriesCnt ; i++) {
								 XYValueSeries bubbleSeries = new XYValueSeries(legende[i]);
								 XYSeriesRenderer bubbleRenderer = new XYSeriesRenderer();
								 for (int j = 0; j < bubbleSiz.get(i).length ; j++) {
									 bubbleSeries.add(x.get(i)[j], values.get(i)[j], bubbleSiz.get(i)[j]);			// bubbleSeries.add(x, y, bubbSiz);
								 }
								 seriesB.addSeries(bubbleSeries);
								 
								 bubbleRenderer.setColor(colors[i]);
								 renderer.addSeriesRenderer(bubbleRenderer);
							 }
						     
							 renderer.setAxisTitleTextSize(16);
							 renderer.setChartTitleTextSize(20);
							 renderer.setLabelsTextSize(15);
							 renderer.setLegendTextSize(15);
							 renderer.setMargins(new int[] { 20, 30, 15, 0 });   
							 
							 /* axes: description  limits  axescolor  textcolor */
							 renderer.setChartTitle(axesVals[0]);
							 renderer.setXTitle(axesVals[1]);
							 renderer.setYTitle(axesVals[2]);
							 renderer.setXAxisMin(Double.valueOf(axesVals[3]));
							 renderer.setXAxisMax(Double.valueOf(axesVals[4]));
							 renderer.setYAxisMin(Double.valueOf(axesVals[5]));
							 renderer.setYAxisMax(Double.valueOf(axesVals[6]));
							 renderer.setAxesColor(Color.parseColor(axesVals[7]));
							 renderer.setLabelsColor(Color.parseColor(axesVals[8]));
							 
							 renderer.setXLabels(Integer.valueOf(noLabels[0]));
							 renderer.setYLabels(Integer.valueOf(noLabels[1]));
							 renderer.setShowGrid(showGrid);
							 renderer.setZoomButtonsVisible(true);
							 renderer.setPanLimits(new double[] { xMin-Math.abs(xMin)-2, xMax+xMax+2, yMin-Math.abs(yMin)-2, yMax+yMax+2 });
							 try{
							 	renderer.setZoomLimits(new double[] { Integer.valueOf(zoomLimits[0]), Integer.valueOf(zoomLimits[1]), Integer.valueOf(zoomLimits[2]), Integer.valueOf(zoomLimits[3]) });
							 }catch(IndexOutOfBoundsException e){
							 }catch(NullPointerException e){}
							    
							 try{
								renderer.setZoomEnabled(Boolean.valueOf(zoomEnabled[0].toLowerCase()), Boolean.valueOf(zoomEnabled[1].toLowerCase()));
							 }catch(IndexOutOfBoundsException e){
								renderer.setZoomEnabled(Boolean.valueOf(zoomEnabled[0].toLowerCase()), Boolean.valueOf(zoomEnabled[0].toLowerCase()));
							 }
	
							 mChartView = ChartFactory.getBubbleChartView(context, seriesB, renderer);
						break;
						
						case BAR:	

							 renderer.setAxisTitleTextSize(16);
							 renderer.setChartTitleTextSize(20);
							 renderer.setLabelsTextSize(15);
							 renderer.setLegendTextSize(15);
							 length = colors.length;
							 for (int i = 0; i < length; i++) {
							   SimpleSeriesRenderer barRenderer = new SimpleSeriesRenderer();
							   barRenderer.setColor(colors[i]);
							   renderer.addSeriesRenderer(barRenderer);
							 }
							    
							 if (axesVals[3].toLowerCase().contains("none")| axesVals[3].contains("\"\"")) axesVals[3] = String.valueOf(xMin-1);
							 if (axesVals[4].toLowerCase().contains("none")| axesVals[4].contains("\"\"")) axesVals[4] = String.valueOf(xMax+1);
							 if (axesVals[5].toLowerCase().contains("none")| axesVals[5].contains("\"\"")) axesVals[5] = String.valueOf(yMin-1);
							 if (axesVals[6].toLowerCase().contains("none")| axesVals[6].contains("\"\"")) axesVals[6] = String.valueOf(yMax+1);
							 if (axesVals[7].toLowerCase().contains("none")| axesVals[7].contains("\"\"")) axesVals[7] = "gray";
							 if (axesVals[8].toLowerCase().contains("none")| axesVals[8].contains("\"\"")) {axesVals[8] = "gray";}
							 
							 /* axes: description  limits  axescolor  textcolor */
						     renderer.setChartTitle(axesVals[0]);
						     renderer.setXTitle(axesVals[1]);
						     renderer.setYTitle(axesVals[2]);
						     renderer.setXAxisMin(Double.valueOf(axesVals[3]));
						     renderer.setXAxisMax(Double.valueOf(axesVals[4]));
						     renderer.setYAxisMin(Double.valueOf(axesVals[5]));
						     renderer.setYAxisMax(Double.valueOf(axesVals[6]));
						     renderer.setAxesColor(Color.parseColor(axesVals[7]));
						     renderer.setLabelsColor(Color.parseColor(axesVals[8]));
							 
							 renderer.setXLabels(12);
							 renderer.setYLabels(10);
							 renderer.setXLabelsAlign(Align.LEFT);
							 renderer.setYLabelsAlign(Align.LEFT);
							 renderer.setPanEnabled(true, true);	// setPanEnabled(x, y)
							 renderer.setPanLimits(new double[] { xMin-Math.abs(xMin)-2, xMax+xMax+2, yMin-Math.abs(yMin)-2, yMax+yMax+2 });
							 renderer.setZoomButtonsVisible(true);
							 try{
								renderer.setZoomEnabled(Boolean.valueOf(zoomEnabled[0].toLowerCase()), Boolean.valueOf(zoomEnabled[1].toLowerCase()));
							}catch(IndexOutOfBoundsException e){
								renderer.setZoomEnabled(Boolean.valueOf(zoomEnabled[0].toLowerCase()), Boolean.valueOf(zoomEnabled[0].toLowerCase()));
							}
							 renderer.setBarSpacing(0.5f);
							 
							 length = renderer.getSeriesRendererCount();
							 for (int i = 0; i < length; i++) {
								 SimpleSeriesRenderer seriesRenderer = renderer.getSeriesRendererAt(i);
							     seriesRenderer.setDisplayChartValues(true);
							 }
							 try{
								 mChartView = ChartFactory.getBarChartView(context, ChartUtil.buildDataset(legende, x, values),renderer, Type.valueOf(ChartLayout[1].toUpperCase()));
							 }catch(NullPointerException e){
								 mChartView = ChartFactory.getBarChartView(context, ChartUtil.buildDataset(legende, x, values),renderer, Type.DEFAULT);
							 }catch(IndexOutOfBoundsException e){
								 mChartView = ChartFactory.getBarChartView(context, ChartUtil.buildDataset(legende, x, values),renderer, Type.DEFAULT);
							 }
							 
						break;
					} // end switch
				
					renderer.setClickEnabled(true);
				    renderer.setSelectableBuffer(10);	// radi around point registered as clicked
				break;
				
				case PIE:
					colors = new int[yVals[0].length];
					for (int i = 0; i < yVals[0].length ; i++) {
						try{
							colors[i] = Color.parseColor(Colors[i].toLowerCase());
						}catch(NullPointerException e){
							int c= i % defaultColors.length;
							colors[i] = Color.parseColor(defaultColors[c]);
						}catch(IndexOutOfBoundsException e){
							int c= i % defaultColors.length;
							colors[i] = Color.parseColor(defaultColors[c]);
						}catch(IllegalArgumentException e){                // unknown Color
							int c= i % defaultColors.length;
							colors[i] = Color.parseColor(defaultColors[c]);
						}
						
					}	
					defrenderer = ChartUtil.buildCategoryRenderer(colors);
					try{
						defrenderer.setZoomEnabled(Boolean.valueOf(zoomEnabled[1].toLowerCase()));
					}catch(IndexOutOfBoundsException e){
						defrenderer.setZoomEnabled(Boolean.valueOf(zoomEnabled[0].toLowerCase()));
					}
					defrenderer.setZoomButtonsVisible(true);
					
					legende = new String[yVals[0].length];
					for (int i = 0; i < yVals[0].length ; i++) {
						try{
							legende[i] = legends[i];
						}catch(NullPointerException e){
							legende[i] = "";
						}catch(IndexOutOfBoundsException e){
							legende[i] = "";
						}
					}	
					
					CategorySeries series = new CategorySeries("title");
					for (int i = 0; i < yVals[0].length ; i++) {
//							Toast.makeText(ChartActi.this, "bubVals " + yVals[0][i] +" " + values.size() , Toast.LENGTH_SHORT).show();
						series.add(legende[i], Double.valueOf(yVals[0][i]));
					}
					mChartView = ChartFactory.getPieChartView(context, series, defrenderer);
				break;
				
				case DOU:
					ArrayList<String[]> legend = new ArrayList<String[]>();
					for (int i = 0; i < seriesCnt; i++) {	
					    int count = yVals[i].length;
						String[] lValues = new String[count]; 
						for (int j = 0; j < yVals[i].length ; j++) {
							try{
								lValues[j] = legends[i]+String.valueOf(j);
							}catch(NullPointerException e){
								lValues[j] = String.valueOf(j);
							}catch(IndexOutOfBoundsException e){
								lValues[j] = String.valueOf(j);
							}
						}
						legend.add(lValues);
					}
					
					colors = new int[yVals[0].length];
					for (int i = 0; i < yVals[0].length ; i++) {
						try{
							colors[i] = Color.parseColor(Colors[i].toLowerCase());
						}catch(NullPointerException e){
							int c= i % defaultColors.length;
							colors[i] = Color.parseColor(defaultColors[c]);
						}catch(IndexOutOfBoundsException e){
							int c= i % defaultColors.length;
							colors[i] = Color.parseColor(defaultColors[c]);
						}catch(IllegalArgumentException e){                // unknown Color
							int c= i % defaultColors.length;
							colors[i] = Color.parseColor(defaultColors[c]);
						}
						
					}	
					
					defrenderer = ChartUtil.buildCategoryRenderer(colors);
					try{
						defrenderer.setZoomEnabled(Boolean.valueOf(zoomEnabled[1].toLowerCase()));
					}catch(IndexOutOfBoundsException e){
						defrenderer.setZoomEnabled(Boolean.valueOf(zoomEnabled[0].toLowerCase()));
					}
					defrenderer.setZoomButtonsVisible(true);
					
					MultipleCategorySeries mSeries = new MultipleCategorySeries("");
					for (int i = 0; i < seriesCnt ; i++) {
//							Toast.makeText(ChartActi.this, "values " + i +"   "+ values.get(i)[0] +"legend " + legend.get(i)[0], Toast.LENGTH_SHORT).show();
						mSeries.add("Series "+ (i+1) + "",legend.get(i), values.get(i));
					}
					mChartView = ChartFactory.getDoughnutChartView(context, mSeries, defrenderer);
					
				break;

			} // switch1
		} // if  ((values.get(0)[0] != 0.0)& (values.size() > 1)){
	
//		Toast.makeText(ChartActi.this, "mChartView " + mChartView , Toast.LENGTH_LONG).show();

		layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

	    mChartView.setOnClickListener(new View.OnClickListener() {
//	        @Override
	        public void onClick(View v) {	
	          SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
	          if (seriesSelection != null) {
	        	if (cdTimer != null) cdTimer.cancel();  
	        	wrSocket("dataClick " +"a:"+seriesSelection.getSeriesIndex()+"b:"+seriesSelection.getPointIndex()+"c:"+ " noError");  
	          }
	        }
	      }); 
	    mChartView.addZoomListener(new ZoomListener() {
	    	
	        public void zoomApplied(ZoomEvent e) {
	        	axesValsZoom = axesVals;
	        	if (zoomEnabled[0].toLowerCase().contains("true")){
		        	axesValsZoom[3] = String.valueOf(renderer.getXAxisMin());
					axesValsZoom[4] = String.valueOf(renderer.getXAxisMax());
//				Toast.makeText(ChartActi.this, "minXval " + axesVals[3], Toast.LENGTH_LONG).show();
	        	}
	        	if (zoomEnabled[1].toLowerCase().contains("true")){
	        		axesValsZoom[5] = String.valueOf(renderer.getYAxisMin());
					axesValsZoom[6] = String.valueOf(renderer.getYAxisMax());
	        	}
	        }
	        public void zoomReset() {
		          System.out.println("Reset");
		          axesValsZoom[3] = "";
		          axesValsZoom[5] = "";
		        }
	      }, true, true);
	    
	    mChartView.addPanListener(new PanListener() {
	        public void panApplied() {
	        	axesValsZoom = axesVals;
	        	axesValsZoom[3] = String.valueOf(renderer.getXAxisMin());
				axesValsZoom[4] = String.valueOf(renderer.getXAxisMax());
        		axesValsZoom[5] = String.valueOf(renderer.getYAxisMin());
				axesValsZoom[6] = String.valueOf(renderer.getYAxisMax());
	        }
	      });
	} // makeChart
	
	
	
	public void callDialog1()
	{
		final boolean[] selOld = selMenueIt.clone();
		new AlertDialog.Builder(ChartActi.this)
        .setTitle("select")
        .setMultiChoiceItems(R.array.default_menue
        		, selMenueIt,   // new boolean[]{false, true},
                new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton,
                            boolean isChecked) {
                    	selMenueIt[whichButton] = isChecked;
                    	
                    	if (selMenueIt[0]){					// zoom X
                    		zoomEnabledZoom[0] = "True";
                    	}
                    	else{
                    		zoomEnabledZoom[0] = "False";
                    	}
                    	
                    	if (selMenueIt[1]){					// zoom Y
                    		zoomEnabledZoom[1] = "True";
                    	}
                    	else{
                    		zoomEnabledZoom[1] = "False";
                    	}
                    		
                    	if (selMenueIt[2]){					// Y - scale starts with 0
                    		panLimits[2] = "-2";
                    		axesVals[5] = "0";
                    	}
                    	else{
                    		panLimits[2] = "none";
                    		axesVals[5] = "none";
                    	}
                    }
                })
        .setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	layout.removeAllViews();
//            	UNDERLayer if activated
            	if ((yList0.length > 0)&(yList0[0]!="")){
    				makeChart(ChartActi.this, ChartLayout0, xList0, yList0, bubbleList0, Colors0, LineWidths0, PointStyles0,
    						PointSize0, seriesCnt0, R.id.chart0);
    			}
    			
//    			MAINLayer (TopLayer)  receives 'mouse' clicks
            	if ((yList.length > 0)&(yList[0]!="")){
    				makeChart(ChartActi.this, ChartLayout, xList, yList, bubbleList, Colors, LineWidths, PointStyles,
    						PointSize, seriesCnt, R.id.chart);
    			}
    			String XstartsAt0 = "False";
    			if (axesVals[5] == "0") XstartsAt0 = "True";
    			if (cdTimer != null) cdTimer.cancel();
    			wrSocket("selZoom" +"a:" +zoomEnabledZoom[0] +"b:" +zoomEnabledZoom[1] +"c:" +XstartsAt0 +"d:"+" noError");
            }
        })
        .setNegativeButton("Cancle",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	selMenueIt = selOld;
            }
        })
       .create().show();

	}	
	
	public void callDialog2()
	{
		final int selOld = selChart;
		new AlertDialog.Builder(ChartActi.this)
        .setTitle("select")
        .setSingleChoiceItems(R.array.chart_menue
        		, selChart,   
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                    	selChart = whichButton;
                    	switch(whichButton){
        				case 0:
        					ChartLayout[0] = "line";
        				break;
        				case 1:
        					ChartLayout[0] = "area";
        				break;
        				case 2:
        					ChartLayout[0] = "scatter";
        				break;
        				case 3:
        					ChartLayout[0] = "bubble";
        				break;
        				case 4:
        					ChartLayout[0] = "bar";
        				break;
        				case 5:
        					ChartLayout[0] = "stack";
        				break;
        				case 6:
        					ChartLayout[0] = "pie";
        				break;
        				case 7:
        					ChartLayout[0] = "dou";
        				break;
        				
                    	} // endSwitch
                    	layout.removeAllViews();
                    	if ((yList.length > 0)&(yList[0]!="")){
                    	  makeChart(ChartActi.this, ChartLayout, xList, yList, bubbleList, Colors, LineWidths, PointStyles,
            					  PointSize, seriesCnt, R.id.chart); 
                    	  if (cdTimer != null) cdTimer.cancel();
                    	  wrSocket("selChart a:" +ChartLayout[0]+"b:" +" noError"); 
                    	}
                    }
                    
                })
       .setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	layout.removeAllViews();    			
//    			MAINLayer (TopLayer)  receives 'mouse' clicks
            	if ((yList.length > 0)&(yList[0]!="")){
    				makeChart(ChartActi.this, ChartLayout, xList, yList, bubbleList, Colors, LineWidths, PointStyles,
    						PointSize, seriesCnt, R.id.chart);
    			}
            }
        })
        .setNegativeButton("Cancle",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	selChart = selOld;
            }
        })
       
       .create().show();

	}	




	private void wrSocket(String msg)
	{
		
		try{
			Socket server = new Socket("localhost", 9999);
			DataOutputStream out = new DataOutputStream(server.getOutputStream());
		    out.writeUTF(msg);
		}
		catch(Exception e) { 
			sokOk = false;                   
			Toast.makeText(this, "Error sending data to socket", Toast.LENGTH_LONG).show(); 
		}
	}
	
	private void setCountDownTimer(String parameters)// countdown, exitcode, noticetime, noticemethod, message
	{
		long		countdown=0,			noticetime=0;
		int			exitcode=0;
		int			noticemethod=0;
		String		message=null;
		String[]	args=parameters.split("\\s*,\\s*");
		for(int i=0; i<args.length; i++)
		{
			switch(i)
			{	
				case 0:	countdown=		Math.round(Double.valueOf(args[i])*1000);	break;
				case 1:	exitcode=		Integer.decode(args[i]);	break;
				case 2:	noticetime=		Math.round(Double.valueOf(args[i])*1000);	break;
				case 3:	noticemethod=	Integer.decode(args[i]);	break;
				case 4:	message=		args[i];					break;
				default: Toast.makeText(this, "countDownTimer:\nExtra parameter: "+args[i], Toast.LENGTH_LONG).show();
			}
		}
		final int		noticeMethod=	noticemethod;
		final long		noticeTime=		noticetime==0?countdown:noticetime;
		final int		exitCode=		exitcode;
		final String	Message=		message!=null?message:"countDownTimer: %s msecond%s";
		
		cdTimer =new CountDownTimer(countdown, noticeTime)
		{
			public void onTick(long millisUntilFinished)
			{
//				long	seconds=millisUntilFinished/1000;
//				String	notice=String.format(Message, seconds+"", (seconds==1?"":"s"));
				long	mseconds=millisUntilFinished;
				String	notice=String.format(Message, mseconds+"", (mseconds==1?"":"s"));
				
				switch(noticeMethod)
				{
					default:
					case 0:	Toast.makeText(getApplicationContext(), notice, Toast.LENGTH_SHORT).show();	return;
					case 1: textTop.setText(notice);	return;
					case 2: textBot.setText(notice);	return;
				}
			}
			public void onFinish()
			{
				wrSocket("noError");
				if (exitCode != 0) {
					putResult(exitCode);
					finish();
				}
				this.cancel();
			}
		}.start();
		
	}
	
	private void putResult(int exitCode)
	{
		Intent	intent=	new Intent();
		intent.putExtra("exit", exitCode);
		setResult(RESULT_OK, intent);	
//		Toast.makeText(this,"putResult", Toast.LENGTH_SHORT).show();
	}
  

	public enum ColorsEnum {
		BLACK, BLUE, CYAN, GRAY, DKGRAY, LTGRAY, GREEN, MAGENTA, RED, TRANSPARENT, WHITE, YELLOW
	}
	
	public enum Charts {LINE, AREA, SCATTER,BUBBLE, BAR, PIE, DOU}
	

}