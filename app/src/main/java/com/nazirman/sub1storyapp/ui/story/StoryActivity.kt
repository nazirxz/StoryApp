package com.nazirman.sub1storyapp.ui.story

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nazirman.sub1storyapp.R
import com.nazirman.sub1storyapp.ViewModelFactory
import com.nazirman.sub1storyapp.adapter.StoryAdapter
import com.nazirman.sub1storyapp.api.ApiConfig
import com.nazirman.sub1storyapp.api.ListStoryItem
import com.nazirman.sub1storyapp.api.SharedViewModel
import com.nazirman.sub1storyapp.api.StoriesResponse
import com.nazirman.sub1storyapp.databinding.ActivityStoryBinding
import com.nazirman.sub1storyapp.model.Story
import com.nazirman.sub1storyapp.ui.auth.MainActivity
import com.nazirman.sub1storyapp.utils.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class StoryActivity : AppCompatActivity() {
    private lateinit var storyViewModel: SharedViewModel
    private lateinit var storyBinding: ActivityStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storyBinding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(storyBinding.root)
        setupViewModel()

        val layoutManager = LinearLayoutManager(this)
        storyBinding.rvStories.layoutManager = layoutManager

        getStories()
    }

        private fun setupViewModel() {
            storyViewModel = ViewModelProvider(
                this,
                ViewModelFactory(UserPreference.getInstance(dataStore))
            )[SharedViewModel::class.java]
        }

        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            val inflater = menuInflater
            inflater.inflate(R.menu.settings, menu)
            return true
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when(item.itemId) {
                R.id.menu_add -> {
                    val intent = Intent(this, AddStoryActivity::class.java)
                    startActivity(intent)
                    return true
                }

                R.id.menu_language -> {
                    val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                    startActivity(intent)
                    return true
                }

                R.id.menu_logout -> {
                    storyViewModel.logout()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    return true
                }
            }
            return true
        }

        private fun getStories() {
            showLoading(true)

            storyViewModel.getUser().observe(this ) {
                if(it != null) {
                    val client = ApiConfig.getApiService().getStories("Bearer " + it.token)
                    client.enqueue(object: Callback<StoriesResponse> {
                        override fun onResponse(
                            call: Call<StoriesResponse>,
                            response: Response<StoriesResponse>
                        ) {
                            showLoading(false)
                            val responseBody = response.body()
                            Log.d(TAG, "onResponse: $responseBody")
                            if(response.isSuccessful && responseBody?.message == "Stories fetched successfully") {
                                setStoriesData(responseBody.listStory)
                                Toast.makeText(this@StoryActivity, getString(R.string.success_load_stories), Toast.LENGTH_SHORT).show()
                            } else {
                                Log.e(TAG, "onFailure1: ${response.message()}")
                                Toast.makeText(this@StoryActivity, getString(R.string.fail_load_stories), Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                            showLoading(false)
                            Log.e(TAG, "onFailure2: ${t.message}")
                            Toast.makeText(this@StoryActivity, getString(R.string.fail_load_stories), Toast.LENGTH_SHORT).show()
                        }

                    })
                }
            }

        }

        private fun setStoriesData(items: List<ListStoryItem>) {
            val listStories = ArrayList<Story>()
            for(item in items) {
                val story = Story(
                    item.name,
                    item.photoUrl,
                    item.description
                )
                listStories.add(story)
            }

            val adapter = StoryAdapter(listStories)
            storyBinding.rvStories.adapter = adapter
        }

        private fun showLoading(isLoading: Boolean) {
            if (isLoading) {
                storyBinding.progressBar.visibility = View.VISIBLE
            } else {
                storyBinding.progressBar.visibility = View.GONE
            }
        }

        companion object {
            private const val TAG = "Story Activity"
        }
    }