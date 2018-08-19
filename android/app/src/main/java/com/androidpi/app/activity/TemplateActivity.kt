package com.androidpi.app.activity

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil.setContentView
import android.databinding.ViewDataBinding
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.androidpi.app.R
import com.androidpi.app.base.di.Injectable
import com.androidpi.app.base.ui.BaseActivity
import com.androidpi.app.base.ui.BindLayout
import com.androidpi.app.databinding.ActivityTemplateBinding
import com.androidpi.app.fragment.FragmentFactory
import com.androidpi.app.fragment.FragmentFactoryMap
import timber.log.Timber

/**
 * Page that has normal launch mode should use this template and
 * fill the content with a fragment.
 */
@BindLayout(R.layout.activity_template)
@Injectable
class TemplateActivity : BaseActivity<ActivityTemplateBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragmentName = intent.getStringExtra(EXTRA_FRAGMENT_NAME)
        if (fragmentName == null) return
        val factory = FragmentFactoryMap.factoryMap[fragmentName]
        try {
            val ft = supportFragmentManager.beginTransaction()
            val fragment = factory?.create()
            ft.replace(R.id.content, fragment)
            ft.commitAllowingStateLoss()
        } catch (e: Exception) {
            Timber.e(e)
        }
        FragmentFactoryMap.factoryMap.remove(fragmentName)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportPostponeEnterTransition()
        }

        val view = window.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }

        // 明亮主题的状态栏，字体为黑色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (com.androidpi.app.pi.base.R.style.AppTheme_NoActionBar_Light === getThemeRes()) {
                view.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            }
        }
    }

    companion object {
        val EXTRA_FRAGMENT_NAME = "TemplateActivity.EXTRA_FRAGMENT_NAME"

        fun startWith(optionsCompat: ActivityOptionsCompat, context: Context, flags: Int = 0, fragmentName: String, factory: FragmentFactory<Fragment>) {
            FragmentFactoryMap.put(fragmentName, factory)
            val intent = Intent(context, TemplateActivity::class.java)
            intent.putExtra(EXTRA_FRAGMENT_NAME, fragmentName)
            intent.flags = flags
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                context.startActivity(intent, optionsCompat.toBundle())
            } else {
                context.startActivity(intent)
            }
        }

        fun startWith(context: Context, flags: Int = 0, fragmentName: String, factory: FragmentFactory<Fragment>) {
            FragmentFactoryMap.put(fragmentName, factory)
            val intent = Intent(context, TemplateActivity::class.java)
            intent.putExtra(EXTRA_FRAGMENT_NAME, fragmentName)
            intent.flags = flags
            context.startActivity(intent)
        }
    }
}

