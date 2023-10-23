package org.mifos.mobile.ui.fragments

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.databinding.FragmentHelpBinding
import org.mifos.mobile.models.FAQ
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.adapters.FAQAdapter
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.DividerItemDecoration
import org.mifos.mobile.utils.HelpUiState
import org.mifos.mobile.viewModels.HelpViewModel
import javax.inject.Inject

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/
@AndroidEntryPoint
class HelpFragment : BaseFragment() {
    private var _binding: FragmentHelpBinding? = null
    private val binding get() = _binding!!

    @JvmField
    @Inject
    var faqAdapter: FAQAdapter? = null

    private val viewModel: HelpViewModel by viewModels()

    private var faqArrayList: ArrayList<FAQ?>? = null
    private lateinit var sweetUIErrorHandler: SweetUIErrorHandler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHelpBinding.inflate(inflater, container, false)
        val rootView = binding.root
        setHasOptionsMenu(true)
        setToolbarTitle(getString(R.string.help))
        sweetUIErrorHandler = SweetUIErrorHandler(activity, rootView)
        showUserInterface()
        if (savedInstanceState == null) {
            viewModel.loadFaq(
                context?.resources?.getStringArray(R.array.faq_qs),
                context?.resources?.getStringArray(R.array.faq_ans)
            )
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.helpUiState.collect() {
                    when (it) {

                        is HelpUiState.ShowFaq -> {
                            showFaq(it.faqArrayList)
                        }

                        HelpUiState.Initial -> {}
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(Constants.HELP, ArrayList<Parcelable?>(faqArrayList))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            val faqs: ArrayList<FAQ?> =
                savedInstanceState.getParcelableArrayList(Constants.HELP) ?: arrayListOf()
            showFaq(faqs)
        }
    }

    private fun showUserInterface() {
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvFaq.layoutManager = layoutManager
        binding.rvFaq.addItemDecoration(
            DividerItemDecoration(
                activity,
                layoutManager.orientation,
            ),
        )
        binding.callButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + getString(R.string.help_line_number))
            startActivity(intent)
        }
        binding.mailButton.setOnClickListener {
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
        binding.locationsButton.setOnClickListener {
            (activity as BaseActivity?)?.replaceFragment(
                LocationsFragment.newInstance(),
                true,
                R.id.container,
            )
        }
    }

    private fun showFaq(faqArrayList: ArrayList<FAQ?>?) {
        faqAdapter?.setFaqArrayList(faqArrayList)
        binding.rvFaq.adapter = faqAdapter
        this.faqArrayList = faqArrayList
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_help, menu)
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.menu_search_faq).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val filteredFAQList = viewModel.filterList(faqArrayList, newText)
                filteredFAQList.let {
                    if (it.isNotEmpty()) {
                        sweetUIErrorHandler.hideSweetErrorLayoutUI(
                            binding.rvFaq,
                            binding.layoutError.clErrorLayout,
                        )
                        faqAdapter?.updateList(it)
                    } else {
                        showEmptyFAQUI()
                    }
                } ?: showEmptyFAQUI()
                return false
            }
        })
    }

    private fun showEmptyFAQUI() {
        sweetUIErrorHandler.showSweetEmptyUI(
            getString(R.string.questions),
            R.drawable.ic_help_black_24dp,
            binding.rvFaq,
            binding.layoutError.clErrorLayout,
        )
    }

    fun showProgress() {
        showProgressBar()
    }

    fun hideProgress() {
        hideProgressBar()
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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
