package cn.androidpi.app.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import cn.androidpi.app.R
import kotlinx.android.synthetic.main.activity_html.*

class HtmlActivity : AppCompatActivity() {

    var mSettings: WebSettings? = null

    companion object {
        val ACTION_VIEW = "cn.androidpi.app.components.activity.HtmlActivity.ACTION_VIEW"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_html)
        config(web_view)

        web_view.loadUrl(intent?.data?.toString())
    }

    override fun onResume() {
        super.onResume()
        mSettings?.javaScriptEnabled = true
    }

    override fun onStop() {
        super.onStop()
        mSettings?.javaScriptEnabled = false
    }

    fun config(webView: WebView) {
        mSettings = webView.settings
        mSettings?.builtInZoomControls = true
        mSettings?.displayZoomControls = false

        //
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = WebViewClient()
    }
}
