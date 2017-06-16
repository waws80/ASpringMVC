package pw.androidthanatos.aspringmvc


import android.app.Activity
import android.app.Application
import android.content.Intent
import pw.androidthanatos.asmvc.AsMVC
import pw.androidthanatos.asmvc.entity.ASMConfig
import pw.androidthanatos.asmvc.util.asmlog
import pw.androidthanatos.aspringmvc.controller.AController
import pw.androidthanatos.aspringmvc.controller.BController
import java.io.File
import java.net.URL

/**
 *
 */


class A: Application(){
    override fun onCreate() {
        super.onCreate()



        val controllerClasses = mutableListOf<Class<*>>()

        controllerClasses.add(AController::class.java)

        controllerClasses.add(BController::class.java)

        val activityPkgs: MutableList<String> = mutableListOf()
        activityPkgs.add(packageName)
        /**
         * @param activityPkgs activity的包名集合
         * @param controllerClasses 控制器的类集合
         */
        val config = ASMConfig("xxx","xxx",activityPkgs,controllerClasses)
        //注册框架
        AsMVC.install(this.applicationContext,config,true)
        //设置程序的入口为 "/a/"接口  并且关闭程序默认主界面
        AsMVC.setIndex("/a/",MainActivity::class.java)
    }


}