package com.hoang.wastenot.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hoang.wastenot.R
import com.hoang.wastenot.api.RecipeResponse
import com.squareup.picasso.Picasso

class RecipeAdapter() : RecyclerView.Adapter<RecipeAdapter.ListViewHolder>() {

    private var dataSet = mutableListOf<RecipeResponse>()


    fun setData(dataParam: List<RecipeResponse>) {
        dataSet.clear()
        dataSet.addAll(dataParam)
        notifyDataSetChanged()
    }



    class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        val recipeTitle: TextView
        val recipeImage: ImageView


        init {

            recipeTitle = view.findViewById(R.id.tv_recipe_title)
            recipeImage = view.findViewById(R.id.iv_recipe_image)

        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_item, parent, false)

        return ListViewHolder(view)

    }


    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val currentData: RecipeResponse = dataSet.get(position)

        holder.recipeTitle.text = currentData.title

        Picasso.get().load("${currentData.image}").into(holder.recipeImage)




}


// Return the size of your dataset (invoked by the layout manager)
override fun getItemCount(): Int {
    return dataSet.size
}

}
