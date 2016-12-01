package com.kishan.runtimepermission;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.kishan.askpermission.AskPermission;
import com.kishan.askpermission.ErrorCallback;
import com.kishan.askpermission.PermissionCallback;
import com.kishan.askpermission.PermissionInterface;

public class DemoActivity extends AppCompatActivity implements PermissionCallback, ErrorCallback {
  private static final String TAG = "DemoActivity";
  private static final int REQUEST_PERMISSIONS = 20;
  private Button reqButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_demo);
    reqButton = (Button) findViewById(R.id.requestButton);
    reqButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        reqPermission();
      }
    });
  }

  private void reqPermission() {
    new AskPermission.Builder(this).setPermissions(Manifest.permission.READ_CONTACTS,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .setCallback(this)
        .setErrorCallback(this)
        .request(REQUEST_PERMISSIONS);
  }

  @Override
  public void onPermissionsGranted(int requestCode) {
    Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();
  }

  @Override
  public void onPermissionsDenied(int requestCode) {
    Toast.makeText(this, "Permissions Denied.", Toast.LENGTH_LONG).show();
  }

  @Override
  public void onShowRationalDialog(final PermissionInterface permissionInterface, int requestCode) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("We need permissions for this app.");
    builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        permissionInterface.onDialogShown();
      }
    });
    builder.setNegativeButton(R.string.btn_cancel, null);
    builder.show();
  }

  @Override
  public void onShowSettings(final PermissionInterface permissionInterface, int requestCode) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("We need permissions for this app. Open setting screen?");
    builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        permissionInterface.onSettingsShown();
      }
    });
    builder.setNegativeButton(R.string.btn_cancel, null);
    builder.show();
  }
}
