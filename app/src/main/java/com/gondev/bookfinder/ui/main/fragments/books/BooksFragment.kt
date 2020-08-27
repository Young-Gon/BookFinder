package com.gondev.bookfinder.ui.main.fragments.books

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import com.gondev.bookfinder.BR
import com.gondev.bookfinder.R
import com.gondev.bookfinder.databinding.BookItemBinding
import com.gondev.bookfinder.databinding.BooksFragmentBinding
import com.gondev.bookfinder.model.database.entity.BookEntity
import com.gondev.bookfinder.ui.RecyclerViewBindingAdapter
import com.gondev.bookfinder.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * 서치바 엑션
 * https://medium.com/@alexstyl/https-medium-com-alexstyl-animating-the-toolbar-7a8f1aab39dd
 * 에니메이션
 * https://www.droidcon.com/news-detail?content-id=/repository/collaboration/Groups/spaces/droidcon_hq/Documents/public/news/android-news/Complex%20UI%20-%20Animations%20on%20Android
 */
class BooksFragment : Fragment() {

    lateinit var binding: BooksFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BooksFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.vm = getViewModel()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerView.adapter = RecyclerViewBindingAdapter<BookEntity, BookItemBinding>(
            layoutResId = R.layout.item_book,
            bindingVariableId = BR.book,
            diffCallback = object : DiffUtil.ItemCallback<BookEntity>() {
                override fun areItemsTheSame(oldItem: BookEntity, newItem: BookEntity) =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: BookEntity, newItem: BookEntity) =
                    oldItem == newItem
            },
            lifecycleOwner = viewLifecycleOwner,
            BR.vm to binding.vm!!,
        )

        binding.vm?.requestKeyboardHide?.observe(viewLifecycleOwner, EventObserver {
            if (it) {
                binding.editTextSearch.clearFocus()
                val inputMethodManager =
                    activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.editTextSearch.windowToken, 0)
            }
        })
    }
}