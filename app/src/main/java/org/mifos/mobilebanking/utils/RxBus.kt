package org.mifos.mobilebanking.utils

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


/*
 * Created by saksham on 31/July/2018
*/

object RxBus {

    @JvmStatic
    private val publisher = PublishSubject.create<Any>()

    @JvmStatic
    fun publish(event: Any) {
        publisher.onNext(event)
    }

    @JvmStatic
    fun<T> listen(eventType: Class<T>): Observable<T> {
        return publisher.ofType(eventType)
    }

}