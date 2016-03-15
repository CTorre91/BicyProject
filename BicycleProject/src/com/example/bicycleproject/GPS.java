package com.example.bicycleproject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


import com.example.bicycleproject.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class GPS extends Activity {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;
	
	private Button btnUpdate;
	private Button btnCancel;
	private TextView lblLatitude;
	private TextView lblLongitude;
	
	private Tracker gpsdata;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_gps);
		setupActionBar();

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.

        

		
		btnUpdate = (Button) findViewById(R.id.BtnActualizar);
		btnCancel = (Button) findViewById(R.id.BtnDesactivar);
		lblLatitude = (TextView) findViewById(R.id.LblLatitud);
		lblLongitude = (TextView) findViewById(R.id.LblLongitud);
		
		final String id = Secure.getString(getBaseContext().getContentResolver(), Secure.ANDROID_ID);
		
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gpsdata = new Tracker(GPS.this);
				if(gpsdata.canGetLocation()){
                    double latitude = gpsdata.getLatitude();
                    double longitude = gpsdata.getLongitude();
                    double altitude = gpsdata.getAltitude();
                    lblLatitude.setText(Double.toString(latitude));
                    lblLongitude.setText(Double.toString(longitude));
                    java.util.Date fecha =  new java.util.Date();
                    String time = Long.toString(fecha.getTime());
                    postData(Double.toString(latitude),Double.toString(longitude),Double.toString(altitude),id,time);          
				}
				else{
					gpsdata.showSettingsAlert();
                }
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gpsdata.stopUsingGPS();
			}

		});
	}
	
	
	public void postData(String latitud, String longitud, String altitud, String id, String tiempo) {
	   
		try{
			String link="http://bicyproject.esy.es/insertStep.php";
			String data  = URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(latitud, "UTF-8");
			data += "&" + URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(longitud, "UTF-8");
			data += "&" + URLEncoder.encode("altitude", "UTF-8") + "=" + URLEncoder.encode(altitud, "UTF-8");
			data += "&" + URLEncoder.encode("phoneid", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
			data += "&" + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(tiempo, "UTF-8");
         
			URL url = new URL(link);
			URLConnection conn = url.openConnection(); 
         
			conn.setDoOutput(true); 
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 
         
			wr.write( data ); 
			wr.flush(); 
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
         
			StringBuilder sb = new StringBuilder();
			String line = null;
         
         
			while((line = reader.readLine()) != null)
			{
				sb.append(line);
			}
			TextView txtinfo = (TextView) findViewById(R.id.textView1);
         	txtinfo.setText(sb.toString());
         
			}
        catch(Exception e){
            TextView txtinfo = (TextView) findViewById(R.id.textView1);
            txtinfo.setText("ERROR");
         }
	} 

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}
	

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			// TODO: If Settings has multiple levels, Up should navigate up
			// that hierarchy.
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
}
