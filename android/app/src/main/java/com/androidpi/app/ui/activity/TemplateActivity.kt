package com.androidpi.app.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import com.androidpi.app.R
import com.androidpi.app.ui.base.BaseActivity
import com.androidpi.app.ui.fragment.FragmentFactory
import com.androidpi.app.ui.fragment.FragmentFactoryMap
import timber.log.Timber

class TemplateActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_template)

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

