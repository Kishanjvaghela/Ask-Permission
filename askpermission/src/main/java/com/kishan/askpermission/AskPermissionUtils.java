package com.kishan.askpermission;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

/**
 * Created by CS02 on 12/1/2016.
 */

class AskPermissionUtils {
  private static final String PERMISSION_TAG = "permission";

  public static void removeFragment(FragmentManager fragmentManager) {
    Fragment fragment = fragmentManager.findFragmentByTag(PERMISSION_TAG);
    if (fragment != null) {
      FragmentTransaction transaction = fragmentManager.beginTransaction();
      transaction.remove(fragment);
      transaction.commit();
    }
  }

  public static void removeFragment(android.support.v4.app.FragmentManager fragmentManager) {
    android.support.v4.app.Fragment fragment = fragmentManager.findFragmentByTag(PERMISSION_TAG);
    if (fragment != null) {
      android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
      transaction.remove(fragment);
      transaction.commit();
    }
  }

  public static void addFragment(FragmentManager fragmentManager, Fragment fragment) {
    if (fragmentManager == null) {
      return;
    }
    if (fragment == null) {
      return;
    }
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.add(fragment, PERMISSION_TAG);
    fragmentTransaction.commit();
  }

  public static void addFragment(android.support.v4.app.FragmentManager fragmentManager,
      android.support.v4.app.Fragment fragment) {
    if (fragmentManager == null) {
      return;
    }
    if (fragment == null) {
      return;
    }
    android.support.v4.app.FragmentTransaction fragmentTransaction =
        fragmentManager.beginTransaction();
    fragmentTransaction.add(fragment, PERMISSION_TAG);
    fragmentTransaction.commit();
  }
}
