package pw.androidthanatos.aspringmvc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import pw.androidthanatos.asmvc.AsMVC
import pw.androidthanatos.asmvc.annotation.Alias

@Alias("thirdActivity")
class ThirdActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

    }

    fun th(view: View){
        AsMVC.router("/a/")
    }
}
