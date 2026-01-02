# Feature Layer - Status & Memory

> **Layer**: UI Modules (ViewModel + Screen + Components)
> **Command**: `/feature [Feature]`
> **Location**: `feature/`

---

## Current Status

| Component | Count | Status |
|-----------|-------|--------|
| Feature Modules | 22 | Complete |
| ViewModels | 30+ | Complete |
| Screens | 35+ | Complete |
| Components | 50+ | Complete |
| DI Modules | 16 | Complete |

---

## Layer Structure

```
feature/
├── accounts/               # Account listing and transactions
├── auth/                   # Login, registration, password recovery
├── beneficiary/            # Beneficiary management
├── client-charge/          # Client charges
├── guarantor/              # Guarantor management
├── home/                   # Home dashboard
├── loan-account/           # Loan account details
├── loan-application/       # Loan application flow
├── location/               # Location/branch finder
├── notification/           # Notifications
├── onboarding-language/    # Language selection onboarding
├── passcode/               # Passcode setup/verification
├── qr/                     # QR code generation/scanning
├── recent-transaction/     # Recent transactions
├── savings-account/        # Savings account details
├── savings-application/    # Savings application flow
├── settings/               # App settings
├── share-account/          # Share account details
├── share-application/      # Share application flow
├── status/                 # Status screens
├── third-party-transfer/   # Third-party transfers
└── transfer-process/       # Transfer flow
```

---

## Feature Modules Status

### Phase 1: Core MVP (6 modules)

| Module | Screen | ViewModel | Components | DI | Status |
|--------|--------|-----------|------------|-----|--------|
| auth | LoginScreen, RegistrationScreen, OtpScreen, RecoverPasswordScreen, SetPasswordScreen, UploadIdScreen | LoginViewModel, OtpAuthenticationViewModel, RecoverPasswordViewModel, SetPasswordViewModel, UploadIdViewmodel, RegistrationViewModel | 5+ | AuthModule | Complete |
| home | HomeScreen | HomeViewModel | 2+ | HomeModule | Complete |
| accounts | AccountsScreen, TransactionScreen | AccountsViewModel, TransactionViewModel | 3+ | AccountsModule | Complete |
| recent-transaction | RecentTransactionScreen | RecentTransactionViewModel | 2+ | RecentTransactionModule | Complete |
| notification | NotificationScreen | NotificationViewModel | 2+ | NotificationModule | Complete |
| settings | SettingsScreen, LanguageScreen, ThemeScreen | SettingsViewModel, LanguageViewModel, ChangeThemeViewModel | 3+ | SettingsModule | Complete |

### Phase 2: Account Management (5 modules)

| Module | Screen | ViewModel | Components | DI | Status |
|--------|--------|-----------|------------|-----|--------|
| savings-account | SavingsAccountScreen, SavingsWithdrawScreen | SavingsAccountViewModel, AccountWithdrawViewModel | 3+ | SavingsAccountModule | Complete |
| loan-account | LoanAccountScreen | LoanAccountViewModel | 3+ | LoanModule | Complete |
| share-account | ShareAccountScreen | ShareAccountViewModel | 2+ | ShareAccountModule | Complete |
| beneficiary | BeneficiaryListScreen, BeneficiaryApplicationScreen, BeneficiaryDetailScreen | BeneficiaryListViewModel, BeneficiaryApplicationViewModel, BeneficiaryDetailViewModel | 3+ | BeneficiaryModule | Complete |
| transfer-process | TransferProcessScreen, MakeTransferScreen | TransferProcessViewModel, MakeTransferViewModel | 3+ | TransferProcessModule | Complete |

### Phase 3: Applications (3 modules)

| Module | Screen | ViewModel | Components | DI | Status |
|--------|--------|-----------|------------|-----|--------|
| loan-application | LoanApplicationScreen, ConfirmDetailsScreen, LoanProductDetailsScreen, SelectLoanTypeScreen, UploadDocsScreen | LoanApplyViewModel, ConfirmDetailsViewModel, LoanProductDetailsViewModel, SelectLoanTypeViewModel, UploadDocsViewmodel | 5+ | LoanApplicationModule | Complete |
| savings-application | SavingsApplicationScreen, FillApplicationScreen | SavingsApplyViewModel, FillApplicationViewModel | 3+ | SavingsApplicationModule | Complete |
| share-application | ShareApplicationScreen | ShareApplicationViewModel | 2+ | ShareApplicationModule | Complete |

### Phase 4: Utilities (5 modules)

| Module | Screen | ViewModel | Components | DI | Status |
|--------|--------|-----------|------------|-----|--------|
| passcode | PasscodeScreen, VerifyPasscodeScreen | PasscodeViewModel, VerifyPasscodeViewModel | 2+ | PasscodeModule | Complete |
| qr | QrCodeReaderScreen, QrCodeDisplayScreen, QrCodeImportScreen | QrCodeReaderViewModel, QrCodeDisplayViewModel, QrCodeImportViewModel | 3+ | QrModule | Complete |
| location | LocationScreen | - | 2+ | - | Complete |
| guarantor | GuarantorListScreen, GuarantorDetailScreen, AddGuarantorScreen | GuarantorListViewModel, GuarantorDetailViewModel, AddGuarantorViewModel | 3+ | GuarantorModule | Complete |
| client-charge | ClientChargeScreen | ClientChargeViewModel | 2+ | ChargeModule | Complete |

### Additional Modules

