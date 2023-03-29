package com.tting.myapplication

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.annotation.AnyRes


class TransForm {
    fun getUriToDrawable(
        context: Context,
        @AnyRes drawableId: Int
    ): Uri {
        return Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + context.resources.getResourcePackageName(drawableId)
                    + '/' + context.resources.getResourceTypeName(drawableId)
                    + '/' + context.resources.getResourceEntryName(drawableId)
        )
    }


    fun resizeImage(view: View, bitmap: Bitmap): Bitmap {
        Log.d("test","width : ${view.width}, // height : ${view.height}" )
        return Bitmap.createScaledBitmap(bitmap, view.width, view.height, true)
    }



}