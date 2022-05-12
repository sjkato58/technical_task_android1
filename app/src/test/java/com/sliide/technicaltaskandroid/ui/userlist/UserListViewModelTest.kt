package com.sliide.technicaltaskandroid.ui.userlist

import androidx.lifecycle.Observer
import androidx.navigation.NavController
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
internal class UserListViewModelTest {

    private val testCoroutineDispatcher = StandardTestDispatcher()

    lateinit var viewModel: UserListViewModel

    private val userRepository = mockk<UserRepository>()

    private val exampleUser = UserModel(id = EXAMPLE_USERID, name = EXAMPLE_USERNAME, email = EXAMPLE_USEREMAIL)
    private val exampleUser2 = UserModel(id = EXAMPLE_USERID2, name = EXAMPLE_USERNAME2, email = EXAMPLE_USEREMAIL2)
    private val exampleUser3 = UserModel(id = EXAMPLE_USERID3, name = EXAMPLE_USERNAME3, email = EXAMPLE_USEREMAIL3)
    private val exampleUserList = listOf(exampleUser, exampleUser2, exampleUser3)
    private val exampleUserState = UserListViewState(id = EXAMPLE_USERID, name = EXAMPLE_USERNAME, email = EXAMPLE_USEREMAIL)

    companion object {
        const val VALUE_ONCE = 1

        const val EXAMPLE_USERID = 100
        const val EXAMPLE_USERNAME = "Sid"
        const val EXAMPLE_USEREMAIL = "sid.james@carryon.co.uk"
        const val EXAMPLE_USERID2 = 101
        const val EXAMPLE_USERNAME2 = "Kenneth"
        const val EXAMPLE_USEREMAIL2 = "kenneth.williams@carryon.co.uk"
        const val EXAMPLE_USERID3 = 102
        const val EXAMPLE_USERNAME3 = "Joan"
        const val EXAMPLE_USEREMAIL3 = "joan.sims@carryon.co.uk"

        const val EXAMPLE_ERROR_RESPONSE = "An unknown error has occurred, please try again later"
    }

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testCoroutineDispatcher)

        viewModel = UserListViewModel(
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
    fun `when attempting to download the user list then confirm that the repository attempts to download the list`() {
        coEvery {
            userRepository.downloadUserList()
        } returns ApiResponse.Success(exampleUserList)

        runTest {
            viewModel.downloadUserList()
        }

        coVerify(exactly = VALUE_ONCE) {
            userRepository.downloadUserList()
        }
    }

    @Test
    fun `when attempting to download the user list then confirm that a complete character list is obtained`() {
        val slot = slot<List<UserListViewState>>()
        val responsesList = mutableListOf<List<UserListViewState>>()
        val observer = mockk<Observer<List<UserListViewState>>>()
        coEvery {
            userRepository.downloadUserList()
        } returns ApiResponse.Success(exampleUserList)
        coEvery {
            observer.onChanged(capture(slot))
        } answers {
            responsesList.add(slot.captured)
        }
        viewModel.userList.observeForever(observer)

        runTest {
            viewModel.downloadUserList()
        }

        print("This is the responsesList: $responsesList")

        coVerify(exactly = VALUE_ONCE) {
            userRepository.downloadUserList()
        }
        assertTrue(responsesList.size > 0)
        assertTrue(responsesList[0][0].isLoading)
        assertTrue(responsesList[1][0].id == EXAMPLE_USERID)
        assertTrue(responsesList[1][0].name == EXAMPLE_USERNAME)

        viewModel.userList.removeObserver(observer)
    }

    @Test
    fun `when attempting to download the user list but an error occurs then the error should be sent back`() {
        val slot = slot<List<UserListViewState>>()
        val responsesList = mutableListOf<List<UserListViewState>>()
        val observer = mockk<Observer<List<UserListViewState>>>()
        coEvery {
            userRepository.downloadUserList()
        } returns ApiResponse.Error(EXAMPLE_ERROR_RESPONSE)
        coEvery {
            observer.onChanged(capture(slot))
        } answers {
            responsesList.add(slot.captured)
        }
        viewModel.userList.observeForever(observer)

        runTest {
            viewModel.downloadUserList()
        }

        print("This is the responsesList: $responsesList")

        coVerify(exactly = VALUE_ONCE) {
            userRepository.downloadUserList()
        }
        assertTrue(responsesList.size > 0)
        assertTrue(responsesList[0][0].isLoading)
        assertTrue(responsesList[1][0].showError)
        assertFalse(responsesList[1][0].errorMessage.isEmpty())
        assertTrue(responsesList[1][0].errorMessage == EXAMPLE_ERROR_RESPONSE)

        viewModel.userList.removeObserver(observer)
    }

    @Test
    fun `when attempting to open page to add a new user then the navigation event will be fired off`() {
        val slot = slot<NavController.() -> Any>()
        val observer = mockk<Observer<NavController.() -> Any>>()
        every {
            observer.onChanged(capture(slot))
        } answers {
            slot.captured
        }
        viewModel.navigationEvent.observeForever(observer)

        viewModel.navigateToAddUser()

        print("This is the slot: ${slot.captured}")

        assertTrue(slot.captured is NavController.() -> Any)

        viewModel.navigationEvent.removeObserver(observer)
    }

    @Test
    fun `when attempting to open page to remove a user then the navigation event will be fired off`() {
        val slot = slot<NavController.() -> Any>()
        val observer = mockk<Observer<NavController.() -> Any>>()
        every {
            observer.onChanged(capture(slot))
        } answers {
            slot.captured
        }
        viewModel.navigationEvent.observeForever(observer)

        viewModel.navigateToDeleteUser(exampleUserState)

        print("This is the slot: ${slot.captured}")

        assertTrue(slot.captured is NavController.() -> Any)

        viewModel.navigationEvent.removeObserver(observer)
    }
}