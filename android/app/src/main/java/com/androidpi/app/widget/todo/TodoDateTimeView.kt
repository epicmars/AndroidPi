package com.androidpi.app.widget.todo

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.annotation.StringRes
import android.support.v4.app.FragmentActivity
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.androidpi.app.R
import com.androidpi.app.ui.fragment.DatePickerFragment
import com.androidpi.app.ui.fragment.TimePickerFragment
import com.androidpi.app.databinding.ViewTodoDatetimeBinding
import com.androidpi.common.datetime.DateTimeUtils
import java.util.*

/**
 * 待办事项开始时间/截止时间控件。
 * Created by jastrelax on 2017/11/23.
 */
class TodoDateTimeView : FrameLayout {

    companion object {
        val TAG_START_TIME = "picker-start"
        val TAG_DEADLINE   = "picker-deadline"
    }

    var mTag: String

    var mBinding: ViewTodoDatetimeBinding

    constructor(context: Context?) : this(context, null, 0)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_todo_datetime, this, true)

        val a = context?.theme?.obtainStyledAttributes(attrs, R.styleable.TodoDateTimeView, defStyleAttr, 0)
        // A tag must be set in the layout.
        mTag = a?.getString(R.styleable.TodoDateTimeView_tag)!!
        mBinding.tvDatetimeLabel.text = a.getString(R.styleable.TodoDateTimeView_label)

        mBinding.tvDate.setOnClickListener {
            DatePickerFragment.newInstance(mTag).show((context as FragmentActivity).supportFragmentManager, mTag + "DatePickerFragment")
        }

        mBinding.tvTime.setOnClickListener {
            TimePickerFragment.newInstance(mTag).show((context as FragmentActivity).supportFragmentManager, mTag + "TimePickerFragment")
        }

        a.recycle()
    }

    fun setDate(date: Date) {
        mBinding.tvDate.text = DateTimeUtils.formatDate(date)
    }

    fun setTime(time: Date) {
        mBinding.tvTime.text = DateTimeUtils.formatTime(time)
    }

    fun setLabel(label: String?) {
        mBinding.tvDatetimeLabel.text = label
    }

    fun setLabel(@StringRes label: Int) {
        mBinding.tvDatetimeLabel.setText(label)
    }
}