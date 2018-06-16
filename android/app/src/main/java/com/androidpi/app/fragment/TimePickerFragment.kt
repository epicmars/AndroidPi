package com.androidpi.app.fragment

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.TimePicker
import java.util.*

/**
 * Created by jastrelax on 2017/11/23.
 */
class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    interface TimePickerListener {
        fun onTimeSet(tag: String?, hourOfDay: Int, minute: Int)
    }

    companion object {
        val ARGS_TAG = "TimePickerFragment.TAG"
        fun newInstance(tag: String): TimePickerFragment {
            val args = Bundle()
            args.putString(ARGS_TAG, tag)
            val fragment = TimePickerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    var mTimePickerListener: TimePickerListener? = null
    var mTag: String? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is TimePickerListener) {
            mTimePickerListener = context
        }
        mTag = arguments?.getString(ARGS_TAG)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val now = Calendar.getInstance()
        val dialog = TimePickerDialog(activity, this, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true)
        return dialog
    }

    override fun onDetach() {
        super.onDetach()
        mTimePickerListener = null
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        mTimePickerListener?.onTimeSet(mTag, hourOfDay, minute)
    }
}