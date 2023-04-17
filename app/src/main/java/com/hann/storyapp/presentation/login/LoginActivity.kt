package com.hann.storyapp.presentation.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.hann.storyapp.databinding.ActivityLoginBinding
import com.hann.storyapp.domain.model.User
import com.hann.storyapp.presentation.main.MainActivity
import com.hann.storyapp.presentation.register.RegisterActivity
import com.hann.storyapp.utils.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                loginViewModel.isLoadingSplash.value
            }
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        playAnimation()

        loginViewModel.getUser().observe(this){
            if (it.isLogin){
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(Constants.PARAM_TOKEN, it.token)
                startActivity(intent)
                finish()
            }
        }

        binding.emailEditLogin.addTextChangedListener(createTextWatcher())
        binding.passwordEditLogin.addTextChangedListener(createTextWatcher())

        loginViewModel.state.observe(this){
            if (it.isLoading){
                binding.loadingLogin.visibility = View.VISIBLE
            }
            if (it.error.isNotBlank()){
                binding.loadingLogin.visibility = View.GONE
                Toast.makeText(this, "Gagal Login...", Toast.LENGTH_SHORT).show()
            }
            if (it.success?.name?.isNotEmpty() == true){
                binding.loadingLogin.visibility = View.GONE
                Toast.makeText(this, it.success.name + " berhasil login", Toast.LENGTH_SHORT).show()
                val user = User(name = it.success.name, token = it.success.token, isLogin = true)
                loginViewModel.saveUser(user)
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(Constants.PARAM_TOKEN, it.success.token)
                startActivity(intent)
                finish()
            }
        }

        binding.loginBtn.setOnClickListener{
            loginViewModel.loginUser(
                binding.emailEditLogin.text.toString(),
                binding.passwordEditLogin.text.toString()
            )
        }

        binding.regist.setOnClickListener {
            Intent(this, RegisterActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView2, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val intro = ObjectAnimator.ofFloat(binding.textView6, View.ALPHA, 1f).setDuration(3500)
        val email = ObjectAnimator.ofFloat(binding.emailInputLayout, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.passwordInputLayout, View.ALPHA, 1f).setDuration(500)
        val button = ObjectAnimator.ofFloat(binding.loginBtn, View.ALPHA, 1f).setDuration(500)
        val regist = ObjectAnimator.ofFloat(binding.regist, View.ALPHA, 1f).setDuration(500)

        val btn = AnimatorSet().apply {
            playTogether(button, regist)
        }

        AnimatorSet().apply {
            playSequentially(intro, email, password, btn)
            start()
        }
    }

    private fun updateLoginButtonState() {
        val fields = arrayOf( binding.emailEditLogin.text, binding.passwordEditLogin.text)
        binding.loginBtn.isEnabled = fields.all { !it.isNullOrEmpty() }
    }

    private fun createTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { updateLoginButtonState() }
            override fun afterTextChanged(p0: Editable?) { }
        }
    }
}