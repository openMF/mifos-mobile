package org.mifos.mobile.ui.registration

/*
/**
 * Created by dilpreet on 31/7/17.
 */
@AndroidEntryPoint
class RegistrationVerificationFragment : BaseFragment() {
    private var _binding: FragmentRegistrationVerificationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegistrationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegistrationVerificationBinding.inflate(inflater, container, false)
        val rootView = binding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registrationVerificationUiState.collect { state ->
                    when (state) {
                        RegistrationUiState.Loading -> showProgress()

                        RegistrationUiState.Success -> {
                            hideProgress()
                            showVerifiedSuccessfully()
                        }

                        is RegistrationUiState.Error -> {
                            hideProgress()
                            showError(getString(state.exception))
                        }

                        RegistrationUiState.Initial -> {}
                    }
                }
            }
        }

        binding.btnVerify.setOnClickListener {
            verifyClicked()
        }
    }

    private fun verifyClicked() {
        val authenticationToken = binding.etAuthenticationToken.text.toString()
        val requestId = binding.etRequestId.text.toString()
        viewModel.verifyUser(authenticationToken, requestId)
    }

    private fun showVerifiedSuccessfully() {
        startActivity(Intent(activity, LoginActivity::class.java))
        Toast.makeText(context, getString(R.string.verified), Toast.LENGTH_SHORT).show()
        activity?.finish()
    }

    fun showError(msg: String?) {
        Toaster.show(binding.root, msg)
    }

    fun showProgress() {
        showMifosProgressDialog(getString(R.string.verifying))
    }

    fun hideProgress() {
        hideMifosProgressDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): RegistrationVerificationFragment {
            return RegistrationVerificationFragment()
        }
    }
}
*/