package com.openhands.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.ClientCertRequest;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;

public class MainActivity extends Activity {

    private static final String TAG = "OpenHands";
    private WebView webView;
    private ProgressBar progressBar;
    private View errorView;
    
    // URLs configurables
    private static final String BASE_URL = "https://app.all-hands.dev/";
    private static final String LOGIN_URL = "https://app.all-hands.dev/login?login_method=github";

    // URLs externas que se abren en navegador externo
    private static final String[] EXTERNAL_URL_PREFIXES = {
        "https://github.com/login",
        "https://gitlab.com/oauth",
        "https://bitbucket.org/oauth",
        "https://discord.com/oauth",
        "https://discord.gg",
        "https://docs.openhands.dev",
        "https://www.all-hands.dev/terms",
        "https://www.all-hands.dev/privacy"
    };

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Pantalla completa sin título
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if ((getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }
        
        setContentView(R.layout.activity_main);
        
        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        errorView = findViewById(R.id.errorView);
        
        setupWebView();
        setupErrorView();
        handleIntent(getIntent());
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        
        // JavaScript (esencial para OpenHands)
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        
        // DOM y almacenamiento
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(true);
        
        // Caché
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCacheEnabled(true);
        
        // Viewport
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        
        // Media y permisos
        settings.setMediaPlaybackRequiresUserGesture(false);
        
        // Mejoras de rendimiento
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setEnableSmoothTransition(true);
        
        // User-Agent personalizado
        String userAgent = settings.getUserAgentString();
        settings.setUserAgentString(userAgent + " OpenHands-Android/1.0");
        
        // Hardware acceleration
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        
        // Cookies
        CookieManager.getInstance().setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }
        
        // WebViewClient para navegación
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return shouldOverrideUrl(request.getUrl().toString());
            }
            
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                errorView.setVisibility(View.GONE);
            }
            
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
            
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (request.isForMainFrame()) {
                    showError("Error de conexión: " + error.getDescription());
                }
            }
            
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                if (request.isForMainFrame() && errorResponse.getStatusCode() >= 500) {
                    showError("Error del servidor (500+). Intenta más tarde.");
                }
            }
            
            // Manejar errores SSL (para desarrollo)
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // En producción, comentar esta línea
                handler.proceed();
            }
        });
        
        // WebChromeClient para diálogos
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
            }
            
            @Override
            public void onReceivedTitle(WebView view, String title) {
                getActionBar().setTitle(title);
            }
            
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, android.webkit.GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
        
        // Descargas
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        
        // Cargar URL inicial
        webView.loadUrl(getInitialUrl());
    }
    
    private String getInitialUrl() {
        Intent intent = getIntent();
        if (intent != null && Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri data = intent.getData();
            if (data != null) {
                return data.toString();
            }
        }
        return BASE_URL;
    }
    
    private boolean shouldOverrideUrl(String url) {
        // Verificar URLs externas
        for (String prefix : EXTERNAL_URL_PREFIXES) {
            if (url.startsWith(prefix)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            }
        }
        
        // Abrir mailto: y tel: en apps correspondientes
        if (url.startsWith("mailto:") || url.startsWith("tel:") || url.startsWith("sms:")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        
        return false;
    }
    
    private void setupErrorView() {
        View retryButton = errorView.findViewById(R.id.retryButton);
        if (retryButton != null) {
            retryButton.setOnClickListener(v -> {
                errorView.setVisibility(View.GONE);
                webView.loadUrl(BASE_URL);
            });
        }
        
        View openBrowserButton = errorView.findViewById(R.id.openBrowserButton);
        if (openBrowserButton != null) {
            openBrowserButton.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(BASE_URL));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            });
        }
    }
    
    private void showError(String message) {
        errorView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        Log.e(TAG, message);
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }
    
    private void handleIntent(Intent intent) {
        if (intent != null && Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            if (uri != null && webView != null) {
                webView.loadUrl(uri.toString());
            }
        }
    }
    
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            showExitDialog();
        }
    }
    
    private void showExitDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Salir de OpenHands")
            .setMessage("¿Estás seguro de que quieres salir?")
            .setPositiveButton("Sí", (dialog, which) -> finish())
            .setNegativeButton("No", null)
            .show();
    }
    
    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
        }
    }
}