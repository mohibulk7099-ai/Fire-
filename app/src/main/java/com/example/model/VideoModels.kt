package com.example.model

enum class VideoStyle(
    val id: String,
    val titleHi: String,
    val titleEn: String,
    val descriptionHi: String,
    val descriptionEn: String,
    val promptModifier: String
) {
    REALISTIC(
        id = "realistic",
        titleHi = "यथार्थवादी",
        titleEn = "Realistic",
        descriptionHi = "प्राकृतिक रोशनी और असली जीवन जैसा विवरण",
        descriptionEn = "Natural lighting, photorealistic textures",
        promptModifier = "photorealistic 8k, natural lighting, ultra-detailed textures, highly realistic film"
    ),
    CINEMATIC(
        id = "cinematic",
        titleHi = "सिनेमैटिक",
        titleEn = "Cinematic",
        descriptionHi = "हॉलीवुड फिल्म जैसी रोशनी और कैमरा एंगल",
        descriptionEn = "Anamorphic lens flare, moody color grading, 35mm film",
        promptModifier = "cinematic shot, 35mm lens, dramatic lighting, depth of field, anamorphic flare, blockbuster movie feel"
    ),
    ANIME(
        id = "anime",
        titleHi = "एनीमे",
        titleEn = "Anime",
        descriptionHi = "जापानी एनीमेशन और वाइब्रेंट रंग",
        descriptionEn = "Japanese studio anime aesthetic, dynamic lines",
        promptModifier = "high quality anime style, Studio Ghibli inspired, vibrant cel shading, artistic anime animation"
    ),
    CARTOON(
        id = "cartoon",
        titleHi = "कार्टून",
        titleEn = "Cartoon",
        descriptionHi = "मजेदार 2D कार्टून और चंचल कैरेक्टर",
        descriptionEn = "Playful, colorful hand-drawn animated style",
        promptModifier = "colorful cartoon animation, bold outlines, expressive animated characters, whimsical feel"
    ),
    THREE_D(
        id = "3d",
        titleHi = "3D रेंडर",
        titleEn = "3D Render",
        descriptionHi = "पिक्सर और ऑक्टेन रेंडर जैसा डिजिटल 3D",
        descriptionEn = "Pixar style, Octane 3D render, smooth physics",
        promptModifier = "Pixar Disney 3D style, Octane Render, ray-traced lighting, subsurface scattering, stylized 3D animation"
    ),
    NATURE(
        id = "nature",
        titleHi = "प्रकृति",
        titleEn = "Nature",
        descriptionHi = "शानदार परिदृश्य, वन्यजीव और शांत प्राकृतिक दृश्य",
        descriptionEn = "Breathtaking landscapes, wildlife, serene environment",
        promptModifier = "National Geographic documentary quality, lush nature, 4k scenic view, organic motion, ambient atmosphere"
    ),
    FANTASY(
        id = "fantasy",
        titleHi = "काल्पनिक",
        titleEn = "Fantasy",
        descriptionHi = "जादुई दुनिया, उड़ते ड्रैगन और रहस्यमयी रोशनी",
        descriptionEn = "Magical realms, mythical aura, glowing runes",
        promptModifier = "mythical fantasy realm, glowing ethereal particles, enchanted aura, hyper-detailed magic lighting"
    ),
    HORROR(
        id = "horror",
        titleHi = "हॉरर",
        titleEn = "Horror",
        descriptionHi = "डरावना माहौल, रहस्यमयी कोहरा और गहरी परछाइयां",
        descriptionEn = "Spooky atmosphere, eerie fog, suspenseful shadows",
        promptModifier = "dark eerie atmosphere, volumetric fog, suspenseful cinematic horror, moody high contrast shadows"
    );

    fun getDisplayName(isHindi: Boolean): String = if (isHindi) titleHi else titleEn
    fun getDisplayDescription(isHindi: Boolean): String = if (isHindi) descriptionHi else descriptionEn
}

enum class AspectRatio(
    val id: String,
    val ratioText: String,
    val titleHi: String,
    val titleEn: String,
    val subtitleHi: String,
    val subtitleEn: String,
    val widthRatio: Float,
    val heightRatio: Float
) {
    PORTRAIT_9_16(
        id = "9:16",
        ratioText = "9:16",
        titleHi = "रील्स / शॉर्ट्स (9:16)",
        titleEn = "Reels / Shorts (9:16)",
        subtitleHi = "Instagram, YouTube Shorts, TikTok",
        subtitleEn = "Vertical video for mobile feeds",
        widthRatio = 9f,
        heightRatio = 16f
    ),
    LANDSCAPE_16_9(
        id = "16:9",
        ratioText = "16:9",
        titleHi = "यूट्यूब (16:9)",
        titleEn = "YouTube (16:9)",
        subtitleHi = "Landscape, Cinema, TV",
        subtitleEn = "Widescreen video for YouTube & TV",
        widthRatio = 16f,
        heightRatio = 9f
    ),
    SQUARE_1_1(
        id = "1:1",
        ratioText = "1:1",
        titleHi = "स्क्वेयर (1:1)",
        titleEn = "Square (1:1)",
        subtitleHi = "Instagram Post, Facebook Feed",
        subtitleEn = "Square format for social media feeds",
        widthRatio = 1f,
        heightRatio = 1f
    );

    fun getDisplayName(isHindi: Boolean): String = if (isHindi) titleHi else titleEn
}

