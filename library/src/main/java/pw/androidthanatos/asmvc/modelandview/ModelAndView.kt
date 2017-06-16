package pw.androidthanatos.asmvc.modelandview


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import pw.androidthanatos.asmvc.AsMVC
import pw.androidthanatos.asmvc.annotation.Values
import pw.androidthanatos.asmvc.cache.AllCache
import pw.androidthanatos.asmvc.util.asmlog
import pw.androidthanatos.asmvc.util.getLast
import kotlin.collections.HashMap

/**
 * 控制器和activity通信的桥梁
 */
class ModelAndView {

    private var alias: String = ""

    private var options: Bundle? = null

    private val values: HashMap<String,Any> by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        HashMap<String,Any>()
    }

    fun addAttribute(key: String,value: Any): ModelAndView{
        values.put(key, value)
        return this
    }

    fun addView(alias: String): ModelAndView{
        this.alias = alias
        return this
    }

    fun addBundle(options: Bundle?): ModelAndView{
        this.options = options
        return this
    }

    fun go(finish: Boolean = false){
        startActivity(finish)
    }

    private fun startActivity(finish: Boolean){
        if (this.alias.isEmpty()){
            if (AsMVC.actis.size == 0){
                toast("请在ModelAndView中添加  View ",AllCache.getContext())
                Handler().postDelayed({
                    System.exit(0)
                },2000)

            }else{
                toast("请在ModelAndView中添加  View ",AllCache.getContext())
                return
            }

        }
        if (AllCache.activities().isNotEmpty()){
            AllCache.activities().forEach {
                if (it.key == this.alias){
                    startactivity(AllCache.getContext(),it.value,options,finish)
                    return
                }
            }
            toast("请先注册activity",AllCache.getContext())
        }
    }

    private fun toast(msg: String,context: Context){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }

    private fun startactivity(context: Context,clazz: Class<Activity>,options: Bundle?,finish: Boolean){

        var nowActivity: Activity? = null
        //获取当前activity
        if (AsMVC.actis.isNotEmpty()){
            getLast(AsMVC.actis).let {
               nowActivity = it!!.value
            }
        }

        val i = Intent(context,clazz)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (Build.VERSION.SDK_INT >= 16){
            context.startActivity(i,options)
        }else{
            context.startActivity(i)
        }
        //finish启动页面
        if (finish && nowActivity != null){
            nowActivity!!.finish()
            AsMVC.actis.remove(nowActivity!!.componentName.className)
        }

        //将hashmap传递给ui
        clazz.declaredFields.forEach {
            if (it.isAnnotationPresent(Values::class.java)){
                asmlog(it.name)
                it.isAccessible = true
                it.set(clazz.newInstance(),values)
                return
            }
        }

    }






}