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
        val config = ASMConfig("thanatos","androidthanatos.pw",activityPkgs,controllerClasses)
        AsMVC.install(this.applicationContext,config,true)
        AsMVC.setIndex("/a/",MainActivity::class.java)
    }


}