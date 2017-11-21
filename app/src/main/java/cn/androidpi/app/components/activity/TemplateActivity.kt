package cn.androidpi.app.components.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import cn.androidpi.app.R
import cn.androidpi.app.components.base.BaseActivity
import cn.androidpi.app.components.fragment.FragmentFactory
import cn.androidpi.app.components.fragment.FragmentFactoryMap
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
            ft.replace(R.id.content, factory?.get()?.create())
            ft.commitAllowingStateLoss()
        } catch (e : Exception) {
            Timber.e(e)
        }
    }

    companion object {
        val EXTRA_FRAGMENT_NAME = "TemplateActivity.EXTRA_FRAGMENT_NAME"

        fun startWith(context: Context, flags: Int = 0, fragmentName: String, factory: FragmentFactory<Fragment>) {
            FragmentFactoryMap.put(fragmentName, factory)
            val intent = Intent(context, TemplateActivity::class.java)
            intent.putExtra(EXTRA_FRAGMENT_NAME, fragmentName)
            intent.flags = flags
            context.startActivity(intent)
        }
    }
}
