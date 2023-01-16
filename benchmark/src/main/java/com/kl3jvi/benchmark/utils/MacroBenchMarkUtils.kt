package com.kl3jvi.benchmark.utils

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until

fun MacrobenchmarkScope.clickOnFirstItem(withText: String) {
    clickOnText(withText)
    waitForTextShown("Description")
    // go back at home
    device.pressBack()
    waitForTextGone("Description")
}

fun MacrobenchmarkScope.waitForTextShown(text: String) {
    check(device.wait(Until.hasObject(By.text(text)), 500)) {
        "View showing '$text' not found after waiting 500 ms."
    }
}

fun MacrobenchmarkScope.waitForTextGone(text: String) {
    check(device.wait(Until.gone(By.text(text)), 500)) {
        "View showing '$text' not found after waiting 500 ms."
    }
}

fun MacrobenchmarkScope.clickOnText(text: String) {
    device.findObject(By.text(text))
        .click()
    Thread.sleep(100)
}
