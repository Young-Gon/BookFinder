package com.gondev.bookfinder.ui.main.fragments.books

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import com.gondev.bookfinder.BR
import com.gondev.bookfinder.R
import com.gondev.bookfinder.databinding.BookItemBinding
import com.gondev.bookfinder.databinding.BooksFragmentBinding
import com.gondev.bookfinder.model.database.entity.BookEntity
import com.gondev.bookfinder.ui.BindingViewHolder
import com.gondev.bookfinder.ui.DataBindingAdapter
import com.gondev.bookfinder.ui.search.startActivityFromFragment
import com.gondev.bookfinder.util.EventObserver
import kotlinx.android.synthetic.main.fragment_books.*
import org.koin.androidx.viewmodel.ext.android.getViewModel


private const val REQUST_CODE_SEARCH = 1

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
        binding.recyclerView.adapter = object : DataBindingAdapter<BookEntity, BookItemBinding>(
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
        ){
            private var expandedPosition = -1

            override fun onBindViewHolder(
                holder: BindingViewHolder<BookEntity, BookItemBinding>,
                position: Int
            ) {
                super.onBindViewHolder(holder, position)
                if (position == expandedPosition) {
                    holder.binding.textVeweDescription.visibility = View.VISIBLE
                } else {
                    holder.binding.textVeweDescription.visibility = View.GONE
                }
                holder.binding.root.setOnClickListener {
                    expandedPosition = if (expandedPosition == position) -1 else {
                        notifyItemChanged(expandedPosition)
                        position
                    }
                    notifyItemChanged(position)
                }
            }
        }

        binding.vm?.requestOpenSearchActivity?.observe(viewLifecycleOwner, EventObserver {
            (activity as? AppCompatActivity)?.startActivityFromFragment(
                this@BooksFragment,
                REQUST_CODE_SEARCH,
                textViewSearch
            )
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                REQUST_CODE_SEARCH ->
                    binding.vm?.query?.value = data?.getStringExtra("keyword")
                else ->
                    throw IllegalArgumentException("지원 하지 않는 코드입니다 (requestCode=$requestCode)")
            }
    }
}
