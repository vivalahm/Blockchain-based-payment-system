package com.example.ethermarket.web3J;

import android.os.Environment;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionTimeoutException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Web3jHandler {

    /** String variable 'network' is used for selecting which network you want to use */
    private static Web3j web3;
    /** Web3j variable 'web3' is used to implement all the functions, exist in Web3j Library */
    private static Credentials credentials;
    /** Credentials variable 'credentials' is used to implement all the functions, exist in Credentials Library */
    private static TransactionReceipt transactionReceipt;

    /**
     * web3Connection function is used to create the connection with the end-client node. // I uses Infura API.
     * @return condition
     */
    public static boolean web3Connection() throws IOException {
        web3 = Web3jFactory.build(new HttpService("http://52.78.228.212:8545"));
        return  web3 != null;
    }

    /**
     * loadCredentials function is used to load the UTC-JSON file from a particular path.
     * @param password  is used to access your UTC-JSON file.
     * @throws IOException
     * @throws CipherException
     */
    public static void loadCredentials(String password, String fileName) throws IOException, CipherException {
        credentials = WalletUtils.loadCredentials(password, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName);
    }

    /**
     * transaction function is used to send funds from your address to another Ethereum address.
     * @param address   is a TO address or a address where you want to transfer funds.
     * @param ethBalance   is a amount you want to send.
     * @throws TransactionTimeoutException
     * @throws IOException
     * @throws InterruptedException
     * @return
     */
    public static TransactionReceipt transaction(String address, double ethBalance) throws InterruptedException, IOException, TransactionTimeoutException {
        return transactionReceipt = Transfer.sendFunds( web3, credentials, address, BigDecimal.valueOf(ethBalance), Convert.Unit.ETHER);
    }


    /**
     * getBalance function is used to get Balance and it returns the BigInteger value.
     * @throws InterruptedException
     * @throws ExecutionException
     * @return
     */
    public static BigInteger getBalance(){
        Future<EthGetBalance> ethGetBalanceFuture = web3.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync();
        try {
            return ethGetBalanceFuture.get().getBalance();
        }catch (Exception e){
            return BigInteger.ONE;
        }
    }

    /**
     * This function is returning wallet address from credentials
     * @return address in String
     * */
    public static String getWalletAddress(){
        return credentials.getAddress();
    }
}
