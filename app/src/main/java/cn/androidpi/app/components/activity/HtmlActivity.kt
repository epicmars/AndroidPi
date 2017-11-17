package cn.androidpi.app.components.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import cn.androidpi.app.R
import kotlinx.android.synthetic.main.activity_html.*

class HtmlActivity : AppCompatActivity() {

    companion object {
        val ACTION_VIEW = "cn.androidpi.app.components.activity.HtmlActivity.ACTION_VIEW"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_html)
        config(web_view)

        web_view.loadUrl(intent?.data?.toString())
    }

    fun config(webView: WebView) {
        val webSetting = webView.settings
        webSetting.javaScriptEnabled = true

        //
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = WebViewClient()
    }
}
