package com.kishan.runtimepermission;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DemoFragActivity extends AppCompatActivity {

  private static final String ARG_IS_SUPPORT = "is_support";

  public static final Intent getInstance(Context context, boolean isSupport) {
    Intent intent = new Intent(context, DemoFragActivity.class);
    intent.putExtra(ARG_IS_SUPPORT, isSupport);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_demo_frag);
    boolean isSupport = false;
    Intent intent = getIntent();
    if (intent != null) {
      isSupport = intent.getBooleanExtra(ARG_IS_SUPPORT, false);
    }
    if (isSupport) {
      addSupportFragment();
    } else {
      addFragment();
    }
  }

  private void addFragment() {
    FragmentTransaction transaction = getFragmentManager().beginTransaction();
    transaction.add(R.id.fragmentContentLayout, new DemoFragment());
    transaction.commit();
  }

  private void addSupportFragment() {
    android.support.v4.app.FragmentTransaction transaction =
        getSupportFragmentManager().beginTransaction();
    transaction.add(R.id.fragmentContentLayout, new DemoSupportFragment());
    transaction.commit();
  }
}
