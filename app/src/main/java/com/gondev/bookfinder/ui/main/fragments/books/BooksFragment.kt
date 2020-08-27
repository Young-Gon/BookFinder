package com.gondev.bookfinder.ui.main.fragments.books

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import com.gondev.bookfinder.BR
import com.gondev.bookfinder.R
import com.gondev.bookfinder.databinding.BookItemBinding
import com.gondev.bookfinder.databinding.BooksFragmentBinding
import com.gondev.bookfinder.model.database.entity.BookEntity
import com.gondev.bookfinder.ui.RecyclerViewBindingAdapter
import org.koin.androidx.viewmodel.ext.android.getViewModel


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
    }
}