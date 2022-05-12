package com.sliide.technicaltaskandroid.ui.userlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sliide.technicaltaskandroid.KEY_UPDATE_USER_LIST
import com.sliide.technicaltaskandroid.R
import com.sliide.technicaltaskandroid.databinding.FragmentUserListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListFragment: Fragment() {

    private val viewModel: UserListViewModel by viewModels()

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        initViews()
        initObservers()
        return binding.root
    }

    fun initViews() {
        binding.srlUserList.setOnRefreshListener {
            viewModel.downloadUserList()
        }
        binding.fabAddUser.setOnClickListener {
            viewModel.navigateToAddUser()
        }
        binding.rvUserList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvUserList.adapter = UserListAdapter(viewModel::navigateToDeleteUser)
    }

    fun initObservers() {
        viewModel.userList.observe(viewLifecycleOwner) { responseList ->
            binding.srlUserList.isRefreshing = when {
                (responseList[0].isLoading) -> {
                    true
                }
                (responseList[0].showError) -> {
                    Toast.makeText(
                        requireContext(),
                        when {
                            responseList[0].errorMessage.isNotEmpty() -> {
                                responseList[0].errorMessage
                            }
                            else -> resources.getString(R.string.err_download_unknown) + resources.getString(R.string.err_please_try_again_later)
                        },
                        Toast.LENGTH_SHORT
                    ).show()
                    false
                }
                else -> {
                    (binding.rvUserList.adapter as UserListAdapter).updateListContents(responseList)
                    false
                }
            }
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>(KEY_UPDATE_USER_LIST)?.observe(
            viewLifecycleOwner) {
            viewModel.downloadUserList()
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<Int>(KEY_UPDATE_USER_LIST)
        }
        viewModel.navigationEvent.observe(viewLifecycleOwner) {
            it(findNavController())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.downloadUserList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}