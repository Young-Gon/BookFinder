package com.gondev.bookfinder.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import com.gondev.bookfinder.R
import com.gondev.bookfinder.BR
import com.gondev.bookfinder.databinding.KeywordItemBinding
import com.gondev.bookfinder.databinding.SearchActivityBinding
import com.gondev.bookfinder.model.database.entity.KeywordEntity
import com.gondev.bookfinder.ui.DataBindingAdapter
import com.gondev.bookfinder.ui.KeyboardVisibilityDelegation
import com.gondev.bookfinder.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.getViewModel

fun AppCompatActivity.startActivityFromFragment(
    fragment: Fragment,
    requestCode: Int,
    sharedElement: View
) {
    val option = ActivityOptionsCompat.makeSceneTransitionAnimation(
        this,
        sharedElement,
        ViewCompat.getTransitionName(sharedElement)!!
    )
    startActivityFromFragment(
        fragment,
        Intent(this, SearchActivity::class.java),
        requestCode,
        option.toBundle()
    )
}


class SearchActivity : AppCompatActivity() {

    val keyboard: KeyboardVisibilityDelegation by lazy {
        KeyboardVisibilityDelegation((getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: SearchActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_search)

        binding.editTextSearch.transitionName = "search"
        binding.vm = getViewModel()
        binding.lifecycleOwner = this
        binding.recyclerView.adapter = DataBindingAdapter<KeywordEntity, KeywordItemBinding>(
            layoutResId = R.layout.item_keyword,
            bindingVariableId = BR.keyword,
            diffCallback = object : DiffUtil.ItemCallback<KeywordEntity>() {
                override fun areItemsTheSame(oldItem: KeywordEntity, newItem: KeywordEntity) =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: KeywordEntity, newItem: KeywordEntity) =
                    oldItem == newItem

            },
            lifecycleOwner = this,
            BR.vm to binding.vm!!
        )

        binding.vm?.requestSetResultAndFinishActivity?.observe(this, EventObserver { keyword ->
            setResult(RESULT_OK, Intent().apply {
                putExtra("keyword", keyword)
            })
            // 역 트렌지션 수행
            supportFinishAfterTransition()
        })
    }

    override fun finish() {
        keyboard.hideKeyboard()

        super.finish()
    }

    override fun onBackPressed() {
        // 역 트렌지션 수행
        supportFinishAfterTransition()
    }
}