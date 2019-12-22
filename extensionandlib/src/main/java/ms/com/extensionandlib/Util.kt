package ms.com.extensionandlib

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import java.security.MessageDigest

object Util {
    fun init(application: Application) {
        context = application
    }

    private lateinit var context: Application
    fun getString(key: Int): String? {
        return context.getString(key)
    }

    fun getAppLicationContext(): Application {
        return context
    }

    fun getSharePre(key: String): SharedPreferences {
        return context.getSharedPreferences(key, Context.MODE_PRIVATE)
    }

//    fun getHash() {
//        try {
//            val info = packageManager.getPackageInfo(
//                    packageName,
//                    PackageManager.GET_SIGNATURES)
//            for (signature in info.signatures) {
//                val md = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
//            }
//        } catch (e: PackageManager.NameNotFoundException) {
//
//        }
//    }

}