package com.kishan.runtimepermission;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v13.app.FragmentCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.SparseArray;

/**
 * Created by CS02 on 10/14/2016.
 */

public class ShadowFragment extends Fragment {

  private PermissionCallback mInterface;
  private SparseArray<String> mErrorString;
  private String[] requestedPermission;
  private String rationaleString;
  private int requestCode;

  public void setInterface(PermissionCallback mAnInterface) {
    this.mInterface = mAnInterface;
  }

  public void setRequestedPermission(String[] requestedPermission) {
    this.requestedPermission = requestedPermission;
  }

  public void setRationale(String stringId) {
    this.rationaleString = stringId;
  }

  public void setRequestCode(int requestCode) {
    this.requestCode = requestCode;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mInterface.log();
    mErrorString = new SparseArray<>();
    requestAppPermissions(requestedPermission, rationaleString, requestCode);
  }

  private void requestAppPermissions(final String[] requestedPermissions, final String stringId,
      final int requestCode) {

    mErrorString.put(requestCode, stringId);
    int permissionCheck = PackageManager.PERMISSION_GRANTED;
    boolean shouldShowRequestPermissionRationale = false;
    for (String permission : requestedPermissions) {
      permissionCheck =
          permissionCheck + ContextCompat.checkSelfPermission(getActivity(), permission);
      shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale
          || FragmentCompat.shouldShowRequestPermissionRationale(getCurrentContext(), permission);
    }
    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
      if (shouldShowRequestPermissionRationale) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(stringId);
        builder.setPositiveButton("GRANT", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            FragmentCompat.requestPermissions(getCurrentContext(), requestedPermissions,
                requestCode);
          }
        });
        builder.show();
      } else {
        FragmentCompat.requestPermissions(getCurrentContext(), requestedPermissions, requestCode);
      }
    } else {
      mInterface.onPermissionsGranted(requestCode);
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
      mInterface.onPermissionsGranted(requestCode);
    } else {
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      builder.setMessage(mErrorString.get(requestCode));
      builder.setPositiveButton("ENABLE", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          Intent intent = new Intent();
          intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
          intent.addCategory(Intent.CATEGORY_DEFAULT);
          intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
          intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
          startActivity(intent);
        }
      });
      builder.show();
    }
  }

  private Fragment getCurrentContext() {
    return this;
  }
}
