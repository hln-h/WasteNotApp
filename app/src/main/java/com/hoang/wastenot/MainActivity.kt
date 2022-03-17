package com.hoang.wastenot

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hoang.wastenot.databinding.ActivityMainBinding
import com.hoang.wastenot.di.KoinGraph
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import com.hoang.wastenot.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}