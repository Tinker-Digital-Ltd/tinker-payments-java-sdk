# Tinker Payments Java SDK

Official Java SDK for [Tinker Payments API](https://payments.tinker.co.ke/docs).

## Installation

### Maven

Add this dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>co.ke.tinker</groupId>
    <artifactId>tinker-payments-java-sdk</artifactId>
    <version>0.1.0</version>
</dependency>
```

### Gradle

Add this dependency to your `build.gradle`:

```gradle
dependencies {
    implementation 'co.ke.tinker:tinker-payments-java-sdk:0.1.0'
}
```

### GitHub Packages

To install from GitHub Packages, add the repository to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>github</id>
        <name>GitHub Packages</name>
        <url>https://maven.pkg.github.com/tinker/payments-java-sdk</url>
    </repository>
</repositories>

<dependency>
    <groupId>co.ke.tinker</groupId>
    <artifactId>tinker-payments-java-sdk</artifactId>
    <version>0.1.0</version>
</dependency>
```

**Note:** You'll need to authenticate with GitHub Packages. Add your GitHub token to `~/.m2/settings.xml`:

```xml
<settings>
  <servers>
    <server>
      <id>github</id>
      <username>YOUR_GITHUB_USERNAME</username>
      <password>YOUR_GITHUB_TOKEN</password>
    </server>
  </servers>
</settings>
```

For Gradle, add to `build.gradle`:

```gradle
repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/tinker/payments-java-sdk")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("GITHUB_USERNAME")
            password = project.findProperty("gpr.token") ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    implementation 'co.ke.tinker:tinker-payments-java-sdk:0.1.0'
}
```

## Requirements

- Java 11 or higher
- Maven or Gradle

## Quick Start

```java
import co.ke.tinker.Payments;

Payments tinker = new Payments(
    "your-public-key",
    "your-secret-key"
);
```

## Usage

### Initiate a Payment

```java
import co.ke.tinker.Payments;
import co.ke.tinker.enums.Gateway;
import co.ke.tinker.model.dto.InitiatePaymentRequestDto;
import co.ke.tinker.model.dto.InitiationDataDto;
import co.ke.tinker.model.Transaction;
import co.ke.tinker.exception.ApiException;
import co.ke.tinker.exception.NetworkException;

try {
    InitiatePaymentRequestDto request = new InitiatePaymentRequestDto(
        100.00,
        "KES",
        Gateway.MPESA,
        "ORDER-12345",
        "https://your-app.com/payment/return",
        "+254712345678",
        "customer@example.com",
        "Payment for order #12345",
        null
    );

    Transaction transaction = tinker.transactions().initiate(request);
    InitiationDataDto initiationData = transaction.getInitiationData();

    if (initiationData != null && initiationData.getAuthorizationUrl() != null) {
        // Redirect user to authorization URL (Paystack, Stripe, etc.)
        // redirectTo(initiationData.getAuthorizationUrl());
    }
} catch (ApiException e) {
    System.out.println("API Error: " + e.getMessage());
} catch (NetworkException e) {
    System.out.println("Network Error: " + e.getMessage());
}
```

**Note:** The `returnUrl` is where users are redirected after payment completion. Webhooks are configured separately in your dashboard.

### Query a Transaction

```java
import co.ke.tinker.model.dto.QueryPaymentRequestDto;
import co.ke.tinker.model.dto.QueryDataDto;

QueryPaymentRequestDto queryRequest = new QueryPaymentRequestDto(
    "TXN-abc123xyz",
    Gateway.MPESA
);

Transaction transaction = tinker.transactions().query(queryRequest);

if (transaction.isSuccessful()) {
    QueryDataDto queryData = transaction.getQueryData();
    System.out.println("Amount: " + queryData.getAmount() + " " + queryData.getCurrency());
}
```

### Handle Webhooks

Webhooks support multiple event types: payment, subscription, invoice, and settlement. Check the event type and handle accordingly:

```java
import co.ke.tinker.webhook.WebhookEvent;
import co.ke.tinker.webhook.dto.PaymentEventDataDto;
import co.ke.tinker.webhook.dto.SubscriptionEventDataDto;
import co.ke.tinker.webhook.dto.InvoiceEventDataDto;
import co.ke.tinker.webhook.dto.SettlementEventDataDto;

WebhookEvent event = tinker.webhooks().handleFromRequest(requestBody);

// Check event type
if (event.isPaymentEvent()) {
    PaymentEventDataDto paymentData = event.getPaymentData();
    // Handle payment.completed, payment.failed, etc.
} else if (event.isSubscriptionEvent()) {
    SubscriptionEventDataDto subscriptionData = event.getSubscriptionData();
    // Handle subscription.created, subscription.cancelled, etc.
} else if (event.isInvoiceEvent()) {
    InvoiceEventDataDto invoiceData = event.getInvoiceData();
    // Handle invoice.paid, invoice.failed
} else if (event.isSettlementEvent()) {
    SettlementEventDataDto settlementData = event.getSettlementData();
    // Handle settlement.processed
}

// Access event details
System.out.println("Event type: " + event.getType());        // e.g., "payment.completed"
System.out.println("Event source: " + event.getSource());    // e.g., "payment"
System.out.println("App ID: " + event.getMeta().getAppId());
System.out.println("Signature: " + event.getSecurity().getSignature());
```

For payment events only, you can convert to a `Transaction` object:

```java
import co.ke.tinker.model.dto.CallbackDataDto;

Transaction transaction = tinker.webhooks().handleAsTransaction(requestBody);
if (transaction != null && transaction.isSuccessful()) {
    CallbackDataDto callbackData = transaction.getCallbackData();
    System.out.println("Payment successful: " + callbackData.getReference());
}
```

## Custom HTTP Client

You can use your own HTTP client by passing it to the constructor. However, the current implementation uses OkHttp. To use a custom client, you would need to implement a compatible HTTP client interface or modify the `HttpClient` class.

```java
import co.ke.tinker.http.HttpClient;

// Create a custom HTTP client that implements the same interface
HttpClient customClient = new MyCustomHttpClient();

Payments tinker = new Payments(
    "your-public-key",
    "your-secret-key",
    customClient
);
```

## Documentation

For detailed API documentation, visit [Tinker Payments API Documentation](https://payments.tinker.co.ke/docs).

## Development

After checking out the repo, run `mvn install` to build the project. Then, run `mvn test` to run the tests.

## Contributing

Bug reports and pull requests are welcome on GitHub at https://github.com/tinker/payments-java-sdk.

## License

MIT License


## API errors

API exceptions preserve `getHttpStatus()`, `getErrorCode()`, `getProviderCode()`, `getRequestId()`, `getRetryAfter()`, and `getOutcome()`.
Request IDs prefer `error.request_id` over the `X-Request-ID` header. `Retry-After`
is retained as its original header string. Non-JSON HTTP failures retain status and
headers as API errors with an unknown outcome. Existing exception constructors and
legacy code/message access remain compatible.

Only an explicit `not_applied` outcome confirms no mutation. A missing outcome
remains uncertain.

## Managed recurring billing

Requires a Tinker server with the managed-billing, payment-method and payment-history
endpoints enabled. Installing the SDK does not deploy or enable those endpoints.
Existing plan-based subscription methods remain available.

Checkout uses exact integer minor-unit prices (for example, `15000` means EUR 150.00),
an explicit recurring interval (`month` or `year`), and a quantity. The SDK does not
convert prices or apply automatic discounts. Managed responses are unwrapped API
data objects/maps; payment history includes `items`, `snapshot`, `has_more` and
`next_cursor`.

| Method | Purpose |
|---|---|
| `checkout` | Create an exact-price recurring checkout |
| `billingResource` | Read a managed billing resource |
| `changeQuantity` | Change the current quantity |
| `scheduleQuantity` | Schedule a quantity change |
| `clearScheduledQuantity` | Remove a scheduled quantity change |
| `setCancelAtRenewal` | Set cancellation at renewal; pass false to clear it |
| `billingOperation` | Recover the durable result by idempotency key |
| `paymentMethodPortal` | Create a short-lived hosted payment-method link |
| `billingPaymentHistory` | Read one page of captured payments and adjustments |

```java
import co.ke.tinker.model.dto.ManagedCheckout;
import co.ke.tinker.exception.ApiException;

var billing = payments.subscriptions();
billing.checkout(new ManagedCheckout(
    "workspace-123", "buyer@example.com", "Buyer", "EUR", 15000L, 3, "year",
    "https://example.com/billing", "checkout-123"));
billing.changeQuantity("resource-id", 4, "quantity-123");
billing.setCancelAtRenewal("resource-id", false, "resume-123");
billing.paymentMethodPortal("resource-id", "https://example.com/billing", "portal-123");
var page = billing.billingPaymentHistory("resource-id", "", 0L);
// After an uncertain command response, recover using the SAME key:
var operation = billing.billingOperation("quantity-123");
// Catch ApiException and inspect getHttpStatus(), getOutcome(), etc.
```

`ManagedCheckout` uses `long` for minor-unit amounts and `int` for quantities.
`billingPaymentHistory(id)` is also available for the initial page.

Use a stable, unique idempotency key for each mutation, and reuse it only with the
same payload. After a timeout, retrieve the durable result with the operation lookup
method. Missing or pending results do not prove a charge failed. Do not retry with a
new key to work around an uncertain result. Only an explicit `outcome == "not_applied"`
confirms no mutation; missing outcome remains uncertain. No automatic charge retry is
performed by these methods.

The payment-method portal updates payment details only. It cannot change quantity or
price, and paying an overdue invoice is a separate action through its hosted invoice
URL. Reuse the operation key after an uncertain response; use a new key to deliberately
open a fresh session after expiry. The server checks app ownership and requires a
managed subscription with a dedicated customer.

For payment history, start with an empty cursor and zero snapshot, then retain the
returned snapshot and pass `next_cursor` until `has_more` is false. Amounts are integer
minor units and timestamps are UTC RFC3339. The history includes captured cash payments,
tax-exclusive eligible amounts, successful refunds, and dispute principal withdrawals
and reinstatements (`reverses_id` links a reinstatement to its withdrawal).

Quarantine items with `reconciliation_reason`; commit a scan only after every page has
been verified. The snapshot is an event cutoff, not an immutable provider snapshot.
Rescan from the first page regularly and apply facts idempotently by payment/adjustment
ID. Zero-cash invoices are omitted from items while still advancing pagination. Currency
conversion and customer credits require explicit reconciliation; processing and dispute
fees are excluded from eligible principal. The SDK returns these facts without applying
credits or reconciling them automatically.

### Setup webhook acknowledgement

The dashboard sends a signed `setup.webhook_test` event with `source: "onboarding"`, `data.message`, and setup metadata. Parse it with the webhook handler, verify its signature using the application webhook secret, then acknowledge it with HTTP 200, 202, or 204. Setup events have onboarding data and do not convert to payment transactions. Event classification alone does not authenticate a request. Invalid signatures must be rejected; no payment lookup or fulfillment is needed for a valid setup test.

The signature uses HMAC-SHA256 over the JSON envelope excluding `security`, preserving envelope field order and Go JSON map ordering and escaping.

## Refunds, withdrawals and checkout cancellation

Use `transactions().refund/refunds/getRefund`, `transactions().cancelCheckout/cancellations/getCancellation` and `payouts().methods/quote/requestPayout/list/get` for merchant lifecycle operations. Every creation requires a stable caller-provided idempotency key. SDK payouts withdraw your merchant balance to your own verified payout account and require the owner's explicit app withdrawal grant. Review/processing/unknown statuses do not confirm money movement or provider cancellation.

See the [typed workflow examples and error/retry guidance](docs/merchant-lifecycle.md) and [unreleased lifecycle notes](docs/merchant-lifecycle-release-notes.md). Signed cancellation updates expose typed cancellation data and never convert into payment transactions.
