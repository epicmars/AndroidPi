package cn.androidpi.app.components.activity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.Editable
import android.text.TextWatcher
import android.widget.SeekBar
import cn.androidpi.app.R
import cn.androidpi.app.components.base.BaseActivity
import cn.androidpi.app.components.fragment.DatePickerFragment
import cn.androidpi.app.databinding.ActivityTodoEditBinding
import cn.androidpi.app.view.TodoEditView
import cn.androidpi.app.viewmodel.TodoEditViewModel
import cn.androidpi.common.color.ColorModel
import cn.androidpi.common.color.HSV
import cn.androidpi.common.datetime.DateUtils
import kotlinx.android.synthetic.main.activity_todo_edit.*
import java.util.*
import javax.inject.Inject

class TodoEditActivity : BaseActivity(), TodoEditView, DatePickerFragment.OnDateSetListener {

    companion object {
        val TAG_START_TIME = "date-picker-starttime"
        val TAG_DEADLINE = "date-picker-deadline"
    }

    @Inject
    lateinit var mViewModel: TodoEditViewModel

    var mBinding: ActivityTodoEditBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_todo_edit)

        mBinding?.llStartTime?.setOnClickListener {
            DatePickerFragment.newInstance(TAG_START_TIME).show(supportFragmentManager, TAG_START_TIME)
        }

        mBinding?.llDeadline?.setOnClickListener {
            DatePickerFragment.newInstance(TAG_DEADLINE).show(supportFragmentManager, TAG_DEADLINE)
        }

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

        mViewModel.mStartTime.observe(this, Observer {
            t -> mBinding?.tvStartTime?.text = DateUtils.formatStandard(t!!)
        })

        mViewModel.mDeadline.observe(this, Observer {
            t -> mBinding?.tvDeadline?.text = DateUtils.formatStandard(t!!)
        })

        val now = Date()
        updateStartTime(now)
        updateDeadline(now)
    }

    override fun onDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val date = DateUtils.formatStandard(calendar.time)
        when (tag) {
            TAG_START_TIME -> {
                mBinding?.tvStartTime?.setText(date)
                updateStartTime(calendar.time)
            }

            TAG_DEADLINE -> {
                mBinding?.tvDeadline?.setText(date)
                updateDeadline(calendar.time)
            }
        }
    }

    override fun updateStartTime(startTime: Date) {
        mViewModel?.mStartTime.value = startTime
    }

    override fun updateDeadline(deadline: Date) {
        mViewModel?.mDeadline.value = deadline
    }

    override fun updateTodoContent(content: String) {
        mViewModel?.mTodoContent.value = content
    }

    override fun commitTodoItem() {
        mViewModel?.addTodoItem()
    }
}
