### Module Graph

```mermaid
%%{
  init: {
    'theme': 'neutral'
  }
}%%

graph LR
  subgraph :core
    :core:data["data"]
    :core:network["network"]
    :core:ui["ui"]
    :core:designsystem["designsystem"]
    :core:domain["domain"]
  end
  subgraph :feature
    :feature:auth["auth"]
    :feature:home["home"]
    :feature:settings["settings"]
    :feature:faq["faq"]
    :feature:editpassword["editpassword"]
    :feature:profile["profile"]
    :feature:history["history"]
    :feature:payments["payments"]
    :feature:finance["finance"]
    :feature:accounts["accounts"]
    :feature:invoices["invoices"]
    :feature:kyc["kyc"]
    :feature:notification["notification"]
    :feature:savedcards["savedcards"]
    :feature:receipt["receipt"]
    :feature:standing-instruction["standing-instruction"]
    :feature:request-money["request-money"]
    :feature:send-money["send-money"]
    :feature:make-transfer["make-transfer"]
    :feature:qr["qr"]
    :feature:merchants["merchants"]
    :feature:upi-setup["upi-setup"]
  end
  subgraph :libs
    :libs:mifos-passcode["mifos-passcode"]
  end
  :mifospay-shared --> :core:data
  :mifospay-shared --> :core:network
  :mifospay-shared --> :core:ui
  :mifospay-shared --> :core:designsystem
  :mifospay-shared --> :core:domain
  :mifospay-shared --> :feature:auth
  :mifospay-shared --> :libs:mifos-passcode
  :mifospay-shared --> :feature:home
  :mifospay-shared --> :feature:settings
  :mifospay-shared --> :feature:faq
  :mifospay-shared --> :feature:editpassword
  :mifospay-shared --> :feature:profile
  :mifospay-shared --> :feature:history
  :mifospay-shared --> :feature:payments
  :mifospay-shared --> :feature:finance
  :mifospay-shared --> :feature:accounts
  :mifospay-shared --> :feature:invoices
  :mifospay-shared --> :feature:kyc
  :mifospay-shared --> :feature:notification
  :mifospay-shared --> :feature:savedcards
  :mifospay-shared --> :feature:receipt
  :mifospay-shared --> :feature:standing-instruction
  :mifospay-shared --> :feature:request-money
  :mifospay-shared --> :feature:send-money
  :mifospay-shared --> :feature:make-transfer
  :mifospay-shared --> :feature:qr
  :mifospay-shared --> :feature:merchants
  :mifospay-shared --> :feature:upi-setup
```