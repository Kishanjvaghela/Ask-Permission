package com.kishan.runtimepermission;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.SparseArray;

/**
 * Created by CS02 on 10/14/2016.
 */

public abstract class RuntimePermissionsActivity extends Activity {

  private SparseArray<String> mErrorString;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mErrorString = new SparseArray<>();
  }

  void requestAppPermissions(final String[] requestedPermissions, final String stringId,
      final int requestCode) {

    mErrorString.put(requestCode, stringId);
    int permissionCheck = PackageManager.PERMISSION_GRANTED;
    boolean shouldShowRequestPermissionRationale = false;
    for (String permission : requestedPermissions) {
      permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
      shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale
          || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
    }
    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
      if (shouldShowRequestPermissionRationale) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(stringId);
        builder.setPositiveButton("GRANT", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            ActivityCompat.requestPermissions(RuntimePermissionsActivity.this, requestedPermissions,
                requestCode);
          }
        });
        builder.show();
      } else {
        ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
      }
    } else {
      onPermissionsGranted(requestCode);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    int permissionCheck = PackageManager.PERMISSION_GRANTED;
    for (int permission : grantResults) {
      permissionCheck = permissionCheck + permission;
    }
    if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
      onPermissionsGranted(requestCode);
    } else {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setMessage(mErrorString.get(requestCode));
      builder.setPositiveButton("ENABLE", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          Intent intent = new Intent();
          intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
          intent.addCategory(Intent.CATEGORY_DEFAULT);
          intent.setData(Uri.parse("package:" + getPackageName()));
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
          intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
          startActivity(intent);
        }
      });
      builder.show();
    }
  }

  public abstract void onPermissionsGranted(int requestCode);
}
