package ms.com.extensionandlib


import android.content.Context


class MemberHelper<T>() {

    fun getMemberString(sharedKey: String, memberKey: String): String? {
        val sharedPreferences =
            Util.getAppLicationContext().getSharedPreferences(sharedKey, Context.MODE_PRIVATE)

        return sharedPreferences?.get(memberKey, "")

    }

    fun editMember(sharedKey: String, memberKey: String, member: T?) {
        member?.let {
            val sharedPreferences =
                Util.getAppLicationContext().getSharedPreferences(sharedKey, Context.MODE_PRIVATE)
            val memberString = parseObjectToJson(it)
            sharedPreferences?.put(memberKey, memberString)
        }
    }

}