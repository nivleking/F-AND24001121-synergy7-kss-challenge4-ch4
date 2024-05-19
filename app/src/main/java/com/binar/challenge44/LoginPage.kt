package com.binar.challenge44

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.binar.challenge44.Database.NoteDatabase
import com.binar.challenge44.Database.SessionManager
import com.binar.challenge44.Repository.UserRepository
import com.binar.challenge44.ViewModel.UserViewModel
import com.binar.challenge44.ViewModel.UserViewModelFactory
import com.binar.challenge44.databinding.FragmentLoginPageBinding
import kotlinx.coroutines.launch

class LoginPage : Fragment() {
    private lateinit var binding: FragmentLoginPageBinding
    private lateinit var userViewModel: UserViewModel

    private fun setupViewModel() {
        val notes_db = NoteDatabase.getDatabase(requireContext())
        val repository = UserRepository(notes_db)
        val factory = UserViewModelFactory(requireActivity().application, repository)
        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            lifecycleScope.launch {
                userViewModel.getUser(email, password)?.observe(viewLifecycleOwner, { user ->
                    if (user != null) {
                        val sessionManager = SessionManager(requireContext())
                        sessionManager.userLogin(user.id)
                        startActivity(Intent(activity, MainActivity::class.java))
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Invalid email or password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }
    }
}