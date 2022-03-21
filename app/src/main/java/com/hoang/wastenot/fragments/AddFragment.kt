package com.hoang.wastenot.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.hoang.wastenot.R
import com.hoang.wastenot.databinding.FragmentAddBinding
import com.hoang.wastenot.models.Food
import com.hoang.wastenot.repositories.CSVReader
import com.hoang.wastenot.repositories.UserRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.IOException
import java.util.*

class AddFragment : Fragment(R.layout.fragment_add), KoinComponent {

    private lateinit var binding: FragmentAddBinding
    private val userRepository: UserRepository by inject()
    private val TAG = "AddFragment"
    private var picUrl: String? = null
    private var expDate: Date? = null
    private var category: String? = null
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

        binding.btnScan.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_addFragment_to_barcodeScannerFragment)
        }

        setOnUploadPictureBtnClicked()
        readIngredients()
        setOnSaveButtonClicked()
        setOnDatePickerClicked(view)

    }

    private fun readIngredients() {
        var rows = mutableListOf<String>()
        val csvReader = CSVReader(requireContext(), "top_1k_ingredients")
        try {
            rows = csvReader.readCSV()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        rows.size

        val textView = (binding.autocompleteCategory) as AutoCompleteTextView

        val categories: MutableList<String> = rows

        val myadapter = ArrayAdapter(requireContext(), R.layout.item_category, categories).also { adapter ->
            textView.setAdapter(adapter)
        }


    }

    private fun setOnDatePickerClicked(view: View) {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build()

        binding.btnAddExpDate.setOnClickListener {
            datePicker
                .show(parentFragmentManager, "tag")
            datePicker
                .addOnPositiveButtonClickListener {
                    expDate = Date(it)
                }
        }
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
            } else if (expDate == null) {
                Toast.makeText(
                    activity,
                    "You haven't selected an expiration date",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val currentUser = userRepository.getCurrentUser() ?: return@setOnClickListener
            val foodName = binding.etFoodName.text.toString()
            category = binding.autocompleteCategory.text.toString()
            val foodRef = Firebase.firestore.collection("foods").document()

            val food = Food(
                foodRef.id,
                foodName,
                Timestamp(expDate!!),
                picUrl,
                category!!,
                currentUser.email
            )

            foodRef.set(food)
                .addOnSuccessListener { documentReference ->
                    Log.d(
                        "Successful Add Message",
                        "DocumentSnapshot added with ID: ${foodRef.id}"
                    )
                }
                .addOnFailureListener { e ->
                    Log.w("Failure Add Message", "Error adding document", e)
                }
        }
    }

}
