package ms.com.extensionandlib

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.text.Html
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun View.setBorder(thickness: Int = 1, color: Int = Color.BLACK) {
    val gradientDrawable = GradientDrawable()
    gradientDrawable.setStroke(thickness, color)
    gradientDrawable.setColor(
        (background as? ColorDrawable)?.color
            ?: Color.TRANSPARENT
    )
    background = gradientDrawable
    setPadding(thickness, thickness, thickness, thickness)
}

fun View.setCornerRadius(radius: Float) {
    val gradientDrawable = GradientDrawable()
    gradientDrawable.cornerRadius = radius
    gradientDrawable.setColor(
        (background as? ColorDrawable)?.color
            ?: Color.TRANSPARENT
    )
    background = gradientDrawable
}

fun View.setBorderWithRoudedCorner(radius: Float, thickness: Int = 1, color: Int = Color.BLACK) {
    val gradientDrawable = GradientDrawable()
    gradientDrawable.cornerRadius = radius
    gradientDrawable.setStroke(thickness, color)
    gradientDrawable.setColor(
        (background as? ColorDrawable)?.color
            ?: Color.TRANSPARENT
    )
    background = gradientDrawable
}

fun View.setBorderWithRoudedCorner(
    radius: Float,
    thickness: Int = 1,
    color: Int = Color.BLACK,
    bgColor: Int
) {
    val gradientDrawable = GradientDrawable()
    gradientDrawable.cornerRadius = radius
    gradientDrawable.setStroke(thickness, color)
    gradientDrawable.setColor(bgColor)
    background = gradientDrawable
}

val View.visiable: Unit
    get() {
        this.visibility = View.VISIBLE
    }

val View.isVisiable: Boolean
    get() {
        return this.visibility == View.VISIBLE
    }

val View.inVisiable: Unit
    get() {
        this.visibility = View.INVISIBLE
    }

val View.isInVisiable: Boolean
    get() {
        return this.visibility == View.INVISIBLE
    }

val View.gone: Unit
    get() {
        this.visibility = View.GONE
    }

val View.isGone: Boolean
    get() {
        return this.visibility == View.GONE
    }

var View.isHidden: Boolean
    get() = this.visibility != View.VISIBLE
    set(value) {
        if (value) this.visibility = View.GONE
        else this.visibility = View.VISIBLE
    }

fun View.replaceFragment(fragment: Fragment) {
    (context as? AppCompatActivity)?.let {
        it.supportFragmentManager.beginTransaction().replace(this.id, fragment).commit()
    }
}

fun <T : View> T.post(view: (T) -> Unit) {
    this.post { view.invoke(this) }
}


fun View.showKeyBoard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE)
            as? InputMethodManager)?.showSoftInput(this, 0)
}

inline fun TextView.toUnderLineText() {
//    val spanStr = SpannableString(this.text)
//    spanStr.setSpan(UnderlineSpan(), 0, spanStr.length, 0)
//    this.text = spanStr
    this.text = Html.fromHtml("<u><i>${this.text}</i></u>")
}

fun View.replaceBy(fragment: Fragment) {
    (context as? AppCompatActivity)?.let {
        it.supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .replace(this.id, fragment).commitAllowingStateLoss()
    }
}

fun View.addBy(fragment: Fragment?) {
    fragment?.let {
        (context as? AppCompatActivity)?.let {
            if (!fragment.isAdded) {
                it.supportFragmentManager.beginTransaction().setCustomAnimations(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
                ).add(this.id, fragment).commitAllowingStateLoss()
            } else {
                it.supportFragmentManager.beginTransaction().setCustomAnimations(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
                ).show(fragment)
            }
        }
    }
}

fun View.showBy(fragment: Fragment?) {
    fragment?.let {
        (context as? AppCompatActivity)?.let {
            if (fragment in it.supportFragmentManager.fragments) {
                it.supportFragmentManager.beginTransaction().show(fragment)
                    .commitAllowingStateLoss()
            }
        }
    }
}