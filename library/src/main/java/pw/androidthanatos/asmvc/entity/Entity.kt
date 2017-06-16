package pw.androidthanatos.asmvc.entity

/**
 * Created by liuxiongfei on 2017/6/15.
 */

/**
 * 框架所需基本配置信息
 */
data class ASMConfig(val secheme: String,val host: String,val activitPkgs: List<String>, val controllers: MutableList<Class<*>>)