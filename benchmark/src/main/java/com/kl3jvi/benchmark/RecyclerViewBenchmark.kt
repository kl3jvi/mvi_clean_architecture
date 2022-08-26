package com.kl3jvi.benchmark

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)

class RecyclerViewBenchmark {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    /* A benchmark test for scrolling the recycler view. */
    @Test
    fun recyclerViewScroll() {
        benchmarkRule.measureRepeated(
            packageName = TARGET_PACKAGE,
            metrics = listOf(FrameTimingMetric()),
            compilationMode = CompilationMode.None(),
            startupMode = StartupMode.WARM,
            iterations = 3,
            setupBlock = {
                startActivityAndWait()
            }
        ) {
            val recycler = device.findObject(By.res(packageName, "restaurantRv"))
            // Set gesture margin to avoid triggering gesture navigation
            // with input events from automation.
            recycler.setGestureMargin(device.displayWidth / 5)

            // Scroll down several times
            repeat(3) {
                recycler.fling(Direction.DOWN)
                recycler.fling(Direction.UP)
            }
        }
    }
}
