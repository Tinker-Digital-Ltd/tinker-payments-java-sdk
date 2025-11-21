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

