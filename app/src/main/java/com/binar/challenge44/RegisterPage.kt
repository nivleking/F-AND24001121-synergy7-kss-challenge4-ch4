package com.binar.challenge44

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.binar.challenge44.Database.NoteDatabase
import com.binar.challenge44.Model.User
import com.binar.challenge44.Repository.UserRepository
import com.binar.challenge44.ViewModel.UserViewModel
import com.binar.challenge44.ViewModel.UserViewModelFactory
import com.binar.challenge44.databinding.FragmentRegisterPageBinding

class RegisterPage : Fragment() {
    private lateinit var binding: FragmentRegisterPageBinding
    private lateinit var userViewModel: UserViewModel

    private fun setupViewModel() {
        val notes_db = NoteDatabase.getDatabase(requireContext())
        val repository = UserRepository(notes_db)
        val factory = UserViewModelFactory(requireActivity().application, repository)
        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)
    }

    private fun setupUser() {
        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                binding.etEmail.error = "Email cannot be empty"
                binding.etPassword.error = "Password cannot be empty"
                binding.etConfirmPassword.error = "Confirm Password cannot be empty"
            } else if (password != confirmPassword) {
                binding.etPassword.error = "Password and Confirm Password must be the same"
                binding.etConfirmPassword.error = "Password and Confirm Password must be the same"
            } else {
                val user = User(0, email, password)
                userViewModel.addUser(user)
                Toast.makeText(requireContext(), "Register Success", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupUser()
        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }
}