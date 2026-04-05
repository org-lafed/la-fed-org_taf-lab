# Scenario Catalog

## UI

- Home page smoke: verify landing page, header navigation, hero visibility, and tolerant cookie consent handling.
- Register user: stable signup happy path from home page to account creation confirmation and logged-in marker.
- Valid login: seed a user through API, login through UI, and verify the logged-in marker in the header.
- Search product: verify products list visibility, search for a known product, and confirm searched results.
- Product details: open a visible product details page and verify the core product information is displayed.
- Remove from cart: add a visible product to cart, remove it, and verify the empty cart state.
- Subscription: submit a unique email address through the footer subscription form and verify the success message.
- Contact us with upload: submit the contact form with a real uploaded file and verify the success confirmation.

## API

- Get all products: validate status, payload shape, and business-critical fields.
- User account operations: starter support for create, get detail, and delete flows used for test data setup and cleanup.

## E2E

- API to UI checkout: seed state through API/test data, then drive a browser checkout path. Kept compile-safe in V1 while checkout locator validation is pending.
