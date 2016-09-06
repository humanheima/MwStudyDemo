package com.humanheima.runtimepermissiondemo;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class SecondActivityFragment extends Fragment {

    @BindView(R.id.btnInFragment)
    Button btnInFragment;
    private final int REQUEST_CODE=7;
    public SecondActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btnInFragment)
    public void onClick() {
        MPermissions.requestPermissions(this, REQUEST_CODE, Manifest.permission.WRITE_CONTACTS);
    }

    @PermissionGrant(REQUEST_CODE)
    public void requestContactSuccess() {
        Toast.makeText(getActivity(), "GRANT WRITE_CONTACTS", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied(REQUEST_CODE)
    public void requestContactFailed() {
        Toast.makeText(getActivity(), "DENY WRITE_CONTACTS", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
