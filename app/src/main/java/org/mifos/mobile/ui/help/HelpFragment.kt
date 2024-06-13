package org.mifos.mobile.ui.help

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.models.FAQ
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.location.LocationsFragment
import org.mifos.mobile.utils.HelpUiState

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/
@AndroidEntryPoint
class HelpFragment : BaseFragment() {

    private val viewModel: HelpViewModel by viewModels()
    private var faqArrayList: MutableState<List<FAQ?>> = mutableStateOf(arrayListOf())
    private var selectedFaqPosition: MutableState<Int> = mutableStateOf(-1)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (savedInstanceState == null) {
            viewModel.loadFaq(
                context?.resources?.getStringArray(R.array.faq_qs),
                context?.resources?.getStringArray(R.array.faq_ans)
            )
        }
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MifosMobileTheme {
                    HelpScreen(
                        faqArrayList = faqArrayList.value,
                        callNow = { callHelpline() },
                        leaveEmail = { mailHelpline() },
                        findLocations = { findLocations() },
                        navigateBack = { activity?.finish() },
                        onSearchDismiss = { viewModel.loadFaq(qs = null, ans = null) },
                        searchQuery = { viewModel.filterList(it) },
                        selectedFaqPosition = selectedFaqPosition.value,
                        updateFaqPosition = { viewModel.updateSelectedFaqPosition(it) }
                    )
                }
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.helpUiState.collect() {
                    when (it) {

                        is HelpUiState.ShowFaq -> {
                            faqArrayList.value = it.faqArrayList
                            selectedFaqPosition.value = it.selectedFaqPosition
                        }

                        HelpUiState.Initial -> {}
                    }
                }
            }
        }
    }

    private fun callHelpline() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + getString(R.string.help_line_number))
        startActivity(intent)
    }

    private fun findLocations() {
        (activity as BaseActivity?)?.replaceFragment(
            LocationsFragment.newInstance(),
            true,
            R.id.container,
        )
    }

    private fun mailHelpline() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.contact_email)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.user_query))
        }
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                getString(R.string.no_app_to_support_action),
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): HelpFragment {
            val fragment = HelpFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}