package br.com.mullumwelt.queimamull;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.view.Gravity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {
    private WebView webView;
    private boolean showingReport = false;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(12, 65, 45));
        getWindow().setNavigationBarColor(Color.rgb(12, 65, 45));
        showHome();
    }

    private int dp(int v) {
        return Math.round(v * getResources().getDisplayMetrics().density);
    }

    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value); t.setTextSize(size); t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setGravity(Gravity.CENTER);
        return t;
    }

    private void showHome() {
        showingReport = false;
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(dp(24), dp(32), dp(24), dp(32));
        root.setBackgroundColor(Color.rgb(246, 249, 246));

        ImageView logo = new ImageView(this);
        logo.setImageResource(R.drawable.logo_mull);
        logo.setAdjustViewBounds(true);
        logo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        root.addView(logo, new LinearLayout.LayoutParams(-1, dp(130)));

        TextView title = text("QUEIMA MULL", 26, Color.rgb(17, 91, 58), true);
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(-1, -2);
        tp.topMargin = dp(28); root.addView(title, tp);
        TextView subtitle = text("Relatórios de operação", 16, Color.DKGRAY, false);
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(-1, -2);
        sp.topMargin = dp(8); root.addView(subtitle, sp);

        Button open = new Button(this);
        open.setText("ABRIR RELATÓRIO DIÁRIO");
        open.setTextColor(Color.WHITE);
        open.setAllCaps(false);
        open.setTextSize(17);
        open.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.rgb(20, 112, 70)));
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, dp(58));
        bp.topMargin = dp(40); root.addView(open, bp);
        open.setOnClickListener(v -> showReport());

        TextView footer = text("MULL UMWELT\nJuntos por um mundo mais limpo!", 13, Color.rgb(70, 100, 80), false);
        LinearLayout.LayoutParams fp = new LinearLayout.LayoutParams(-1, -2);
        fp.topMargin = dp(36); root.addView(footer, fp);
        setContentView(root);
    }

    private void showReport() {
        showingReport = true;
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setBackgroundColor(Color.WHITE);
        Button back = new Button(this);
        back.setText("‹  Voltar ao início"); back.setAllCaps(false);
        back.setOnClickListener(v -> showHome());
        page.addView(back, new LinearLayout.LayoutParams(-1, dp(48)));
        ProgressBar progress = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        page.addView(progress, new LinearLayout.LayoutParams(-1, dp(3)));
        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface public void printPage(final String name) {
                runOnUiThread(() -> {
                    PrintManager pm = (PrintManager) getSystemService(Context.PRINT_SERVICE);
                    PrintAttributes attrs = new PrintAttributes.Builder()
                        .setMediaSize(PrintAttributes.MediaSize.ISO_A4).build();
                    pm.print(name, webView.createPrintDocumentAdapter(name), attrs);
                });
            }
        }, "AndroidBridge");
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override public void onProgressChanged(WebView view, int newProgress) {
                progress.setProgress(newProgress);
                progress.setVisibility(newProgress >= 100 ? View.GONE : View.VISIBLE);
            }
        });
        page.addView(webView, new LinearLayout.LayoutParams(-1, 0, 1));
        setContentView(page);
        webView.loadUrl("file:///android_asset/index.html");
    }

    @Override public void onBackPressed() {
        if (showingReport) { showHome(); }
        else { super.onBackPressed(); }
    }
}
