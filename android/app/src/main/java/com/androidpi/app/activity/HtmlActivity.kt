package com.androidpi.app.activity

import android.arch.lifecycle.Observer
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.*
import com.androidpi.app.R
import com.androidpi.app.base.BaseActivity
import com.androidpi.app.buiness.viewmodel.HtmlReaderViewModel
import com.androidpi.app.fragment.HtmlReaderFragment
import com.androidpi.common.libs.readability.Readability
import com.androidpi.common.libs.readability.ReaderHelper
import com.androidpi.news.entity.Bookmark
import kotlinx.android.synthetic.main.activity_html.*
import timber.log.Timber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.Reader
import java.util.concurrent.Executors
import javax.inject.Inject

class HtmlActivity : BaseActivity() {

    var mSettings: WebSettings? = null
    var mUrl: String? = null
    var mReadabilityJs: String? = null
    var mReaderJs: String? = null
    var mReadListenerJs: String? = null
    var mIsReadable: Boolean? = null
    var mReaderModelEnable = true
    var mReaderLoaded = false
    var mTemplate: String? = null
    var mHtml: String? = null
    var mArticltHtml: String? = null
    var mBookmarked = false

    @Inject
    lateinit var mHtmlReaderViewModel: HtmlReaderViewModel

    companion object {
        val ACTION_VIEW = "cn.androidpi.app.components.activity.HtmlActivity.ACTION_VIEW"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_html)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        // initiate
        initResources()
        // configurate web view
        configWebView(web_view)
        // load web page
        mUrl = intent?.data?.toString()
        web_view.loadUrl(mUrl)

        Executors.newSingleThreadExecutor().execute({
            val readability = Readability(mUrl)
            if (readability.init()) {
                val articleHtml = ReaderHelper.replaceTemplateByTagId(mTemplate, "article", readability.articleHtml)
                read(readability.html, articleHtml)
            }
        })

        mHtmlReaderViewModel.mBookmark.observe(this, object : Observer<Bookmark> {
            override fun onChanged(t: Bookmark?) {
                if (t != null) {
                    mBookmarked = true
                    invalidateOptionsMenu()
                }
            }
        })
        mHtmlReaderViewModel.getBookmark(mUrl)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_html_toolbar, menu)
        val item = menu?.findItem(R.id.action_bookmark)
        if (item != null && !item.isCheckable) {
            item.setCheckable(true)
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.action_reader_mode)?.setVisible(mIsReadable ?: false)
        if (mIsReadable != null) {
            menu?.findItem(R.id.action_bookmark)?.setVisible(!mIsReadable!!)
        }
        if (mBookmarked) {
            menu?.findItem(R.id.action_bookmark)?.setChecked(true)
            menu?.findItem(R.id.action_bookmark)?.setIcon(R.drawable.ic_star_fill_24dp)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.action_reader_mode -> {
                readPage(web_view)
                true
            }
            R.id.action_bookmark -> {
                if (!item.isChecked) {
                    mHtmlReaderViewModel.saveBookmark(mUrl, mHtml, mArticltHtml)
                    item.setChecked(true)
                    item.setIcon(R.drawable.ic_star_fill_24dp)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Initiate needed resources, e.g. javascript code, html templates, etc.
     */
    fun initResources() {
        loadJsFromAssets()
    }

    /**
     * Load javascript code from asset files.
     */
    fun loadJsFromAssets() {
        try {
            mTemplate = readFromAssets("web/static/readability/template.html")
            mReadabilityJs = readFromAssets("web/static/readability/js/ipreadability-1.7.1.js")
            mReaderJs = readFromAssets("web/static/readability/js/read.js")
            mReadListenerJs = readFromAssets("web/static/readability/js/readlistener.js")
        } catch (e : IOException) {
            Timber.e(e)
        }
    }

    fun readFromAssets(filename: String): String? {
        var assetReader: Reader? = null
        try {
            assetReader = BufferedReader(InputStreamReader(assets.open(filename)))
            return assetReader.readText()
        } catch (e: IOException) {
            Timber.e(e)
        } finally {
            try {
                assetReader?.close()
            } catch (e: IOException){}
        }
        return null
    }

    /**
     * Configurate the web view.
     */
    fun configWebView(webView: WebView) {
        mSettings = webView.settings
        mSettings?.javaScriptEnabled = true
        mSettings?.builtInZoomControls = true
        mSettings?.displayZoomControls = false
        mSettings?.allowUniversalAccessFromFileURLs = true

        webView.addJavascriptInterface(this, "reader")

        webView.webChromeClient = object : WebChromeClient() {

            override fun onReceivedTouchIconUrl(view: WebView?, url: String?, precomposed: Boolean) {
                super.onReceivedTouchIconUrl(view, url, precomposed)
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                injectReadListener(view)
                supportActionBar?.title = title
                if (mReaderModelEnable) {
                    toggleReaderModeVisibility(true)
                }
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (pb.visibility == View.GONE) return
                pb.progress = newProgress
                if (pb.progress >= pb.max) {
                    pb.visibility = View.GONE
                }
            }
        }
        webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                injectReadListener(view)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }
    }

    /**
     * Toggle the visibility of reader mode action.
     */
    fun toggleReaderModeVisibility(visible: Boolean) {
        if (mIsReadable == false) return
        mIsReadable = visible
        invalidateOptionsMenu()
    }

    fun injectReadListener(view: WebView?) {
        evaluateJs(view,mReadabilityJs + mReadListenerJs, null)
    }

    fun readPage(view: WebView?) {
        evaluateJs(view,mReadabilityJs + mReaderJs, null)
    }

    fun evaluateJs(view: WebView?, jsCode: String?, valueCallback: ValueCallback<String>?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view?.evaluateJavascript(jsCode,  valueCallback)
        } else {
            view?.loadUrl("javascript:" + jsCode)
        }
    }

    @JavascriptInterface
    fun readable() {
        toggleReaderModeVisibility(true)
    }

    /**
     * Read parsed html page, it may be slow, therefore the javascript parse function
     * is executed asynchronous, this method is called on the JavaBridge thread.
     * @param html
     */
    @JavascriptInterface
    fun read(html: String?, articleHtml: String?) {
        if (html == null || html.equals("null") || articleHtml == null)
            return
        runOnUiThread {
            if (mReaderLoaded)
                return@runOnUiThread
            mHtml = html
            mArticltHtml = articleHtml
            mReaderLoaded = true
            // hide current web_view
            web_view.visibility = View.GONE
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.content, HtmlReaderFragment.newInstance(mUrl, articleHtml))
            ft.commitAllowingStateLoss()
            toggleReaderModeVisibility(false)
        }
    }
}
