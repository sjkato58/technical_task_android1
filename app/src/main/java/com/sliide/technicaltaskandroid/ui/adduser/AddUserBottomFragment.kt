package com.sliide.technicaltaskandroid.ui.adduser

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sliide.technicaltaskandroid.DEFAULT_INTEGER
import com.sliide.technicaltaskandroid.KEY_UPDATE_USER_LIST
import com.sliide.technicaltaskandroid.R
import com.sliide.technicaltaskandroid.databinding.FragmentAddUserBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddUserBottomFragment: BottomSheetDialogFragment() {

    private val viewModel: AddUserBottomViewModel by viewModels()

    private var _binding: FragmentAddUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddUserBinding.inflate(inflater, container, false)
        initViews()
        initObservers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel
    }

    fun initViews() {
        binding.tietAdduserName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tilAdduserName.error = null
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.tietAdduserEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.tilAdduserEmail.error = null
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        val list = requireContext().resources.getStringArray(R.array.a_genderlist)
        val adapter = ArrayAdapter(requireContext(), R.layout.item_add_user_gender, list)
        binding.sAdduserGender.adapter = adapter
        binding.bAdduser.setOnClickListener {
            viewModel.addNewUser(
                binding.tietAdduserName.text.toString(),
                binding.tietAdduserEmail.text.toString(),
                binding.sAdduserGender.selectedItem.toString()
            )
        }
    }

    fun initObservers() {
        viewModel.addUserState.observe(viewLifecycleOwner) { responseViewState ->
            binding.clAdduserSpinnercontainer.isVisible = when {
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
                (responseViewState.nameError) -> {
                    binding.tilAdduserName.error = requireContext().resources.getString(R.string.err_name_empty)
                    false
                }
                (responseViewState.emailError != AddUserViewState.EmailErrorType.DEFAULT) -> {
                    binding.tilAdduserEmail.error = when {
                        (responseViewState.emailError == AddUserViewState.EmailErrorType.EMPTY_EMAIL) -> {
                            requireContext().resources.getString(R.string.err_email_empty)
                        }
                        else -> {
                            requireContext().resources.getString(R.string.err_email_invalid)
                        }
                    }
                    false
                }
                else -> {
                    Toast.makeText(
                        requireContext(),
                        "${responseViewState.name} ${requireContext().resources.getString(R.string.d_add_user_success)}",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(KEY_UPDATE_USER_LIST, responseViewState.id)
                    this.dismiss()
                    false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}