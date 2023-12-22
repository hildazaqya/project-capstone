package com.example.foodection

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.foodection.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class GalleryActivity : AppCompatActivity() {
    private lateinit var selectBtn: Button
    private lateinit var predBtn: Button
    private lateinit var imageView: ImageView
    private lateinit var resView: TextView
    private lateinit var bitmap: Bitmap
    private val imageSize = 224
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        selectBtn = findViewById(R.id.selectBtn)
        predBtn = findViewById(R.id.predictBtn)
        imageView = findViewById(R.id.imageView)
        resView = findViewById(R.id.resView)

        val labels = application.assets.open("labels.txt").bufferedReader().readLines()

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
            .build()


        selectBtn.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        predBtn.setOnClickListener {
            try {
                var tensorImage = TensorImage(DataType.FLOAT32)
                tensorImage.load(bitmap)

                tensorImage = imageProcessor.process(tensorImage)

                val model = Model.newInstance(applicationContext)

                // Creates inputs for reference.
                val inputFeature0 =
                    TensorBuffer.createFixedSize(
                        intArrayOf(1, imageSize, imageSize, 3),
                        DataType.FLOAT32
                    )

                val intValues = IntArray(bitmap.width * bitmap.height)
                bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

                // Check if the intValues array has enough elements
//                require(intValues.size >= imageSize * imageSize * 3) { "Insufficient pixels in the image" }

                val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
                byteBuffer.order(ByteOrder.nativeOrder())

                var pixel = 0
                // iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
                for (i in 0 until imageSize) {
                    for (j in 0 until imageSize) {
                        val `val` = intValues[pixel++] // RGB
                        byteBuffer.putFloat(((`val` shr 16) and 0xFF).toFloat() * (1f / 255f))
                        byteBuffer.putFloat(((`val` shr 8) and 0xFF).toFloat() * (1f / 255f))
                        byteBuffer.putFloat((`val` and 0xFF).toFloat() * (1f / 255f))
                    }
                }
                inputFeature0.loadBuffer(byteBuffer)

                // Runs model inference and gets result.
                val outputs = model.process(inputFeature0)
                val outputFeature0 = outputs.outputFeature0AsTensorBuffer

                val confidences = outputFeature0.floatArray
                // find the index of the class with the biggest confidence.
                var maxPos = 0
                var maxConfidence = 0f
                for (i in confidences.indices) {
                    if (confidences[i] > maxConfidence) {
                        maxConfidence = confidences[i]
                        maxPos = i
                    }
                }
                resView.text = labels[maxPos]

                // Releases model resources if no longer used.
                model.close()
            } catch (e: IOException) {
                // TODO Handle the exception
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            100 -> {
                if (resultCode == RESULT_OK) {
                    val uri = data?.data
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                        val screenBitmap = bitmap
                        bitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, false)
                        imageView.setImageBitmap(screenBitmap)
                    } catch (e: IOException) {
                        // Handle the exception
                    }
                }
            }
        }
    }
}