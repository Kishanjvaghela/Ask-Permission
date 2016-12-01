package com.kishan.askpermission;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import java.security.InvalidParameterException;

/**
 * Created by CS02 on 10/14/2016.
 */

public class AskPermission implements PermissionCallback, ErrorCallback {
  private static final String TAG = "AskPermission";
  private static final String PERMISSION_TAG = "permission";
  private PermissionCallback mCallback;
  private ErrorCallback mErrorCallback;
  private Activity activity;
  private Fragment fragment;

  public AskPermission(Activity activity, PermissionCallback callback,
      ErrorCallback errorCallback) {
    this.activity = activity;
    this.mCallback = callback;
    this.mErrorCallback = errorCallback;
  }

  public AskPermission(Fragment fragment, PermissionCallback callback,
      ErrorCallback errorCallback) {
    this.fragment = fragment;
    this.mCallback = callback;
    this.mErrorCallback = errorCallback;
  }

  private void requestAppPermissions(Builder builder, int requestCode) {
    if (activity != null) {
      requestAppPermissions(builder, activity.getFragmentManager(), requestCode);
    } else if (fragment != null) {
      requestAppPermissions(builder, fragment.getFragmentManager(), requestCode);
    }
  }

  private void requestAppPermissions(Builder builder, FragmentManager fragManager,
      int requestCode) {
    if (builder.permissions == null) {
      throw new InvalidParameterException("Permissions must be set");
    }
    if (builder.callback == null) {
      throw new InvalidParameterException("Callback must be set");
    }
    ShadowFragment fragment = new ShadowFragment();
    fragment.setInterface(this);
    if (mErrorCallback == null) {
      fragment.setErrorInterface(null);
    } else {
      fragment.setErrorInterface(this);
    }
    fragment.setShowRationalDialog(builder.showRationalDialog);
    fragment.setPermission(builder.permissions);
    fragment.setRequestCode(requestCode);
    FragmentTransaction fragmentTransaction = fragManager.beginTransaction();
    fragmentTransaction.add(fragment, PERMISSION_TAG);
    fragmentTransaction.commit();
  }

  @Override
  public void onPermissionsGranted(int requestCode) {
    mCallback.onPermissionsGranted(requestCode);
    removeFragment();
  }

  @Override
  public void onPermissionsDenied(int requestCode) {
    mCallback.onPermissionsDenied(requestCode);
    removeFragment();
  }

  @Override
  public void onShowRationalDialog(PermissionInterface permissionInterface, int requestCode) {
    if (mErrorCallback != null) {
      mErrorCallback.onShowRationalDialog(permissionInterface, requestCode);
    }
  }

  @Override
  public void onShowSettings(PermissionInterface permissionInterface, int requestCode) {
    if (mErrorCallback != null) mErrorCallback.onShowSettings(permissionInterface, requestCode);
  }

  private void removeFragment() {
    if (activity != null) {
      removeFragment(activity.getFragmentManager());
    } else if (fragment != null) {
      removeFragment(fragment.getFragmentManager());
    }
  }

  private void removeFragment(FragmentManager fragmentManager) {
    Fragment fragment = fragmentManager.findFragmentByTag(PERMISSION_TAG);
    if (fragment != null) {
      FragmentTransaction transaction = fragmentManager.beginTransaction();
      transaction.remove(fragment);
      transaction.commit();
    }
  }

  /**
   * Builder class
   */
  public static class Builder {
    private String[] permissions;
    private PermissionCallback callback;
    private ErrorCallback errorCallback;
    private Activity activity;
    private Fragment fragment;
    private boolean showRationalDialog;

    public Builder(Activity activity) {
      this.activity = activity;
    }

    public Builder(Fragment fragment) {
      this.fragment = fragment;
    }

    /**
     * Set the permission to request
     *
     * @param permissions permissions
     * @return {@link Builder}
     */
    public Builder setPermissions(@NonNull String... permissions) {
      this.permissions = permissions;
      return this;
    }

    /**
     * Set the callback to be call once the permissions are granted
     *
     * @param callback {@link PermissionCallback} callback
     * @return {@link Builder}
     */
    public Builder setCallback(@NonNull PermissionCallback callback) {
      this.callback = callback;
      return this;
    }

    public Builder setErrorCallback(@NonNull ErrorCallback callback) {
      this.errorCallback = callback;
      return this;
    }

    public Builder setShowRationalDialog(boolean showRationalDialog) {
      this.showRationalDialog = showRationalDialog;
      return this;
    }

    /**
     * Request the permissions set using the Builder
     *
     * @param requestCode positive <code>int</code> value to identify the permission request
     */
    public void request(@IntRange(from = 1, to = Integer.MAX_VALUE) final int requestCode) {
      AskPermission permission;
      if (activity != null) {
        permission = new AskPermission(activity, callback, errorCallback);
        permission.requestAppPermissions(this, requestCode);
      } else if (fragment != null) {
        permission = new AskPermission(fragment, callback, errorCallback);
        permission.requestAppPermissions(this, requestCode);
      }
    }

    private Context getContext() {
      if (activity != null) {
        return activity.getApplicationContext();
      }
      if (fragment != null) {
        return fragment.getActivity().getApplicationContext();
      }
      return null;
    }
  }
}
