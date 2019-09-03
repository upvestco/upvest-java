package co.upvest

import co.upvest.models.*

import org.junit.Test
import org.junit.Assert.*

import co.upvest.APIErrorInterceptor.APIError

import org.junit.Assert.*
import java.io.IOException
import java.math.BigDecimal
import java.util.Arrays

/**
 * Tests if every class have @Nullable and @NotNull properly set up
 * so the library can painlessly used from kotlin
 * */
class KotlinCompatibilityTest {

    @Test
    fun tenancyAPINotNulls() {
        val tenancyAPI: TenancyAPI = TestHelper.getTenancyAPI()

        val username = "user_" + java.time.Instant.now().getEpochSecond().toString()
        val pass1 = "password"
        
        val user: User = tenancyAPI.users().create(username, pass1, arrayOf<String>())
        assertEquals(user.getUsername(), user.username)
        assertEquals(user.getWalletIds()?.size, user.walletIds?.size)
        assertEquals(user.getRecoverykit(), user.recoverykit)
    }

    @Test
    fun testWalletListAndGet() {
        val tenancyAPI = TestHelper.getTenancyAPI()

        val username = "user_" + java.time.Instant.now().epochSecond.toString()

        val assets = TestHelper.config.getJSONObject("assetIds")
        val assetIds = assets.toMap().values.map { it as String }.toTypedArray()

        val user = tenancyAPI.users().create(username, "password", assetIds)

        assertEquals("ETH and ERC20 get combined in 1 wallet.", (assetIds.size - 1).toLong(), user.walletIds!!.size.toLong())
        assertTrue(TestHelper.isValidRecoveryKit(user.recoverykit))
    }

    @Test
    fun testUserWallet() {
        val clienteleAPI = TestHelper.getClienteleAPI()

        TestHelper.config.getJSONObject("assetIds").toMap().values.forEach {
            clienteleAPI.wallets().create(TestHelper.commonPassword, it as String, null, null, null, null)
        }

        testCursorWithPageSize(clienteleAPI.wallets(), 1)
    }

    @Test
    fun testUsersListAndGet() {
        val tenancyAPI = TestHelper.getTenancyAPI()

        tenancyAPI.users().list(5).forEach { user ->
            val otherUser = tenancyAPI.users().get(user.username)
            assertEquals(user.username, otherUser.username)
        }
    }

    private fun <Type : Listable> testCursorWithPageSize(endpoint: Listable.Endpoint<Type>, pageSize: Int) {
        var cursor = endpoint.list(pageSize)

        var counter = 0
        for (t in cursor) {
            counter++
        }

        assertEquals(cursor.toArray().size, counter)
        assertEquals(pageSize, counter)
        assertTrue(cursor.hasNextPage())
        assertFalse(cursor.hasPreviousPage())

        cursor = cursor.nextPage()
        assertTrue(cursor.hasPreviousPage())
    }
}
