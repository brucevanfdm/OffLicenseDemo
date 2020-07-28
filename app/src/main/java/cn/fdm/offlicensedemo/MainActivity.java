package cn.fdm.offlicensedemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import cn.fdm.offlicensedemo.utils.LicUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView tvLic = findViewById(R.id.lic);
        final TextView tvResult = findViewById(R.id.result);
        Button btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lic = LicUtil.getLic();
                tvLic.setText(lic);
                if (LicUtil.checkLic(lic)){
                    tvResult.setText("验证成功");
                }else {
                    tvResult.setText("验证失败");
                }
            }
        });
    }
}