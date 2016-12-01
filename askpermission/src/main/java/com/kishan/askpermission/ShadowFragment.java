package com.kishan.askpermission;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v13.app.FragmentCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

/**
 * Created by CS02 on 10/14/2016.
 */

public class ShadowFragment extends Fragment implements PermissionInterface {

  private PermissionCallback mInterface;
  private ErrorCallback mErrorCallback;
  private String[] requestedPermission;
  private int requestCode;
  private boolean showRationalDialog;

  public void setInterface(PermissionCallback mAnInterface) {
    this.mInterface = mAnInterface;
  }

  public void setErrorInterface(ErrorCallback errorCallback) {
    this.mErrorCallback = errorCallback;
  }

  public void setPermission(String[] requestedPermission) {
    this.requestedPermission = requestedPermission;
  }

  public void setShowRationalDialog(boolean showRationalDialog) {
    this.showRationalDialog = showRationalDialog;
  }

  public void setRequestCode(int requestCode) {
    this.requestCode = requestCode;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestAppPermissions(requestedPermission, requestCode);
  }

  private void requestAppPermissions(final String[] requestedPermissions, final int requestCode) {

    int permissionCheck = PackageManager.PERMISSION_GRANTED;
    boolean shouldShowRequestPermissionRationale = false;
    for (String permission : requestedPermissions) {
      permissionCheck =
          permissionCheck + ContextCompat.checkSelfPermission(getActivity(), permission);
      shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale
          || FragmentCompat.shouldShowRequestPermissionRationale(getCurrentContext(), permission);
    }
    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
      if (showRationalDialog && mErrorCallback != null && shouldShowRequestPermissionRationale) {
        mErrorCallback.onShowRationalDialog(this, requestCode);
      } else {
        FragmentCompat.requestPermissions(getCurrentContext(), requestedPermissions, requestCode);
      }
    } else {
      mInterface.onPermissionsGranted(requestCode);
    }
  }

  private void showRationalDialog(final String[] requestedPermissions, final String stringId,
      final int requestCode) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setMessage(stringId);
    builder.setPositiveButton("GRANT", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        FragmentCompat.requestPermissions(getCurrentContext(), requestedPermissions, requestCode);
      }
    });
    builder.show();
  }

  @Override
  public void onRequestPermissionsResult(final int requestCode, String[] permissions,
      int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    int permissionCheck = PackageManager.PERMISSION_GRANTED;
    for (int permission : grantResults) {
      permissionCheck = permissionCheck + permission;
    }
    if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
      mInterface.onPermissionsGranted(requestCode);
    } else {
      if (mErrorCallback == null) {
        mInterface.onPermissionsDenied(requestCode);
      } else {
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : permissions) {
          shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale
              || FragmentCompat.shouldShowRequestPermissionRationale(getCurrentContext(),
              permission);
        }
        if (shouldShowRequestPermissionRationale) {
          mInterface.onPermissionsDenied(requestCode);
        } else {
          mErrorCallback.onShowSettings(this, requestCode);
        }
      }
    }
  }

  private String getPackageName() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      return getContext().getPackageName();
    } else {
      return getActivity().getPackageName();
    }
  }

  private Fragment getCurrentContext() {
    return this;
  }

  @Override
  public void onDialogShown() {
    FragmentCompat.requestPermissions(getCurrentContext(), requestedPermission, requestCode);
  }

  @Override
  public void onSettingsShown() {
    Intent intent = new Intent();
    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    intent.addCategory(Intent.CATEGORY_DEFAULT);
    intent.setData(Uri.parse("package:" + getPackageName()));
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
    startActivity(intent);
  }
}
