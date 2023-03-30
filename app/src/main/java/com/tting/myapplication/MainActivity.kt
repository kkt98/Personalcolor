package com.tting.myapplication

import android.annotation.SuppressLint
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.tting.myapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val TAG = ActivityMainBinding::class.java.simpleName

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        binding.btn.setBackgroundColor(Color.parseColor("#55ff0000"))
        binding.btn2.setBackgroundColor(Color.parseColor("#55FFA200"))
        binding.btn3.setBackgroundColor(Color.parseColor("#222B2B2B"))

        var bitmap = BitmapFactory.decodeResource(resources, R.drawable.picture2)

        val transForm = TransForm()

        Log.d(TAG,"이미지 확인 : 61")

        Log.d(TAG,"이미지 확인 :")

        //view 사이즈에 맞게 bitmap 크기 변경
        binding.customView.post {
            bitmap = transForm.resizeImage(binding.customView, bitmap)
        }

        // FaceDetector 선언 및 옵션 설정
        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .build()
        val detector = FaceDetection.getClient(highAccuracyOpts)

        binding.btn.setOnClickListener {
            val resultImg =
                Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            val maskImg = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)

            //input image 생성
            val inputImage = InputImage.fromBitmap(bitmap, 0)
            val result = detector.process(inputImage)
                .addOnSuccessListener { faces ->
                    Log.d("test", "face 인식 : 성공")
                    for (face in faces) {

                        //얼굴 윤곽 좌표 가져오기
                        val getContour = face.getContour(FaceContour.FACE)?.points

                        if (getContour != null) {
                            //binding.customView.onPoint(binding.customView,getContour)
                            var paint = Paint(Paint.ANTI_ALIAS_FLAG)
                            paint.isAntiAlias = true
                            paint.style = Paint.Style.FILL
                            var path = Path()

                            path.moveTo(getContour[0].x, getContour[0].y)
                            getContour.forEach {
                                path.lineTo(it.x, it.y)
                            }
                            path.close()

                            //캔버스 겹치기
                            val mCanvas = Canvas(resultImg)

                            //얼굴윤곽 캔버스
                            val maskCanvas = Canvas(maskImg)
                            maskCanvas.drawPath(path, paint)

                            //겹치는 부분 설정
                            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)

                            mCanvas.drawBitmap(bitmap, 0f, 0f, null)
                            mCanvas.drawBitmap(maskImg, 0f, 0f, paint)

                            //view에 bitmap 넣기(겹치는 이미지)
                            binding.customView.setImageDrawable(
                                BitmapDrawable(
                                    resources,
                                    resultImg
                                )
                            )
                            //배경 넣기
                            binding.customView.background = BitmapDrawable(resources, bitmap)

                        }

                    }

                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    Log.d("test", "face 인식 : 에러")
                    e.printStackTrace()
                }
        }

        //필터 씌우기
        binding.btn2.setOnClickListener {
            binding.customView.setColorFilter(Color.parseColor("#55FFA200"))
        }

        binding.btn3.setOnClickListener {
            binding.customView.setColorFilter(Color.parseColor("#222B2B2B"))
        }

    }

}
