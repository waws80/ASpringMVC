package pw.androidthanatos.asmvc.controller

import android.app.Activity
import android.content.Context
import java.lang.ref.WeakReference

/**
 * 服务控制类
 */
interface ASM {



    fun register(weakReference: WeakReference<Context>,packages: List<String>)

    fun unRegister()

    fun setIndex(api: String)
}