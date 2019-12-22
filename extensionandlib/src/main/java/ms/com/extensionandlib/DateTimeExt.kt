package ms.com.extensionandlib

import java.text.SimpleDateFormat
import java.util.*

fun dateToString(date: Date?, templete: String): String {
    val format = SimpleDateFormat(templete)
    date?.let {
        return format.format(it)
    } ?: run { return "" }
}

fun dateToDateServer(date: String?): Date {
    val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    return if (date?.isNullOrEmpty() == false) {
        format.parse(date)
    } else {
        val currentDate = dateToString(Date(), "dd/MM/yyyy")
        format.parse(currentDate)
    }
}
