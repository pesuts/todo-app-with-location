package com.daniel.todoapp.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.daniel.todoapp.databinding.FragmentErrorDialogBinding

class ErrorDialogFragment : DialogFragment() {
    private lateinit var binding : FragmentErrorDialogBinding
    private var dialogListener : DialogListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentErrorDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var arguments = arguments
        if(arguments != null){
            val mArgs: Bundle = requireArguments()
            val value = mArgs.getString("MESSAGE")

            binding.message.setText(value.toString())
            binding.defaultMessage.visibility = View.GONE
            binding.message.visibility = View.VISIBLE
        }

        binding.btnSubmit.setOnClickListener {
            dialogListener?.onSubmit("")
            dialog?.dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val fragment = parentFragment
        if(fragment is HomeFragment){
        }
    }

    override fun onDetach() {
        super.onDetach()
        this.dialogListener = null
    }

    interface DialogListener{
        fun onSubmit(text: String)
    }

}