package com.levifreire.navegacao

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class SecondLevelFragment : Fragment() {

    private lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_second_level, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textView = view.findViewById(R.id.textView)

        arguments?.getString(EXTRA_TEXT)?.let {
            textView.text = it
        }

        arguments?.getInt(EXTRA_TEXT_COLOR)?.let {
            textView.setTextColor(it)
        }

        arguments?.getInt(EXTRA_BG_COLOR)?.let {
            view.setBackgroundColor(it)
        }
    }

    companion object {
        private const val EXTRA_TEXT = "texto"
        private const val EXTRA_BG_COLOR = "corBg"
        private const val EXTRA_TEXT_COLOR = "corTexto"

        fun newInstance(text: String, background: Int, textColor: Int): SecondLevelFragment {
            val params = Bundle()
            params.putString(EXTRA_TEXT, text)
            params.putInt(EXTRA_BG_COLOR, background)
            params.putInt(EXTRA_TEXT_COLOR, textColor)
            val slf = SecondLevelFragment()
            slf.arguments = params
            return slf
        }
    }
}