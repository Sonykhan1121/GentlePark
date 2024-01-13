package com.example.gentlepark.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.gentlepark.R
import com.example.gentlepark.activities.ShoppingActivity
import com.example.gentlepark.databinding.FragmentLoginBinding
import com.example.gentlepark.util.Resource
import com.example.gentlepark.viewmodel.LoginViewModel
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginFragment: Fragment() {
    private lateinit var binding:FragmentLoginBinding
    private val viewmodel by viewModels<LoginViewModel> ()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.donotHaveAccount.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.apply {
            signinId.setOnClickListener {
                val email  = editTextTextEmailAddress.text.toString().trim()
                val password = editTextTextPassword.text.toString()
                viewmodel.login(email,password)

            }
        }
        lifecycleScope.launchWhenStarted {
            viewmodel.login.collect{
                when(it){
                    is Resource.Loading  ->{
                        binding.signinId.startAnimation()
                    }
                    is Resource.Success ->{
                        FancyToast.makeText(requireActivity(),"Successfull Login",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                        binding.signinId.revertAnimation()
                        Intent(requireActivity(),ShoppingActivity::class.java).also{ intent->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                    is Resource.Error ->{
                        FancyToast.makeText(requireActivity(),it.message,FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();

                        binding.signinId.revertAnimation()
                    }
                     else ->{
                       Unit
                    }
                }
            }
        }
    }
}