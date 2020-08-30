package com.gondev.bookfinder.model.network

import java.io.PrintWriter
import java.io.StringWriter

/**
 * 네트워크 상태를 나타냅니다
 */
sealed class State {
	/**
	 * 네트워크 로딩중
	 */
	object Loading : State()

	/**
	 * 네트워크 로딩 성공
	 */
	object Success : State()

	/**
	 * 네트워크 로딩 실패
	 */
	class Error(val throwable: Throwable) : State(){
		fun getStackTrace(): String {
			val stringWriter = StringWriter()
			throwable.printStackTrace(PrintWriter(stringWriter))
			return stringWriter.toString()
		}
	}

	companion object{
		fun loading() = Loading

		fun success() = Success

		fun error(throwable: Throwable) = Error(throwable)
	}
}
