package pw.androidthanatos.aspringmvc.controller

import pw.androidthanatos.asmvc.annotation.Controller
import pw.androidthanatos.asmvc.annotation.RequestMapping

/**
 * Created by liuxiongfei on 2017/6/15.
 */

@Controller
@RequestMapping("/b")
class BController {

    @RequestMapping("/b")
    fun a(){}
}