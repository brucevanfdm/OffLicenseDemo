package cn.fdm.offlicensedemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cn.fdm.offlicensedemo.utils.LicUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText appId = findViewById(R.id.et_app_id);
        final EditText notAfter = findViewById(R.id.et_not_after);
        final EditText customer = findViewById(R.id.et_info);
        final TextView tvLic = findViewById(R.id.lic);
        Button btn = findViewById(R.id.btn);
        Button btnVerify = findViewById(R.id.btn_ver);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LicUtil.licenseBean licenseBean = new LicUtil.licenseBean();
                licenseBean.setAppId(appId.getText().toString());
                licenseBean.setIssuedTime(System.currentTimeMillis());
                licenseBean.setNotAfter(Long.parseLong(notAfter.getText().toString()));
                licenseBean.setNotBefore(System.currentTimeMillis());
                licenseBean.setCustomerInfo(customer.getText().toString());
                tvLic.setText(LicUtil.getLic(licenseBean));
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = LicUtil.checkLic(tvLic.getText().toString());
                Toast.makeText(MainActivity.this, "验证结果:" + isChecked, Toast.LENGTH_LONG).show();
            }
        });
    }
}

