package com.example.http

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.http.databinding.FragmentBooksListBinding

class BooksListFragment : Fragment() {
    private var asyncTask: BooksDownloadTask? = null
    private val booksList = mutableListOf<Book>()
    private var adapter: ArrayAdapter<Book>? = null
    private var _binding: FragmentBooksListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
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
            if (asyncTask == null) {
                if (BookHttp.hasConnection(requireContext())) {
                    startDownloadJson()
                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.txtMessage.setText(R.string.error_no_connection)
                }
            } else if (asyncTask?.status == AsyncTask.Status.RUNNING) {
                showProgress(true)
            }
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
        if (asyncTask?.status != AsyncTask.Status.RUNNING) {
            asyncTask = BooksDownloadTask()
            asyncTask?.execute()
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
        asyncTask = null
    }

    inner class BooksDownloadTask : AsyncTask<Void, Void, List<Book>?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            showProgress(true)
        }

        override fun doInBackground(vararg strings: Void): List<Book>? {
            return BookHttp.loadBooks()
        }

        override fun onPostExecute(livros: List<Book>?) {
            super.onPostExecute(livros)
            showProgress(false)
            updateBookList(livros)
        }
    }
}