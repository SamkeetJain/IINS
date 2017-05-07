package com.samkeet.iins;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * Created by leelash on 04-05-2017.
 */

public class CameraFragment extends Fragment {

    private Camera mCamera;
    private CameraPreview mPreview;
    public FrameLayout preview;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_fragment, container, false);
        preview = (FrameLayout) view.findViewById(R.id.preview);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA,}, 1);
            return;
        }
        displayCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    displayCamera();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Permission denied to Access Camera", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void displayCamera() {

        if (mCamera == null) {
            mCamera = getCameraInstance(0);
        }
        mPreview = new CameraPreview(getActivity().getApplicationContext(), mCamera);
        preview.addView(mPreview);

    }

    @Override
    public void onPause() {
        super.onPause();
        releaseCamera();              // release the camera immediately on pause event
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releaseCamera();
    }


    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
        if (mPreview != null) {
            mPreview.surfaceDestroyed(mPreview.getHolder());
            mPreview.getHolder().removeCallback(mPreview);
            mPreview.destroyDrawingCache();
            preview.removeView(mPreview);
            mPreview = null;
        }
        preview.removeAllViews();
    }

    public static Camera getCameraInstance(int id) {
        Camera c = null;
        try {
            c = Camera.open(id); // attempt to get a Camera instance
        } catch (Exception e) {
            Log.d("Camera", "Camera Could not be opened " + e.getMessage());// Camera is not available (in use or does not exist)
        }
        return c;
    }
}

