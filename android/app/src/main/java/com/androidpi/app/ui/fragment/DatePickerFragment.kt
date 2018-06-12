package com.androidpi.app.ui.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.DatePicker
import java.util.*

/**
 * Created by jastrelax on 2017/11/9.
 */

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener{

    companion object {

        val KEY_TAG = "DatePickerFragment.KEY_TAG"

        fun newInstance(tag: String): DatePickerFragment {
            val args = Bundle()
            args.putString(KEY_TAG, tag)
            val fragment = DatePickerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    interface OnDateSetListener {
        fun onDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int)
    }

    private var mListener: OnDateSetListener? = null
    var mTag: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDateSetListener) {
            mListener = context
        }
        mTag = arguments?.getString(KEY_TAG)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(activity, this, year, month, day)
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        mListener?.onDateSet(mTag, year, month, dayOfMonth)
    }

}
