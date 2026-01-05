# Feature Layer - Testing Status

> ViewModel, Screen, and Integration testing documentation

---

## Overview

The feature layer is the primary testing target. Each feature needs:
- ViewModel unit tests (state, events, actions)
- Screen UI tests (rendering, interactions)
- Integration tests (full user flows)

---

## Current State

| Component | Total | Tested | Coverage |
|-----------|:-----:|:------:|:--------:|
| ViewModels | 49 | 0 | 0% |
| Screens | 63 | 0 | 0% |
| Integration Flows | 8 | 0 | 0% |

---

## Testing Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│  FEATURE LAYER TESTING STACK                                                │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │  Integration Tests (E2E Flows)                                        │  │
│  │  - Login → Passcode → Home                                           │  │
│  │  - Home → Transfer → Confirm                                         │  │
│  │  - Location: cmp-android/src/androidTest/                            │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
│                              │                                              │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │  UI Tests (Screen Tests)                                              │  │
│  │  - Compose test rules                                                │  │
│  │  - TestTag-based assertions                                          │  │
│  │  - Location: feature/*/src/androidInstrumentedTest/                  │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
│                              │                                              │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │  Unit Tests (ViewModel Tests)                                         │  │
│  │  - StateFlow testing with Turbine                                    │  │
│  │  - Event emission testing                                            │  │
│  │  - Action handling testing                                           │  │
│  │  - Location: feature/*/src/commonTest/                               │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## ViewModel Testing Status

### By Feature

| # | Feature | VMs | Tests | Coverage | Status |
|:-:|---------|:---:|:-----:|:--------:|:------:|
| 1 | auth | 5 | 0 | 0% | ⬜ Not Started |
| 2 | home | 1 | 0 | 0% | ⬜ Not Started |
| 3 | accounts | 3 | 0 | 0% | ⬜ Not Started |
| 4 | savings-account | 3 | 0 | 0% | ⬜ Not Started |
| 5 | loan-account | 4 | 0 | 0% | ⬜ Not Started |
| 6 | share-account | 2 | 0 | 0% | ⬜ Not Started |
| 7 | beneficiary | 4 | 0 | 0% | ⬜ Not Started |
| 8 | transfer-process | 2 | 0 | 0% | ⬜ Not Started |
| 9 | recent-transaction | 1 | 0 | 0% | ⬜ Not Started |
| 10 | notification | 1 | 0 | 0% | ⬜ Not Started |
| 11 | settings | 5 | 0 | 0% | ⬜ Not Started |
| 12 | mifos-passcode | 2 | 0 | 0% | ⬜ Not Started |
| 13 | guarantor | 3 | 0 | 0% | ⬜ Not Started |
| 14 | qr-code | 3 | 0 | 0% | ⬜ Not Started |
| 15 | location | 1 | 0 | 0% | ⬜ Not Started |
| 16 | user-profile | 2 | 0 | 0% | ⬜ Not Started |

**Legend**: ✅ 80%+ | ⚠️ <80% | ⬜ 0%

---

## UI Testing Status

### TestTag Coverage

| # | Feature | Screens | TestTags | Coverage | Status |
|:-:|---------|:-------:|:--------:|:--------:|:------:|
| 1 | auth | 6 | ~40% | Partial | ⚠️ |
| 2 | home | 1 | ~30% | Partial | ⚠️ |
| 3 | accounts | 3 | ~30% | Partial | ⚠️ |
| 4 | savings-account | 4 | ~30% | Partial | ⚠️ |
| 5 | loan-account | 4 | ~30% | Partial | ⚠️ |
| 6 | share-account | 2 | ~30% | Partial | ⚠️ |
| 7 | beneficiary | 4 | ~30% | Partial | ⚠️ |
| 8 | transfer-process | 2 | ~30% | Partial | ⚠️ |
| 9 | recent-transaction | 1 | ~30% | Partial | ⚠️ |
| 10 | notification | 1 | ~30% | Partial | ⚠️ |
| 11 | settings | 9 | ~30% | Partial | ⚠️ |
| 12 | mifos-passcode | 2 | ~30% | Partial | ⚠️ |
| 13 | guarantor | 3 | ~30% | Partial | ⚠️ |
| 14 | qr-code | 3 | ~30% | Partial | ⚠️ |
| 15 | location | 1 | ~30% | Partial | ⚠️ |
| 16 | user-profile | 2 | ~30% | Partial | ⚠️ |

**Legend**: ✅ 80%+ TestTags | ⚠️ <80% TestTags | ⬜ No TestTags

---

## Integration Test Status

### Critical User Flows

| # | Flow | Screens | Tests | Status |
|:-:|------|:-------:|:-----:|:------:|
| 1 | Login → Passcode → Home | 3 | 0 | ⬜ |
| 2 | Registration → OTP → Login | 4 | 0 | ⬜ |
| 3 | Home → Account Details | 2 | 0 | ⬜ |
| 4 | Home → Transfer → Confirm | 3 | 0 | ⬜ |
| 5 | Home → Beneficiary → Add | 2 | 0 | ⬜ |
| 6 | Settings → Change Password | 2 | 0 | ⬜ |
| 7 | Loan → Schedule → Summary | 3 | 0 | ⬜ |
| 8 | QR → Scan → Transfer | 3 | 0 | ⬜ |

