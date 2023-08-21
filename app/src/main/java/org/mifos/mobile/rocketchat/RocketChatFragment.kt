package org.mifos.mobile.rocketchat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentRocketChatBinding

class RocketChatFragment : Fragment() {
    private var _binding : FragmentRocketChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRocketChatBinding.inflate(inflater, container, false)
        return binding.root
    }
}