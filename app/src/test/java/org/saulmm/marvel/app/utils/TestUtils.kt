package org.saulmm.marvel.app.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*

/**
 * Replaces the main dispatcher by we can control when testing.
 * This is handy when testing view-models, as they use by default the Main dispatcher.
 * Running a view-model test without this function might produce issues when asserting the init {} method.
 */
fun runViewModelTest(
    testBody: suspend TestScope.() -> Unit
) {
    runTest {
        val dispatcher = StandardTestDispatcher(this.testScheduler)
        Dispatchers.setMain(dispatcher)
        testBody()
        Dispatchers.resetMain()
    }
}