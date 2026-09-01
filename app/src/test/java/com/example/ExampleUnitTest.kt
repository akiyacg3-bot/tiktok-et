package com.example

import com.example.data.MockData
import com.example.model.VideoThemeStyle
import com.example.network.GeminiHashtagService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testMockVideosHaveValidUrlsAndThemes() {
    assertTrue(MockData.initialVideos.isNotEmpty())
    val firstVideo = MockData.initialVideos.first()
    assertTrue(firstVideo.videoUrl.startsWith("http"))
    assertNotNull(firstVideo.username)
    assertNotNull(firstVideo.musicTitle)
    assertTrue(firstVideo.description.isNotBlank())
  }

  @Test
  fun testVirtualGiftsContainEthiopianAndGlobalGifts() {
    val gifts = MockData.virtualGifts
    assertTrue(gifts.isNotEmpty())
    assertTrue(gifts.any { it.name.contains("Buna") || it.name.contains("Coffee") })
    assertTrue(gifts.any { it.name.contains("Lion") || it.name.contains("Judah") })
    assertTrue(gifts.any { it.name.contains("Rose") && it.coins == 10 })
    assertTrue(gifts.any { it.name.contains("Crown") && it.coins == 50 })
    assertTrue(gifts.any { it.name.contains("Lion") && it.coins == 500 })
    assertTrue(gifts.all { it.coins > 0 })
  }

  @Test
  fun testTelebirrPackagesConfiguration() {
    val packages = MockData.telebirrPackages
    assertTrue(packages.isNotEmpty())
    val p100 = packages.find { it.coins == 100 }
    val p500 = packages.find { it.coins == 500 }
    val p1000 = packages.find { it.coins == 1000 }
    assertNotNull(p100)
    assertNotNull(p500)
    assertNotNull(p1000)
    assertEquals(50, p100?.priceEtb)
    assertEquals(200, p500?.priceEtb)
    assertEquals(400, p1000?.priceEtb)
  }

  @Test
  fun testMonetizationSendGiftAndCreatorWalletAccumulation() {
    val initialCoins = com.example.data.MonetizationManager.userCoins.value
    val roseGift = MockData.virtualGifts.first { it.name == "Rose" } // 10 coins
    val creatorHandle = "@selam_ethiopia"
    val initialCreatorCoins = com.example.data.MonetizationManager.getCreatorWallet(creatorHandle).accumulatedCoins

    // Send Rose x 2 = 20 coins
    val success = com.example.data.MonetizationManager.sendGiftToCreator(
      gift = roseGift,
      combo = 2,
      creatorHandle = creatorHandle,
      creatorName = "Selamawit Addis",
      senderName = "Abebe"
    )

    assertTrue(success)
    assertEquals(initialCoins - 20, com.example.data.MonetizationManager.userCoins.value)
    val updatedCreatorWallet = com.example.data.MonetizationManager.getCreatorWallet(creatorHandle)
    assertEquals(initialCreatorCoins + 20, updatedCreatorWallet.accumulatedCoins)
  }

  @Test
  fun testTelebirrRechargeCreditsCoins() {
    val beforeCoins = com.example.data.MonetizationManager.userCoins.value
    val pkg500 = MockData.telebirrPackages.first { it.coins == 500 }
    val receipt = com.example.data.MonetizationManager.rechargeCoinsWithTelebirr(
      pkg = pkg500,
      phoneNumber = "0911223344"
    )

    assertEquals(500, receipt.coinsPurchased)
    assertEquals(200, receipt.amountEtb)
    assertEquals("0911223344", receipt.phoneNumber)
    assertEquals(beforeCoins + 500, com.example.data.MonetizationManager.userCoins.value)
  }

  @Test
  fun testFormatCount() {
    assertEquals("894.5K", MockData.formatCount(894500))
    assertEquals("1.4M", MockData.formatCount(1400000))
    assertEquals("500", MockData.formatCount(500))
  }

  @Test
  fun testGeminiFallbackHashtags() = runBlocking {
    val tags = GeminiHashtagService.generateTrendingEthiopianHashtags("Traditional coffee ceremony")
    assertTrue(tags.isNotEmpty())
    assertTrue(tags.all { it.startsWith("#") })
  }
}
