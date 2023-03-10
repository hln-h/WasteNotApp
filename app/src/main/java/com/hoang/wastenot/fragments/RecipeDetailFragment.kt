package com.hoang.wastenot.fragments

import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.hoang.wastenot.R
import com.hoang.wastenot.databinding.FragmentRecipeDetailBinding
import com.hoang.wastenot.viewmodels.RecipeViewModel
import android.os.Handler;
import android.view.WindowManager


class RecipeDetailFragment : Fragment(R.layout.fragment_recipe_detail) {

    private lateinit var binding: FragmentRecipeDetailBinding
    private val viewModel by viewModels<RecipeViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRecipeDetailBinding.bind(view)
        binding.loadingAnimation.visibility = View.VISIBLE
        val handler = Handler(Looper.getMainLooper())


        val myArgument = arguments?.get("RecipeId")
        viewModel.getRecipeUrl(myArgument as Int)



        binding.webView.apply {
            if (Build.VERSION.SDK_INT >= 19) {
                this.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            this.settings.loadsImagesAutomatically = true
            this.settings.javaScriptEnabled = true
            this.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            this.webViewClient = WebViewClient()
            viewModel.recipeUrl.observe(viewLifecycleOwner) {
                it.sourceUrl?.let { it1 -> binding.webView.loadUrl(it1) }
            }
            handler.postDelayed({
                binding.loadingAnimation.visibility = View.GONE
            }, 2000)
        }


//        Toast.makeText(context, "${myArgument}", Toast.LENGTH_SHORT).show()

//        viewModel.recipeUrl.observe(viewLifecycleOwner) {
//            it.sourceUrl?.let { it1 -> binding.webView.loadUrl(it1) }
//        }
        binding.btnHomeRecipedetailfragment.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_global_inventoryFragment)
        }
        setStatusBarAppearance()
    }

    private fun setStatusBarAppearance() {
        // To show content behind status and navigation bar
        val window = activity?.window
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        )
    }


}