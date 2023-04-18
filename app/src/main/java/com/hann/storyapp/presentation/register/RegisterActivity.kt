package com.hann.storyapp.presentation.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.hann.storyapp.databinding.ActivityRegisterBinding
import com.hann.storyapp.presentation.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private val registerViewModel: RegisterViewModel by viewModel()
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        playAnimation()

        binding.nameEditRegist.addTextChangedListener(createTextWatcher())
        binding.emailEditRegist.addTextChangedListener(createTextWatcher())
        binding.passwordEditRegist.addTextChangedListener(createTextWatcher())

        registerViewModel.state.observe(this){
            if (it.isLoading){
                binding.progressBar.visibility = View.VISIBLE
            }
            if (it.error.isNotBlank()){
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Error..", Toast.LENGTH_SHORT).show()
            }
            if (it.success.isNotEmpty()){
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        binding.registBtn.setOnClickListener {
            registerUser()
        }

        binding.login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageRegist, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val intro = ObjectAnimator.ofFloat(binding.titleRegist, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.nameInputLayout, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.emailEditRegistLayout, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.passwordInputLayoutRegist, View.ALPHA, 1f).setDuration(500)
        val button = ObjectAnimator.ofFloat(binding.registBtn, View.ALPHA, 1f).setDuration(500)
        val regist = ObjectAnimator.ofFloat(binding.login, View.ALPHA, 1f).setDuration(500)

        val btn = AnimatorSet().apply {
            playTogether(button, regist)
        }

        AnimatorSet().apply {
            playSequentially(intro,name, email, password, btn)
            start()
        }
    }

    private fun registerUser() {
        registerViewModel.registerUser(
            binding.nameEditRegist.text.toString(),
            binding.emailEditRegist.text.toString(),
            binding.passwordEditRegist.text.toString())
    }

    private fun updateRegisterButtonState() {
        val fields = arrayOf(binding.nameEditRegist.text, binding.emailEditRegist.text, binding.passwordEditRegist.text)
        binding.registBtn.isEnabled = fields.all { !it.isNullOrEmpty() }
    }

    private fun createTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { updateRegisterButtonState() }
            override fun afterTextChanged(p0: Editable?) { }
        }
    }
}