package ms.com.extensionandlib

import android.content.Context
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso


fun AppCompatActivity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    // else {
    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    // }
}

fun loadImage(url: String?, imageView: ImageView, defatultImage: Int?) {
    if (!url.isNullOrEmpty()) {
        defatultImage?.let { Picasso.get()?.load(url)?.placeholder(it)?.into(imageView) } ?: run {
            Picasso.get()?.load(url)?.into(imageView)
        }
    }
}