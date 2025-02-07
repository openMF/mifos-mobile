### Module Graph

```mermaid
%%{
  init: {
    'theme': 'neutral'
  }
}%%

graph LR
  subgraph :core
    :core:common["common"]
    :core:data["data"]
    :core:model["model"]
    :core:datastore["datastore"]
  end
  :mifos-desktop --> :core:common
  :mifos-desktop --> :core:data
  :mifos-desktop --> :core:model
  :mifos-desktop --> :core:datastore
  :mifos-desktop --> :mifos-shared
```