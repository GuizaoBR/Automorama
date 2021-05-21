package com.guizaotech.automorama.database

import androidx.room.TypeConverter
import com.guizaotech.automorama.custom.formatString
import com.guizaotech.automorama.custom.toDate
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {
    @TypeConverter
    fun fromString(value: String): Date? {
        return value.toDate("dd/MM/yyyy")
    }

    @TypeConverter
    fun stringToDate(date: Date?): String {
        return date?.formatString("dd/MM/yyyy")!!
    }
}