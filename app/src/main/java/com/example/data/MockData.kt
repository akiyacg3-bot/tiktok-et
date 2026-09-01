package com.example.data

import com.example.model.CommentItem
import com.example.model.TelebirrPackage
import com.example.model.VideoPost
import com.example.model.VideoThemeStyle
import com.example.model.VirtualGift

object MockData {
    val initialVideos = listOf(
        VideoPost(
            id = "v1",
            username = "Selamawit Addis",
            userHandle = "@selam_ethiopia",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            description = "Traditional Ethiopian coffee ceremony (Buna Tetu) in Addis Ababa with burning frankincense & roasted beans! ☕🇪🇹✨ #HabeshaTikTok #EthioVibes #AddisAbaba #EthiopianCoffee #VisitEthiopia",
            musicTitle = "Tizita Groove (Acoustic Masinqo)",
            musicAuthor = "Teddy Afro Remix",
            likesCount = 894500,
            commentsCount = 18400,
            sharesCount = 42300,
            bookmarksCount = 31200,
            isLiked = false,
            videoTheme = VideoThemeStyle.COZY_COFFEE,
            tags = listOf("HabeshaTikTok", "EthioVibes", "AddisAbaba", "EthiopianCoffee", "VisitEthiopia")
        ),
        VideoPost(
            id = "v2",
            username = "Habesha Dance Crew",
            userHandle = "@habesha_groove",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyBlazes.mp4",
            description = "Eskista shoulder dance challenge in Piazza! 💃🔥 Can you match this tempo? Drop your duet! #Eskista #EthiopianDance #GurshaChallenge #TikTokeEthiopia #AddisNightlife",
            musicTitle = "Gondar Eskista Fast Beats 2026",
            musicAuthor = "Ethio Sound Lab",
            likesCount = 1420000,
            commentsCount = 52100,
            sharesCount = 98400,
            bookmarksCount = 112000,
            isLiked = true,
            videoTheme = VideoThemeStyle.SUNSET_BEACH,
            tags = listOf("Eskista", "EthiopianDance", "GurshaChallenge", "TikTokeEthiopia", "AddisNightlife")
        ),
        VideoPost(
            id = "v3",
            username = "CodeCraft Studio",
            userHandle = "@codecraft",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            description = "Building a full TikTok clone in modern Jetpack Compose with Media3 ExoPlayer, Virtual Gifts & Gemini AI! 🚀💻 #android #jetpackcompose #developer #tech #coding",
            musicTitle = "Cyberpunk Synthwave 2077 - LO-FI Mix",
            musicAuthor = "SynthWave Labs",
            likesCount = 1250000,
            commentsCount = 34500,
            sharesCount = 78200,
            bookmarksCount = 95400,
            isLiked = true,
            videoTheme = VideoThemeStyle.TECH_CODING,
            tags = listOf("android", "jetpackcompose", "developer", "tech", "coding")
        ),
        VideoPost(
            id = "v4",
            username = "Lalibela Wanderer",
            userHandle = "@ethio_travel",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
            description = "Exploring the mystical rock-hewn churches of Lalibela at dawn ⛪⛰️ Pure architectural wonder! #Lalibela #EthiopiaTravel #AfricaTourism #HistoricEthiopia #fyp",
            musicTitle = "Echoes of the Highlands (Washint Flute)",
            musicAuthor = "Aura Travels",
            likesCount = 672000,
            commentsCount = 15800,
            sharesCount = 39100,
            bookmarksCount = 58200,
            isLiked = false,
            videoTheme = VideoThemeStyle.NATURE_FOREST,
            tags = listOf("Lalibela", "EthiopiaTravel", "AfricaTourism", "HistoricEthiopia", "fyp")
        ),
        VideoPost(
            id = "v5",
            username = "Neon Tokyo Vibes",
            userHandle = "@tokyobeats",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
            description = "Rainy night in Shibuya 🌧️ neon lights reflecting off the pavement. Rate this aesthetic 1-10! #tokyo #cyberpunk #neon #aesthetic #streetvibes",
            musicTitle = "Midnight In Tokyo - Chillhop Track",
            musicAuthor = "TokyoBeats",
            likesCount = 492300,
            commentsCount = 8900,
            sharesCount = 21400,
            bookmarksCount = 43200,
            isLiked = false,
            videoTheme = VideoThemeStyle.CYBERPUNK_NEON,
            tags = listOf("tokyo", "cyberpunk", "neon", "aesthetic", "streetvibes")
        ),
        VideoPost(
            id = "v6",
            username = "Pixel Quest",
            userHandle = "@pixelarcade",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
            description = "Insane speedrun clutch at 0.01 seconds remaining! 🎮🔥 Watch till the end! #gaming #speedrun #arcade #epicmoment #fyp",
            musicTitle = "8-Bit Victory Anthem - PixelQuest",
            musicAuthor = "pixelarcade",
            likesCount = 980400,
            commentsCount = 27600,
            sharesCount = 61200,
            bookmarksCount = 54000,
            isLiked = false,
            videoTheme = VideoThemeStyle.GAMING_ARCADE,
            tags = listOf("gaming", "speedrun", "arcade", "epicmoment", "fyp")
        )
    )

