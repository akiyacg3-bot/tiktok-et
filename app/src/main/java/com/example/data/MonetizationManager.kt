package com.example.data

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import com.example.model.CreatorWallet
import com.example.model.GiftTransaction
import com.example.model.TelebirrPackage
import com.example.model.VirtualGift
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class TelebirrReceipt(
    val transactionId: String,
    val amountEtb: Int,
    val coinsPurchased: Int,
    val phoneNumber: String,
    val timestamp: String,
    val status: String = "SUCCESS"
)

object MonetizationManager {
    private val _userCoins = MutableStateFlow(500)
    val userCoins: StateFlow<Int> = _userCoins.asStateFlow()

    private val _globalGiftTransactions = MutableStateFlow<List<GiftTransaction>>(emptyList())
    val globalGiftTransactions: StateFlow<List<GiftTransaction>> = _globalGiftTransactions.asStateFlow()

    private val _lastTelebirrReceipt = MutableStateFlow<TelebirrReceipt?>(null)
    val lastTelebirrReceipt: StateFlow<TelebirrReceipt?> = _lastTelebirrReceipt.asStateFlow()

    // Creator wallets mapped by user handle
    private val _creatorWallets = MutableStateFlow<Map<String, CreatorWallet>>(
        mapOf(
            "@selam_ethiopia" to CreatorWallet(
                creatorHandle = "@selam_ethiopia",
                creatorName = "Selamawit Addis",
                accumulatedCoins = 14500,
                totalGiftsReceived = 380,
                followerCount = 128400,
                totalVideoViews = 1450000,
                isMonetized = true,
                transactions = listOf(
                    GiftTransaction("tx_1", MockData.virtualGifts[4], "Abebe B.", "@selam_ethiopia", 1, 50, "10m ago"),
                    GiftTransaction("tx_2", MockData.virtualGifts[6], "Dawit K.", "@selam_ethiopia", 1, 500, "1h ago"),
                    GiftTransaction("tx_3", MockData.virtualGifts[2], "Hanna M.", "@selam_ethiopia", 5, 125, "3h ago")
                )
            ),
            "@habesha_groove" to CreatorWallet(
                creatorHandle = "@habesha_groove",
                creatorName = "Habesha Dance Crew",
                accumulatedCoins = 28900,
                totalGiftsReceived = 740,
                followerCount = 84200,
                totalVideoViews = 920000,
                isMonetized = true,
                transactions = listOf(
                    GiftTransaction("tx_4", MockData.virtualGifts[6], "Yonas T.", "@habesha_groove", 2, 1000, "25m ago"),
                    GiftTransaction("tx_5", MockData.virtualGifts[4], "Tigist G.", "@habesha_groove", 5, 250, "2h ago")
                )
            ),
            "@codecraft" to CreatorWallet(
                creatorHandle = "@codecraft",
                creatorName = "CodeCraft Studio",
                accumulatedCoins = 8200,
                totalGiftsReceived = 210,
                followerCount = 6500,
                totalVideoViews = 68000,
                isMonetized = true,
                transactions = listOf(
                    GiftTransaction("tx_6", MockData.virtualGifts[5], "Bereket N.", "@codecraft", 1, 100, "45m ago")
                )
            ),
            "@ethio_travel" to CreatorWallet(
                creatorHandle = "@ethio_travel",
                creatorName = "Lalibela Wanderer",
                accumulatedCoins = 11400,
                totalGiftsReceived = 295,
                followerCount = 14200,
                totalVideoViews = 185000,
                isMonetized = true,
                transactions = listOf(
                    GiftTransaction("tx_7", MockData.virtualGifts[6], "Helen S.", "@ethio_travel", 1, 500, "1d ago")
                )
            ),
            "@arivera_official" to CreatorWallet(
                creatorHandle = "@arivera_official",
                creatorName = "Alex Rivera",
                accumulatedCoins = 5600,
                totalGiftsReceived = 142,
                followerCount = 4320,
                totalVideoViews = 46800,
                isMonetized = false,
                transactions = listOf(
                    GiftTransaction("tx_8", MockData.virtualGifts[0], "Marcus L.", "@arivera_official", 10, 100, "2h ago"),
                    GiftTransaction("tx_9", MockData.virtualGifts[4], "Sophia K.", "@arivera_official", 1, 50, "5h ago")
                )
            )
        )
    )
    val creatorWallets: StateFlow<Map<String, CreatorWallet>> = _creatorWallets.asStateFlow()

