package com.sliide.technicaltaskandroid.ui.adduser

import androidx.lifecycle.Observer
import com.sliide.technicaltaskandroid.DEFAULT_STRING
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
internal class AddUserBottomViewModelTest {

    private val testCoroutineDispatcher = StandardTestDispatcher()

    lateinit var viewModel: AddUserBottomViewModel

    private val userRepository = mockk<UserRepository>()

    private val exampleUser = UserModel(id = EXAMPLE_USERID, name = EXAMPLE_USERNAME, email = EXAMPLE_USEREMAIL)
    private val exampleAddUserState = AddUserViewState(id = EXAMPLE_USERID, name = EXAMPLE_USERNAME)

    companion object {
        const val VALUE_ONCE = 1

        const val EXAMPLE_USERID = 100
        const val EXAMPLE_USERNAME = "Sid James"
        const val EXAMPLE_USEREMAIL = "sid.james@carryon.co.uk"
        const val EXAMPLE_INVALIDUSEREMAIL = "carryon.co.uk"
        const val EXAMPLE_USERGENDER = "male"

        const val EXAMPLE_RESPONSE = "{\"id\":100,\"name\":\"Sid James\",\"email\":\"sid.james@carryon.co.uk\",\"gender\":\"male\",\"status\":\"active\"}"
        const val EXAMPLE_ERROR_RESPONSE = "email has already been taken"
    }

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testCoroutineDispatcher)

        viewModel = AddUserBottomViewModel(
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
    fun `when attempting to add a user then a successful response should be returned`(){
        val slot = slot<AddUserViewState>()
        val responsesList = mutableListOf<AddUserViewState>()
        val observer = mockk<Observer<AddUserViewState>>()
        coEvery {
            userRepository.addNewUser(
                EXAMPLE_USERNAME,
                EXAMPLE_USEREMAIL,
                EXAMPLE_USERGENDER
            )
        } returns ApiResponse.Success(exampleUser)
        coEvery {
            observer.onChanged(capture(slot))
        } answers {
            responsesList.add(slot.captured)
        }
        viewModel.addUserState.observeForever(observer)

        runTest {
            viewModel.addNewUser(
                EXAMPLE_USERNAME,
                EXAMPLE_USEREMAIL,
                EXAMPLE_USERGENDER
            )
        }

        print("This is the responsesList: $responsesList")

        coVerify(exactly = VALUE_ONCE) {
            userRepository.addNewUser(
                EXAMPLE_USERNAME,
                EXAMPLE_USEREMAIL,
                EXAMPLE_USERGENDER
            )
        }
        assertTrue(responsesList.size > 0)
        assertTrue(responsesList[0].showLoading)
        assertTrue(responsesList[1].id == EXAMPLE_USERID)
        assertTrue(responsesList[1].name == EXAMPLE_USERNAME)

        viewModel.addUserState.removeObserver(observer)
    }

    @Test
    fun `when attempting to add a user but the user was already present then an error response should be returned`(){
        val slot = slot<AddUserViewState>()
        val responsesList = mutableListOf<AddUserViewState>()
        val observer = mockk<Observer<AddUserViewState>>()
        coEvery {
            userRepository.addNewUser(
                EXAMPLE_USERNAME,
                EXAMPLE_USEREMAIL,
                EXAMPLE_USERGENDER
            )
        } returns ApiResponse.Error(EXAMPLE_ERROR_RESPONSE)
        coEvery {
            observer.onChanged(capture(slot))
        } answers {
            responsesList.add(slot.captured)
        }
        viewModel.addUserState.observeForever(observer)

        runTest {
            viewModel.addNewUser(
                EXAMPLE_USERNAME,
                EXAMPLE_USEREMAIL,
                EXAMPLE_USERGENDER
            )
        }

        print("This is the responsesList: $responsesList")

        coVerify(exactly = VALUE_ONCE) {
            userRepository.addNewUser(
                EXAMPLE_USERNAME,
                EXAMPLE_USEREMAIL,
                EXAMPLE_USERGENDER
            )
        }
        assertTrue(responsesList.size > 0)
        assertTrue(responsesList[0].showLoading)
        assertTrue(responsesList[1].showError)
        assertFalse(responsesList[1].errorMessage.isEmpty())
        assertTrue(responsesList[1].errorMessage == EXAMPLE_ERROR_RESPONSE)

        viewModel.addUserState.removeObserver(observer)
    }

    @Test
    fun `when attempting to add a user but dont add a name then a name error response should be returned`(){
        val slot = slot<AddUserViewState>()
        val responsesList = mutableListOf<AddUserViewState>()
        val observer = mockk<Observer<AddUserViewState>>()
        coEvery {
            observer.onChanged(capture(slot))
        } answers {
            responsesList.add(slot.captured)
        }
        viewModel.addUserState.observeForever(observer)

        runTest {
            viewModel.addNewUser(
                DEFAULT_STRING,
                DEFAULT_STRING,
                DEFAULT_STRING
            )
        }

        assertTrue(responsesList.size > 0)
        assertTrue(responsesList[0].nameError)

        viewModel.addUserState.removeObserver(observer)
    }

    @Test
    fun `when attempting to add a user but dont add an email then the empty email error response should be returned`(){
        val slot = slot<AddUserViewState>()
        val responsesList = mutableListOf<AddUserViewState>()
        val observer = mockk<Observer<AddUserViewState>>()
        coEvery {
            observer.onChanged(capture(slot))
        } answers {
            responsesList.add(slot.captured)
        }
        viewModel.addUserState.observeForever(observer)

        runTest {
            viewModel.addNewUser(
                EXAMPLE_USERNAME,
                DEFAULT_STRING,
                DEFAULT_STRING
            )
        }

        assertTrue(responsesList.size > 0)
        assertTrue(responsesList[0].emailError == AddUserViewState.EmailErrorType.EMPTY_EMAIL)

        viewModel.addUserState.removeObserver(observer)
    }

    @Test
    fun `when attempting to add a user but add an invalid email then the invalid email error response should be returned`(){
        val slot = slot<AddUserViewState>()
        val responsesList = mutableListOf<AddUserViewState>()
        val observer = mockk<Observer<AddUserViewState>>()
        coEvery {
            observer.onChanged(capture(slot))
        } answers {
            responsesList.add(slot.captured)
        }
        viewModel.addUserState.observeForever(observer)

        runTest {
            viewModel.addNewUser(
                EXAMPLE_USERNAME,
                EXAMPLE_INVALIDUSEREMAIL,
                DEFAULT_STRING
            )
        }

        assertTrue(responsesList.size > 0)
        assertTrue(responsesList[0].emailError == AddUserViewState.EmailErrorType.INVALID_EMAIL)

        viewModel.addUserState.removeObserver(observer)
    }
}