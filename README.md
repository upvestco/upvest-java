# Upvest client library for the JVM

Java client library for the [Upvest API](https://upvest.co/)

## Installation

Add dependency:

```groovy
repositories {
    jcenter()
    maven { url "https://jitpack.io" }
}
dependencies {
        implementation 'co.upvest:api-client:1.0'
}

```

## Usage

In order to retrieve your API credentials for using this Python client, you'll need to [sign up with Upvest](https://login.upvest.co/sign-up).

### Tenancy API - API Keys Authentication
The Upvest API uses the notion of _tenants_, which represent customers that build their platform upon the Upvest API. The end-users of the tenant (i.e. your customers), are referred to as _clients_. A tenant is able to manage their users directly (CRUD operations for the user instance) and is also able to initiate actions on the user's behalf (create wallets, send transactions).

The authentication via API keys and secret allows you to perform all tenant related operations.
Please create an API key pair within the [Upvest account management](https://login.upvest.co/).

The default `BASE_URL` for both authentication objects is `https://api.playground.upvest.co`, but feel free to adjust it, once you retrieve your live keys. Next, create an `TenancyAPI` object in order to authenticate your API calls:

```java
TenancyAPI tenancy = new TenancyAPI(BASE_URL, KEY, SECRET, PASSPHRASE)

String username = "Example User";
String password = "ex@mp1e p@55w0rd";
 
User exampleUser;

try {
    // create user
    exampleUser = tenancy.users().create(username, password);
    
    // delete user
    tenancy.users().delete(exampleUser.getUsername());
    
    // list users
    for (User user : tenancy.users().list()){
        System.out.println(user.getUsername());
    }

} catch (IOException e) {
    // Handle error
}

```

### Clientele API - OAuth Authentication

The authentication via OAuth allows you to perform operations on behalf of your user.
For more information on the OAuth concept, please refer to our [documentation](https://doc.upvest.co/docs/oauth2-authentication).
Again, please retrieve your client credentials from the [Upvest account management](https://login.upvest.co/).

Next, create an `ClienteleAPI` object with these credentials and your user authentication data in order to authenticate your API calls on behalf of a user:

```java
String username = "Example User";
String password = "ex@mp1e p@55w0rd";

ClienteleAPI clientele = new ClienteleAPI(BASE_URL, auth, username, password);

try {
    // list the first 5 wallets
    Cursor<Wallet> wallets = clientele.wallets().list(5); 
    while (Wallet wallet : wallets){
        System.out.println(wallet.getAddress());
        wallet.transactions().create(...);
    }

    // load the next 5 wallets
    wallets.nextPage();

} catch (IOException e) {
    // Handle error
}

```

### Kotlin

Using the library from kotlin:

```kotlin
val tenancyAPI = TenancyAPI(BASE_URL, KEY, SECRET, PASSPHRASE)

val username = "Example User"
val password = "ex@mp1e p@55w0rd"

val assetIds = arrayOf("a3c18f74-935e-5d75-bd3c-ce0fb5464414", "deaaa6bf-d944-57fa-8ec4-2dd45d1f5d3f")

val user = tenancyAPI.users().create(username, password, assetIds)

tenancy.users().delete(user.username)

tenancyAPI.users().list(5).forEachIndexed { i, user ->
    println("My ${i}th favorite user: ${user.username}")
}

```

## Runing tests

Set up `test_config.json` based on `test_config.example.json` (retrieve your client credentials from the [Upvest account management](https://login.upvest.co/)), then run
```
./gradlew test
```

## Contributing

Bug reports and pull requests are welcome on GitHub at https://github.com/hangyas/upvest-jvm-client.


## More

For a comprehensive reference, check out the [Upvest documentation](https://doc.upvest.co).
