package com.example.ethermarket.WalletProfile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ethermarket.R;
import com.example.ethermarket.web3J.Web3jHandler;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

public class WalletProfile extends AppCompatActivity {

    TextView etherTextView, ethTextView;

    BigInteger bigInteger;

    ImageView qrScanImageView;

    Button sendButton, copyWalletAddressButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_profile);


        bigInteger = Web3jHandler.getBalance();


        ethTextView = (TextView) findViewById(R.id.ethBalanceTextView);

        qrScanImageView = (ImageView) findViewById(R.id.imageView);

        sendButton = (Button) findViewById(R.id.sendButton);

        copyWalletAddressButton = (Button) findViewById(R.id.copyWalletAddress);

        final Activity activity = this;
        qrScanImageView.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick override method for setUp on click action
             * @param view
             * */
            @Override
            public void onClick(View view) {
                /** initializing integrator */
                IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                intentIntegrator.setPrompt("Scan QR");
                intentIntegrator.setCameraId(0);
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setBarcodeImageEnabled(false);
                intentIntegrator.initiateScan();;
            }
        });


        sendButton.setOnClickListener(new View.OnClickListener() {
            /**
             * override onClick method
             * This method is calling send method for transaction using business logic
             * */
            @Override
            public void onClick(View v) {
                send();
            }
        });

        this.runOnUiThread(new Runnable() {
            /**
             * run method for setUp
             * balances in ether
             * */
            @Override
            public void run() {
                /** setting balance to edit text*/
                ethTextView.setText(Convert.toWei(bigInteger.toString(), Convert.Unit.WEI).divide(new BigDecimal("1000000000000000000")) + " ETH");
            }
        });

        /**
         * setting copy wallet setOnClickListner
         * @param OnClickListener
         * */
        copyWalletAddressButton.setOnClickListener(new View.OnClickListener() {
            /**
             * method for performing specified logic for copying wallet address
             * @param view
             * */
            @Override
            public void onClick(View view) {
                /** copying wallet address to clipboard */
                ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).setText(Web3jHandler.getWalletAddress());
                Toast.makeText(WalletProfile.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void send() {
        /**
         * startActivity method for for starting new activity
         * @param Intent
         * */
        startActivity(new Intent(WalletProfile.this,SendTransactionActivity.class));
    }

    /**
     * onActivity override method for showing results
     * @param requestCode
     * @param resultCode
     * @param data
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /** initializing IntentResult  for getting results from parameter
         * @param requestCode
         * @param requestCode
         * @param data
         * */
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        /** Condition for is intent result is not null then proceed code */
        if(intentResult!=null){
            /** Condition for if intent contents are null, suppose scanning cancelled  */
            if(intentResult.getContents()==null){
                Toast.makeText(this, "Scanning cancelled", Toast.LENGTH_SHORT).show();
                /** Condition for is intent result is null */
            }else{
                /** Condition for is intentResults contains : symbol then split is into to portions  */
                if(intentResult.getContents().contains(":")){
                    ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).setText(intentResult.getContents().split(":")[1]);
                    /** Condition for is : symbol not consist */
                } else{
                    ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).setText(intentResult.getContents());
                }
                Toast.makeText(WalletProfile.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
            /** if results are empty then calling to default methods from super class */
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }




}


