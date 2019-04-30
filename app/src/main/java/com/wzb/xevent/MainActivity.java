package com.wzb.xevent;

import android.app.Activity;
import android.os.Bundle;

import com.wzb.R;
import com.wzb.xevent.test.constant.EventConstant;
import com.wzb.xevent.wrapper.XEventWrapper;

/**
 * simple sample
 */
public class MainActivity extends Activity {
  private static final String TAG = "MainActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override
  protected void onResume() {
    super.onResume();
    XEventWrapper.sendEvent(new com.wzb.xevent.XPEvent(EventConstant.EVENT_ONRESUME));
  }

  @Override
  protected void onPause() {
    super.onPause();
    XEventWrapper.sendEvent(new com.wzb.xevent.XPEvent(EventConstant.EVENT_ONPAUSE));
  }
}