---

## TestTag System

### Pattern: `feature:component:element`

```kotlin
object TestTags {
    object Auth {
        const val SCREEN = "auth:screen"
        const val USERNAME_FIELD = "auth:username"
        const val PASSWORD_FIELD = "auth:password"
        const val LOGIN_BUTTON = "auth:loginButton"
        const val ERROR_MESSAGE = "auth:error"
        const val LOADING_INDICATOR = "auth:loading"
    }

    object Home {
        const val SCREEN = "home:screen"
        const val LOAN_BALANCE = "home:loanBalance"
        const val SAVINGS_BALANCE = "home:savingsBalance"
        const val TRANSFER_BUTTON = "home:transferButton"
        const val ACCOUNTS_CARD = "home:accountsCard"
    }

    // ... for all 17 features
}
```

### Usage in Screens

```kotlin
@Composable
fun LoginScreenContent(
    state: LoginState,
    onAction: (LoginAction) -> Unit
) {
    Column(modifier = Modifier.testTag(TestTags.Auth.SCREEN)) {
        MifosOutlinedTextField(
            value = state.username,
            onValueChange = { onAction(LoginAction.UsernameChanged(it)) },
            modifier = Modifier.testTag(TestTags.Auth.USERNAME_FIELD)
        )
        // ...
    }
}
```

---

## ViewModel Test Template

```kotlin
class LoginViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: LoginViewModel
    private lateinit var fakeUserAuthRepository: FakeUserAuthRepository

    @BeforeTest
    fun setup() {
        fakeUserAuthRepository = FakeUserAuthRepository()
        viewModel = LoginViewModel(
            userAuthRepository = fakeUserAuthRepository,
            savedStateHandle = SavedStateHandle()
        )
    }

    // STATE TESTS
    @Test
    fun `initial state has empty credentials`() = runTest {
        viewModel.stateFlow.test {
            val state = awaitItem()
            assertEquals("", state.username)
            assertEquals("", state.password)
        }
    }

    // ACTION TESTS
    @Test
    fun `username changed updates state`() = runTest {
        viewModel.trySendAction(LoginAction.UsernameChanged("testuser"))

        viewModel.stateFlow.test {
            assertEquals("testuser", expectMostRecentItem().username)
        }
    }

    // EVENT TESTS
    @Test
    fun `login success navigates to passcode`() = runTest {
        fakeUserAuthRepository.setResult(DataState.Success(UserFixture.create()))

        viewModel.trySendAction(LoginAction.LoginClicked)

        viewModel.eventFlow.test {
            assertEquals(LoginEvent.NavigateToPasscode, awaitItem())
        }
    }
}
```

---

## UI Test Template

```kotlin
class LoginScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `login screen displays all elements`() {
        composeTestRule.setContent {
            LoginScreenContent(state = LoginState(), onAction = {})
        }

        composeTestRule.onNodeWithTag(TestTags.Auth.USERNAME_FIELD).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.Auth.PASSWORD_FIELD).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTags.Auth.LOGIN_BUTTON).assertIsDisplayed()
    }

    @Test
    fun `login button disabled when credentials empty`() {
        composeTestRule.setContent {
            LoginScreenContent(
                state = LoginState(username = "", password = ""),
                onAction = {}
            )
        }

        composeTestRule.onNodeWithTag(TestTags.Auth.LOGIN_BUTTON).assertIsNotEnabled()
    }
}
```

---

## Implementation Priority

### Phase 1: Core Features (P0)
| Feature | VMs | Tests Needed | Effort |
|---------|:---:|:------------:|:------:|
| auth | 5 | 50 | L |
| home | 1 | 12 | M |
| accounts | 3 | 24 | M |
| transfer-process | 2 | 20 | M |

### Phase 2: Account Features (P1)
| Feature | VMs | Tests Needed | Effort |
|---------|:---:|:------------:|:------:|
| beneficiary | 4 | 32 | M |
| loan-account | 4 | 32 | M |
| savings-account | 3 | 24 | M |
| share-account | 2 | 16 | S |

### Phase 3: Supporting Features (P2)
| Feature | VMs | Tests Needed | Effort |
|---------|:---:|:------------:|:------:|
| settings | 5 | 40 | L |
| notification | 1 | 8 | S |
| recent-transaction | 1 | 8 | S |
| qr-code | 3 | 24 | M |
| mifos-passcode | 2 | 16 | M |
| guarantor | 3 | 24 | M |
| user-profile | 2 | 16 | S |
| location | 1 | 8 | S |

---

## Commands

```bash
# Run feature tests
./gradlew :feature:auth:test
./gradlew :feature:home:test

# Run UI tests
./gradlew :cmp-android:connectedDebugAndroidTest

# Check test status
/gap-analysis feature testing

# Plan feature tests
/gap-planning feature auth testing
```

---

## Related Files

- [MODULES_INDEX.md](./MODULES_INDEX.md) - All feature modules
- [SCREENS_INDEX.md](./SCREENS_INDEX.md) - All screens
- [LAYER_GUIDE.md](./LAYER_GUIDE.md) - Architecture patterns
