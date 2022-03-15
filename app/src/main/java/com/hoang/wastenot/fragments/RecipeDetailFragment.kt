package com.hoang.wastenot.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.hoang.wastenot.R
import com.hoang.wastenot.databinding.FragmentRecipeDetailBinding

class RecipeDetailFragment : Fragment(R.layout.fragment_recipe_detail) {

    private lateinit var binding: FragmentRecipeDetailBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRecipeDetailBinding.bind(view)

        binding.btnHomeRecipedetailfragment.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_global_inventoryFragment)
        }

    }
}