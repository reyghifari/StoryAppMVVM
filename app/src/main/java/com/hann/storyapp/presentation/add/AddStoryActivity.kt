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
import androidx.lifecycle.viewModelScope
import com.hann.storyapp.R
import com.hann.storyapp.data.Resource
import com.hann.storyapp.databinding.ActivityAddStoryBinding
import com.hann.storyapp.domain.model.User
import com.hann.storyapp.presentation.main.MainActivity
import com.hann.storyapp.utils.DataMapper
import com.hann.storyapp.utils.DataMapper.reduceFileImage
import com.hann.storyapp.utils.DataMapper.uriToFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    private lateinit var description : RequestBody
    private  var lat : RequestBody? = null
    private  var lon : RequestBody? = null
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

        val latitude = intent.getStringExtra(EXTRA_LATITUDE)
        val longtitude = intent.getStringExtra(EXTRA_LONGTITUDE)

        binding.latitudeEditText.setText(latitude ?: "")
        binding.longtitudeEditText.setText(longtitude ?: "")

        addStoryViewModel.getUser().observe(this){
            user = it
        }

        binding.btnCamera.setOnClickListener {
            starCameraX()
        }
        binding.btnGallery.setOnClickListener {
            startGallery()
        }
        binding.btnUpload.setOnClickListener {
            uploadStories()
        }
        binding.btnLocation.setOnClickListener {
            val intent = Intent(this, AddMapActivity::class.java)
            startActivity(intent)
        }
    }

    private fun uploadStories(){
        val desc = binding.tvDescriptionUpload.text.toString()
        val latParam = binding.latitudeEditText.text.toString()
        val lonParam = binding.longtitudeEditText.text.toString()
        description = desc.toRequestBody("text/plain".toMediaType())

        if (latParam.isNotEmpty() && lonParam.isNotEmpty()){
            lat = latParam.toRequestBody("text/plain".toMediaType())
            lon = lonParam.toRequestBody("text/plain".toMediaType())
        }

        if (status){
            addStoryViewModel.uploadStory(multipartBody, description, user.token, lat, lon).onEach {
                result ->
                when(result){
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, getString(R.string.failed_upload_story), Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, getString(R.string.success_upload_story), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@AddStoryActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }.launchIn(CoroutineScope(Dispatchers.Main))
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
                val reduce = reduceFileImage(myFile)
                val requestImageFile = reduce.asRequestBody("image/jpeg".toMediaTypeOrNull())
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
                val reduceFile = reduceFileImage(file)
                val requestImageFile = reduceFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                multipartBody =  MultipartBody.Part.createFormData("photo", "photo", requestImageFile)
                status = true
                binding.previewImage.setImageBitmap(BitmapFactory.decodeFile(reduceFile.path))
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
        const val EXTRA_LATITUDE = "extra_latitude"
        const val EXTRA_LONGTITUDE = "extra_longtitude"
    }
}