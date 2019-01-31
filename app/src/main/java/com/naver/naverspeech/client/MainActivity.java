package com.naver.naverspeech.client;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.naverspeech.client.utils.AudioWriterPCM;
import com.naver.speech.clientapi.SpeechRecognitionResult;

import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivity extends Activity {

    WebView mWebView;
    int w, h;

	private static final String TAG = MainActivity.class.getSimpleName();
	private static final String CLIENT_ID = "g23iinslr3";
    // 1. "내 애플리케이션"에서 Client ID를 확인해서 이곳에 적어주세요.
    // 2. build.gradle (Module:app)에서 패키지명을 실제 개발자센터 애플리케이션 설정의 '안드로이드 앱 패키지 이름'으로 바꿔 주세요

    private RecognitionHandler handler;
    private NaverRecognizer naverRecognizer;

    private TextView txtResult;
    private ImageButton btnStart;
    private String mResult, iResult;

    private AudioWriterPCM writer;

    // Handle speech recognition Messages.
    private void handleMessage(Message msg) {
        Intent intent;
        switch (msg.what) {
            case R.id.clientReady:
                // Now an user can speak.
                txtResult.setText("Connected");
                w = btnStart.getWidth();
                h = txtResult.getHeight();
                txtResult.setLayoutParams(new LinearLayout.LayoutParams(w, 200));
                writer = new AudioWriterPCM(
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/NaverSpeechTest");
                writer.open("Test");
                break;

            case R.id.audioRecording:
                writer.write((short[]) msg.obj);
                break;

            case R.id.partialResult:
                // Extract obj property typed with String.
                mResult = (String) (msg.obj);
                txtResult.setText(mResult);
                iResult = mResult;
                break;

            case R.id.finalResult:
                // Extract obj property typed with String array.
                // The first element is recognition result for speech.
            	SpeechRecognitionResult speechRecognitionResult = (SpeechRecognitionResult) msg.obj;
            	List<String> results = speechRecognitionResult.getResults();
            	StringBuilder strBuf = new StringBuilder();
            	for(String result : results) {
            		strBuf.append(result);
            		strBuf.append("\n");
            	}
                mResult = strBuf.toString();

            	if(iResult.contains("전화")) {
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                                int permissionResult = checkSelfPermission(Manifest.permission.CALL_PHONE);
                                if (permissionResult == PackageManager.PERMISSION_DENIED) {
                                    if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                                        dialog.setTitle("권한이 필요합니다.").setMessage("이 기능을 사용하기 위해서는 권한이 필요합니다.").setPositiveButton("네", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1000);
                                                }

                                            }
                                        }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(MainActivity.this, "기능을 취소했습니다", Toast.LENGTH_SHORT).show();

                                            }
                                        }).create().show();
                                    } else {
                                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1000);
                                    }
                                } else {
                                    intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:0539505114"));
                                    startActivity(intent);
                                    try {
                                        Thread.sleep(1000);
                                    } catch (Exception e) {

                                    }
                                }
                            }
                            else
                            {
                                intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:0539505114"));
                                startActivity(intent);
                                try {
                                    Thread.sleep(1000);
                                } catch (Exception e) {

                                }
                            }
                            txtResult.setLayoutParams(new LinearLayout.LayoutParams(w, h));

                }
                else if(iResult.contains("홈") || iResult.contains("메인 화면") || iResult.contains("메인")) {
                    mWebView.loadUrl("http://54.180.71.148:8080/index.jsp");
                    mWebView.setWebViewClient(new WebViewClient());
                    mWebView.setWebChromeClient(new WebChromeClient());
                    mWebView.getSettings().setJavaScriptEnabled(true);
                    try{
                        Thread.sleep(1000);
                    }
                    catch(Exception e){

                    }
                    txtResult.setLayoutParams(new LinearLayout.LayoutParams(w, h));
                }
                else if(iResult.contains("센터 소개") || iResult.contains("소개")) {
                    mWebView.loadUrl("http://54.180.71.148:8080/about.html");
                    mWebView.setWebViewClient(new WebViewClient());
                    mWebView.setWebChromeClient(new WebChromeClient());
                    mWebView.getSettings().setJavaScriptEnabled(true);
                    try{
                        Thread.sleep(1000);
                    }
                    catch(Exception e){

                    }
                    txtResult.setLayoutParams(new LinearLayout.LayoutParams(w, h));
                }
                else if(iResult.contains("상담") || iResult.contains("신고")) {
                    mWebView.loadUrl("http://54.180.71.148:8080/services.html");
                    mWebView.setWebViewClient(new WebViewClient());
                    mWebView.setWebChromeClient(new WebChromeClient());
                    mWebView.getSettings().setJavaScriptEnabled(true);
                    try{
                        Thread.sleep(1000);
                    }
                    catch(Exception e){

                    }
                    txtResult.setLayoutParams(new LinearLayout.LayoutParams(w, h));
                }
                else if(iResult.contains("게시판") || iResult.contains("커뮤니티")) {
                    mWebView.loadUrl("http://54.180.71.148:8080/community.jsp");
                    mWebView.setWebViewClient(new WebViewClient());
                    mWebView.setWebChromeClient(new WebChromeClient());
                    mWebView.getSettings().setJavaScriptEnabled(true);
                    try{
                        Thread.sleep(1000);
                    }
                    catch(Exception e){

                    }
                    txtResult.setLayoutParams(new LinearLayout.LayoutParams(w, h));
                }
                else if(iResult.contains("연락") || iResult.contains("메일")) {
                    mWebView.loadUrl("http://54.180.71.148:8080/contact.html");
                    mWebView.setWebViewClient(new WebViewClient());
                    mWebView.setWebChromeClient(new WebChromeClient());
                    mWebView.getSettings().setJavaScriptEnabled(true);
                    try{
                        Thread.sleep(1000);
                    }
                    catch(Exception e){

                    }
                    txtResult.setLayoutParams(new LinearLayout.LayoutParams(w, h));
                }
                else {
            	    try{
            	        Thread.sleep(2000);
                    }
                    catch(Exception e){

                    }
                    txtResult.setLayoutParams(new LinearLayout.LayoutParams(w, h));
                }
                break;

            case R.id.recognitionError:
                if (writer != null) {
                    writer.close();
                }

                mResult = "Error code : " + msg.obj.toString();
                txtResult.setText(mResult);
                btnStart.setImageResource(R.drawable.ic_launcher1);
                btnStart.setEnabled(true);
                break;

            case R.id.clientInactive:
                if (writer != null) {
                    writer.close();
                }

                btnStart.setImageResource(R.drawable.ic_launcher1);
                btnStart.setEnabled(true);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.layout);

        //
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.loadUrl("http://54.180.71.148:8080/index.jsp");
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient());


        //

        txtResult = (TextView) findViewById(R.id.txt_result);
        btnStart = (ImageButton) findViewById(R.id.btn_start);

        handler = new RecognitionHandler(this);
        naverRecognizer = new NaverRecognizer(this, handler, CLIENT_ID);

        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                    int permissionResult = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
                    if (permissionResult == PackageManager.PERMISSION_DENIED) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                            dialog.setTitle("권한이 필요합니다.").setMessage("이 기능을 사용하기 위해서는 권한이 필요합니다.").setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 1000);
                                    }

                                }
                            }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(MainActivity.this, "기능을 취소했습니다", Toast.LENGTH_SHORT).show();

                                }
                            }).create().show();
                        } else {
                            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 1000);
                        }
                    } else {
                        if (!naverRecognizer.getSpeechRecognizer().isRunning()) {
                            // Start button is pushed when SpeechRecognizer's state is inactive.
                            // Run SpeechRecongizer by calling recognize().
                            mResult = "";
                            txtResult.setText("Connecting...");
                            btnStart.setImageResource(R.drawable.ic_launcher);
                            naverRecognizer.recognize();
                        } else {
                            Log.d(TAG, "stop and wait Final Result");
                            btnStart.setEnabled(false);
                            naverRecognizer.getSpeechRecognizer().stop();
                        }
                    }
                }
                else
                {
                    if(!naverRecognizer.getSpeechRecognizer().isRunning()) {
                        // Start button is pushed when SpeechRecognizer's state is inactive.
                        // Run SpeechRecongizer by calling recognize().
                        mResult = "";
                        txtResult.setText("Connecting...");
                        btnStart.setImageResource(R.drawable.ic_launcher);
                        naverRecognizer.recognize();
                    } else {
                        Log.d(TAG, "stop and wait Final Result");
                        btnStart.setEnabled(false);
                        naverRecognizer.getSpeechRecognizer().stop();
                    }
                }
               txtResult.setLayoutParams(new LinearLayout.LayoutParams(w, h));
            }
        });


    }
    public class WebClient extends WebViewClient {
        private AlertDialog show;

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
    	super.onStart();
    	// NOTE : initialize() must be called on start time.
    	naverRecognizer.getSpeechRecognizer().initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mResult = "";
        txtResult.setText("");
        btnStart.setImageResource(R.drawable.ic_launcher1);
        btnStart.setEnabled(true);
    }

    @Override
    protected void onStop() {
    	super.onStop();
    	// NOTE : release() must be called on stop time.
    	naverRecognizer.getSpeechRecognizer().release();
    }

    // Declare handler for handling SpeechRecognizer thread's Messages.
    static class RecognitionHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        RecognitionHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }
}
