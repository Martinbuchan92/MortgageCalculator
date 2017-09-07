package net.martinbuchan.homesrus;

import android.icu.text.NumberFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class ConfirmActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        fillText();

        Button btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void fillText() {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());

        TextView monthlyPayment = (TextView) findViewById(R.id.txtMonthlyPayment);
        TextView totalPayment = (TextView) findViewById(R.id.txtTotalPayment);
        TextView totalInterest = (TextView) findViewById(R.id.txtTotalInterest);
        TextView rateUsed = (TextView) findViewById(R.id.txtRateUsed);

        monthlyPayment.setText(format.format(MainActivity.monthlyPayment));
        totalPayment.setText(format.format(MainActivity.totalPayments));
        totalInterest.setText(format.format(MainActivity.ttlInterest));
        String testRate = MainActivity.rateUsed + "%";
        rateUsed.setText(testRate);
    }
}
