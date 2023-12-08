package com.example.project_3

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var btnImage1: Button
    private lateinit var btnImage2: Button
    private lateinit var btnImage3: Button
    private lateinit var imageView: ImageView

    private val fadeDuration = 350 // in milliseconds
    private var currentImageResId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnImage1 = findViewById(R.id.btnImg1)
        btnImage2 = findViewById(R.id.btnImg2)
        btnImage3 = findViewById(R.id.btnImg3)
        imageView = findViewById(R.id.imageView)

        // Set preview image initially
        setPreviewImage()

        btnImage1.setOnClickListener { switchImage(R.drawable.img1) }
        btnImage2.setOnClickListener { switchImage(R.drawable.img2) }
        btnImage3.setOnClickListener { switchImage(R.drawable.img3) }
    }

    private fun switchImage(targetImageResId: Int) {
        // Skip transition if the target image is already displayed
        if (currentImageResId == targetImageResId) {
            return
        }

        val newDrawable = ContextCompat.getDrawable(this, targetImageResId)
        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.duration = fadeDuration.toLong()

        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                // Fade-out animation ended, set the new image and start fade-in
                imageView.setImageDrawable(newDrawable)
                fadeInAnimation()
                currentImageResId = targetImageResId
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // Animation repeated
            }
        })

        imageView.startAnimation(fadeOut)
    }

    private fun fadeInAnimation() {
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = fadeDuration.toLong()

        imageView.startAnimation(fadeIn)
    }

    // Load the preview image and set it to the ImageView
    private fun setPreviewImage() {
        GlobalScope.launch(Dispatchers.Main) {
            val previewImage: Drawable? = loadPreviewImage()
            imageView.setImageDrawable(previewImage)
        }
    }

    // Load and return the preview image
    private suspend fun loadPreviewImage(): Drawable? {
        return ContextCompat.getDrawable(this, R.drawable.preview)
    }
}
