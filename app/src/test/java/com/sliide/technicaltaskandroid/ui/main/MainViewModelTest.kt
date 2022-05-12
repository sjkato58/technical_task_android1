package com.sliide.technicaltaskandroid.ui.main

import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.sliide.technicaltaskandroid.InstantExecutorExtension
import com.sliide.technicaltaskandroid.ui.userlist.UserListFragmentDirections
import com.sliide.technicaltaskandroid.utils.isEmailValid
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class MainViewModelTest {

    private val testCoroutineDispatcher = StandardTestDispatcher()

    lateinit var viewModel: MainViewModel

    companion object {
        const val VALUE_ONCE = 1

        const val EXAMPLE_USEREMAIL = "sid.james@carryon.co.uk"
        const val EXAMPLE_INVALIDUSEREMAIL = "carryon.co.uk"
    }

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testCoroutineDispatcher)

        viewModel = MainViewModel()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `when checking a proper email then you should get a valid response`() {
        val response = isEmailValid(EXAMPLE_USEREMAIL)

        assertTrue(response)
    }

    @Test
    fun `when checking an invalid email then you should get a valid response`() {
        val response = isEmailValid(EXAMPLE_INVALIDUSEREMAIL)

        assertFalse(response)
    }

    @Test
    fun `when attempting to navigate to a new page then the navigation event will be fired off`() {
        val slot = slot<NavController.() -> Any>()
        val observer = mockk<Observer<NavController.() -> Any>>()
        every {
            observer.onChanged(capture(slot))
        } answers {
            slot.captured
        }
        viewModel.navigationEvent.observeForever(observer)

        viewModel.navigateInDirection(UserListFragmentDirections.actionUserListFragmentToAddUserBottomFragment())

        print("This is the slot: ${slot.captured}")

        assertTrue(slot.captured is NavController.() -> Any)

        viewModel.navigationEvent.removeObserver(observer)
    }
}