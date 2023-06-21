# LoyaltyMobileSDK-Android
# Save Time and Effort With Loyalty Management Mobile SDK for Android

Experience Loyalty Management on your Android mobile devices and use the mobile software development kit’s (SDK) capabilities and build custom mobile applications with unique user experiences. Use pre-built loyalty components to build your own apps or enhance your existing mobile apps. Build the SDK for your loyalty program members to view their profile, membership details, and benefits, to enroll for promotions, and to avail vouchers. The SDK is bundled with a ready-to-use sample app, which is embedded with Loyalty Management features.

**Where:** This feature is available in Lightning Experience in all editions.  
**How:** Install the Loyalty Management Mobile SDK for Android, create a connected app, and then clone the GitHub repository.

## Android Mobile SDK for Loyalty Management

Enhance brand engagement by providing Loyalty Management features on your Android mobile devices. Use the Android Mobile Software Development Kit (SDK) for Loyalty Management to build custom mobile applications with immersive member experiences. Elevate member experience and loyalty, by providing personalized offers, rewards, and checkouts on mobile devices.

### Supported Versions of Tools and Components

| Tool or Component | Supported Version | Installation Details                             |
|-------------------|-------------------|--------------------------------------------------|
| Kotlin Version    | 1.8.0             | Installed by Android Studio                      |
| Android Studio    | Electric Eel      | Install from the Official Android Developer site |

### Installation

To integrate Loyalty Management Mobile SDK for Android with your Android project, add it as a package dependency.

In the Android Project gradle file, add the following lines:
   `dependencies {
      implementation fileTree(dir: 'libs', include: ['*.jar'])
      implementation project(path: ':Sources')
    }`


## Import SDK in an Android Project

Adding the above lines in gradle file will automatically download and manage the external dependencies.
Start using the SDK files by importing the appropriate SDK package.

`import com.salesforce.loyalty.mobile.sources.forceUtils.Logger
 import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyAPIManager
 import com.salesforce.loyalty.mobile.sources.loyaltyAPI.LoyaltyClient
 import com.salesforce.loyalty.mobile.sources.forceUtils.ForceAuthenticator`

## LoyaltyAPIManager

The `LoyaltyAPIManager` class manages requests related to loyalty programs using the Force API. Interact with the Salesforce Loyalty Management API and retrieve member benefits, transactions, profile, and more, in development and production environments. With LoyaltyAPIManager, you can:
- Manage authentication by creating an instance of ForceAuthenticator.
- Interact with the Loyalty Management APIs, including:
    - Individual Member Enrollments
    - Enroll for Promotions
    - Get Member Promotions
    - Member Benefits
    - Member Profile
    - Member Vouchers
    - Transaction History
    - Opt Out from a Promotion
- Manage asynchronous requests by using Retrofit and Kotlin coroutines.

### Usage

1. Create an instance of `LoyaltyAPIManager` with the necessary parameters:

```
val loyaltyAPIManager = LoyaltyAPIManager(
            auth = forceAuthManager,
            instanceUrl = mInstanceUrl,
            loyaltyClient = LoyaltyClient(forceAuthManager, mInstanceUrl)
        )```

2. Call the appropriate methods to interact with the Loyalty Management API:

```Kotlin
import LoyaltyMobileSDK

val forceAuthManager = ForceAuthManager(applicationContext)
val mInstanceUrl =
            forceAuthManager.getInstanceUrl() ?: AppSettings.DEFAULT_FORCE_CONNECTED_APP.instanceUrl
val loyaltyAPIManager = LoyaltyAPIManager(
            auth = forceAuthManager,
            instanceUrl = mInstanceUrl,
            loyaltyClient = LoyaltyClient(forceAuthManager, mInstanceUrl)
        )         
        
// Enroll Members
let membershipNumber = "1234567890"
let firstName = "John"
let lastName = "Doe"
let email = "john.doe@example.com"
let phone = "4157891234"
let emailNotification = true

loyaltyAPIManager.postEnrollment(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    additionalContactAttributes = mapof("phone" to phone),
                    emailNotification = emailNotification,
                    memberStatus = MemberStatus.ACTIVE,
                    createTransactionJournals = true,
                    transactionJournalStatementFrequency = TransactionalJournalStatementFrequency.MONTHLY,
                    transcationJournalStatementMethod = TransactionalJournalStatementMethod.EMAIL,
                    enrollmentChannel = EnrollmentChannel.EMAIL,
                    canReceivePromotions = true,
                    canReceivePartnerPromotions = true
                )

// Retrieve the member benefits
loyaltyAPIManager.getMemberBenefits(memberId = null, membershipKey = "1234567890")

// Retrieve the member profile
loyaltyAPIManager.getMemberProfile(memberId = null, membershipKey = "1234567890", programCurrencyName = null)

// Opt in a promotion for member
loyaltyAPIManager.enrollInPromotions(membershipNumber = "1234567890", promotionName = "PromotionName")

// Opt out a member from a promotion using promotion ID or promotion name
loyaltyAPIManager.unEnrollPromotion(membershipNumber = "1234567890", promotionName = "PromotionName")

// Retrieve loyalty member transactions
loyaltyAPIManager.getTransactions(membershipNumber = "1234567890", pageNumber = null, 
            journalTypeName = null, journalSubTypeName = null, periodStartDate = null, periodEndDate = null)

// Retrieve promotions for a loyalty member
loyaltyAPIManager.getEligiblePromotions(membershipKey = "1234567890", memberId = null)

// Retrieve vouchers for a loyalty member
loyaltyAPIManager.getVouchers(
                membershipKey = "1234567890", vouchreStatus = null, pageNumber = 1, productId = null,
                productCategoryId = null, productName = null, productCategoryName = null
            )
```

For a detailed understanding of each method and its parameters, please refer to the comments in the provided `LoyaltyAPIManager` code.

## Test the Loyalty Management Mobile SDK for Android and Sample App

Run the `run_tests.sh` script from the command line to test the LoyaltyMobileSDK-Android and the sample app. To ensure that the script is executable, run the `chmod +x run_tests.sh` script. The script provides these capabilities.

## Contribute to the SDK

You can contribute to the development of the Loyalty Management Mobile SDK.
1. Fork the Loyalty Management Mobile SDK for Android // TODO Add the repository link.
2. Create a branch with a descriptive name.
3. Implement your changes.
4. Test your changes.
5. Submit a pull request.

See also:
[Fork a repo](https://docs.github.com/en/get-started/quickstart/fork-a-repo)

## License

LoyaltyMobileSDK-Android is available under the BSD 3-Clause License.

Copyright (c) 2023, Salesforce Industries
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
