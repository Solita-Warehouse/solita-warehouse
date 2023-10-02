package com.example.solitawarehouse

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.JavaCameraView
import org.opencv.android.OpenCVLoader
import org.opencv.core.Mat
import com.example.solita_warehouse.R

class ReturnFragment : Fragment() {

    private lateinit var cameraView: JavaCameraView
    private val CAMERA_PERMISSION_CODE = 101

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_return, container, false)

        // Initialize OpenCV
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization failure
        }

        // Find the camera view
        cameraView = rootView.findViewById(R.id.cameraView)
        cameraView.visibility = SurfaceView.VISIBLE
        cameraView.setCvCameraViewListener(cameraViewListener)

        // Check and request camera permission
        if (checkCameraPermission()) {
            cameraView.setCameraPermissionGranted();
            cameraView.enableView()

        } else {
            requestCameraPermission()
        }

        return rootView
    }

    override fun onResume() {
        super.onResume()
        if (checkCameraPermission()) {
            cameraView.enableView()
        }
    }

    override fun onPause() {
        super.onPause()
        cameraView.disableView()
    }

    private val cameraViewListener = object : CameraBridgeViewBase.CvCameraViewListener2 {
        override fun onCameraViewStarted(width: Int, height: Int) {}

        override fun onCameraViewStopped() {}

        override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {
            // Process the camera frame here
            val frame = inputFrame?.rgba()
            // Implement your OpenCV image processing logic here
            return frame!!
        }
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, proceed with camera initialization
                cameraView.enableView()
            } else {
                // Camera permission denied, handle this case (e.g., show a message)
            }
        }
    }
}