package com.hoang.wastenot.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.hoang.wastenot.R
import com.hoang.wastenot.databinding.FragmentRecipeDetailBinding
import com.hoang.wastenot.viewmodels.RecipeViewModel


class RecipeDetailFragment : Fragment(R.layout.fragment_recipe_detail) {

    private lateinit var binding: FragmentRecipeDetailBinding
    private val viewModel by viewModels<RecipeViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRecipeDetailBinding.bind(view)
        val myArgument = arguments?.get("RecipeId")
        viewModel.getRecipeUrl(myArgument as Int)

        binding.webView.apply{
            this.settings.loadsImagesAutomatically = true
            this.settings.javaScriptEnabled = true
            binding.webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            binding.webView.webViewClient = WebViewClient()
            viewModel.recipeUrl.observe(viewLifecycleOwner) {
                it.sourceUrl?.let { it1 -> binding.webView.loadUrl(it1) }
            }
        }


//        Toast.makeText(context, "${myArgument}", Toast.LENGTH_SHORT).show()

//        viewModel.recipeUrl.observe(viewLifecycleOwner) {
//            it.sourceUrl?.let { it1 -> binding.webView.loadUrl(it1) }
//        }
        binding.btnHomeRecipedetailfragment.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_global_inventoryFragment)
        }

    }
}