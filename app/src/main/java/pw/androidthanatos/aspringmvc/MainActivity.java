package pw.androidthanatos.aspringmvc;


import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import pw.androidthanatos.asmvc.annotation.Alias;

@Alias("home")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}
