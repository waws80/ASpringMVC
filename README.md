#<center>AsMVC框架

###由来：
前段时间在公司写微信公众号，用的是SpringMVC写的后台，以前从来没接触过这玩意，第一次接触感觉还不错挺容易上手的不就是接口获取到数据往前台发么，至于你前台H5页面想干嘛我管不着，爱干嘛干嘛去，从此就掉进了坑，没事写着玩玩，突然有天感觉安卓端的是不是也可以向类似SpringMVC框架这样子呢？虽然说目前用的MVP框架也挺不错的，但是还是想尝试下自己写一个类似于大SpringMVC那样的框架<br>
###使用的技术<br>
反射、注解、Kotlin
<br>
###流程<br>
![](https://github.com/waws80/ASpringMVC/blob/master/AsMVC%E6%A1%86%E6%9E%B6.png?raw=true)
<br>
###代码流程

* application中注册<br>

######代码示例

       /**
         * @param activityPkgs activity的包名集合
         * @param controllerClasses 控制器的类集合
         */	
        val config = ASMConfig("xxx","xxx",activityPkgs,controllerClasses)
        //注册框架
        AsMVC.install(this.applicationContext,config,true)
        //设置程序的入口为 "/a/"接口  并且关闭程序默认主界面
        AsMVC.setIndex("/a/",MainActivity::class.java)
* controller示例<br>

######代码示例

    @Controller
    @RequestMapping("/a")
    class AController {
	 //当前显示的activity的callback实例对象
    @CallBack
    private lateinit var callBack: ControllerCallBack



    /**
     * @param activityPkgs activity的包名集合
     * @param controllerClasses 控制器的类集合
     */	
	@RequestMapping("/thirdb")
    fun b(map: HashMap<String,Any>,ctx: Context){
        Toast.makeText(ctx,"hello\n${map["list"]}",Toast.LENGTH_SHORT).show()
       when((map["list"] as List<String>)[0] == "1"){
            true ->{
                ModelAndView().addAttribute("user",map["user"] as AB).addView("thirdActivity").go(false)
            }
            false ->{
                callBack.callBack(map)
            }
        }
    }
######代码介绍：<br>
  注解是当前接口名字<br>
  map：从UI界面传过来的所有的值，ctx：上下文对象（applicationContext）<br>
  接下来是一个判断<br>
  true：跳转到 别名是：“thirdActivity”的activity<br>
  go()：参数 true： 关闭跳转到接口的activity，false:不关闭，默认是 false<br>
  false：给跳转当前接口activity的回调。
  
* Activity<br>
 
######代码示例：<br>
	@Alias("second")
	class SecondActivity : 	AppCompatActivity(),ControllerCallBack {

    companion object{
        @Values
        val value: HashMap<String,Any>? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val tv = findViewById(R.id.second) as TextView
        tv.text = value!!["hehe"] as String
    }

    override fun callBack(any: HashMap<String, Any>) {
        Toast.makeText(this,"${any.size}",Toast.LENGTH_SHORT).show()
    }
	}
######代码介绍<br>
类注解：别名<br>
@Values注解：控制器启动当前页面后返回的值<br>
当前activity想要拿到控制器的回调信息，必须实现ControllerCallBack接口。<br>
要获取控制器回调的信息从callBack（any：HashMap<String,Any>）中获取

----------------------------------------<br>
以上就是这个框架的功能的介绍，有什么地方不足之处请各位大婶多指点。<br>喜欢的话 [点个start](https://github.com/waws80/ASpringMVC) ，有话要说？[请在下方留言](#)


  
 
         