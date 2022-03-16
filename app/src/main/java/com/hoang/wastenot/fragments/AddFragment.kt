package com.hoang.wastenot.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.hoang.wastenot.R
import com.hoang.wastenot.databinding.FragmentAddBinding
import com.hoang.wastenot.models.Food
import com.hoang.wastenot.repositories.UserRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AddFragment : Fragment(R.layout.fragment_add), KoinComponent {

    private lateinit var binding: FragmentAddBinding
    private val userRepository: UserRepository by inject()
    private val TAG = "AddFragment"
    private var picUrl: String? = null
    private val launchCameraIntentLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { fileUri ->
            if (fileUri != null) {
                uploadFromUri(fileUri)
            } else {
                Log.w(TAG, "File URI is null")
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddBinding.bind(view)

        binding.btnHomeAddfragment.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_global_inventoryFragment)
        }

        setOnUploadPictureBtnClicked()
        setOnSaveButtonClicked()
    }


    private fun setOnUploadPictureBtnClicked() {
        binding.btnAddFoodPicture.setOnClickListener {
            launchCameraIntentLauncher.launch(arrayOf("image/*"))
        }
    }

    private fun uploadFromUri(fileUri: Uri) {
        val storageRef = FirebaseStorage.getInstance().reference
        fileUri.lastPathSegment?.let {
            val photoRef = storageRef.child("pictures").child(it)

            // Upload file to Firebase Storage
            Log.d(TAG, "uploadFromUri:dst:" + photoRef.path)
            photoRef.putFile(fileUri).continueWithTask { task ->
                // Forward any exceptions
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                Log.d(TAG, "uploadFromUri: upload success")

                // Request the public download URL
                photoRef.downloadUrl
            }.addOnSuccessListener { downloadUri ->
                // Upload succeeded
                Log.d(TAG, "uploadFromUri: getDownloadUri success: $downloadUri")

                picUrl = downloadUri.toString()

            }.addOnFailureListener { exception ->
                // Upload failed
                Log.w(TAG, "uploadFromUri:onFailure", exception)

            }
        }

    }

    private fun setOnSaveButtonClicked() {
        binding.btnSaveFood.setOnClickListener {
            if (picUrl == null) {
                Toast.makeText(activity, "You haven't selected a picture yet", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val currentUser = userRepository.getCurrentUser() ?: return@setOnClickListener
            val foodName = binding.etFoodName.text.toString()
            val foodExpDate = binding.etFoodExpDate.text.toString()

            val food = Food(
                "",
                foodName,
                foodExpDate,
                picUrl,
                currentUser.email
            )

            val database = Firebase.firestore
            database.collection("foods")
                .add(food)
                .addOnSuccessListener { documentReference ->
                    Log.d(
                        "Successful Add Message",
                        "DocumentSnapshot added with ID: ${documentReference.id}"
                    )
                }
                .addOnFailureListener { e ->
                    Log.w("Failure Add Message", "Error adding document", e)
                }
        }
    }

}