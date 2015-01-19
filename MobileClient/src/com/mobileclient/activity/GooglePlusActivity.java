/**
 * 
 */
package com.mobileclient.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.PlusClient;

/**
 * @author zl
 * 
 */
public class GooglePlusActivity extends Activity implements
		View.OnClickListener, ConnectionCallbacks, OnConnectionFailedListener {
	
	private static final String TAG = "GooglePlusActivity";
    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

    private ProgressDialog mConnectionProgressDialog;
    
    private PlusClient mPlusClient;
    
    private ConnectionResult mConnectionResult;
    
    
    private SignInButton mSignInButton;
	
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_plus);
//        mPlusClient = new PlusClient.Builder(this, this, this)
//        		.setVisibleActivities("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
//                .build();
        // 参考地址:http://stackoverflow.com/questions/19887187/setvisibleactivities-undefined
        
        mPlusClient =
        	    new PlusClient.Builder(this, this, this)
//        			.setActions("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
//        	        .setScopes("PLUS_LOGIN") // Space separated list of scopes
        	        .build();
        
        // 在未解决连接故障时，显示进度条。
        mConnectionProgressDialog = new ProgressDialog(this);
        mConnectionProgressDialog.setMessage("Signing in...");
        
        mSignInButton = (SignInButton)findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(this);
    }
    
    
    @Override
    protected void onStart() {
        super.onStart();
        mPlusClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlusClient.disconnect();
    }

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (result.hasResolution()) {
            try {
                result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
            } catch (SendIntentException e) {
                mPlusClient.connect();
            }
        }
        // 在用户点击时保存结果并解决连接故障。
        mConnectionResult = result;
	}
	
	@Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == RESULT_OK) {
            mConnectionResult = null;
            mPlusClient.connect();
        }
    }

	@Override
	public void onConnected(Bundle arg0) {
		String accountName = mPlusClient.getAccountName();
		
		Log.i(TAG, accountName+"is connected.");
        //Toast.makeText(this, accountName + " is connected.", Toast.LENGTH_LONG).show();
        
        mConnectionProgressDialog.dismiss();
       // Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onDisconnected() {
		Log.d(TAG, "disconnected");
	}

//	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.sign_in_button && !mPlusClient.isConnected()) {
			Log.i(TAG, "点击登录按钮...未连接");
	        if (mConnectionResult == null) {
	            mConnectionProgressDialog.show();
	        } else {
	            try {
	            	Log.i(TAG, "未连接，开始解决方案...");
	                mConnectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
	                Log.i(TAG, ""+mConnectionResult.getResolution());
	               
	            } catch (SendIntentException e) {
	            	Log.i(TAG, "未连接，重新尝试连接...");
	                // 重新尝试连接。
	                mConnectionResult = null;
	                mPlusClient.connect();
	            }
	        }
	    }else{
	    	String accountName = mPlusClient.getAccountName();
     		Log.i(TAG, accountName+"is connected.");
	    }
	}

}
