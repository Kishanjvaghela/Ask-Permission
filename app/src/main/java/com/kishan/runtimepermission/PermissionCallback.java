package com.kishan.runtimepermission;

/**
 * Created by CS02 on 10/14/2016.
 */

public interface PermissionCallback {
  void log();

  void onPermissionsGranted(int requestCode);
}
