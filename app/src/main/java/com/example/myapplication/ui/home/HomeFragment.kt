package com.example.myapplication.ui.home

import android.graphics.BitmapFactory
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var cameraPreview: CameraPreview

    private val apiClient = ModelAPIClient()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel.loadModel(
            onSuccess = {
                // Model loaded successfully
                Log.d("HomeFragment", "Model loaded successfully")
            },
            onFailure = { message ->
                // Error loading the model
                Log.e("HomeFragment", "Error loading model: $message")
            }
        )

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Add camera preview to the layout
        cameraPreview = CameraPreview(requireContext(), null)
        binding.cameraPreviewContainer.addView(cameraPreview)

        // Set up scan button
        binding.scanButton.setOnClickListener {
            Log.d("HOME.CAMERA", "Opened")
            // Hide the scan button
            binding.scanButton.visibility = View.GONE

            // Show the camera preview and capture button
            binding.cameraPreviewContainer.visibility = View.VISIBLE
            binding.captureButton.visibility = View.VISIBLE
            binding.closeCameraButton.visibility = View.VISIBLE

            // UnRelease the camera preview
            cameraPreview.unReleaseCamera()
        }

        val pictureCallback = Camera.PictureCallback { data, camera ->
            // Decode the image data
            val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            // Read the content from the bitmap
             Log.d("HOME.CAMERA", bitmap.toString())
            // Restart the camera preview
            camera.stopPreview()
        }


        // Set up capture button
        binding.captureButton.setOnClickListener {
            Log.d("HOME.CAMERA", "Captured")
            // Release the camera preview
            cameraPreview.setCameraToCapture(pictureCallback)
        }

        // Set up close button
        binding.closeCameraButton.setOnClickListener {
            Log.d("HOME.CAMERA", "Closed")
            // Hide the camera preview and capture button
            binding.cameraPreviewContainer.visibility = View.GONE
            binding.captureButton.visibility = View.GONE
            binding.closeCameraButton.visibility = View.GONE

            // Show the scan button
            binding.scanButton.visibility = View.VISIBLE

            // Release the camera preview
            cameraPreview.releaseCamera()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}