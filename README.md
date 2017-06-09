# Ask Permission
https://kishanjvaghela.github.io/Ask-Permission/

[ ![Download](https://api.bintray.com/packages/kishanvaghela/maven/askpermission/images/download.svg) ](https://bintray.com/kishanvaghela/maven/askpermission/_latestVersion)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Ask--Permission-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/5857)
[![Stories in Ready](https://badge.waffle.io/Kishanjvaghela/Ask-Permission.png?label=ready&title=Ready)](https://waffle.io/Kishanjvaghela/Ask-Permission)

Simple RunTime permission manager

### How to use
Add url to your gradle file
```Gradle
compile 'com.kishan.askpermission:askpermission:1.0.3'
```
If you got conflicting in support library then 
```Gradle
compile('com.kishan.askpermission:askpermission:1.0.3', {
        exclude group: 'com.android.support'
    })
```

Now you can Ask for permission 
```java
new AskPermission.Builder(this)
        .setPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .setCallback(/* PermissionCallback */)
        .setErrorCallback(/* ErrorCallback */)
        .request(/* Request Code */);
```

Here you have two callback

### PermissionCallback
```java
@Override
  public void onPermissionsGranted(int requestCode) {
    // your code
  }

  @Override
  public void onPermissionsDenied(int requestCode) {
    // your code
  }
 ```
 
### ErrorCallback
[Example](https://github.com/Kishanjvaghela/Ask-Permission/blob/master/app/src/main/java/com/kishan/runtimepermission/DemoActivity.java#L53-L78)
```java
  @Override
  public void onShowRationalDialog(PermissionInterface permissionInterface, int requestCode) {
    // Alert user by Dialog or any other layout that you want.
    // When user press OK you must need to call below method.
    permissionInterface.onDialogShown();
  }

  @Override
  public void onShowSettings(PermissionInterface permissionInterface, int requestCode) {
    // Alert user by Dialog or any other layout that you want.
    // When user press OK you must need to call below method.
    // It will open setting screen.
    permissionInterface.onSettingsShown();
  }
  ```
 
This Library support Activity,Fragment and SupportFragment.
```java
 new AskPermission.Builder(/* android.app.Activity */)
 new AskPermission.Builder(/* android.app.Fragment */)
 new AskPermission.Builder(/* android.support.v4.app.Fragment */)
 ```
 
