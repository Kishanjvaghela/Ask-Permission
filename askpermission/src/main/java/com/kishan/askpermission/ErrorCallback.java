package com.kishan.askpermission;

/**
 * Created by CS02 on 10/14/2016.
 */

public interface ErrorCallback {

  void onShowRationalDialog(PermissionInterface permissionInterface, int requestCode);

  void onShowSettings(PermissionInterface permissionInterface, int requestCode);
}
