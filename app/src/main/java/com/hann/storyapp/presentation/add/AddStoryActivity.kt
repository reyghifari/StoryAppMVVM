package com.hann.storyapp.presentation.add

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hann.storyapp.R
import com.hann.storyapp.databinding.ActivityAddStoryBinding
import com.hann.storyapp.domain.model.User
import com.hann.storyapp.utils.DataMapper
import com.hann.storyapp.utils.DataMapper.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private val addStoryViewModel : AddStoryViewModel by viewModel()
    private lateinit var multipartBody: MultipartBody.Part
    private lateinit var requestBody : RequestBody
    private var status : Boolean = false
    private lateinit var user : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.upload_story)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        addStoryViewModel.getUser().observe(this){
            user = it
        }

        addStoryViewModel.state.observe(this){
            if (it.isLoading){
                binding.progressBar.visibility = View.VISIBLE
            }
            if (it.error.isNotBlank()){
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, getString(R.string.failed_upload_story), Toast.LENGTH_SHORT).show()
            }
            if (it.success.isNotEmpty()){
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, getString(R.string.success_upload_story), Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        binding.btnCamera.setOnClickListener {
            starCameraX()
        }
        binding.btnGallery.setOnClickListener {
            startGallery()
        }
        binding.btnUpload.setOnClickListener {
            uploadStory()
        }
    }

    private fun uploadStory() {
        val description = binding.tvDescriptionUpload.text.toString()
        requestBody = description.toRequestBody("text/plain".toMediaType())
        if (status){
            addStoryViewModel.uploadStories(multipartBody, requestBody, user.token)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AddStoryActivity)
                val requestImageFile = myFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                multipartBody =  MultipartBody.Part.createFormData("photo", "photo", requestImageFile)
                status = true
                binding.previewImage.setImageURI(uri)
            }
        }
    }

    private fun starCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            myFile?.let { file ->
                DataMapper.rotateFile(file, isBackCamera)
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                multipartBody =  MultipartBody.Part.createFormData("photo", "photo", requestImageFile)
                status = true
                binding.previewImage.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.no_permission),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}