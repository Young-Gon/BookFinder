package com.gondev.bookfinder.ui.main.fragments.bookshelf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gondev.bookfinder.BR
import com.gondev.bookfinder.databinding.BooksShelfFragmentBinding
import com.gondev.bookfinder.ui.main.fragments.ExpandableDataBindingAdapter
import com.gondev.bookfinder.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * 책장탭 입니다
 * 구매한 책 목록을 표시 합니다
 *
 * @see [MainActivity][com.gondev.bookfinder.ui.main.MainActivity]
 * @see [BookShelfViewModel]
 */
class BookShelfFragment : Fragment() {

    lateinit var binding: BooksShelfFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BooksShelfFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.vm = getViewModel()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerView.adapter =
            ExpandableDataBindingAdapter(viewLifecycleOwner, BR.vm to binding.vm!!)

        binding.vm?.requestInitiateExpendedPositionWhenItemRemoved?.observe(
            viewLifecycleOwner,
            EventObserver { isOwen ->
                if(!isOwen)
                    (binding.recyclerView.adapter as ExpandableDataBindingAdapter).setExpandedPosition(-1)
            })
    }
}