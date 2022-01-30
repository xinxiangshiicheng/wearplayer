package cn.luern0313.wristvideoplayer.about;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import cn.luern0313.wristvideoplayer.R;

public class InformationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informations);
    }
    public void tuichu(View view){
        finish();
    }
}