| Module | Screen | ViewModel | Purpose |
|--------|--------|-----------|---------|
| onboarding-language | SetOnboardingLanguageScreen | SetOnboardingLanguageViewModel | Language selection |
| status | StatusScreen | StatusViewModel | Status display |
| third-party-transfer | ThirdPartyTransferScreen | ThirdPartyTransferViewModel | Third-party transfers |

---

## Module Details

### home/
```
home/
├── HomeScreen.kt
├── HomeViewModel.kt
├── ServiceItem.kt
├── components/
│   └── BottomSheetContent.kt
├── navigation/
│   └── HomeNavigation.kt
└── di/
    └── HomeModule.kt
```

### auth/
```
auth/
├── login/
│   ├── LoginScreen.kt
│   ├── LoginViewModel.kt
│   └── LogInNavigation.kt
├── registration/
│   ├── RegistrationScreen.kt
│   ├── RegistrationViewModel.kt
│   └── RegistrationNavigation.kt
├── otpAuthentication/
│   ├── OtpAuthenticationScreen.kt
│   ├── OtpAuthenticationViewModel.kt
│   └── OtpAuthenticationNavigation.kt
├── recoverPassword/
│   ├── RecoverPasswordScreen.kt
│   ├── RecoverPasswordViewModel.kt
│   └── RecoverPasswordNavigation.kt
├── setNewPassword/
│   ├── SetPasswordScreen.kt
│   ├── SetPasswordViewModel.kt
│   └── SetPasswordNavigation.kt
├── uploadId/
│   ├── UploadIdScreen.kt
│   ├── UploadIdViewmodel.kt
│   └── UploadIdNavigation.kt
├── navigation/
│   └── AuthenticationNavGraph.kt
└── di/
    └── AuthModule.kt
```

### accounts/
```
accounts/
├── accounts/
│   ├── AccountsScreen.kt
│   ├── AccountsViewModel.kt
│   └── AccountNavigation.kt
├── accountTransactions/
│   ├── TransactionScreen.kt
│   ├── TransactionViewModel.kt
│   └── AccountsTransactionNavigation.kt
├── component/
│   └── FilterSection.kt
├── model/
│   └── CheckboxStatus.kt
├── utils/
│   └── StatusUtils.kt
└── di/
    └── AccountsModule.kt
```

### beneficiary/
```
beneficiary/
├── beneficiaryList/
│   ├── BeneficiaryListScreen.kt
│   └── BeneficiaryListViewModel.kt
├── beneficiaryApplication/
│   ├── BeneficiaryApplicationScreen.kt
│   ├── BeneficiaryApplicationViewModel.kt
│   └── BeneficiaryApplicationContent.kt
├── beneficiaryDetail/
│   ├── BeneficiaryDetailScreen.kt
│   └── BeneficiaryDetailViewModel.kt
├── navigation/
│   └── BeneficiaryNavGraph.kt
└── di/
    └── BeneficiaryModule.kt
```

---

## MVI Pattern Reference

```kotlin
// State - UI data (immutable)
@Immutable
data class [Feature]State(
    val isLoading: Boolean = false,
    val items: List<Item> = emptyList(),
    val uiState: [Feature]ScreenState? = null,
)

// ScreenState - Loading/Success/Error
sealed interface [Feature]ScreenState {
    data object Loading : [Feature]ScreenState
    data object Success : [Feature]ScreenState
    data class Error(val message: StringResource) : [Feature]ScreenState
}

// Event - Navigation/one-shot effects
sealed interface [Feature]Event {
    data class NavigateToDetail(val id: Long) : [Feature]Event
}

// Action - User interactions
sealed interface [Feature]Action {
    data class OnItemClick(val id: Long) : [Feature]Action

    sealed interface Internal : [Feature]Action {
        data class ReceiveData(val dataState: DataState<Data>) : Internal
    }
}
```

---

## Build Commands

```bash
# Build specific feature
./gradlew :feature:home:build
./gradlew :feature:auth:build
./gradlew :feature:accounts:build
./gradlew :feature:beneficiary:build
./gradlew :feature:loan-account:build
./gradlew :feature:savings-account:build
./gradlew :feature:share-account:build
./gradlew :feature:transfer-process:build
./gradlew :feature:recent-transaction:build
./gradlew :feature:notification:build
./gradlew :feature:settings:build
./gradlew :feature:passcode:build
./gradlew :feature:qr:build
./gradlew :feature:location:build
./gradlew :feature:guarantor:build
./gradlew :feature:client-charge:build

# Build all features
./gradlew build
```

---

## Navigation Registration

All features registered in:
- **Routes**: `cmp-navigation/src/commonMain/kotlin/.../navigation/`
- **DI Modules**: `cmp-navigation/di/KoinModules.kt`

```kotlin
// KoinModules.kt
val featureModule = module {
    includes(
        AuthModule,
        HomeModule,
        AccountsModule,
        BeneficiaryModule,
        LoanModule,
        SavingsAccountModule,
        ShareAccountModule,
        TransferProcessModule,
        RecentTransactionModule,
        NotificationModule,
        SettingsModule,
        PasscodeModule,
        QrModule,
        GuarantorModule,
        ChargeModule,
        StatusModule,
    )
}
```

---

## Related Docs

- Patterns: `claude-product-cycle/design-spec-layer/_shared/PATTERNS.md`
- Feature Specs: `claude-product-cycle/design-spec-layer/features/[feature]/SPEC.md`
- Client Layer: `claude-product-cycle/client-layer/LAYER_STATUS.md`

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-26 | Created LAYER_STATUS.md |
| 2025-12-26 | All 22 feature modules verified complete |
