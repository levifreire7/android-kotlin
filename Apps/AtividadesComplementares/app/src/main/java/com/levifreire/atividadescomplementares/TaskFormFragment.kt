package com.levifreire.atividadescomplementares

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.levifreire.atividadescomplementares.databinding.FragmentTaskFormBinding
import java.text.SimpleDateFormat
import java.util.*


class TaskFormFragment : Fragment(), TaskFormView {

    private var fragmentBinding: FragmentTaskFormBinding? = null
    private val presenter = TaskFormPresenter(this, MemoryRepository)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTaskFormBinding.bind(view)
        fragmentBinding = binding

        val taskId = arguments?.getInt(TASK_ID, 0) ?: 0
        presenter.loadTask(taskId)

        initSpinner()
        presenter.initFields()

        binding.buttonSave.setOnClickListener {
            saveTask()
        }
    }

    private fun initSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.categories)
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        fragmentBinding?.edtCategory?.adapter = adapter
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }

    override fun showTask(task: Task) {
        fragmentBinding?.edtTitle?.setText(task.title)
        fragmentBinding?.edtDate?.setText(task.date)
        fragmentBinding?.edtTime?.setText(task.time)
        fragmentBinding?.edtLocal?.setText(task.local)
        fragmentBinding?.edtDescription?.setText(task.description)
        fragmentBinding?.edtWorkload?.setText(task.workload)

        val index = resources.getStringArray(R.array.categories).indexOfFirst { it == task.category }
        fragmentBinding?.edtCategory?.setSelection(index)
    }

    override fun errorInvalidTask() {
        Toast.makeText(requireContext(), R.string.error_invalid_task, Toast.LENGTH_SHORT).show()
    }

    override fun errorSaveTask() {
        Toast.makeText(requireContext(), R.string.error_task_not_found, Toast.LENGTH_SHORT).show()
    }

    override fun initFields(date: String, hour: String) {
        fragmentBinding?.edtDate?.setText(date)
        fragmentBinding?.edtTime?.setText(hour)
    }

    companion object {
        private const val TASK_ID = "taskId"
    }

    private fun saveTask() {
        val task = Task()
        val taskId = arguments?.getInt(TASK_ID, 0) ?: 0
        task.id = taskId
        task.title = fragmentBinding?.edtTitle?.text.toString()
        task.date = fragmentBinding?.edtDate?.text.toString()
        task.time = fragmentBinding?.edtTime?.text.toString()
        task.local = fragmentBinding?.edtLocal?.text.toString()
        task.description = fragmentBinding?.edtDescription?.text.toString()
        task.category = fragmentBinding?.edtCategory?.selectedItem.toString()
        task.workload = fragmentBinding?.edtWorkload?.text.toString().toIntOrNull() ?: 0

        if (presenter.saveTask(task)) {
            activity?.finish()
        }
    }
}