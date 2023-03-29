package com.tting.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class CustomView: View {
    constructor(context : Context) : this(context, null)
    constructor(context : Context, attrs : AttributeSet?): this(context, attrs, 0)
    constructor(context : Context, attrs : AttributeSet?, defStyle: Int ): super(context, attrs, defStyle)
    var draw_list : List<PointF>? = null
    var paint : Paint? = null
    var xAxis : Float? = null
    var yAxis : Float? = null

    var transForm = TransForm()


    init {
        paint = Paint()
        xAxis = -100F
        yAxis = -100F
    }

    fun onPoint(v: View?, list : List<PointF>) {
        list.forEach{
            draw_list = list
        }
        invalidate()

    }


    //canvas : 그림판 , paint : 붓
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //canvas 좌표 및 라인 그리기
        paint?.style = Paint.Style.STROKE
        paint?.strokeWidth = 5F
        paint?.color = Color.BLUE
        paint?.isAntiAlias

        //라인 그리기
        val path = Path()
        if(draw_list != null){
            path.moveTo(draw_list!![0].x, draw_list!![0].y)
            draw_list?.forEach {
                xAxis = it.x
                yAxis = it.y
                path.lineTo(it.x,it.y)
                //좌표 점 찍기
                // canvas?.drawCircle(xAxis!!, yAxis!!, 5F, paint!!)
            }
            path.close()
            paint?.let {
                canvas?.drawPath(path, it)
            }
        }

    }


}