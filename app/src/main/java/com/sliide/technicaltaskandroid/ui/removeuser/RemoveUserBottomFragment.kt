package com.sliide.technicaltaskandroid.ui.removeuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sliide.technicaltaskandroid.KEY_UPDATE_USER_LIST
import com.sliide.technicaltaskandroid.R
import com.sliide.technicaltaskandroid.databinding.FragmentRemoveUserBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemoveUserBottomFragment: BottomSheetDialogFragment() {

    private val viewModel: RemoveUserBottomViewModel by viewModels()

    private var _binding: FragmentRemoveUserBinding? = null
    private val binding get() = _binding!!

    private val args: RemoveUserBottomFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onStart() {
        super.onStart()
        val behaviour = BottomSheetBehavior.from(requireView().parent as View)
        behaviour.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRemoveUserBinding.inflate(inflater,container, false)
        initViews()
        initObservers()
        return binding.root
    }

    fun initViews() {
        binding.bRemoveuserPositive.setOnClickListener {
            viewModel.removeUser()
        }
        binding.bRemoveuserCancel.setOnClickListener {
            this.dismiss()
        }
    }

    fun initObservers() {
        viewModel.removeUserBottomState.observe(viewLifecycleOwner) { responseViewState ->
            binding.clRemoveuserSpinnercontainer.isVisible = when {
                (responseViewState.showLoading) -> {
                    true
                }
                (responseViewState.showError) -> {
                    Toast.makeText(
                        requireContext(),
                        when {
                            responseViewState.errorMessage.isNotEmpty() -> {
                                responseViewState.errorMessage
                            }
                            else -> resources.getString(R.string.err_download_unknown) + resources.getString(R.string.err_please_try_again_later)
                        },
                        Toast.LENGTH_SHORT
                    ).show()
                    false
                }
                else -> {
                    Toast.makeText(
                        requireContext(),
                        requireContext().resources.getString(R.string.d_remove_user_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(
                        KEY_UPDATE_USER_LIST, responseViewState.id)
                    this.dismiss()
                    false
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.saveUserData(args.userid, args.username)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}