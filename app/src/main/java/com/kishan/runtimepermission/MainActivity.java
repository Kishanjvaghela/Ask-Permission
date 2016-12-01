package com.kishan.runtimepermission;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void activityDemoClick(View view) {
    Intent intent = new Intent(this, DemoActivity.class);
    startActivity(intent);
  }

  public void fragmentDemoClick(View view) {
    Intent intent = new Intent(this, DemoFragActivity.class);
    startActivity(intent);
  }
}
