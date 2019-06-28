package com.mike;


//import static android.provider.Settings.System.SCREEN_OFF_TIMEOUT;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.net.Uri;
//import android.provider.Settings;
//import android.widget.Toast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.view.Window;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.*;
import android.view.View.OnKeyListener;
import android.view.KeyEvent;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;


public class TestScreen extends Activity {
  /** Called when the activity is first created. */
  //private Context mContext;
  //Intent intent = new Intent("MIKE_MIKE_MIKE_MIKE");
  //private Button mButton;
  private boolean mFullScreen = false;
  //DisplayMetrics dm;
  private static String TAG = "Test";
  private EditText BattNumEdit = null;
  private TextView textTips = null;
  
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(Intent.ACTION_SCREEN_ON)) {
                Log.d(TAG, "ACTION_SCREEN_ON");
            } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                Log.d(TAG, "ACTION_SCREEN_OFF");
            }
        }
    };



  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      //requestWindowFeature(Window.FEATURE_NO_TITLE);
      setContentView(R.layout.main);

        textTips = (TextView) findViewById(R.id.text_tips);
        textTips.setTextSize(18);
        // Capture text edit key press
        BattNumEdit = (EditText) findViewById(R.id.edit_num);
        BattNumEdit.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Toast.makeText(TestScreen.this, BattNumEdit.getText(),Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
                }
            }
        );

/*      
        // register for various Intents
        IntentFilter filter = new IntentFilter();
        filter.setPriority(500);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mIntentReceiver, filter);
*/

      //switchGPS(this);
      //Settings.System.putInt(getContentResolver(), SCREEN_OFF_TIMEOUT, -1);
      //mContext = this;

/*     
     mButton = (Button) findViewById(R.id.button00);
     mButton.setOnClickListener(
     new OnClickListener() {
         @Override
         public void onClick(View v) {
            // TODO Auto-generated method stub
            if (mFullScreen) {
                showFullScreen(false); // show status bar and navigation bar
            } else {
                showFullScreen(true); // hide status bar and navigation bar
            }
         }
     });
     
      dm = new DisplayMetrics();  
      getWindowManager().getDefaultDisplay().getMetrics(dm);  
        
      float density  = dm.density;      // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）  
      int densityDPI = dm.densityDpi;     // 屏幕密度（每寸像素：120/160/240/320）  
      float xdpi = dm.xdpi;           
      float ydpi = dm.ydpi;  
        
      Log.e(TAG + "  DisplayMetrics", "xdpi=" + xdpi + "; ydpi=" + ydpi);  
      Log.e(TAG + "  DisplayMetrics", "density=" + density + "; densityDPI=" + densityDPI);  
        
      int screenWidthDip = dm.widthPixels;        // 屏幕宽（dip，如：320dip）  
      int screenHeightDip = dm.heightPixels;      // 屏幕宽（dip，如：533dip）  
        
      Log.e(TAG + "  DisplayMetrics(222)", "screenWidthDip=" + screenWidthDip + "; screenHeightDip=" + screenHeightDip);  
        
      int screenWidth  = (int)(dm.widthPixels * density + 0.5f);      // 屏幕宽（px，如：480px）  
      int screenHeight = (int)(dm.heightPixels * density + 0.5f);     // 屏幕高（px，如：800px）  
        
      Log.e(TAG+"  DisplayMetrics(222)",  " screenWidth=" + screenWidth + "; screenHeight=" + screenHeight); 
*/
  }
  

//  public static final void switchGPS(Context context) {
//      Intent GPSIntent = new Intent();
//      GPSIntent.setClassName("com.android.settings",
//              "com.android.settings.widget.SettingsAppWidgetProvider");
//      GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
//      GPSIntent.setData(Uri.parse("custom:3"));
//      try {
//          PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
//      } catch (PendingIntent.CanceledException e) {
//          e.printStackTrace();
//      }
//  }

//  public void onRotate(View v) {
//          String msg = intent.getStringExtra("msg");
//          Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
//  }

/*
  private void showFullScreen(final boolean fullScreen) {
     final Intent fullScreenIntent = new Intent();
     if (fullScreenIntent != null) {
         fullScreenIntent.setAction("ACTION_HIDE_STATUSBAR");
         fullScreenIntent.putExtra("ui", 0);
         fullScreenIntent.putExtra("state", fullScreen);
         this.sendBroadcast(fullScreenIntent);
         mFullScreen = fullScreen;
     }
  }
*/

    public int SetBattThreshold(String str) {
        int result = 0;
        try {
            FileWriter fw = new FileWriter("/sys/class/power_supply/battery/stop_charging_soc");
            fw.write(str);
            fw.flush();
            fw.close();
        }catch(IOException e){
            e.printStackTrace();
            Log.d(TAG,"Failed to set battery threshold, IOException error");
            result = -1;
        }
        return result;
    }

    public int getBattThreshold() {
        int result = 0;
        FileReader reader = null;
        try {
            reader = new FileReader("/sys/class/power_supply/battery/stop_charging_soc");
            char[] buf = new char[15];
            int n =reader.read(buf);
            if(n > 1) {
                result = Integer.parseInt(new String(buf, 0, n-1));
            }
        } catch (IOException e){
            e.printStackTrace();
            Log.d(TAG,"Failed to get battery threshold, IOException error");
            result = -1;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                }
            }
        }
        return result;
    }

    public void onSet(View view) {
            String item = BattNumEdit.getText().toString().trim();
            Log.d(TAG,"############################# num: "+item);

            if(item != null) {
                SetBattThreshold(item);
            }
    }

    public void onSet30(View view) {
            String item = "30";
            SetBattThreshold(item);
    }

    public void onSet60(View view) {
            String item = "60";
            SetBattThreshold(item);
    }

    public void onSet85(View view) {
            String item = "85";
            SetBattThreshold(item);
    }

    public void onGet(View view) {
            int num = getBattThreshold();
            textTips.setText("Current threshold: "+num);
    }
}
