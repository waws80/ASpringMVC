package pw.androidthanatos.asmvc.cache

import android.app.Activity
import android.content.Context
import java.util.concurrent.ConcurrentHashMap

/**
 * 全局缓存对象
 */
object AllCache {


    val ACTIVITY_MAIN_ACTION = "android.intent.action.MAIN"

    val ACTIVITY_MAIN_CATEGORY = "android.intent.category.LAUNCHER"

    private val i:Info  = Info().clone() as Info

    fun activities() = i.getAllActivity()

    fun addActivity(key: String,value: Class<Activity>) = i.addActivity(key, value)

    fun controllers() = i.getController()

    fun addController(controllers: MutableList<Class<*>>) = i.addControllers(controllers)

    fun setSecheme(secheme: String) = i.addSecheme(secheme)

    fun getSecheme() = i.getSecheme()

    fun setHost(host: String) = i.addHost(host)

    fun getHost() = i.getHost()

    fun getContext() = i.getContext()

    fun setContext(context: Context) = i.setContext(context)




     private class Info: Cloneable{

         private lateinit var context: Context

        /**
         * 全局保存注册进来的activity
         */
        private val activities: ConcurrentHashMap<String, Class<Activity>> by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ConcurrentHashMap<String,Class<Activity>>()
        }
         /**
          * 全局使用的secheme
          */
        private var secheme: String = ""
         /**
          * 全局使用的host
          */
        private var host: String = ""


        private val controllers: MutableList<Class<*>> by lazy {
            mutableListOf<Class<*>>()
        }

        public override fun clone(): Any {
            val info:Info = Info()
            this.secheme = info.secheme
            this.host = info.host
            return info
        }


         fun setContext(context: Context){
             this.context =context
         }

         fun getContext() = this.context

        fun addSecheme(secheme: String){
            this.secheme = secheme
        }

        fun getSecheme() = this.secheme

        fun addHost(host: String){
            this.host = host
        }

        fun getHost() = this.host

        fun addActivity(key: String , value: Class<Activity>){
            activities.put(key, value)
        }

        fun getAllActivity() = this.activities

        fun addControllers(controllers: MutableList<Class<*>>){
            this.controllers.addAll(controllers)
        }

        fun getController() = this.controllers

    }


}



