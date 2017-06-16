package pw.androidthanatos.asmvc.util

import android.app.ActivityManager
import android.content.Context
import android.util.Log
import pw.androidthanatos.asmvc.AsMVC
import java.util.Map
import kotlin.jvm.internal.markers.KMutableMap


/**
 * 判断两个对象是否为空
 */
fun <T,E> checkNoNull(t:T,e: E): Boolean = (t != null && e != null)


/**
 * 打印日志
 */
fun asmlog(msg: String){
    if (AsMVC.debug){
        Log.d("ASM",msg)
    }

}

fun <K,V> getLast(map: LinkedHashMap<K,V>): MutableMap.MutableEntry<K,V>?{
    val iterator = map.entries.iterator()
    var a: MutableMap.MutableEntry<K,V>? = null
    while (iterator.hasNext()){
        a = iterator.next()
    }
    return a!!
}