package com.gondev.bookfinder.ui.main.fragments.bookshelf

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import com.gondev.bookfinder.model.database.dao.BookDAO
import com.gondev.bookfinder.model.database.entity.BookEntity
import com.gondev.bookfinder.util.Event
import kotlinx.coroutines.launch

/**
 * 책장 목록입니다
 * 내가 구입한 책의 목록을 보여줍니다
 */
open class BookShelfViewModel(
    val dao: BookDAO,
) : ViewModel() {

    /**
     * 책장 목록입니다
     */
    open val books = LivePagedListBuilder(dao.findOwnedBooks(), 20)
        .build()

    // 삭제된 확장 아이템의 위치를 초기화 요청
    // 책장에서 삭제 되었을 경우, 카드뷰가 확장 되어 있는 상태에서 삭제 된다
    // 이럴 때 expandedPosition 변수를 초기화 해주지 않으면
    // 그위치에 다음 카드가 오게 되고 오작동 하게 된다
    val requestInitiateExpendedPositionWhenItemRemoved = MutableLiveData<Event<Boolean>>()
    fun onClickBuy(clickedItem: BookEntity) {
        requestInitiateExpendedPositionWhenItemRemoved.value= Event(!clickedItem.isOwned)
        viewModelScope.launch {
            // 디비 Entity를 직접 수정하시 마세요
            // 직접 수정하게 되면 레퍼런스 참조에 의해
            // 리스트의 값도 같이 바뀌게 되어 DiffUtil이 변경사항을 확인 할 수 없게 됩니다
            // Entity의 필드는 최대한 val의 형태로 선언 해야 합니다
            // https://stackoverflow.com/questions/54493764/pagedlistadapter-does-not-update-list-if-just-the-content-of-an-item-changes
            dao.update(clickedItem.copy(isOwned = !clickedItem.isOwned))
        }
    }
}