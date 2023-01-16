package com.kl3jvi.benchmark

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.By
import com.kl3jvi.benchmark.utils.clickOnFirstItem
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep

@LargeTest
@RunWith(AndroidJUnit4::class)
class ClickBenchmarks {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun recyclerViewItemClick() {
        var firstStart = true
        benchmarkRule.measureRepeated(
            packageName = TARGET_PACKAGE,
            metrics = listOf(FrameTimingMetric()),
            compilationMode = CompilationMode.Full(),
            startupMode = null,
            iterations = 5,
            setupBlock = {
                if (firstStart) {
                    startActivityAndWait()
                    firstStart = false
                }
            }
        ) {
            val favoriteButton = device.findObject(By.res(packageName, "ib_restaurant_save"))
            sleep(300)
            favoriteButton.click()
        }
    }

    @Test
    fun recyclerViewFavoriteItemClick() {
        var firstStart = true
        benchmarkRule.measureRepeated(
            packageName = TARGET_PACKAGE,
            metrics = listOf(FrameTimingMetric()),
            compilationMode = CompilationMode.Full(),
            startupMode = null,
            iterations = 5,
            setupBlock = {
                if (firstStart) {
                    startActivityAndWait()
                    firstStart = false
                }
            }
        ) {
            clickOnFirstItem(withText = "Lunchpakketdienst")
        }
    }
}
