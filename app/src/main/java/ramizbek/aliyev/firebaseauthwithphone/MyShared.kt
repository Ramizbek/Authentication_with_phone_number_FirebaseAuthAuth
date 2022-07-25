package ramizbek.aliyev.firebaseauthwithphone

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object MyShared {
    var number = ""
    private lateinit var sp: SharedPreferences
    fun init(c: Context) {
        sp = c.getSharedPreferences("name", Context.MODE_PRIVATE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var list: ArrayList<String>
        get() = gsonStringToList(sp.getString("keyList", "[]")!!)
        set(value) = sp.edit {
            it.putString("keyList", listToGsonString(value))
        }

    private fun gsonStringToList(gsonString: String): ArrayList<String> =
        Gson().fromJson(gsonString, object : TypeToken<ArrayList<String>>() {}.type)

    private fun listToGsonString(list: ArrayList<String>): String = Gson().toJson(list)
}
