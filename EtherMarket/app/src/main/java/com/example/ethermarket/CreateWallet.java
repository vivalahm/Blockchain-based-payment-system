package com.example.ethermarket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class CreateWallet extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_WRITE_STORAGE = 0;

    /** Variable button for unlock wallet */
    Button createWalletButton;
    /** Variable password edit text for password input */
    EditText setPasswordEditText;
    /** Variable progress dialog for loading */
    ProgressDialog progressDialog;

    // 설정 비밀번호
    String walletPassword;
    // 파일 기기에 이름 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);

        int permissionCheck = ContextCompat.checkSelfPermission(CreateWallet.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    CreateWallet.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_WRITE_STORAGE);
        }

        /** initializing unlockButton */
        createWalletButton = (Button) findViewById(R.id.createWalletButton);
        /** initializing password edit text for user input */
        setPasswordEditText = (EditText) findViewById(R.id.setPasswordEditText);

        /** method on click action performed for unlock wallet
         * @param OnClickListner
         * */
        createWalletButton.setOnClickListener(new View.OnClickListener() {
            /**
             * @param view
             * @return void
             * @override onClick
             */
            @Override
            public void onClick(View view) {
                // 패스워드 저장
                walletPassword = setPasswordEditText.getText().toString();
                createWallet(walletPassword);
            }
        });
    }



    public void createWallet(final String password) {

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        String fileName;

        try {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!path.exists()) {
                path.mkdir();
            }
            fileName = WalletUtils.generateLightNewWalletFile(password, new File(String.valueOf(path)));
            Log.e("TAG", "generateWallet: " + path + "/" + fileName);
            Credentials credentials =
                    WalletUtils.loadCredentials(
                            password,
                            path + "/" + fileName);
            Log.e("TAG", "generateWallet: " + credentials.getAddress() + " " + credentials.getEcKeyPair().getPublicKey());
            editor.putString("fileName", fileName);
            editor.commit();
            startActivity(new Intent(CreateWallet.this, MainActivity.class));
            finish();
        } catch (NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | IOException
                | CipherException e) {
            e.printStackTrace();
        }
    }
}
