package com.example.ethermarket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ethermarket.WalletProfile.WalletProfile;
import com.example.ethermarket.web3J.Web3jHandler;

import org.web3j.crypto.CipherException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    /** Variable button for unlock wallet */
    Button unlockButton;
    Button createButton;
    /** Variable password edit text for password input */
    EditText passwordEditText;
    /** Variable progress dialog for loading */
    ProgressDialog progressDialog;

    public void checkWalletExist() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String fileName = pref.getString("fileName", null);
        if (fileName == null) {
            startActivity(new Intent(MainActivity.this, CreateWallet.class));
        }
        else return;
    }

    /**
     * onCreate method
     * @param savedInstanceState
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(this);
        checkWalletExist();
        setContentView(R.layout.activity_main);

        /** setting strict mode thread policy */
        final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        try {
            /** Web3j Connection */
            Web3jHandler.web3Connection();
        }catch (IOException e){
            Toast.makeText(this, "Connection Error", Toast.LENGTH_SHORT).show();
            finish();
        }

        /** initializing unlockButton */
        unlockButton = (Button) findViewById(R.id.unlockWallet);

        createButton = (Button) findViewById(R.id.createButton);
        /** initializing password edit text for user input */
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        /** method on click action performed for unlock wallet
         * @param OnClickListner
         * */
        unlockButton.setOnClickListener(new View.OnClickListener() {
            /**
             * @override onClick
             * @param view
             * @return void
             * */
            @Override
            public void onClick(View view) {
                unlock();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            /**
             * @override onClick
             * @param view
             * @return void
             * */
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateWallet.class));
            }
        });
    }

    /**
     * This method is used to unlock wallet using web3j
     * @exception CipherException
     * @exception IOException
     * */
    void unlock() {
        try {
            /**
             * This method is loading credential file of your wallet from phone external storage
             * @param password
             * @throws CipherException
             * @throws IOException
             * */
            SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
            Web3jHandler.loadCredentials(passwordEditText.getText().toString(), pref.getString("fileName", null));
            /**
             * starting wallet profile activity
             * @param Intent
             * */
            startActivity(new Intent(MainActivity.this, WalletProfile.class));
        } catch (CipherException e) {
            Toast.makeText(MainActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
        }
    }
}
