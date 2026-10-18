# :feature:pocket module

Pocket is the client account-linking feature. It presents linked loan, savings, and share accounts together, calculates the displayed Pocket balance, and lets the client add or remove account links.

## Screens and navigation

The feature has two destinations:

- `PocketDashboardRoute` displays the linked accounts and total active balance. Accounts are grouped into loan, savings, and share sections. Selecting an account emits an event that routes to the corresponding account-detail feature.
- `ManagePocketRoute` displays linked accounts and opens the link-account or delink-confirmation flows. The link flow supports account-type tabs, search, multi-selection, and submit/error states.

The graph entry point is `PocketGraphRoute`, which registers the dashboard and management destinations. `PocketModule` provides the feature dependencies.

## Dashboard behavior

`PocketDashboardViewModel` loads detailed Pocket accounts for the current client and maps each account into `DetailedPocket`. Active accounts display a formatted balance; non-active accounts display their account status. The ViewModel also:

- Groups accounts by `AccountType.LOAN`, `SAVINGS`, and `SHARE`.
- Aggregates active balances by currency for the total shown on the dashboard.
- Exposes loading, empty, error, network, refresh, and retry states.
- Emits navigation events for loan, savings, and share account details.

## Manage Pocket behavior

`ManagePocketViewModel` observes linked accounts and available accounts through the repository. It keeps selection, tab, and search state local to the management screen. Available accounts are filtered by the selected account type and by product name or account number.

Linking sends the selected account IDs and types together with the full selected account details. Delinking sends the Pocket mapping ID after confirmation. Successful mutations clear the dialog and refresh the linked/available data; failures show the feature-specific error string.

## Repository

Implementation: [`PocketRepositoryImp`](../../core/data/src/nonJsCommonMain/kotlin/org/mifos/mobile/core/data/repositoryImpl/PocketRepositoryImp.kt)

Contract: [`PocketRepository`](../../core/data/src/commonMain/kotlin/org/mifos/mobile/core/data/repository/PocketRepository.kt)

The repository coordinates `DataManager`, `NetworkMonitor`, and `PocketAccountDao`:

- `getPocketAccounts()` synchronizes basic Pocket mappings and returns the DAO’s persisted mappings.
- `getDetailedPocketAccounts(clientId, forceRefresh)` loads mappings, joins them with the client’s loan/savings/share account response, and emits `DetailedPocketAccount` values through the detailed cache.
- `getAvailableAccountsToLink(clientId)` builds `LinkableAccount` values from client accounts and excludes accounts already linked in the detailed cache.
- `linkAccounts(...)` optimistically persists the explicitly selected accounts with temporary negative mapping IDs, updates the detailed cache, then sends the link request when online.
- `linkAccount(...)` performs the same local-first operation for one account.
- `delinkAccounts(...)` removes the selected local rows immediately, records positive server mapping IDs as pending delinks, and retries the remote request when online.
- `resetPocketCache()` clears the detailed result and its client key.

Account details are enriched by account type. Loan balances come from `loanBalance`, savings balances from `accountBalance`, and share balances are calculated as approved shares multiplied by the current market price. If share-detail loading fails, the repository falls back to the client account response.

Synchronization first retries persisted pending delinks, fetches the server Pocket mappings, submits locally retained links that are missing on the server, and merges any still-local mappings before replacing the DAO contents. This preserves user actions across temporary network failures while allowing the next successful refresh to reconcile server IDs.

## Repository tests

[`PocketRepositoryTest`](../../core/data/src/nonJsCommonTest/kotlin/org/mifos/mobile/core/data/repositoryImpl/PocketRepositoryTest.kt) exercises the repository with fake client, Pocket, and share services, a fake network monitor, and an in-memory DAO.

The test cases cover:

- Mapping all three Pocket account types and persisting them.
- Returning persisted mappings when the Pocket API fails.
- Enriching a Pocket with client account product, balance, currency, and status.
- Creating typed single-account and multi-account link requests.
- Excluding already linked accounts from the available-account result.
- Refreshing the detailed cache after a link.
- Sending only positive mapping IDs for remote delinks.
- Removing delinked accounts from the cache and retaining failed delinks for retry.
- Retrying persisted pending delinks on the next Pocket load.
- Avoiding duplicate remote delinks when detailed data has not been loaded first.

## Source layout

```text
feature/pocket/src/commonMain/kotlin/org/mifos/mobile/feature/pocket/
├── di/PocketModule.kt
├── navigation/PocketGraphRoute.kt
├── pocketDashboard/
│   ├── PocketDashboardRoute.kt
│   ├── PocketDashboardScreen.kt
│   └── PocketDashboardViewModel.kt
└── managePocket/
    ├── ManagePocketRoute.kt
    ├── ManagePocketScreen.kt
    └── ManagePocketViewModel.kt
```

Feature tests are under `feature/pocket/src/commonTest`, with separate dashboard, management, link, and delink coverage. Pocket data support lives in `core/model`, `core/network`, `core/database`, and `core/data`.
