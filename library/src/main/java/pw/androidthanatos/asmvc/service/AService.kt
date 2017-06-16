package pw.androidthanatos.asmvc.service

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.widget.Toast
import pw.androidthanatos.asmvc.annotation.Alias
import pw.androidthanatos.asmvc.annotation.Controller
import pw.androidthanatos.asmvc.annotation.RequestMapping
import pw.androidthanatos.asmvc.cache.AllCache
import pw.androidthanatos.asmvc.controller.ASM
import pw.androidthanatos.asmvc.exception.*
import pw.androidthanatos.asmvc.util.asmlog
import pw.androidthanatos.asmvc.util.checkNoNull
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap

/**
 * 框架核心服务类
 */
class AService: ASM{

    /**
     * 上下文对象
     */
    private lateinit var weakReference: WeakReference<Context>

    /**
     * 注册所有activity的包集合
     */
    private lateinit var packages: List<String>

    /**
     * 开始注册activity
     */
    override fun register(weakReference: WeakReference<Context>,packages: List<String>) {
        this.weakReference = weakReference
        this.packages = packages
        when(checkNoNull(weakReference,packages)){
            true ->{
                putAllActivity()
            }
            false -> throw ASMVCContextException("请初始化框架 context为空，或 包集合为空！")
        }
    }


    /**
     * 设置程序的主入口，并且关闭默认接口
     */
    override fun setIndex(api: String) {
        val controllers = AllCache.controllers()
        when(controllers.isNotEmpty()){
            true ->{
                startAPI(api,controllers)

            }

            false ->{
                throw  ASMVCControllerNullException("在控制器指定的包里面没有找到控制器")
            }
        }
    }

    /**
     * 停止系统默认启动的activity
     */
    private fun stopDefault(defaultActivity: Class<*>) {
        (defaultActivity.newInstance() as Activity).finish()
    }

    /**
     * 启动接口
     */
    private fun startAPI(api: String, controllers: MutableList<Class<*>>) {
        controllers.forEach {
            if (it.isAnnotationPresent(Controller::class.java)){
                var c_baseapi = ""
                val clz = it
                if (it.isAnnotationPresent(RequestMapping::class.java)){
                    c_baseapi +=it.getAnnotation(RequestMapping::class.java).value
                }
                it.declaredMethods.forEach {
                    if (it.isAnnotationPresent(RequestMapping::class.java)){
                        if (api == c_baseapi+it.getAnnotation(RequestMapping::class.java).value){
                            it.isAccessible = true
                            if (it.parameterTypes.isNotEmpty() && it.parameterTypes.size == 1 ){
                                asmlog(it.parameterTypes[0].name)
                                if (it.parameterTypes[0].name == "android.content.Context"){
                                    it.invoke(clz.newInstance(),weakReference.get())
                                    return

                                }else{
                                    throw ASMVCindexAPIparameterTypeErrorException("启动程序入口API接口的参数类型只能为Context")
                                }
                            }else{
                                throw ASMVCindexAPIparameterTypeErrorException("启动程序入口API接口必须有一个参数并且只能为Context")
                            }

                        }
                    }
                }
            }
        }
    }


    /**
     * 将所有标注过的activity注册进入框架，由框架统一管理
     */
    private fun putAllActivity(){
       val pm =  weakReference.get()!!.applicationContext.packageManager
        packages.forEach {
            pm.getPackageInfo(it,PackageManager.GET_ACTIVITIES).activities.forEach {
                addMap(it)
            }
        }
        AllCache.setContext(weakReference.get()!!)

        if (AllCache.activities().isEmpty()){
            Toast.makeText(weakReference.get(),"添加的包里没有找到activity\n请先注册activity",Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 添加activity执行方法
     */
    private fun addMap(ai: ActivityInfo?) {
        ai.let {
            val clz = Class.forName(it!!.name)
            when(clz.isAnnotationPresent(Alias::class.java)){
                true ->{
                    val v = clz.getAnnotation(Alias::class.java)
                    if (AllCache.activities().containsKey(v.value)){
                        throw ASMVCAddActivityException("当前activity:$clz 的别名为：${v.value}已经被其他activity注册过了，请排查！")
                    }
                    AllCache.addActivity(v.value,clz as Class<Activity>)
                    asmlog("${it.name} 被注册到了 ASM框架中")
                }
                false ->{}
            }
        }
    }

    /**
     * 注销调ASpringMVC
     */
    override fun unRegister() {
        if (checkNoNull(weakReference,packages)){
            weakReference.clear()
        }
    }
}