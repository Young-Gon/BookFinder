package com.gondev.bookfinder.ui.main.fragments

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import com.gondev.bookfinder.BR
import com.gondev.bookfinder.R
import com.gondev.bookfinder.databinding.BookItemBinding
import com.gondev.bookfinder.model.database.entity.BookEntity
import com.gondev.bookfinder.ui.BindingViewHolder
import com.gondev.bookfinder.ui.DataBindingAdapter
import timber.log.Timber

/**
 * 원래 [DataBindingAdapter]는 데이터바인딩을 위해 만들어진 [RecyclerView.Adapter][androidx.recyclerview.widget.RecyclerView.Adapter]이다
 * item layout의 화면을 구성하고 사용자 요청을 vm으로 전달한다
 * 하지만 가끔씩이 아답터가 처리 하지 못하는 상황이 발생하는데,
 * 지금과 같이 item 내부에서 adapter가 필요한 경우나 payload를 처리 해야 하는 경우이다
 * 이경우는 사용자의 클릭으로 레이아웃의 모양이 변하게 되고
 * 이를 adapter에서 통지하여 아래에 있는 아이템들의 위치를 변경 시켜 줘야 한다
 * 이럴 경우 [notifyItemChanged] 함수를 호출하기 위해 adapter가 필요 한데
 * item layout에서 들고 있을 방법이 없다
 * 이런경우 어쩔 수 없이 DataBindingAdapter를 직접 상속 받아 필요한 부분을 수정해 줘야 한다
 */
class ExpandableDataBindingAdapter(
    lifecycleOwner: LifecycleOwner? = null,
    vararg param: Pair<Int, Any>
) : DataBindingAdapter<BookEntity, BookItemBinding>(
    layoutResId = R.layout.item_book,
    bindingVariableId = BR.book,
    diffCallback = object : DiffUtil.ItemCallback<BookEntity>() {
        override fun areItemsTheSame(oldItem: BookEntity, newItem: BookEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: BookEntity, newItem: BookEntity) =
            oldItem == newItem
    },
    lifecycleOwner = lifecycleOwner,
    param = param,
) {
    /**
     * 확장된 카드뷰가 있는 위치
     * 이 위치를 기준으로 확장된 카드뷰를 그리기 때문에
     * 리사이클러뷰를 스크롤해도 엉뚱한 카드뷰가 확장 되지 않는다
     */
    private var expandedPosition = -1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindingViewHolder<BookEntity, BookItemBinding> =
        super.onCreateViewHolder(parent, viewType).also { holder ->
            holder.binding.root.setOnClickListener {
                Timber.d("position=$holder.adapterPosition")
                // 책장 리스트에서 아이템이 추가/삭제 되는 경우
                // onBindViewHolder에서 넘어 온 position과
                // 위치 값이 틀려지게 된다
                // 정확한 위치값은 holder.adapterPosition에 있다
                setExpandedPosition(holder.adapterPosition)
            }
        }

    override fun onBindViewHolder(
        holder: BindingViewHolder<BookEntity, BookItemBinding>,
        position: Int
    ) {
        super.onBindViewHolder(holder, position)

        if (position == expandedPosition) {
            holder.binding.expandableLayout.visibility = View.VISIBLE
        } else {
            holder.binding.expandableLayout.visibility = View.GONE
        }
    }

    fun setExpandedPosition(position: Int) {
        expandedPosition = if (expandedPosition == position) {
            -1
        } else {
            notifyItemChanged(expandedPosition)
            position
        }
        notifyItemChanged(position)
    }
}