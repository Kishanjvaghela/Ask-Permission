package com.kishan.askpermission;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.app.ActivityCompat;
import android.support.v4.app.Fragment;

/**
 * Created by CS02 on 12/1/2016.
 */

public class ShadowSupportFragment extends Fragment
    implements AskPermissionImp.AskPermissionInterface {

  private PermissionCallback mInterface;
  private ErrorCallback mErrorCallback;
  private String[] requestedPermission;
  private int requestCode;
  private boolean showRationalDialog;
  private AskPermissionImp mImp;

  static ShadowSupportFragment getInstance(String[] requestedPermission, int requestCode,
      boolean showRationalDialog, PermissionCallback mAnInterface, ErrorCallback errorCallback) {
    ShadowSupportFragment shadowFragment = new ShadowSupportFragment();
    shadowFragment.mInterface = mAnInterface;
    shadowFragment.mErrorCallback = errorCallback;
    shadowFragment.requestedPermission = requestedPermission;
    shadowFragment.requestCode = requestCode;
    shadowFragment.showRationalDialog = showRationalDialog;
    return shadowFragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mImp = new AskPermissionImp(getActivity(), this);
    mImp.setErrorInterface(mErrorCallback);
    mImp.setInterface(mInterface);
    if (requestedPermission != null && requestedPermission.length > 0) {
      mImp.requestAppPermissions(requestedPermission, requestCode, showRationalDialog);
    }
  }

  @Override
  public void requestPermission(@NonNull String[] permissions, int requestCode) {
    requestPermissions(permissions, requestCode);
  }

  @Override
  public boolean shouldShowPermissionRationale(@NonNull String permission) {
    return ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    mImp.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }
}
