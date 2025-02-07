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
    :core:ui["ui"]
  end
  :mifospay-android --> :mifospay-shared
  :mifospay-android --> :core:data
  :mifospay-android --> :core:ui
```