package com.kl3jvi.benchmark

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import com.kl3jvi.benchmark.utils.waitForTextGone
import com.kl3jvi.benchmark.utils.waitForTextShown
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep
import kotlin.concurrent.thread

@LargeTest
@RunWith(AndroidJUnit4::class)
class SortSelector {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun clickSortMenu() = benchmarkRule.measureRepeated(
        packageName = TARGET_PACKAGE,
        metrics = listOf(FrameTimingMetric()),
        compilationMode = CompilationMode.None(),
        startupMode = StartupMode.WARM,
        iterations = 1,
        setupBlock = {
            startActivityAndWait()
        }
    ) {
        val sortButton = device.findObject(By.res(packageName, "sort_action"))
        val recycler = device.findObject(By.res(packageName, "restaurantRv"))
        recycler.setGestureMargin(device.displayWidth / 5)


        repeat(3) {
            sortButton.click()
            sleep(1000)
            val sortByNewest = device.findObject(By.text("Newest"))
            waitForTextShown("Newest")
            sortByNewest.click()
            waitForTextGone("Newest")

            recycler.fling(Direction.DOWN)
            sleep(1000)

            sortButton.click()
            sleep(1000)
            val sortByDistance = device.findObject(By.text("Distance"))
            waitForTextShown("Distance")
            sortByDistance.click()
            waitForTextGone("Distance")

            recycler.fling(Direction.UP)
            sleep(1000)

            sortButton.click()
            sleep(1000)
            val sortByRatingAverage = device.findObject(By.text("Min cost"))
            waitForTextShown("Min cost")
            sortByRatingAverage.click()
            waitForTextGone("Min cost")


        }

    }
}