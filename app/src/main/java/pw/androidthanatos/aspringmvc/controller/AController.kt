package pw.androidthanatos.aspringmvc.controller

import android.content.Context
import android.widget.Toast
import pw.androidthanatos.asmvc.annotation.CallBack
import pw.androidthanatos.asmvc.annotation.Controller
import pw.androidthanatos.asmvc.annotation.RequestMapping
import pw.androidthanatos.asmvc.controller.ControllerCallBack
import pw.androidthanatos.asmvc.modelandview.ModelAndView
import pw.androidthanatos.asmvc.util.asmlog

/**
 * 控制器
 */
@Controller
@RequestMapping("/a")
class AController {

    @CallBack
    private lateinit var callBack: ControllerCallBack

    @RequestMapping("/")
    fun a(context: Context){
        asmlog("hahahahaha")
//        ModelAndView().addAttribute("hehe","我来自${this.javaClass.name}")
//                .addAttribute("list", mutableListOf("1","2"))
//                .addAttribute("user",AB("zhangsan","1234"))
//                .addView("second").go(true)
    }
    data class AB(val name: String, val pas: String)

    @RequestMapping("/thirdb")
    fun b(map: HashMap<String,Any>,ctx: Context){
        Toast.makeText(ctx,"hello\n${map["list"]}",Toast.LENGTH_SHORT).show()

        callBack.callBack(map)

       // ModelAndView().addAttribute("user",map["user"] as AB).addView("thirdActivity").go()



    }
}

