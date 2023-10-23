package com.example.solitawarehouse

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
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
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.core.net.toUri
import androidx.camera.core.ImageCaptureException
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Scalar
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc


class ReturnFragment : Fragment(R.layout.fragment_return) {

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var previewView: PreviewView
    private lateinit var cameraSelector: CameraSelector
    private lateinit var imageCapture: ImageCapture
    private var isFrontCameraActive = false
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startCameraWithPermissionCheck()
        } else {
            // Handle the situation if the permissions are not granted
            // You can show a message or take appropriate action here
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        previewView = view.findViewById(R.id.preview_view)

        startCameraWithPermissionCheck()


        // Find the capture button
        val captureButton = view.findViewById<Button>(R.id.capture_button)

        captureButton.setOnClickListener {
            takePhoto()
        }
    }
    private fun startCameraWithPermissionCheck() {
        if (allPermissionsGranted()) {
            startCamera()
            // Initialize CameraSelector to use the back camera by default
            cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            bindCameraUseCases()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
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