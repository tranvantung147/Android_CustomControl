package ms.com.extensionandlib



import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Patterns
import androidx.annotation.Nullable
import com.google.gson.reflect.TypeToken
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import java.nio.charset.Charset
import java.security.MessageDigest
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * Created by N on 21/12/17.
 */

val String.toSHA512: String
    get() {
        return MessageDigest
            .getInstance("SHA-512")
            .digest(this.toByteArray(Charset.forName("UTF-16LE"))).joinToString("") { String.format("%02X", it) }
    }



val String.toColorInt: Int?
    get() {
        var colorStr = this
        if (!this.contains('#')) {
            colorStr = '#' + this
        }

        return try {
            Color.parseColor(colorStr)
        } catch (e: Exception) {
            null
        }
    }

val String.checkEmail: Boolean
    get() {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(this).matches()
    }

val String.checkUrl: Boolean
    get() {
        return Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")
            .matcher(this).matches()
    }

fun String.toDate(format: String = "dd/MM/yyyy"): Date {
    return try {
        SimpleDateFormat(format).parse(this)
    } catch (e: ParseException) {
        Date()
    }
}

fun String.splitByNumber(number: Int): ArrayList<String> {
    val lsString = arrayListOf<String>()

    var i = 0
    while (i < length) {
        lsString.add(substring(i, Math.min(length, i + number)))
        i += number
    }

    return lsString
}


fun String.boolValue(): Boolean {
    if (this.toLowerCase() == "true" || this.trim() == "1") {
        return true
    }

    return false
}

val String?.toHTML: Spanned?
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(this)
        }
    }

fun String.generateBitmapQRCode(width: Int, height: Int): Bitmap {
    val bitMatrix = QRCodeWriter().encode(this, BarcodeFormat.QR_CODE, width, height)
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
    (0 until width).forEach { x ->
        (0 until height).forEach { y ->
            bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
        }
    }
    return bitmap
}

fun String.randomIDString(@Nullable size: Int = 36): String{
    val SALTCHARS = "abcdefghijklmnopqrstuvwxyz1234567890"
    val salt = StringBuilder()
    val rnd = Random()
    while (salt.length < size) { // length of the random string.
        val index = (rnd.nextFloat() * SALTCHARS.length)?.toInt()
        if (salt.length == 8 || salt.length == 13 || salt.length == 18 || salt.length == 23)
            salt.append("-")
        else
            salt.append(SALTCHARS[index])
    }
    return salt.toString()
}

fun String.mark(isStarFirst: Boolean = true, showRange: Int): String {
    if (showRange >= this.count()){
        return "*".repeat(showRange)
    }

    var star = ""
    if (isStarFirst){
        val range = this.count() - showRange
        star = "*".repeat(range)
        return star + this.substring(range , this.count())
    }

    star = "*".repeat(4)
    return this.substring(0, showRange) + star
}

fun String.isValidEmail(): Boolean {
    return this.isNotEmpty() &&
            Patterns.EMAIL_ADDRESS.matcher(this).matches()
}