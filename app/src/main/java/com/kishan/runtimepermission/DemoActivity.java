package com.kishan.runtimepermission;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.kishan.askpermission.AskPermission;
import com.kishan.askpermission.PermissionCallback;

public class DemoActivity extends AppCompatActivity implements PermissionCallback {
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
    AskPermission.Builder builder =
        new AskPermission.Builder(this).setRationale(R.string.runtime_permission)
            .setCallback(this)
            .setPermissions(Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    builder.request(REQUEST_PERMISSIONS);
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
  public void onShowRationalDialog(int requestCode) {
    Log.d(TAG, "onShowRationalDialog: ");
  }
}
