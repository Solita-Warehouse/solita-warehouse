package com.example.solitawarehouse

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.example.solita_warehouse.R
import android.widget.Button
import android.widget.Toast
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.net.toUri
import androidx.camera.core.ImageCaptureException


class ReturnFragment : Fragment(R.layout.fragment_return) {

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var previewView: PreviewView
    private lateinit var cameraSelector: CameraSelector
    private lateinit var imageCapture: ImageCapture
    private var isFrontCameraActive = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        previewView = view.findViewById(R.id.preview_view)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        // Initialize CameraSelector to use the back camera by default
        cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        // Find the switch camera button
        val switchCameraButton = view.findViewById<Button>(R.id.switch_camera_button)

        switchCameraButton.setOnClickListener {
            // Toggle between front and back cameras
            isFrontCameraActive = !isFrontCameraActive
            if (isFrontCameraActive) {
                cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                    .build()
            } else {
                cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()
            }

            // Rebind the camera with the new camera selector
            bindCameraUseCases()
        }

        // Find the capture button
        val captureButton = view.findViewById<Button>(R.id.capture_button)

        captureButton.setOnClickListener {
            takePhoto()
        }
    }

    private fun bindCameraUseCases() {
        // Bind the camera use cases with the updated camera selector
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()

                // Build the preview use case
                val preview = Preview.Builder().build()
                preview.setSurfaceProvider(previewView.surfaceProvider)

                // Build the image capture use case
                imageCapture = ImageCapture.Builder()
                    .setTargetRotation(requireActivity().windowManager.defaultDisplay.rotation)
                    .build()

                // Bind both the preview and image capture use cases
                cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                // Handle errors
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }


    private fun startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                bindPreview(cameraProvider)
                bindImageCapture(cameraProvider)
            } catch (e: Exception) {
                // Handle errors
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().build()

        preview.setSurfaceProvider(previewView.surfaceProvider)

        cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview)
    }

    private fun bindImageCapture(cameraProvider: ProcessCameraProvider) {
        imageCapture = ImageCapture.Builder()
            .setTargetRotation(requireActivity().windowManager.defaultDisplay.rotation)
            .build()
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        // Create a timestamped file in the app's external files directory
        val photoFile = File(
            requireActivity().getExternalFilesDir(null),
            SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = outputFileResults.savedUri ?: photoFile.toUri()
                    showToast("Photo saved: $savedUri")
                }

                override fun onError(exception: ImageCaptureException) {
                    // Handle error
                    showToast("Photo capture failed: ${exception.message}")
                }
            }
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}