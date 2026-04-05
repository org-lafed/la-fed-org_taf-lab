# Test Data Model

## TestData Categories

The repository currently uses three kinds of test data:

- Static resource data committed under `src/test/resources`.
- Generated user-oriented data created at runtime.
- Optional shared scenario state held in `StateStore`.

## Static Test Data

| Data Asset | Path | Current Use |
|---|---|---|
| Register user sample data | `src/test/resources/testdata/ui/register-user.json` | Reserved support data for UI registration-style scenarios |
| Create user API sample data | `src/test/resources/testdata/api/create-user.json` | Reserved support data for API user operations |
| Account checkout data | `src/test/resources/testdata/e2e/account-checkout.json` | Intended for checkout-oriented E2E once stabilized |
| Products schema | `src/test/resources/schemas/products-list-schema.json` | Used by `GetProductsApiTest` schema validation |

## Generated Test Data

| Generator | Repository Element | Current Use |
|---|---|---|
| Timestamp suffix | `FakeDataGenerator.timestamp()` | Produce unique test user identifiers |
| User builder | `UserDataBuilder` | Create registration and login test users |

## Shared Scenario State

`StateStore` is implemented today, but its heavy usage is still a V1 modeling concern rather than a broad as-is behavior.

| Example State Key | Current Status | Intended Meaning |
|---|---|---|
| `registeredUserEmail` | Target V1 | Persist user identity across steps |
| `userId` | Target V1 | Persist created user identifier when exposed |
| `cartState` | Target V1 | Persist cart-related state across UI/E2E steps |
| `createdOrderId` | Target V2 direction | Persist order confirmation data |

## Data Handling Notes

- Registration and login tests generate user data to avoid collisions on the live site.
- UI teardown uses best-effort API cleanup for generated users.
- Checkout data files exist, but the live checkout path is not yet stabilized and must not be presented as fully implemented.
