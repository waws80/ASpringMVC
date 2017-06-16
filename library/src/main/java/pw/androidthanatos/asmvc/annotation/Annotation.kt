package pw.androidthanatos.asmvc.annotation

import org.jetbrains.annotations.NotNull

/**
 * ASM框架注解
 */

/**
 * 标注当前activity受框架统一管理
 */
@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Alias( @NotNull val value:String)

/**
 * 控制器注解
 */
@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Controller

/**
 * 访问方式注解
 */
@MustBeDocumented
@Target(AnnotationTarget.CLASS,AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequestMapping(@NotNull val value: String)


/**
 * ui界面上获取到Controller传过去的值
 */
@MustBeDocumented
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Values

/**
 * 控制层执行逻辑后跳转之前对ui界面的回调接口属性注解
 */
@MustBeDocumented
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class CallBack
