package com.guizaotech.automorama.custom

import java.text.SimpleDateFormat
import java.util.*

fun Date.formatString(template: String) : String{
    val sdf = SimpleDateFormat(template)
    return  sdf.format(this)
}
fun String.toDate(template: String): Date?{
    val sdf = SimpleDateFormat(template)
    return  sdf.parse(this)
}