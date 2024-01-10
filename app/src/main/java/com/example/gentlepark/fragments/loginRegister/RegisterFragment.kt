package com.example.gentlepark.fragments.loginRegister


import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope

import com.example.gentlepark.R
import com.example.gentlepark.data.User
import com.example.gentlepark.databinding.FragmentRegisterBinding
import com.example.gentlepark.util.Resource
import com.example.gentlepark.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterFragment: Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            registerId.setOnClickListener{
                val user = User(
                    firstName.text.toString().trim(),
                    lastNameId.text.toString().trim(),
                    emailRegisterId.text.toString().trim()
                )
                val password = editTextTextPassword.text.toString()
                viewModel.createAccountwithEmailandPassword(user,password)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.register.collect{
                when (it) {
                    is Resource.Loading -> {
                        binding.registerId.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.registerId.revertAnimation()
                    }
                    is Resource.Error -> {
                        Log.e(TAG,it.message.toString())
                        binding.registerId.revertAnimation()
                    }
                    else -> Unit
                }
            }
        }
    }

}