data class SamplePrompt(
    val promptHi: String,
    val promptEn: String,
    val style: VideoStyle
)

object PromptRepository {
    val samplePrompts = listOf(
        SamplePrompt(
            promptHi = "बारिश में नाचती मोर और खिलते हुए नीले कमल के फूल",
            promptEn = "Majestic peacock dancing in gentle monsoon rain with glowing blue lotus flowers",
            style = VideoStyle.NATURE
        ),
        SamplePrompt(
            promptHi = "साइबरपंक शहर की रात, उड़ती कारें और नीयन रोशनी",
            promptEn = "Futuristic cyberpunk metropolis at night with flying vehicles and purple neon rain reflections",
            style = VideoStyle.CINEMATIC
        ),
        SamplePrompt(
            promptHi = "चेरी ब्लॉसम के पेड़ों के बीच तलवार चलाता जापानी समुराई योद्धा",
            promptEn = "Legendary anime samurai warrior practicing katana strikes under falling cherry blossoms",
            style = VideoStyle.ANIME
        ),
        SamplePrompt(
            promptHi = "हिमालय के बर्फ से ढके पहाड़ों के ऊपर उड़ता सुनहरा चील",
            promptEn = "Golden eagle soaring high above snow-capped Himalayan mountain peaks at sunrise",
            style = VideoStyle.REALISTIC
        ),
        SamplePrompt(
            promptHi = "जादुई तैरते द्वीप और बादलों में उड़ता क्रिस्टल ड्रैगन",
            promptEn = "Floating magical islands with glowing crystal dragon flying through purple clouds",
            style = VideoStyle.FANTASY
        ),
        SamplePrompt(
            promptHi = "प्यारा सा रोबोट जो बारिश में छाता पकड़कर खड़ा है",
            promptEn = "Cute miniature robot holding a glowing yellow umbrella in a puddle of water",
            style = VideoStyle.THREE_D
        )
    )

    fun enhancePrompt(inputPrompt: String, style: VideoStyle, isHindi: Boolean): String {
        val base = inputPrompt.trim()
        if (base.isEmpty()) return ""
        val addition = when (style) {
            VideoStyle.CINEMATIC -> if (isHindi) ", 8K सिनेमैटिक, धीमी गति, नाटकीय रोशनी और गहराई" else ", 8K cinematic, slow motion, dramatic atmospheric lighting, depth of field"
            VideoStyle.REALISTIC -> if (isHindi) ", अत्यधिक यथार्थवादी, प्राकृतिक रोशनी, विस्तृत दृश्य" else ", hyper-realistic, natural sunlight, highly detailed textures, photorealistic"
            VideoStyle.ANIME -> if (isHindi) ", उच्च गुणवत्ता एनीमे शैली, स्टूडियो घिबली सौंदर्यशास्त्र" else ", high quality anime style, Studio Ghibli aesthetics, vivid colors"
            VideoStyle.NATURE -> if (isHindi) ", 4K वन्यजीव वृत्तचित्र, जैविक गति, प्राकृतिक वातावरण" else ", 4K wildlife documentary look, organic gentle motion, ambient environmental sound"
            VideoStyle.FANTASY -> if (isHindi) ", जादुई रोशनी, ईथर कण, पौराणिक दुनिया" else ", ethereal glowing particles, enchanted atmosphere, epic fantasy lighting"
            VideoStyle.THREE_D -> if (isHindi) ", 3D पिक्सर शैली, रे-ट्रेस्ड रोशनी, चिकनी एनिमेशन" else ", 3D Pixar render, ray-traced lighting, smooth fluid animation"
            VideoStyle.CARTOON -> if (isHindi) ", हंसमुख 2D एनिमेशन, चटक रंग, चंचल गति" else ", cheerful 2D animation, vibrant hues, playful motion dynamics"
            VideoStyle.HORROR -> if (isHindi) ", घना कोहरा, रहस्यमयी गहरी परछाइयां, सिनेमाई सस्पेंस" else ", dense volumetric fog, eerie suspenseful shadows, dark cinematic grading"
        }
        return base + addition
    }
}
