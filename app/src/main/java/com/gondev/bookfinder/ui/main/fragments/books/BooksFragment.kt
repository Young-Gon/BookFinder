package com.gondev.bookfinder.ui.main.fragments.books

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gondev.bookfinder.BR
import com.gondev.bookfinder.R
import com.gondev.bookfinder.databinding.BooksFragmentBinding
import com.gondev.bookfinder.ui.main.fragments.ExpandableDataBindingAdapter
import com.gondev.bookfinder.ui.search.startSearchActivityFromFragment
import com.gondev.bookfinder.util.EventObserver
import kotlinx.android.synthetic.main.fragment_books.*
import org.koin.androidx.viewmodel.ext.android.getViewModel


private const val REQUST_CODE_SEARCH = 1

/**
 * 도서 탭입니다
 * 도서 목록을 검색하고 [SearchActivity][com.gondev.bookfinder.ui.search.SearchActivity]를 호출합니다
 *
 * @see [MainActivity][com.gondev.bookfinder.ui.main.MainActivity]
 * @see [BooksViewModel]
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
        binding.recyclerView.adapter =
            ExpandableDataBindingAdapter(viewLifecycleOwner, BR.vm to binding.vm!!)

        binding.vm?.requestOpenSearchActivity?.observe(viewLifecycleOwner, EventObserver {
            (activity as? AppCompatActivity)?.startSearchActivityFromFragment(
                this@BooksFragment,
                REQUST_CODE_SEARCH,
                binding.textViewSearch
            )
        })

        binding.vm?.state?.observe(viewLifecycleOwner, { state ->
            if(state is Error)
                Toast.makeText(context, R.string.error_network,Toast.LENGTH_SHORT).show()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                REQUST_CODE_SEARCH -> {
                    // 도서 목록 검색을 시작하기 전에 확장 되어있는 카드뷰를 초기화 하자
                    (binding.recyclerView.adapter as ExpandableDataBindingAdapter).setExpandedPosition(
                        -1
                    )
                    binding.vm?.setQuery( data?.getStringExtra("keyword")?:"")
                }
                else ->
                    throw IllegalArgumentException("지원 하지 않는 코드입니다 (requestCode=$requestCode)")
            }
    }
}
