package com.levifreire.atividadescomplementares

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.levifreire.atividadescomplementares.databinding.FragmentTaskDetailsBinding

class TaskDetailsFragment : Fragment(), TaskDetailsView {
    private var taskDetailsBinding: FragmentTaskDetailsBinding? = null
    private val presenter = TaskDetailsPresenter(this, MemoryRepository)
    private var task: Task? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTaskDetailsBinding.inflate(inflater, container, false)
        taskDetailsBinding = binding

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        taskDetailsBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.loadTaskDetails(arguments?.getInt(TASK_ID, -1) ?: -1)
    }

    override fun showTaskDetails(task: Task) {
        this.task = task
        taskDetailsBinding?.txtTitle?.text = task.title
        taskDetailsBinding?.txtDate?.text = task.date
        taskDetailsBinding?.txtTime?.text = task.time
        taskDetailsBinding?.txtLocal?.text = task.local
        taskDetailsBinding?.txtDescription?.text = task.description
        taskDetailsBinding?.txtCategory?.text = task.category
        taskDetailsBinding?.txtWorkload?.text = task.workload.toString()
    }

    override fun errorTaskNotFound() {
        taskDetailsBinding?.txtLabelTitle?.text = getString(R.string.error_task_not_found)
        taskDetailsBinding?.txtLabelDate?.visibility = View.GONE
        taskDetailsBinding?.txtLabelTime?.visibility = View.GONE
        taskDetailsBinding?.txtLabelLocal?.visibility = View.GONE
        taskDetailsBinding?.txtLabelDescription?.visibility = View.GONE
        taskDetailsBinding?.txtLabelCategory?.visibility = View.GONE
        taskDetailsBinding?.txtLabelWorkload?.visibility = View.GONE

        taskDetailsBinding?.txtTitle?.visibility = View.GONE
        taskDetailsBinding?.txtDate?.visibility = View.GONE
        taskDetailsBinding?.txtTime?.visibility = View.GONE
        taskDetailsBinding?.txtLocal?.visibility = View.GONE
        taskDetailsBinding?.txtDescription?.visibility = View.GONE
        taskDetailsBinding?.txtCategory?.visibility = View.GONE
        taskDetailsBinding?.txtWorkload?.visibility = View.GONE
    }

    companion object {
        const val TAG_DETAILS = "tagDetalhe"
        private const val TASK_ID = "taskId"

        fun newInstance(id: Int) = TaskDetailsFragment().apply {
            arguments = Bundle().apply {
                putInt(TASK_ID, id)
            }
        }
    }
}