package com.example.studentprofile.utils

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.ColorInt

fun View.show() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun EditText.trimmedText(): String = text.toString().trim()

fun Double.toAcademicRanking(): String = when {
    this >= 3.6 -> "Xuất sắc"
    this >= 3.2 -> "Giỏi"
    this >= 2.5 -> "Khá"
    this >= 2.0 -> "Trung bình"
    this >= 1.0 -> "Yếu"
    else -> "Kém"
}

@ColorInt
fun Double.toRankingColor(): Int = Color.parseColor(
    when {
        this >= 3.6 -> "#34B469"
        this >= 3.2 -> "#00BCD4"
        this >= 2.5 -> "#FF9800"
        else -> "#F44336"
    }
)
