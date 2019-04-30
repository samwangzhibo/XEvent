package com.wzb;

import android.app.Activity;
import android.os.Bundle;

import com.wzb.wrapper.XEventWrapper;
import com.wzb.xevent.XPEvent;
import com.wzb.xevent.constant.EventConstant;

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
    XEventWrapper.sendEvent(new XPEvent(EventConstant.EVENT_ONRESUME));
  }

  @Override
  protected void onPause() {
    super.onPause();
    XEventWrapper.sendEvent(new XPEvent(EventConstant.EVENT_ONPAUSE));
  }
}
