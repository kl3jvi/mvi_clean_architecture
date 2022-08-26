package com.kl3jvi.common

import app.cash.turbine.test
import com.kl3jvi.common.result.Result
import com.kl3jvi.common.result.asResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals


/**
 *  A test case for the [Result] class.
 */


class ResultKtTest {
    @Test
    fun Result_catches_errors() = runTest {
        flow {
            emit(1)
            throw Exception("Test Done")
        }.asResult()
            .test {
                assertEquals(Result.Loading, awaitItem())
                assertEquals(Result.Success(1), awaitItem())

                when (val errorResult = awaitItem()) {
                    is Result.Error -> assertEquals(
                        "Test Done",
                        errorResult.exception?.message
                    )
                    Result.Loading,
                    is Result.Success -> throw IllegalStateException(
                        "The flow should have emitted an Error Result"
                    )
                }
                awaitComplete()
            }
    }
}