package com.sliide.technicaltaskandroid.ui.adduser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sliide.technicaltaskandroid.databinding.FragmentAddUserBinding
import com.sliide.technicaltaskandroid.databinding.FragmentUserListBinding
import com.sliide.technicaltaskandroid.ui.userlist.UserListViewModel
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class AddUserBottomFragment @Inject constructor(): BottomSheetDialogFragment() {

    private val viewModel: AddUserBottomViewModel by viewModels()

    private var _binding: FragmentAddUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}