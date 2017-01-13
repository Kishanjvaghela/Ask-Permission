package com.kishan.askpermission;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

/**
 * Created by CS02 on 10/14/2016.
 */

class AskPermissionImp implements PermissionInterface {

  private PermissionCallback mInterface;
  private ErrorCallback mErrorCallback;
  private String[] requestedPermission;
  private int requestCode;
  private Context mContext;
  private AskPermissionInterface askPermissionInterface;

  public AskPermissionImp(Context mContext, AskPermissionInterface anInterface) {
    this.mContext = mContext;
    askPermissionInterface = anInterface;
  }

  public void setInterface(PermissionCallback mAnInterface) {
    this.mInterface = mAnInterface;
  }

  public void setErrorInterface(ErrorCallback errorCallback) {
    this.mErrorCallback = errorCallback;
  }

  private void setPermission(String[] requestedPermission) {
    this.requestedPermission = requestedPermission;
  }

  private void setRequestCode(int requestCode) {
    this.requestCode = requestCode;
  }

  public void requestAppPermissions(@NonNull final String[] requestedPermissions,
      final int requestCode, boolean showRationalDialog) {
    setRequestCode(requestCode);
    setPermission(requestedPermissions);
    int permissionCheck = PackageManager.PERMISSION_GRANTED;
    boolean shouldShowRequestPermissionRationale = false;
    for (String permission : requestedPermissions) {
      permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(mContext, permission);
      shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale
          || askPermissionInterface.shouldShowPermissionRationale(permission);
    }
    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
      if (mErrorCallback != null) {
        if (shouldShowRequestPermissionRationale) {
          mErrorCallback.onShowRationalDialog(this, requestCode);
        } else {
          if (showRationalDialog) {
            mErrorCallback.onShowRationalDialog(this, requestCode);
          } else {
            askPermissionInterface.requestPermission(requestedPermission, requestCode);
          }
        }
      } else {
        askPermissionInterface.requestPermission(requestedPermission, requestCode);
      }
    } else {
      mInterface.onPermissionsGranted(requestCode);
    }
  }

  public void onRequestPermissionsResult(final int requestCode, String[] permissions,
      int[] grantResults) {
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
              || askPermissionInterface.shouldShowPermissionRationale(permission);
        }
        if (shouldShowRequestPermissionRationale) {
          mInterface.onPermissionsDenied(requestCode);
        } else {
          mErrorCallback.onShowSettings(this, requestCode);
        }
      }
    }
  }

  @Override
  public void onDialogShown() {
    askPermissionInterface.requestPermission(requestedPermission, requestCode);
  }

  @Override
  public void onSettingsShown() {
    // open setting screen
    Intent intent = new Intent();
    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    intent.addCategory(Intent.CATEGORY_DEFAULT);
    intent.setData(Uri.parse("package:" + mContext.getPackageName()));
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
    mContext.startActivity(intent);
  }

  interface AskPermissionInterface {

    void requestPermission(@NonNull String[] permissions, int requestCode);

    boolean shouldShowPermissionRationale(@NonNull String permission);
  }
}
