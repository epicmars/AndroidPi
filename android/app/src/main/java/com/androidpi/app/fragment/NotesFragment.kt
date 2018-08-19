package com.androidpi.app.fragment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.androidpi.app.R
import com.androidpi.app.activity.TextNoteEditActivity
import com.androidpi.app.base.ui.BaseFragment
import com.androidpi.app.base.ui.BindLayout
import com.androidpi.app.base.ui.RecyclerAdapter
import com.androidpi.app.buiness.viewmodel.NotesViewModel
import com.androidpi.app.buiness.viewmodel.ViewModelFactory
import com.androidpi.app.base.vm.vo.Resource
import com.androidpi.app.databinding.FragmentNotesBinding
import com.androidpi.app.viewholder.ErrorViewHolder
import com.androidpi.app.viewholder.TextNoteViewHolder
import com.androidpi.app.viewholder.items.ErrorItem
import com.androidpi.note.entity.TextNote
import com.androidpi.note.model.TextNotesModel
import javax.inject.Inject

/**
 * Created by jastrelax on 2017/12/7.
 */
@BindLayout(R.layout.fragment_notes)
class NotesFragment : BaseFragment<FragmentNotesBinding>(), TextNotesModel{

    companion object {
        val REQUEST_CODE_EDIT_TEXT_NOTE = 1

        fun newInstance(): NotesFragment {
            return NotesFragment()
        }
    }

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    lateinit var mNotesViewModel: NotesViewModel

    var mAdapter: RecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNotesViewModel = mViewModelFactory.create(NotesViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = RecyclerAdapter()
        mAdapter?.register(ErrorViewHolder::class.java,
                TextNoteViewHolder::class.java)
        binding.recyclerNotes.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerNotes.adapter = mAdapter

        binding.btnAdd.setOnClickListener { v ->
            startActivityForResult(Intent(activity, TextNoteEditActivity::class.java), REQUEST_CODE_EDIT_TEXT_NOTE)
        }

        mNotesViewModel.mTextNotes.observe(this, object : Observer<Resource<List<TextNote>>> {

            override fun onChanged(t: Resource<List<TextNote>>?) {
                if (t == null || t.isError()) {
                    setGridColumn(1)
                    val errorMessage = if (t != null) t.message else "加载文本笔记失败"
                    mAdapter?.setPayloads(ErrorItem(errorMessage))
                } else {
                    if (t.data == null) {
                        setGridColumn(1)
                        mAdapter?.setPayloads(ErrorItem("空的笔记本"))
                    } else {
                        setGridColumn()
                        mAdapter?.setPayloads(t.data)
                    }
                }
            }
        })

        getAllTextNotes()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_EDIT_TEXT_NOTE) {
            getAllTextNotes()
        }
    }

    fun setGridColumn(count: Int = 2) {
        (binding.recyclerNotes.layoutManager as StaggeredGridLayoutManager).spanCount = count
    }

    override fun getAllTextNotes() {
        mNotesViewModel.getAllTextNotes()
    }

}