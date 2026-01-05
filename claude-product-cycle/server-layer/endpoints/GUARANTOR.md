# Guarantor Endpoints

> **Category**: Guarantor
> **Endpoints**: 5
> **Service**: `GuarantorService`

---

## GET /loans/{loanId}/guarantors

**Purpose**: Get list of guarantors for a loan

**Service**: `GuarantorService.getGuarantorList(loanId)`

**Response**:
```json
[
    {
        "id": 1,
        "loanId": 67890,
        "clientRelationshipType": { "id": 1, "value": "Spouse" },
        "guarantorType": { "id": 1, "value": "External" },
        "firstname": "Jane",
        "lastname": "Doe",
        "mobileNumber": "1234567890",
        "status": true
    }
]
```

**DTO**: `List<GuarantorPayload>`

---

## GET /loans/{loanId}/guarantors/template

**Purpose**: Get guarantor template (relationship types, guarantor types)

**Service**: `GuarantorService.getGuarantorTemplate(loanId)`

**Response**:
```json
{
    "guarantorTypeOptions": [
        { "id": 1, "value": "External" },
        { "id": 2, "value": "Internal" }
    ],
    "clientRelationshipTypeOptions": [
        { "id": 1, "value": "Spouse" },
        { "id": 2, "value": "Parent" },
        { "id": 3, "value": "Sibling" }
    ]
}
```

**DTO**: `GuarantorTemplatePayload`

---

## POST /loans/{loanId}/guarantors

**Purpose**: Add guarantor to loan

**Service**: `GuarantorService.addGuarantor(loanId, payload)`

**Request**:
```json
{
    "guarantorTypeId": 1,
    "clientRelationshipTypeId": 1,
    "firstname": "Jane",
    "lastname": "Doe",
    "addressLine1": "123 Main St",
    "city": "City",
    "mobileNumber": "1234567890",
    "locale": "en",
    "dateFormat": "dd MMMM yyyy"
}
```

**Response**:
```json
{
    "loanId": 67890,
    "resourceId": 1
}
```

**DTO**: `GuarantorPayload`

---

## PUT /loans/{loanId}/guarantors/{guarantorId}

**Purpose**: Update guarantor

**Service**: `GuarantorService.updateGuarantor(loanId, guarantorId, payload)`

**Request**:
```json
{
    "guarantorTypeId": 1,
    "clientRelationshipTypeId": 2,
    "firstname": "Jane",
    "lastname": "Doe Updated",
    "mobileNumber": "0987654321"
}
```

---

## DELETE /loans/{loanId}/guarantors/{guarantorId}

**Purpose**: Delete guarantor

**Service**: `GuarantorService.deleteGuarantor(loanId, guarantorId)`

**Response**:
```json
{
    "loanId": 67890,
    "resourceId": 1
}
```

---

## Related

- Design Layer: [guarantor/API.md](../../design-spec-layer/features/guarantor/API.md)
- Patterns: [CLIENT_PATTERNS.md](../CLIENT_PATTERNS.md)