    /**
     * Deducts coins from viewer and accumulates coins in creator wallet
     */
    fun sendGiftToCreator(
        gift: VirtualGift,
        combo: Int,
        creatorHandle: String,
        creatorName: String,
        senderName: String = "You"
    ): Boolean {
        val totalCost = gift.coins * combo
        val currentCoins = _userCoins.value

        if (currentCoins < totalCost) {
            return false
        }

        // Deduct from sender
        _userCoins.value = currentCoins - totalCost

        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val timestamp = timeFormat.format(Date())

        val newTransaction = GiftTransaction(
            id = "tx_${System.currentTimeMillis()}",
            gift = gift,
            senderName = senderName,
            recipientHandle = creatorHandle,
            comboCount = combo,
            totalCoins = totalCost,
            timestamp = "Just now ($timestamp)"
        )

        // Update creator wallet
        val existingWallet = _creatorWallets.value[creatorHandle] ?: CreatorWallet(
            creatorHandle = creatorHandle,
            creatorName = creatorName,
            accumulatedCoins = 0,
            totalGiftsReceived = 0,
            transactions = emptyList()
        )

        val updatedWallet = existingWallet.copy(
            accumulatedCoins = existingWallet.accumulatedCoins + totalCost,
            totalGiftsReceived = existingWallet.totalGiftsReceived + combo,
            transactions = listOf(newTransaction) + existingWallet.transactions
        )

        val updatedMap = _creatorWallets.value.toMutableMap()
        updatedMap[creatorHandle] = updatedWallet
        _creatorWallets.value = updatedMap

        // Update global transaction log
        _globalGiftTransactions.value = listOf(newTransaction) + _globalGiftTransactions.value
        return true
    }

    /**
     * Simulates Telebirr payment transaction, crediting coins to user
     */
    fun rechargeCoinsWithTelebirr(
        pkg: TelebirrPackage,
        phoneNumber: String
    ): TelebirrReceipt {
        _userCoins.value = _userCoins.value + pkg.coins

        val timeFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        val txId = "TB-" + (10000..99999).random() + "-ET"
        val receipt = TelebirrReceipt(
            transactionId = txId,
            amountEtb = pkg.priceEtb,
            coinsPurchased = pkg.coins,
            phoneNumber = phoneNumber.ifBlank { "0911234567" },
            timestamp = timeFormat.format(Date())
        )
        _lastTelebirrReceipt.value = receipt
        return receipt
    }

    /**
     * Allows a creator to withdraw accumulated diamonds/coins to Telebirr
     * Requires the creator to be monetized
     */
    fun withdrawCreatorEarnings(
        creatorHandle: String,
        amountCoins: Long,
        telebirrPhone: String
    ): Boolean {
        val wallet = _creatorWallets.value[creatorHandle] ?: return false
        if (!wallet.isMonetized || wallet.accumulatedCoins < amountCoins || amountCoins <= 0) return false

        val updatedWallet = wallet.copy(
            accumulatedCoins = wallet.accumulatedCoins - amountCoins
        )
        val updatedMap = _creatorWallets.value.toMutableMap()
        updatedMap[creatorHandle] = updatedWallet
        _creatorWallets.value = updatedMap
        return true
    }

    /**
     * Applies for creator monetization when 5,000 followers and 50,000 views criteria are met
     */
    fun applyForMonetization(creatorHandle: String): Boolean {
        val wallet = _creatorWallets.value[creatorHandle] ?: return false
        if (!wallet.isEligibleToApply) return false

        val updatedWallet = wallet.copy(isMonetized = true)
        val updatedMap = _creatorWallets.value.toMutableMap()
        updatedMap[creatorHandle] = updatedWallet
        _creatorWallets.value = updatedMap
        return true
    }

    /**
     * Boosts stats for testing/demoing reaching the 5,000 followers and 50,000 views threshold
     */
    fun boostCreatorMilestones(creatorHandle: String): CreatorWallet {
        val wallet = getCreatorWallet(creatorHandle)
        val updatedWallet = wallet.copy(
            followerCount = maxOf(wallet.followerCount, 5200),
            totalVideoViews = maxOf(wallet.totalVideoViews, 54500)
        )
        val updatedMap = _creatorWallets.value.toMutableMap()
        updatedMap[creatorHandle] = updatedWallet
        _creatorWallets.value = updatedMap
        return updatedWallet
    }

    fun getCreatorWallet(creatorHandle: String): CreatorWallet {
        return _creatorWallets.value[creatorHandle] ?: CreatorWallet(
            creatorHandle = creatorHandle,
            creatorName = creatorHandle.removePrefix("@").replace("_", " ")
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
            accumulatedCoins = 0,
            totalGiftsReceived = 0,
            followerCount = 0,
            totalVideoViews = 0,
            isMonetized = false,
            transactions = emptyList()
        )
    }
}
