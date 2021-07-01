package com.example.http

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.http.databinding.FragmentBooksListBinding
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class BooksListFragment : InternetFragment(), CoroutineScope {
    private val booksList = mutableListOf<Book>()
    private var adapter: ArrayAdapter<Book>? = null
    private var _binding: FragmentBooksListBinding? = null
    private val binding get() = _binding!!
    private lateinit var job: Job
    private var downloadJob: Job? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBooksListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BookListAdapter(requireContext(), booksList)

        binding.listView.emptyView = binding.txtMessage
        binding.listView.adapter = adapter

        if (booksList.isNotEmpty()) {
            showProgress(false)
        } else {
            startDownload()
        }
    }

    override fun startDownload() {
        if (downloadJob == null) {
            if (BookHttp.hasConnection(requireContext())) {
                startDownloadJson()
            } else {
                binding.progressBar.visibility = View.GONE
                binding.txtMessage.setText(R.string.error_no_connection)
            }
        } else if (downloadJob?.isActive == true) {
            showProgress(true)
        }
    }

    private fun showProgress(show: Boolean) {
        if (show) {
            binding.txtMessage.setText(R.string.message_progress)
        }
        binding.txtMessage.visibility = if (show) View.VISIBLE else View.GONE
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun startDownloadJson() {
        downloadJob = launch {
            showProgress(true)
            val booksTask = withContext(Dispatchers.IO) {
                BookHttp.loadBooksGson()
            }
            updateBookList(booksTask)
            showProgress(false)
        }
    }

    private fun updateBookList(result: List<Book>?) {
        if (result != null) {
            booksList.clear()
            booksList.addAll(result)
        } else {
            binding.txtMessage.setText(R.string.error_load_books)
        }
        adapter?.notifyDataSetChanged()
        downloadJob = null
    }
}