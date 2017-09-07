package net.martinbuchan.homesrus;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.ss.formula.functions.FinanceLib;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    final double FIRST_HOME_DISCOUNT = 1.0;
    final double VET_DISCOUNT = 0.75;
    final double ACTIVE_DUTY_DISCOUNT = 1.25;
    final double RESERVE_DISCOUNT = 0.25;
    final double FINANCIAL = 500;
    final double SERVICE = 200;
    final double OTHER = 500;

    static double discount;
    static double monthlyPayment;
    static double totalPayments;
    static double ttlInterest;
    static double rateUsed;
    static int numPayments;
    static double extraServices;
    double years;

    Button btn;
    Button reset;
    Spinner spnYears;
    Spinner spnRate;
    TextView txtLoanAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_form);
        populateYearSpinner();
        populateRateSpinner();
        nameCheckBox();

        btn = (Button) findViewById(R.id.btnConfirm);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    calculate();
                    startActivity(new Intent(MainActivity.this, ConfirmActivity.class));
                } catch (NumberFormatException e) {

                    String s = ("Enter valid number");
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, s, duration);
                    toast.show();
                }
            }
        });

        reset = (Button) findViewById(R.id.btnClear);
        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reset();
            }
        });
    }
        public void populateYearSpinner() {
            ArrayList<Integer> yearList = new ArrayList<>();

            spnYears = (Spinner) findViewById(R.id.spnYears);

            int year = 10;
            while (year <= 30) {
                yearList.add(year);
                year += 5;
            }
            ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearList);
            spnYears.setAdapter(adapter);
        }


    public void populateRateSpinner() {
        ArrayList<Double> rateList = new ArrayList<>();
        spnRate = (Spinner) findViewById(R.id.spnRate);
        double rate = 4;
        while (rate <= 8) {
            rateList.add(rate);
            rate += 0.25;
        }
        ArrayAdapter<Double> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, rateList);
        spnRate.setAdapter(adapter);
    }

    public void nameCheckBox() {
        Resources res = getResources();
        String[] checkNames = res.getStringArray(R.array.offers);

        ViewGroup container = (ViewGroup) findViewById(R.id.linearLayout2);
        for (int i = 0; i < container.getChildCount(); i++) {
            CheckBox cb = (CheckBox) container.getChildAt(i);
            cb.setText(checkNames[i]);
        }
    }

    public void calculate() {
        discount = 0;
        extraServices = 0;

        txtLoanAmount = (TextView) findViewById(R.id.txtLoanAmount);
        double dblLoanAmount = Double.parseDouble(txtLoanAmount.getText().toString());
        years = (double) ((int) spnYears.getSelectedItem());
        double rate = (double) spnRate.getSelectedItem();

        //Check if first time buyer
        CheckBox cbFirstTimeBuyer = (CheckBox) findViewById(R.id.chbFirstTime);
        if (cbFirstTimeBuyer.isChecked()) {
            discount += FIRST_HOME_DISCOUNT;
        }

        ArmedServicesDiscount();
        extraServices();

        rateUsed = rate - discount;
        numPayments = (int) years * 12;
        monthlyPayment = FinanceLib.pmt((rateUsed / 100) / 12, numPayments, -dblLoanAmount, 0, false) + (extraServices / 12);
        totalPayments = numPayments * monthlyPayment;
        ttlInterest = totalPayments - dblLoanAmount;
    }

    public void ArmedServicesDiscount(){
        //check Armed services discount
        RadioGroup rgASD = (RadioGroup) findViewById(R.id.radioGroup);
        int selectedID = rgASD.getCheckedRadioButtonId();
        RadioButton radiobutton = (RadioButton) findViewById(selectedID);

        if (years >= 20) {
            if (radiobutton.getText().equals("Veteran")) {
                discount += VET_DISCOUNT;
            } else if (radiobutton.getText().equals("Active Service")) {
                discount += ACTIVE_DUTY_DISCOUNT;
            } else if (radiobutton.getText().equals("Reserve")) {
                discount += RESERVE_DISCOUNT;
            } else {
                discount += 0;
            }
        }
    }

    public void extraServices() {
        //Check extra services required
        ViewGroup container = (ViewGroup) findViewById(R.id.linearLayout2);
        for (int i = 0; i < container.getChildCount(); i++) {
            CheckBox cb = (CheckBox) container.getChildAt(i);
            if (cb.isChecked()) {
                if (i == 0 || i == 6) {
                    extraServices += OTHER;
                } else if (i == 1 || i == 2 || i == 4 || i == 9 || i == 10) {
                    extraServices += FINANCIAL;
                } else if (i == 3 || i == 5 || i == 7 || i == 8) {
                    extraServices += SERVICE;
                }
            }
        }
    }

    public void reset() {
        ViewGroup container = (ViewGroup) findViewById(R.id.linearLayout2);
        for (int i = 0; i < container.getChildCount(); i++) {
            CheckBox cb = (CheckBox) container.getChildAt(i);
            cb.setChecked(false);
        }

        RadioButton radiobutton = (RadioButton) findViewById(R.id.radioButton);
        radiobutton.setChecked(true);

        TextView mText = (TextView) findViewById(R.id.txtLoanAmount);
        mText.setText("");

        spnYears.setSelection(0);

        Spinner mSpin2 = (Spinner) findViewById(R.id.spnRate);
        mSpin2.setSelection(0);

        CheckBox chkbx = (CheckBox) findViewById(R.id.chbFirstTime);
        chkbx.setChecked(false);

    }

    @Override
    public void onClick(View v) {

    }
}
