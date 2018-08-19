package com.androidpi.app.activity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import com.androidpi.app.R
import com.androidpi.app.base.di.Injectable
import com.androidpi.app.base.ui.BaseActivity
import com.androidpi.app.base.ui.BindLayout
import com.androidpi.app.databinding.ActivityMainBinding
import com.androidpi.app.fragment.FragmentFactory
import com.androidpi.app.fragment.MainFragment
import com.androidpi.app.fragment.NotesFragment
import com.androidpi.app.fragment.TodoListFragment

@BindLayout(R.layout.activity_main)
@Injectable
class MainActivity : BaseActivity<ActivityMainBinding>(), NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setSupportActionBar(binding?.toolbar)
//        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding?.navigationView?.setNavigationItemSelectedListener(this)

        if (null === savedInstanceState) {
            initFragments()
        }
    }


    fun initFragments() {
        val ft = supportFragmentManager?.beginTransaction()
        ft?.replace(R.id.content, MainFragment.newInstance())
        ft?.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        when(itemId) {
            R.id.nav_todo -> TemplateActivity.startWith(this,
                    fragmentName = TodoListFragment.javaClass.canonicalName,
                    factory = object : FragmentFactory<TodoListFragment>() {
                        override fun create(): TodoListFragment {
                            return TodoListFragment.newInstance()
                        }
                    })
            R.id.nav_notes -> TemplateActivity.startWith(this,
                    fragmentName = NotesFragment.javaClass.canonicalName,
                    factory = object : FragmentFactory<NotesFragment>() {
                        override fun create(): NotesFragment {
                            return NotesFragment.newInstance()
                        }
                    })
        }
        binding?.drawer?.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
    }
}
