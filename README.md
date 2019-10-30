# Upvest client library for the JVM

Java client library for the [Upvest API](https://upvest.co/)

## Installation

Step 1. Add the JitPack repository to your build file 

Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

Step 2. Add the dependency

```groovy
dependencies {
  implementation 'com.github.upvestco:upvest-java:1.0'
}
```

## Usage

In order to retrieve your API credentials for using this Python client, you"ll need to [sign up with Upvest](https://login.upvest.co/sign-up).

### Tenancy API - API Keys Authentication
The Upvest API uses the notion of _tenants_, which represent customers that build their platform upon the Upvest API. The end-users of the tenant (i.e. your customers), are referred to as _clients_. A tenant is able to manage their users directly (CRUD operations for the user instance) and is also able to initiate actions on the user"s behalf (create wallets, send transactions).

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

## Usage

### Tenancy

#### User management

##### Create a user
```java
User user = tenancy.users().create("username", "password", assetIds);
```
##### Retrieve a user
```java
User user = tenancy.users().get("username");
```
##### List a specific number of users under tenancy
```java
Cursor<User> users = tenancy.users().list(10);
```
##### Change password of a user
```java
User user = tenancy.users().get("username")
user = tenancy.users().update(user.getUsername(), "oldPassword", "newPassword");
```
##### Delete a user
```java
tenancy.users().delete("username");
```

### Clientele

#### Assets

##### List available assets
```java
Cursor<Asset> assets = clientele.assets().list();
while (assets.hasNextPage()) {
    Cursor<Asset> assets = assets.nextPage();
}
```
Note that it"s also possible to retrieve the same list from `tenancy.assets().list()`.

#### Wallets

##### Create a wallet for a user
```java
Wallet wallet = clientele.wallets().create("asset_id", "password");
```

##### Retrieve specific wallet for a user
```java
Wallet wallet = clientele.wallets().get("wallet_id");
```

##### List wallets for a user
```java
Cursor<Wallet> wallets = clientele.wallets().list();
```

##### List a specific number of wallets
```java
Cursor<Wallet> wallets = clientele.wallets().list(40);
```

#### Transactions

##### Create transaction
```java
Wallet wallet = clientele.wallets().create("asset_id","password");
Transaction transaction = wallet.transactions().create("password", "asset_id", "quantity", "fee", "recipient");
```

#### Retrieve specific transaction
```java
Wallet wallet = clientele.wallets().create("asset_id","password");
String id = wallet.transactions().list().toArray()[i].id;
Transaction transaction = wallet.transactions().get(id);
```

##### List transactions of a wallet for a user
```java
Wallet wallet = clientele.wallets().create("asset_id","password");
Cursor<Transaction> transactions = wallet.transactions().list();
```

##### List a specific number of transactions of a wallet for a user
```java
Wallet wallet = clientele.wallets().create("asset_id","password");
Cursor<Transaction> transactions = wallet.transactions().list(8);
```

#### Usage of Cursor\<T>

##### Load the first 10 users
```java
Cursor<User> users = tenancy.users().list(10);
```

##### Iterate through the loaded users
```java
for (User user : users){
    System.out.println(user.getUsername());
}
```

##### Load the next 10 users
```java
if (users.hasNextPage()) {
    users = users.nextPage();
}
```


Usage
------

### Tenant creation
The business "Blockchain4Everyone", founded by [John](https://en.wikipedia.org/wiki/The_man_on_the_Clapham_omnibus), would like to build a platform for Ethereum wallets with easy access and wallet management. Therefore, John visits the [Upvest Signup Page](https://login.upvest.co/sign-up), creates an account, and retrieves his API keys from the account management page. He is now able to create the API Keys Authentication object:
```java
import co.upvest.TenancyAPI;
TenancyAPI tenancy = TenancyAPI(API_KEY, API_SECRET, API_PASSPHRASE, BASE_URL);
```

### User creation
John sets up his platform and soon has the first person signing up for his service. Jane Apple, his first user, creates an account entering the username `Jane Apple` and the password `very secret`. Via an API call from his application's backend to the Upvest API, John creates an account for Jane under his tenancy account with Upvest, by implementing the following call using the API keys object from before:
```java
User user = tenancy.users().create("Jane Apple", "very secret");
String recoverykit = user.getRecoverykit();
```
After the request, John can access the recovery kit in the user instance and pass it on to Jane. Recovery kits are encrypted using a public key whose private counterpart is provided to tenants at sign-up on the Upvest Account Management portal, and not stored by Upvest. In case Jane loses her password, John is able to reset her password on her behalf, using her password and his decryption key, after conducting a proper KYC process in order to prevent identity fraud.

### Wallet creation
After creating an account Jane wants to create an Ethereum wallet on John's platform. In order to do that on behalf of Jane, John needs to initialize an OAuth object with his client credentials and Jane's username and password. After doing so, John can easily create a wallet by providing the respective `assetId` for Ethereum to the `wallets().create()` method. The `assetId` can be retrieved via a call to the Upvest asset endpoint, using the clientele or tenancy authentication:
```java
import co.upvest.ClienteleAPI;

TenancyAPI tenancy = ClienteleAPI(CLIENT_ID, CLIENT_SECRET, "Jane Apple", "very secret", BASE_URL);

// List assets and their ids
assetId = clientele.assets.list().toArray()[i].getId();
assetId = tenancy.assets.list().toArray()[i].getId();

// Create a wallet for Jane on Ethereum with her password and the respective assetId
Wallet ethereumWallet = clientele.wallets().create(assetId, "very secret");
Wallet walletAddress = ethereumWallet.getAddress();
```
Using the address, Jane is now able to receive funds in her Ethereum wallet on John"s platform. Thus she sends Ethereum from her current Ethereum wallet provider and sends the funds to her newly created wallet on John's platform.

### Transaction sending
After a couple of days, Jane would like to buy a new road bike, paying with Ether. The address of the seller is `0x6720d291A72B8673E774A179434C96D21eb85E71` and Jane needs to transfer 1 ETH. As a quantity it's denoted in [Wei](http://ethdocs.org/en/latest/ether.html#denominations) (Ether"s smallest unit), John will need to implement a transformation of this amount. The transaction can be sent via the Upvest API making the following call:
```java
// Retrieve Jane's walletId
Cursor<Wallet> walletsOfJane = clientele.wallets().list()
Wallet wallet = walletsOfJane.toArray()[0];
String recipient = "0x6720d291A72B8673E774A179434C96D21eb85E71";

// Send the transaction
Transaction transaction = wallet.transactions().create("very secret", "asset_id", 1000000000000000000, 4000000000, recipient);
String txhash = transaction.getTxhash();
```

That"s it! Jane has successfully sent a transaction and is able to monitor it via [Etherscan](https://etherscan.io).


## Runing tests

Set up `test_config.json` based on `test_config.example.json` (retrieve your client credentials from the [Upvest account management](https://login.upvest.co/), and transfer some test Ether to the user’s wallet), then run
```
./gradlew unitTest
./gradlew integrationTest
```

## Contributing

Bug reports and pull requests are welcome on GitHub at https://github.com/hangyas/upvest-jvm-client.


## More

For a comprehensive reference, check out the [Upvest documentation](https://doc.upvest.co).
