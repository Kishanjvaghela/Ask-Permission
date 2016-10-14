package com.kishan.runtimepermission;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements PermissionCallback {
  private static final int REQUEST_PERMISSIONS = 20;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Button button = (Button) findViewById(R.id.requestButton);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        askForPermission();
      }
    });
  }

  private void askForPermission() {
    AskPermission.Builder builder = new AskPermission.Builder(this);
    builder.setRationale(R.string.runtime_permission);
    builder.setPermissions(Manifest.permission.READ_CONTACTS,
        Manifest.permission.WRITE_EXTERNAL_STORAGE);
    builder.setCallback(this);
    builder.request(REQUEST_PERMISSIONS);
  }

  @Override
  public void log() {

  }

  @Override
  public void onPermissionsGranted(int requestCode) {
    Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();
  }
}
