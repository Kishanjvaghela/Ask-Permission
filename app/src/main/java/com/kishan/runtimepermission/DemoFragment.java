package com.kishan.runtimepermission;

import android.Manifest;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class DemoFragment extends Fragment implements PermissionCallback {
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
    AskPermission.Builder builder = new AskPermission.Builder(this);
    builder.setRationale(R.string.runtime_permission);
    builder.setPermissions(Manifest.permission.READ_CONTACTS,
        Manifest.permission.WRITE_EXTERNAL_STORAGE);
    builder.setCallback(this);
    builder.request(REQUEST_PERMISSIONS);
  }

  @Override
  public void onPermissionsGranted(int requestCode) {
    Toast.makeText(getActivity(), "Permissions Received.", Toast.LENGTH_LONG).show();
  }
}
