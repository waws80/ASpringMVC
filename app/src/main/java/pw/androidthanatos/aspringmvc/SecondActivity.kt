package pw.androidthanatos.aspringmvc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import pw.androidthanatos.asmvc.AsMVC
import pw.androidthanatos.asmvc.annotation.Alias
import pw.androidthanatos.asmvc.annotation.Values
import pw.androidthanatos.asmvc.controller.ControllerCallBack
import pw.androidthanatos.asmvc.util.asmlog
import pw.androidthanatos.aspringmvc.controller.AController

@Alias("second")
class SecondActivity : AppCompatActivity(),ControllerCallBack {

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
