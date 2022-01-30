package cn.luern0313.wristvideoplayer.game;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import cn.luern0313.wristvideoplayer.R;

public class GamelistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamelist);
    }
    public void fanhui(View view){
        finish();
    }
}