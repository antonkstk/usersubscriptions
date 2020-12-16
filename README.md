# Description

### Getting started guides
There are two possible options to start this application:
* Execute com.test.usersubscriptionsservice.UsersubscriptionsserviceApplication in Intellij IDEA
* Run `mvn spring-boot:run` in your command line tool
This will start a webserver on port 8080 (http://localhost:8080) 

Service uses H2 Database and has some test data loading into it on start up.
After service started up, some user data is added to the database and can be fetched 
by calling `Fetch information about all users` endpoint.

### Used technologies:
* Kotlin
* Spring
* Maven
* H2 database

### Service overview
UserSubscriptionService provides you with the following API:

* Fetch list of all products with pagination:
    ```
    curl "http://localhost:8080/api/v1/products?size={size}&page={page}"
    ```
* Fetch single product:
    ```
    curl "http://localhost:8080/api/v1/products/{productId}"
    ```
* Buy single product:
    ```
    curl -X "POST" "http://localhost:8080/api/v1/products/{productId}/users/{userId}" \
         -H 'Content-Type: application/json; charset=utf-8' \
         -d $'{}'
    ```
* Fetch user's subscription information:
    ```
    curl "http://localhost:8080/api/v1/subscriptions/users/{userId}" \
         -H 'Content-Type: application/json; charset=utf-8' \
         -d $'{}'
    ```
* Update subscription (pause/unpause):
    ```
    curl -X "PATCH" "http://localhost:8080/api/v1/subscriptions/{subscriptionId}" \
         -H 'Content-Type: application/json; charset=utf-8' \
         -d $'{
      "setActive": false
    }'
    ```
* Cancel subscription:
    ```
    curl -X "DELETE" "http://localhost:8080/api/v1/subscriptions/{subscriptionId}" \
         -H 'Content-Type: application/json; charset=utf-8' \
         -d $'{}'
    ```
* Fetch information about all users (for testing purposes):
    ```
    curl "http://localhost:8080/api/v1/users"
    ```
