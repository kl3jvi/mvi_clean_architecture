//package com.kl3jvi.feature_home.viewModel
//
////import app.cash.turbine.test
////import com.kl3jvi.feature_home.common.MainDispatcherRule
////import com.kl3jvi.feature_home.home.SortOptions
////import com.kl3jvi.feature_home.shared.RestaurantUiState
////import com.kl3jvi.feature_home.shared.SharedViewModel
////import com.kl3jvi.feature_home.testdoubles.TestRestaurantRepository
////import kotlinx.coroutines.flow.collect
////import kotlinx.coroutines.launch
////import kotlinx.coroutines.test.StandardTestDispatcher
////import kotlinx.coroutines.test.TestDispatcher
////import kotlinx.coroutines.test.UnconfinedTestDispatcher
////import kotlinx.coroutines.test.runTest
////import org.junit.Before
////import org.junit.Rule
////import org.junit.Test
////import kotlin.test.assertEquals
//
//
//class SharedViewModelTest {
//
//    @get:Rule
//    val dispatcherRule = MainDispatcherRule()
//
//    private val repository = TestRestaurantRepository()
//
//    private lateinit var viewModel: SharedViewModel
//    private var testDispatcher: TestDispatcher = StandardTestDispatcher()
//
//
//    @Before
//    fun setup() {
//        viewModel = SharedViewModel(
//            repository = repository,
//            ioDispatcher = testDispatcher,
//            defaultDispatcher = testDispatcher
//        )
//    }
//
//    @Test
//    fun uiRestaurantState_whenInitialized_thenShowLoading() = runTest(testDispatcher) {
//        assertEquals(
//            RestaurantUiState.Loading,
//            viewModel.uiRestaurantState.value
//        )
//    }
//
//
//    @Test
//    fun uiRestaurantState_whenCollectSuccess_thenShowLoading() = runTest(testDispatcher) {
//        val collectJob =
//            launch(UnconfinedTestDispatcher()) { viewModel.uiRestaurantState.collect() }
//
//        repository.sendRestaurantList(emptyList())
//        assertEquals(RestaurantUiState.Loading, viewModel.uiRestaurantState.value)
//
//        collectJob.cancel()
//    }
//
//
//    @Test
//    fun update_SortingType_when_option_changed() = runTest(testDispatcher) {
//        viewModel.onSortOptionSelected(SortOptions.AVERAGE_PRODUCT_PRICE)
//        viewModel.sortOption.test { assertEquals(awaitItem(), SortOptions.AVERAGE_PRODUCT_PRICE) }
//    }
//
//
//}