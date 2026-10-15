# Refunds, merchant withdrawals and checkout cancellation

These APIs use the authenticated app's scope under `/v1/merchant`. The existing client adds `/v1` to a custom host automatically. Refunds and cancellations require exact payment/app ownership. Payout reads show only this app's attributed withdrawals.

## Refund requests and review

```java
import co.ke.tinker.Payments;
import co.ke.tinker.model.lifecycle.*;

Payments client = new Payments("pk_test_your_key", "sk_test_your_secret");

// Persist these keys with the business operation before the first submission.
String refundKey = "refund-order-123-v1";
RefundRequest refund = new RefundRequest(RefundReason.REQUESTED_BY_CUSTOMER);
refund.amount = 25.0; // Omit amount to request the full refundable remainder.
RefundResult requested = client.transactions().refund("payment-id", refund, refundKey);
Refund currentRefund = client.transactions().getRefund("payment-id", requested.refund.id);
boolean customerRefunded = currentRefund.status == RefundStatus.REFUNDED;
RefundHistory history = client.transactions().refunds("payment-id");
```

A returned request usually has `pending_review` status. Only `refunded` confirms a completed refund. Approved, processing and failed requests can still hold funds while the platform reviews or reconciles them. Unsupported automatic provider execution can use the existing manual refund workflow; the capability describes the execution mode.

## Withdraw your merchant balance

Payout means withdrawal of the merchant's available Tinker wallet balance to its own active, verified payout account. Create and verify destinations in the existing dashboard. The account owner must explicitly enable **SDK withdrawal permission** in the app's settings; existing apps start disabled. SDK access cannot approve withdrawals, verify destinations or pay arbitrary recipients.

```java
PayoutMethod destination = client.payouts().methods().stream()
    .filter(method -> Boolean.TRUE.equals(method.isVerified) && "active".equals(method.status))
    .findFirst().orElseThrow();
PayoutQuoteRequest quoteRequest = new PayoutQuoteRequest(destination.id, 100.0);
quoteRequest.currency = destination.currency;
PayoutQuote quote = client.payouts().quote(quoteRequest);
PayoutResult accepted = client.payouts().requestPayout(
    new PayoutRequest(destination.id, 100.0, destination.currency, quote.feeAmount),
    "withdrawal-order-123-v1");
Payout withdrawal = client.payouts().get(accepted.withdrawal.id);
boolean transferred = withdrawal.status == PayoutStatus.PAID;
PayoutFilters filters = new PayoutFilters();
filters.page = 1;
filters.perPage = 20;
PayoutPage page = client.payouts().list(filters);
```

Always quote before submitting and send the displayed `fee_amount` as `expected_fee_amount`. A gross request of 100 with a fee of 2 reserves 100 and pays 98. Rejection returns the gross reservation without charging the fee. A fee-change conflict contains a fresh quote; review it before making a new intended request with a new key.

`pending_approval`, `approved` and `processing` do not mean money was sent. Only `paid` confirms completion. Manual transfers and external evidence stay in the existing admin queue; app responses omit private evidence. Listing supports `page`, `per_page`, `status` and `currency`, with pagination in the response.

## Cancel an unpaid checkout

```java
CancellationHistory cancellations = client.transactions().cancellations("payment-id");
if (cancellations.capability.supported && cancellations.capability.eligible) {
    CancellationResult acceptedCancellation = client.transactions().cancelCheckout(
        "payment-id", new CancellationRequest("Customer left checkout"),
        "cancel-order-123-v1");
    Cancellation outcome = client.transactions().getCancellation(
        "payment-id", acceptedCancellation.cancellation.id);
    boolean providerConfirmed = outcome.status == CancellationStatus.CANCELLED;
}
```

Use the Tinker payment ID. Capability depends on the provider, exact resource and current payment state. Stripe hosted Checkout is supported only when the API reports it eligible. A paid payment needs the refund workflow. Browser redirects and locally blocking a payment never prove provider cancellation.

If `payment_conflict` is true, successful collection conflicts with provider cancellation evidence. The payment remains successful: follow its investigation and use the refund workflow if money must be returned. Cancellation never proves a refund or void.

Cancellation statuses are `requested`, `processing`, `cancelled`, `rejected` and `outcome_unknown`. Only `cancelled` confirms provider evidence. For pending or unknown outcomes, keep the request ID and poll `getCancellation`; Tinker reconciles provider evidence. Do not issue a replacement cancellation to retry an uncertain provider operation. History returns the newest 100 requests with `has_more`; older requests remain available through their ID.

Unsupported requests return `422 CHECKOUT_CANCELLATION_UNSUPPORTED` after recording a rejected request. Lifecycle conflicts return `409 CHECKOUT_CANCELLATION_CONFLICT`. Their structured error details can include `request_id` and `status`; read that request/history to follow its durable outcome. A replay of a rejected request keeps its original 422 or 409 result.

## Idempotency and errors

Every refund, payout and cancellation creation requires a caller-provided key of 1 to 255 UTF-8 bytes. Persist the key before sending. Retry the same semantic body with the same key after a timeout or unavailable response; never generate a new key merely because a response was lost. Reusing a key with a changed body returns `409 IDEMPOTENCY_KEY_REUSED`. A new intended operation uses a new key. Read and quote methods require no key.

The SDK retains the HTTP status, machine error code, response envelope and nested error details on `ApiException`. Infrastructure failures do not prove that a request was accepted or rejected. Inspect the persisted resource rather than interpreting a truthy result as financial completion. Amounts remain currency-unit numbers; zero-decimal currencies such as JPY use whole currency units. Non-finite and non-positive amounts are rejected before transport where an amount is supplied.

## Signed lifecycle updates

The implemented events are `refund.updated.v1`, `withdrawal.updated.v1` and `checkout.cancellation.updated.v1`, all with `source: payment` and `data.resource`. Refund, withdrawal and cancellation helpers decode their respective typed resources. Verify the signature before consuming any fields, deduplicate by event ID, and retain polling as recovery for delayed events.

```java
var event = client.webhooks().handle(requestBody);
if (!client.webhooks().verifySignature(event, webhookSecret)) {
    throw new SecurityException("Invalid webhook signature");
}
if (event.isCheckoutCancellationEvent()) {
    Cancellation cancellation = event.getCancellationData();
    // Persist and deduplicate event.getId(); then inspect cancellation.status.
    // event.toTransaction() is null for cancellation events.
}
```

Refund, withdrawal and cancellation events, including unknown future versions of these event families, never convert to payment transactions. Event classification alone does not authenticate a webhook.

## Contract checks and release gate

The tests exercise the shared `tinker-payments-openapi/fixtures/merchant-lifecycle.json` contract and compare the bundled snapshot when that repository is present. Set `TINKER_LIFECYCLE_FIXTURE` to verify a separate canonical checkout. The signed cancellation fixture is produced by the backend's Go signing convention and tested against tampering.

See [lifecycle release notes](merchant-lifecycle-release-notes.md). Backend deployment, disposable-database integration evidence and deployed sandbox parity are release prerequisites; fixture success alone does not establish them.
