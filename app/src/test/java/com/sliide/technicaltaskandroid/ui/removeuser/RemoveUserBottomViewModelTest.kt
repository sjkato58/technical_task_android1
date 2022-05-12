package com.sliide.technicaltaskandroid.ui.removeuser

import androidx.lifecycle.Observer
import com.sliide.technicaltaskandroid.InstantExecutorExtension
import com.sliide.technicaltaskandroid.data.ApiResponse
import com.sliide.technicaltaskandroid.data.user.UserModel
import com.sliide.technicaltaskandroid.data.user.UserRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class RemoveUserBottomViewModelTest {

    private val testCoroutineDispatcher = StandardTestDispatcher()

    lateinit var viewModel: RemoveUserBottomViewModel

    private val userRepository = mockk<UserRepository>()

    companion object {
        const val VALUE_ONCE = 1

        const val EXAMPLE_USERID = 100
        const val EXAMPLE_USERNAME = "Sid James"
        const val EXAMPLE_USEREMAIL = "sid.james@carryon.co.uk"

        const val EXAMPLE_ERROR_RESPONSE = "Resource not found"
    }

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testCoroutineDispatcher)

        viewModel = RemoveUserBottomViewModel(
            userRepository
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `when attempting to remove a user then a successful response should be returned`(){
        val slot = slot<RemoveUserBottomViewState>()
        val responsesList = mutableListOf<RemoveUserBottomViewState>()
        val observer = mockk<Observer<RemoveUserBottomViewState>>()
        coEvery {
            userRepository.removeUser(EXAMPLE_USERID)
        } returns ApiResponse.Success(UserModel())
        coEvery {
            observer.onChanged(capture(slot))
        } answers {
            responsesList.add(slot.captured)
        }
        viewModel.removeUserBottomState.observeForever(observer)

        runTest {
            viewModel.saveUserData(EXAMPLE_USERID, EXAMPLE_USERNAME)
            viewModel.removeUser()
        }

        print("This is the responsesList: $responsesList")

        coVerify(exactly = VALUE_ONCE) {
            userRepository.removeUser(EXAMPLE_USERID)
        }
        assertTrue(responsesList.size > 0)
        assertTrue(responsesList[0].showLoading)
        assertTrue(responsesList[1].id == EXAMPLE_USERID)
        assertTrue(responsesList[1].name == EXAMPLE_USERNAME)

        viewModel.removeUserBottomState.removeObserver(observer)
    }

    @Test
    fun `when attempting to remove a user that does not exist then an error response should be returned`(){
        val slot = slot<RemoveUserBottomViewState>()
        val responsesList = mutableListOf<RemoveUserBottomViewState>()
        val observer = mockk<Observer<RemoveUserBottomViewState>>()
        coEvery {
            userRepository.removeUser(EXAMPLE_USERID)
        } returns ApiResponse.Error(EXAMPLE_ERROR_RESPONSE)
        coEvery {
            observer.onChanged(capture(slot))
        } answers {
            responsesList.add(slot.captured)
        }
        viewModel.removeUserBottomState.observeForever(observer)

        runTest {
            viewModel.saveUserData(EXAMPLE_USERID, EXAMPLE_USERNAME)
            viewModel.removeUser()
        }

        print("This is the responsesList: $responsesList")

        coVerify(exactly = VALUE_ONCE) {
            userRepository.removeUser(EXAMPLE_USERID)
        }
        assertTrue(responsesList.size > 0)
        assertTrue(responsesList[0].showLoading)
        assertTrue(responsesList[1].showError)
        assertFalse(responsesList[1].errorMessage.isEmpty())
        assertTrue(responsesList[1].errorMessage == EXAMPLE_ERROR_RESPONSE)

        viewModel.removeUserBottomState.removeObserver(observer)
    }
}