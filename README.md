# Modern Java Concurrency
Using Virtual Threads demonstrate the ability to scale Java services. Use StructuredTaskScope to
implement Structured Concurrency

## Usecase 1 : All child tasks are submitted in parallel and wait for all to complete(success/fail)
Find the Best Priced car by invoking multiple Car stores in parallel. Await their responses to find out the best value
Also add timing statistics using the ScopedValue

## Usecase 2 : Shutdown when the first child task succeeds
Find the stock price by submitting requests in parallel to Reuters, Bloomberg, Markit. When anyone of them returns the
price cancel the remaining threads and use the returned value

## Usecase 3 : Shutdown when the first child task fails
Submit parallel requests for client and product data. If anyone task fails cancel the remaining
and throw an error. If both succeed, then submit the order