package org.mifos.mobile.rocketchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.mifos.mobile.databinding.ActivityRocketChatBinding

class RocketChatActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRocketChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRocketChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}