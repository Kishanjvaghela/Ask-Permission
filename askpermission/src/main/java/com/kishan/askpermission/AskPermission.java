package com.kishan.askpermission;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import java.security.InvalidParameterException;

/**
 * Created by CS02 on 10/14/2016.
 */

public class AskPermission implements PermissionCallback, ErrorCallback {
  private static final String TAG = "AskPermission";

  private PermissionCallback mCallback;
  private ErrorCallback mErrorCallback;
  private FragmentManager manager;
  private android.support.v4.app.FragmentManager supportManager;

  private AskPermission(FragmentManager fragmentManager, PermissionCallback callback,
      ErrorCallback errorCallback) {
    this.manager = fragmentManager;
    this.mCallback = callback;
    this.mErrorCallback = errorCallback;
  }

  private AskPermission(android.support.v4.app.FragmentManager fragmentManager,
      PermissionCallback callback, ErrorCallback errorCallback) {
    this.supportManager = fragmentManager;
    this.mCallback = callback;
    this.mErrorCallback = errorCallback;
  }

  public AskPermission(Activity activity, PermissionCallback callback,
      ErrorCallback errorCallback) {
    this(activity.getFragmentManager(), callback, errorCallback);
  }

  public AskPermission(Fragment fragment, PermissionCallback callback,
      ErrorCallback errorCallback) {
    this(fragment.getFragmentManager(), callback, errorCallback);
  }

  public AskPermission(android.support.v4.app.Fragment fragment, PermissionCallback callback,
      ErrorCallback errorCallback) {
    this(fragment.getFragmentManager(), callback, errorCallback);
  }

  private void requestAppPermissions(Builder builder, int requestCode) {
    if (builder.permissions == null) {
      throw new InvalidParameterException("Permissions must be set");
    }
    if (builder.callback == null) {
      throw new InvalidParameterException("Callback must be set");
    }
    if (manager != null) {
      ShadowFragment shadowFragment = builder.buildFragment(requestCode);
      AskPermissionUtils.addFragment(manager, shadowFragment);
    } else if (supportManager != null) {
      ShadowSupportFragment shadowFragment = builder.buildSupportFragment(requestCode);
      AskPermissionUtils.addFragment(supportManager, shadowFragment);
    }
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
    if (manager != null) {
      AskPermissionUtils.removeFragment(manager);
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
    private android.support.v4.app.Fragment supportFragment;
    private boolean showRationalDialog;

    public Builder(Activity activity) {
      this.activity = activity;
    }

    public Builder(Fragment fragment) {
      this.fragment = fragment;
    }

    public Builder(android.support.v4.app.Fragment fragment) {
      this.supportFragment = fragment;
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

    private ShadowFragment buildFragment(int reqCode) {
      if (permissions != null) {
        return ShadowFragment.getInstance(permissions, reqCode, showRationalDialog, callback,
            errorCallback);
      } else {
        return null;
      }
    }

    private ShadowSupportFragment buildSupportFragment(int reqCode) {
      if (permissions != null) {
        return ShadowSupportFragment.getInstance(permissions, reqCode, showRationalDialog, callback,
            errorCallback);
      } else {
        return null;
      }
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
      } else if (supportFragment != null) {
        permission = new AskPermission(supportFragment, callback, errorCallback);
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
