package com.hoang.wastenot.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.hoang.wastenot.api.RecipeDetailResponse
import com.hoang.wastenot.api.RecipeResponse
import com.hoang.wastenot.api.RecipeRootResponse
import com.hoang.wastenot.api.Retrofit.recipeService
import kotlinx.coroutines.flow.Flow


class RecipeRepository {



    suspend fun getRecipes(diet: String): RecipeRootResponse? {
        val response =
            recipeService.getRecipes(
                "tomato",
                10,
                "min-missing-ingredients",
                diet,
                "34d327a0420a45d4904a5ee6d1de0ce7"
            ).execute()
        if (response.isSuccessful) {
            return response.body()
        } else {
            Log.e("HTTP ERROR TAG", "${response.errorBody()}")
            return null
        }
    }

    suspend fun getRecipeUrl(recipeId: Int): RecipeDetailResponse? {
        val response =
            recipeService.getRecipeUrl(recipeId, false, "35ffd0936a0b48209553394c38c0b8bd")
                .execute()
        if (response.isSuccessful) {
            return response.body()
        } else {
            Log.e("HTTP ERROR TAG", "${response.errorBody()}")
            return null
        }

    }
}
