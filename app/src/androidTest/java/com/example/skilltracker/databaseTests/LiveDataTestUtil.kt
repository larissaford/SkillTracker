package com.example.skilltracker.databaseTests

import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * A utility class that is used to get the value of data that is returned as LiveData
 */
class LiveDataTestUtil {
    @Suppress("UNCHECKED_CAST")
    companion object {
        @Throws(InterruptedException::class)
        fun <T> getValue(liveData: LiveData<T>): T? {
            val data = arrayOfNulls<Any>(1)
            val latch = CountDownLatch(1)
            val observer: Observer<T> = object: Observer<T> {
                override fun onChanged(@Nullable o: T) {
                    data[0] = o
                    latch.countDown()
                    liveData.removeObserver(this)
                }
            }
            liveData.observeForever(observer)
            latch.await(2, TimeUnit.SECONDS)
            return data[0] as T?
        }
    }
}