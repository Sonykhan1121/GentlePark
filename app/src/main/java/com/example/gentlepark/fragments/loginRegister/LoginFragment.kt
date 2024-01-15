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
import com.example.gentlepark.dialog.setupBottomSheetDialog
import com.example.gentlepark.util.RegisterValidation
import com.example.gentlepark.util.Resource
import com.example.gentlepark.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

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
        binding.forgetPassword.setOnClickListener {
            setupBottomSheetDialog { email->
                viewmodel.resetPassword(email)
            }

        }
        lifecycleScope.launchWhenStarted {
            viewmodel.resetPassword.collect{
                when(it){
                    is Resource.Loading  ->{

                    }
                    is Resource.Success ->{
                        Snackbar.make(requireView(),"Reset link was sent to your email",Snackbar.LENGTH_LONG).show()
                    }
                    is Resource.Error ->{
                        Snackbar.make(requireView(),"Error ${it.message}",Snackbar.LENGTH_LONG).show()

                    }
                    else ->{
                        Unit
                    }
                }
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
        lifecycleScope.launchWhenStarted {
            viewmodel.validation.collect{validation ->
                if(validation.email  is RegisterValidation.Failed)
                {
                    withContext(Dispatchers.Main){
                        binding.editTextTextEmailAddress.apply {
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }
                if(validation.password is RegisterValidation.Failed)
                {
                    withContext(Dispatchers.Main){
                        binding.editTextTextPassword.apply {
                            requestFocus()
                            error = validation.password.message
                        }
                    }
                }

            }
        }
    }
}