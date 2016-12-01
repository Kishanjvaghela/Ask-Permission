package com.kishan.runtimepermission;

import android.Manifest;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.kishan.askpermission.AskPermission;
import com.kishan.askpermission.ErrorCallback;
import com.kishan.askpermission.PermissionCallback;
import com.kishan.askpermission.PermissionInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class DemoFragment extends Fragment implements PermissionCallback, ErrorCallback {
  private static final String TAG = "DemoFragment";
  private static final int REQUEST_PERMISSIONS = 20;
  private Button reqButton;

  public DemoFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_demo, container, false);
    reqButton = (Button) view.findViewById(R.id.requestButton);
    reqButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        reqPermission();
      }
    });
    return view;
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
    Toast.makeText(getActivity(), "Permissions Received.", Toast.LENGTH_LONG).show();
  }

  @Override
  public void onPermissionsDenied(int requestCode) {
    Toast.makeText(getActivity(), "Permissions Denied.", Toast.LENGTH_LONG).show();
  }

  @Override
  public void onShowRationalDialog(final PermissionInterface permissionInterface, int requestCode) {
    Log.d(TAG, "onShowRationalDialog: ");
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
    Log.d(TAG, "onShowSettings: ");
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
