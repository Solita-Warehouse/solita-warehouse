package com.example.solita_warehouse

import android.content.Context
import android.graphics.Bitmap
import org.opencv.android.OpenCVLoader
import org.opencv.core.Mat
import org.opencv.core.MatOfByte
import org.opencv.core.MatOfKeyPoint
import org.opencv.core.MatOfDMatch
import org.opencv.core.Scalar
import org.opencv.features2d.BFMatcher
import org.opencv.features2d.Features2d
import org.opencv.features2d.ORB
import org.opencv.imgproc.Imgproc

class ImageComparator(private val context: Context) {

    init {
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initDebug()
        }
    }

    fun compareImages(image1: Mat, image2: Mat): Double {
        val grayImage1 = Mat()
        val grayImage2 = Mat()

        // Convert the images to grayscale
        Imgproc.cvtColor(image1, grayImage1, Imgproc.COLOR_BGR2GRAY)
        Imgproc.cvtColor(image2, grayImage2, Imgproc.COLOR_BGR2GRAY)

        // Detect ORB keypoints and descriptors
        val orb = ORB.create()
        val keypoints1 = MatOfKeyPoint()
        val descriptors1 = Mat()
        orb.detectAndCompute(grayImage1, Mat(), keypoints1, descriptors1)

        val keypoints2 = MatOfKeyPoint()
        val descriptors2 = Mat()
        orb.detectAndCompute(grayImage2, Mat(), keypoints2, descriptors2)

        // Match the descriptors using BFMatcher
        val bfMatcher = BFMatcher.create(BFMatcher.BRUTEFORCE_HAMMING, true)
        val matches = MatOfDMatch()
        bfMatcher.match(descriptors1, descriptors2, matches)

        // Calculate the percentage similarity
        val totalKeypoints = maxOf(descriptors1.rows(), descriptors2.rows())
        val matchedKeypoints = matches.rows()
        val percentageSimilarity = (matchedKeypoints.toDouble() / totalKeypoints) * 100

        return percentageSimilarity
    }
}