    val virtualGifts = listOf(
        VirtualGift("g1", "Rose", 10, "🌹", "Show love with a classic red rose", 0xFFFF2A6D),
        VirtualGift("g2", "TikTok Note", 20, "🎵", "Official TikTok musical rhythm beat", 0xFF00F2FE),
        VirtualGift("g3", "Buna Coffee", 25, "☕", "Traditional Ethiopian clay Jebena coffee", 0xFF8D6E63),
        VirtualGift("g4", "Mesob Basket", 35, "🧺", "Traditional Habesha handcrafted Mesob", 0xFFFFD700),
        VirtualGift("g5", "Habesha Crown", 50, "👑", "Golden royal crown of ancient kings", 0xFFFFB800),
        VirtualGift("g6", "Diamond Ring", 100, "💍", "Sparkling luxury jewel", 0xFF00E5FF),
        VirtualGift("g7", "Lion of Judah", 500, "🦁", "Majestic roaring Golden Lion of Judah", 0xFFFF9900),
        VirtualGift("g8", "Sports Car", 1000, "🏎️", "High-speed neon supercar ride", 0xFFFF3366),
        VirtualGift("g9", "Galactic Universe", 2500, "🌌", "Full screen cosmic universe wonder", 0xFF9D4EDD)
    )

    val telebirrPackages = listOf(
        TelebirrPackage("tb_100", 100, 50, "Standard", isPopular = false),
        TelebirrPackage("tb_500", 500, 200, "+20% Extra Value", isPopular = true),
        TelebirrPackage("tb_1000", 1000, 400, "+25% Best Value", isPopular = false),
        TelebirrPackage("tb_2500", 2500, 950, "+30% Super Saver", isPopular = false),
        TelebirrPackage("tb_5000", 5000, 1800, "+35% VIP Creator", isPopular = false)
    )

    val defaultEthiopianHashtags = listOf(
        "#HabeshaTikTok",
        "#EthioVibes",
        "#AddisAbaba",
        "#EthiopianMusic",
        "#EskistaChallenge",
        "#GurshaMoments",
        "#VisitEthiopia",
        "#TikTokeEthiopia",
        "#HabeshaCulture",
        "#EthioGenZ",
        "#GondarVibes",
        "#BunaTetu"
    )

    val sampleComments = listOf(
        CommentItem(
            id = "c1",
            username = "Alex Rivera",
            userHandle = "@arivera",
            commentText = "The transitions are unbelievably smooth! 🔥 How did you shoot this?",
            timeAgo = "2h",
            likesCount = 3420,
            isLiked = false,
            isCreatorLiked = true
        ),
        CommentItem(
            id = "c2",
            username = "Sarah Jenkins",
            userHandle = "@sarah_j",
            commentText = "Watched this at least 10 times in a row now 😭✨ so satisfying!!",
            timeAgo = "4h",
            likesCount = 1890,
            isLiked = true,
            isCreatorLiked = false
        ),
        CommentItem(
            id = "c3",
            username = "Devin Tech",
            userHandle = "@devintech",
            commentText = "Clean Jetpack Compose architecture and UI layout, amazing implementation 👌",
            timeAgo = "6h",
            likesCount = 890,
            isLiked = false,
            isCreatorLiked = true
        ),
        CommentItem(
            id = "c4",
            username = "Maya Lin",
            userHandle = "@mayalin_arts",
            commentText = "The color grading and lighting on this is top tier aesthetic 🎨💖",
            timeAgo = "1d",
            likesCount = 540,
            isLiked = false,
            isCreatorLiked = false
        ),
        CommentItem(
            id = "c5",
            username = "Lucas Vance",
            userHandle = "@lucas_v",
            commentText = "Sending you a Golden Dragon gift right now!! Keep up the fantastic content! 🐉🚀",
            timeAgo = "1d",
            likesCount = 312,
            isLiked = false,
            isCreatorLiked = false
        )
    )

    fun formatCount(count: Long): String {
        return when {
            count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
            count >= 1_000 -> String.format("%.1fK", count / 1_000.0)
            else -> count.toString()
        }
    }

    fun formatCount(count: Int): String = formatCount(count.toLong())
}
