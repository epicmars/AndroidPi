package com.androidpi.app.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.androidpi.app.R
import com.androidpi.app.base.BaseFragment
import com.androidpi.app.base.BindLayout
import com.androidpi.app.databinding.FragmentHtmlReaderBinding

/**
 * Created by jastrelax on 2018/1/1.
 */
@BindLayout(R.layout.fragment_html_reader, injectable = false)
class HtmlReaderFragment : BaseFragment<FragmentHtmlReaderBinding>() {

    var mSettings: WebSettings? = null

    companion object {

        val ARGS_BASE_URL = "HtmlReaderFragment.ARGS_URL"
        val ARGS_HTML = "HtmlReaderFragment.ARGS_HTML"

        fun newInstance(baseUrl: String?, html: String?): HtmlReaderFragment {
            val b = Bundle()
            b.putString(ARGS_BASE_URL, baseUrl)
            b.putString(ARGS_HTML, html)
            val fragment = HtmlReaderFragment()
            fragment.arguments = b
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val baseUrl = arguments?.getString(ARGS_BASE_URL)
        val html = arguments?.getString(ARGS_HTML)
        config(mBinding.webView)
        mBinding.webView.loadDataWithBaseURL(baseUrl, html, "text/html", "utf-8", null)
    }

    fun toggleImmersiveMode() {
        val options = activity?.window?.decorView?.systemUiVisibility
        var immersiveOptions = options
        if (immersiveOptions == null) return
        // Navigation bar hiding:  Backwards compatible to ICS.
        if (Build.VERSION.SDK_INT >= 14) {
            immersiveOptions = immersiveOptions xor View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }

        // Status bar hiding: Backwards compatible to Jellybean
        if (Build.VERSION.SDK_INT >= 16) {
            immersiveOptions = immersiveOptions xor View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        // Immersive mode: Backward compatible to KitKat.
        if (Build.VERSION.SDK_INT >= 18) {
            immersiveOptions = immersiveOptions xor View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }

        activity?.window?.decorView?.systemUiVisibility = immersiveOptions
    }

    fun config(webView: WebView) {
        mSettings = webView.settings
        mSettings?.javaScriptEnabled = true
        mSettings?.builtInZoomControls = true
        mSettings?.displayZoomControls = false
        mSettings?.allowUniversalAccessFromFileURLs = true

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                toggleImmersiveMode()
            }

            override fun onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?): Boolean {
                return super.onRenderProcessGone(view, detail)
            }
        }
    }

}