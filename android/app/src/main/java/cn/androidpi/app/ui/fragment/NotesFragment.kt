package cn.androidpi.app.ui.fragment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import cn.androidpi.app.R
import cn.androidpi.app.databinding.FragmentNotesBinding
import cn.androidpi.app.ui.activity.TextNoteEditActivity
import cn.androidpi.app.ui.base.BaseFragment
import cn.androidpi.app.ui.base.BindLayout
import cn.androidpi.app.ui.base.RecyclerAdapter
import cn.androidpi.app.ui.viewholder.ErrorViewHolder
import cn.androidpi.app.ui.viewholder.TextNoteViewHolder
import cn.androidpi.app.ui.viewholder.items.ErrorItem
import cn.androidpi.app.viewmodel.NotesViewModel
import cn.androidpi.app.viewmodel.vo.Resource
import cn.androidpi.app.viewmodel.ViewModelFactory
import cn.androidpi.note.entity.TextNote
import cn.androidpi.note.model.TextNotesModel
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
        mBinding.recyclerNotes.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mBinding.recyclerNotes.adapter = mAdapter

        mBinding.btnAdd.setOnClickListener { v ->
            startActivityForResult(Intent(activity, TextNoteEditActivity::class.java), REQUEST_CODE_EDIT_TEXT_NOTE)
        }

        mNotesViewModel.mTextNotes.observe(this, object : Observer<Resource<List<TextNote>>> {

            override fun onChanged(t: Resource<List<TextNote>>?) {
                if (t == null || t.isError()) {
                    setGridColumn(1)
                    val errorMessage = if (t != null) t.message else "加载文本笔记失败"
                    mAdapter?.setPayloads(ErrorItem(errorMessage))
                } else {
                    if (t.data == null || t.data.isEmpty()) {
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
        (mBinding.recyclerNotes.layoutManager as StaggeredGridLayoutManager).spanCount = count
    }

    override fun getAllTextNotes() {
        mNotesViewModel.getAllTextNotes()
    }

}