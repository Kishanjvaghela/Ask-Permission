package com.kishan.askpermission;

/**
 * Created by CS02 on 10/14/2016.
 */

public interface PermissionCallback {

  void onPermissionsGranted(int requestCode);

  void onPermissionsDenied(int requestCode);
}
