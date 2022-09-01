package com.kl3jvi.benchmark

import androidx.benchmark.macro.ExperimentalBaselineProfilesApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import org.junit.Rule
import org.junit.Test


/**
 * Generates a baseline profile which can be copied to `app/src/main/baseline-prof.txt`.
 */
@ExperimentalBaselineProfilesApi
class BaselineProfileGenerator {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun startup() =
        baselineProfileRule.collectBaselineProfile(
            packageName = "com.kl3jvi.takeaway_task"
        ) {
            pressHome()
            // This block defines the app's critical user journey. Here we are interested in
            // optimizing for app startup. But you can also navigate and scroll
            // through your most important UI.
            startActivityAndWait()
            device.waitForIdle()

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