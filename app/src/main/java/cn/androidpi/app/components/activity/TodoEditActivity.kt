package cn.androidpi.app.components.activity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.Editable
import android.text.TextWatcher
import android.widget.SeekBar
import cn.androidpi.app.R
import cn.androidpi.app.components.base.BaseActivity
import cn.androidpi.app.components.fragment.DatePickerFragment
import cn.androidpi.app.components.fragment.TimePickerFragment
import cn.androidpi.app.databinding.ActivityTodoEditBinding
import cn.androidpi.app.view.TodoEditView
import cn.androidpi.app.viewmodel.TodoEditViewModel
import cn.androidpi.app.widget.todo.TodoDateTimeView.Companion.TAG_DEADLINE
import cn.androidpi.app.widget.todo.TodoDateTimeView.Companion.TAG_START_TIME
import cn.androidpi.common.color.ColorModel
import cn.androidpi.common.color.HSV
import kotlinx.android.synthetic.main.activity_todo_edit.*
import java.util.*
import javax.inject.Inject

class TodoEditActivity : BaseActivity(), TodoEditView, DatePickerFragment.OnDateSetListener,
    TimePickerFragment.TimePickerListener{

    lateinit var mViewModel: TodoEditViewModel

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    var mBinding: ActivityTodoEditBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_todo_edit)
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(TodoEditViewModel::class.java)
        mBinding?.etContent?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateTodoContent(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        mBinding?.btnCommit?.setOnClickListener {
            if (!mViewModel.isValidContent()) {
                Snackbar.make(btn_commit, R.string.todo_tip_content_is_blank, Snackbar.LENGTH_SHORT)
                        .show()
            } else if (!mViewModel.isValidDate()) {
                Snackbar.make(btn_commit, R.string.todo_top_start_later_than_deadline, Snackbar.LENGTH_SHORT)
                        .show()
            } else {
                commitTodoItem()
                setResult(Activity.RESULT_OK)
                finish()
            }
        }

        // priority selection
        mBinding?.sbPriority?.max = 180
        mBinding?.sbPriority?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            val hsv = HSV()

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                seekBar?.setBackgroundColor(ColorModel.hsvToRgb(hsv.set(180 - progress, 0.78f, 1f), alpha = 255))
//                mBinding?.tvPriority?.setBackgroundColor()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        mViewModel.mStartDate.observe(this, Observer {
            it?.let {
                mBinding?.todoStartTime?.setDate(it)
            }
        })

        mViewModel.mStartTime.observe(this, Observer {
            it?.let {
                mBinding?.todoStartTime?.setTime(it)
            }
        })

        mViewModel.mDeadlineDate.observe(this, Observer {
            it?.let {
                mBinding?.todoDeadline?.setDate(it)
            }
        })

        mViewModel.mDeadlineTime.observe(this, Observer {
            it?.let {
                mBinding?.todoDeadline?.setTime(it)
            }
        })
    }

    override fun onDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val now = Calendar.getInstance()
        now.set(year, month, dayOfMonth)
        when (tag) {
            TAG_START_TIME -> {
                updateStartDate(now.time)
            }

            TAG_DEADLINE -> {
                updateDeadlineDate(now.time)
            }
        }
    }

    override fun onTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val now = Calendar.getInstance()
        now.set(Calendar.HOUR_OF_DAY, hourOfDay)
        now.set(Calendar.MINUTE, minute)
        when (tag) {
            TAG_START_TIME -> {
                updateStartTime(now.time)
            }

            TAG_DEADLINE -> {
                updateDeadlineTime(now.time)
            }
        }
    }

    override fun updateStartDate(startDate: Date) {
        mViewModel.updateStartDate(startDate)
    }

    override fun updateStartTime(startTime: Date) {
        mViewModel.updateStartTime(startTime)
    }

    override fun updateDeadlineDate(deadline: Date) {
        mViewModel.updateDeadlineDate(deadline)
    }

    override fun updateDeadlineTime(deadlineTime: Date) {
        mViewModel.updateDeadlineTime(deadlineTime)
    }

    override fun updateTodoContent(content: String) {
        mViewModel.mTodoContent.value = content
    }

    override fun commitTodoItem() {
        mViewModel.addTodoItem()
    }
}
