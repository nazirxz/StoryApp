package com.nazirman.sub1storyapp.ui.story

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.nazirman.sub1storyapp.databinding.ActivityStoryBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class StoryActivity : AppCompatActivity() {
    private lateinit var storyBinding: ActivityStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storyBinding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(storyBinding.root)
    }
}