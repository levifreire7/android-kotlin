package com.levifreire.navegacao

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class FirstLevelFragment : Fragment() {
    private var navigationType: String? = null
    private var actions = mutableMapOf<String, Class<*>>()
    private var button: Button? = null
    private var textView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actions[getString(R.string.option_tab)] = TabsActivity::class.java
        actions[getString(R.string.option_bottom)] = BottomNavActivity::class.java
        actions[getString(R.string.option_pager)] = PagerActivity::class.java

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first_level, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button = view?.findViewById(R.id.button)
        textView = view?.findViewById(R.id.textView)

        navigationType = arguments?.getString(EXTRA_TYPE)
        button?.setOnClickListener {
            val key = navigationType
            val clazz = actions[key]
            startActivity(Intent(activity, clazz))
        }
        textView?.text = navigationType
    }

    companion object {
        private const val EXTRA_TYPE = "tipoNavegacao"
        fun newInstance(type: String): FirstLevelFragment {
            val params = Bundle()
            params.putString(EXTRA_TYPE, type)
            val f = FirstLevelFragment()
            f.arguments = params
            return f
        }
    }
}