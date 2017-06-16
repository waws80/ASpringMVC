package pw.androidthanatos.asmvc

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import pw.androidthanatos.asmvc.annotation.CallBack
import pw.androidthanatos.asmvc.annotation.RequestMapping
import pw.androidthanatos.asmvc.cache.AllCache
import pw.androidthanatos.asmvc.controller.ASM
import pw.androidthanatos.asmvc.controller.ControllerCallBack
import pw.androidthanatos.asmvc.entity.ASMConfig
import pw.androidthanatos.asmvc.service.AService
import pw.androidthanatos.asmvc.util.asmlog
import pw.androidthanatos.asmvc.util.getLast
import java.lang.ref.WeakReference
import java.lang.reflect.Field
import java.lang.reflect.Method
import kotlin.collections.HashMap

/**
 * ASpringMVC初始化类
 */
object AsMVC: Application.ActivityLifecycleCallbacks {

    val actis: LinkedHashMap<String,Activity> by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        LinkedHashMap<String,Activity>()
    }

    /**
     * 初始化框架所必须的上下文对象
     */
    private var weakContext: WeakReference<Context>? = null

    /**
     * 获取服务对象
     */
    private val asm: ASM = AService()


    internal var debug: Boolean = false


    private var defaultActivity: Class<Activity>? = null

    /**
     * 对外公布的初始化框架方法
     */
    @JvmStatic fun install(app: Context,config: ASMConfig,debug: Boolean = false){
        weakContext = WeakReference<Context>(app)
        this.debug =debug
        AllCache.setSecheme(config.secheme)
        AllCache.setHost(config.host)
        AllCache.addController(config.controllers)
        asm.register(weakContext!!,config.activitPkgs)
        (app as Application).registerActivityLifecycleCallbacks(this)
    }



    /**
     * 对外公布的注销框架的方法
     */
    private fun unInstall(){
        actis.forEach {
            it.value.finish()
            actis.remove(it.key)
        }

        weakContext.let {
            (weakContext!!.get() as Application).unregisterActivityLifecycleCallbacks(this)
            it!!.clear()
        }
        weakContext = null

        System.exit(0)

        asm.unRegister()
    }

    /**
     * 对外公布的启动程序的访问接口
     * 设置此方法即表示不使用app主activity启用直接调用接口
     * @param startAPI 启动控制层的主接口名字，（备注： 主接口只能有一个参数即 上下文对象）
     * @param defaultActivity android系统配置文件中的主activity
     */
    @JvmStatic fun setIndex(startAPI: String,defaultActivity: Class<*>) {
        this.defaultActivity = defaultActivity as Class<Activity>
        asm.setIndex(startAPI)

    }


     override fun onActivityPaused(activity: Activity?) {
    }

    override fun onActivityResumed(activity: Activity?) {
    }

    override fun onActivityStarted(activity: Activity?) {
    }

    override fun onActivityDestroyed(activity: Activity?) {

        if (activity!!::class.java != defaultActivity){
            asmlog("-  ${actis.size}   --")
            if (actis.size ==1){
                activity.finish()
                asmlog("完了")
                actis.remove(activity.componentName.className)
                unInstall()

            }else{
               // activity.finish()
                asmlog("${activity.componentName.className} onDestory")
                //actis.remove(activity.componentName.className)

            }
        }


    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {

    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        if (activity!!::class.java == defaultActivity && defaultActivity != null){
            activity.finish()
        }else{
            asmlog(activity.componentName.className)
            actis.put(activity.componentName.className,activity)
        }


    }


    /**
     * activity请求接口
     */
    @JvmStatic fun router(api: String, any: HashMap<String,Any> = HashMap()){
        val apis: MutableList<String> = mutableListOf()
        val clzs: MutableList<Class<*>> = mutableListOf()
        val methods: MutableList<Method> = mutableListOf()
        AllCache.controllers().forEach {
            var c_base = ""
            val cl = it
            var v = ""
            if (it.isAnnotationPresent(RequestMapping::class.java)){
                v =it.getAnnotation(RequestMapping::class.java).value
                c_base += v
            }
            asmlog("${it.declaredMethods.size}")
            it.declaredMethods.forEach {
                if (it.isAnnotationPresent(RequestMapping::class.java)){
                    val v1 = it.getAnnotation(RequestMapping::class.java).value
                    c_base += v1
                    if (api == c_base){
                        apis.add(c_base)
                        clzs.add(cl)
                        methods.add(it)
                    }else{
                        c_base =  v
                    }
                }
            }
        }

        if (apis.isEmpty()){
            Toast.makeText(weakContext!!.get(),"404，没有找到接口名字。请确认请求的接口存在Controller里！",Toast.LENGTH_SHORT).show()
        }else{
            when(apis.size == 1){
                true ->{
                    putDataToAPI(any,methods[0],clzs[0])
                }
                false ->{
                    Toast.makeText(weakContext!!.get(),"请求的接口不唯一。请确认请求的接口的唯一性！",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * 将数据传给接口
     * 接口的参数最多只能是两个（context hashMap）
     */
    private fun putDataToAPI(any: HashMap<String, Any>, method: Method, clazz: Class<*>) {
        val c = clazz.newInstance()
        val fs = clazz.declaredFields
        val fields: MutableList<Field> = mutableListOf()
        when(fs.isNotEmpty()){
            true ->{
                fs.forEach {
                    if (it.isAnnotationPresent(CallBack::class.java)){
                        fields.add(it)
                    }
                }
            }
        }
        if (fields.isNotEmpty()){
            if (fields.size == 1){
                var nowActivity: Activity? = null
                //获取当前activity
                if (AsMVC.actis.isNotEmpty()){
                    getLast(actis).let {
                        nowActivity = it!!.value
                    }
                }
                if (nowActivity != null && nowActivity!!::class.java.interfaces.isNotEmpty()){
                    nowActivity!!::class.java.interfaces.forEach {
                        if (it == ControllerCallBack::class.java){
                            val fi = fields[0]
                            fi.isAccessible =true
                            fi.set(c,nowActivity as ControllerCallBack)
                        }
                    }
                }
            }
        }
        method.isAccessible = true

        val pas = method.parameterTypes
        if (pas.isEmpty()){
            method.invoke(c)
        }
        if (pas.size ==1){
            if (method.parameterTypes[0].name == "kotlin.collections.HashMap"
                    ||  method.parameterTypes[0].name == "java.util.HashMap"){
                method.invoke(c,any)

            }else if (method.parameterTypes[0].name == "android.content.Context"){
                method.invoke(c, weakContext!!.get())
            }else{
                Toast.makeText(weakContext!!.get(),"接口参数出错，不是hashmap 也不是 context",Toast.LENGTH_SHORT).show()
            }
        }

        if (pas.size ==2){
            if ((pas.contains(HashMap::class.java)|| pas.contains(java.util.HashMap::class.java))
                    && pas.contains(Context::class.java)){

                if (pas[0].name =="android.content.Context" ){
                    method.invoke(c, weakContext!!.get(),any)
                }else{
                    method.invoke(c,any, weakContext!!.get())
                }

            }else{
                Toast.makeText(weakContext!!.get(),"接口的两个参数只能是hashMap 和context",Toast.LENGTH_SHORT).show()
            }
        }
    }

}