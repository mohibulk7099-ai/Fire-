package com.example

import com.example.model.AspectRatio
import com.example.model.PromptRepository
import com.example.model.VideoStyle
import com.example.ui.common.Strings
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AiVideoMakerUnitTest {

    @Test
    fun testLocalizationStrings_HindiAndEnglish() {
        val appNameHi = Strings.get("app_name", isHindi = true)
        val appNameEn = Strings.get("app_name", isHindi = false)
        assertEquals("AI Video Maker", appNameHi)
        assertEquals("AI Video Maker", appNameEn)

        val homeHi = Strings.get("nav_home", isHindi = true)
        val homeEn = Strings.get("nav_home", isHindi = false)
        assertEquals("होम", homeHi)
        assertEquals("Home", homeEn)

        val creationsHi = Strings.get("nav_creations", isHindi = true)
        val creationsEn = Strings.get("nav_creations", isHindi = false)
        assertEquals("मेरी लाइब्रेरी", creationsHi)
        assertEquals("Creations", creationsEn)

        val cancelRefundHi = Strings.get("cancel_and_refund", isHindi = true)
        val cancelRefundEn = Strings.get("cancel_and_refund", isHindi = false)
        assertEquals("रद्द करें और क्रेडिट वापस लें", cancelRefundHi)
        assertEquals("Cancel & Refund Credits", cancelRefundEn)
    }

    @Test
    fun testVideoStylesAndAspectRatios() {
        assertEquals(8, VideoStyle.entries.size)
        val realistic = VideoStyle.REALISTIC
        assertEquals("यथार्थवादी", realistic.getDisplayName(isHindi = true))
        assertEquals("Realistic", realistic.getDisplayName(isHindi = false))

        val cinematic = VideoStyle.CINEMATIC
        assertEquals("सिनेमैटिक", cinematic.getDisplayName(isHindi = true))
        assertEquals("Cinematic", cinematic.getDisplayName(isHindi = false))

        assertEquals(3, AspectRatio.entries.size)
        val shortsAspect = AspectRatio.PORTRAIT_9_16
        assertEquals("9:16", shortsAspect.ratioText)
        assertTrue(shortsAspect.getDisplayName(isHindi = true).contains("9:16"))
    }

    @Test
    fun testPromptEnhancement() {
        val input = "मोर का नाच"
        val enhancedHi = PromptRepository.enhancePrompt(input, VideoStyle.CINEMATIC, isHindi = true)
        assertTrue(enhancedHi.contains("मोर का नाच"))
        assertTrue(enhancedHi.contains("सिनेमैटिक"))

        val inputEn = "Dancing peacock"
        val enhancedEn = PromptRepository.enhancePrompt(inputEn, VideoStyle.REALISTIC, isHindi = false)
        assertTrue(enhancedEn.contains("Dancing peacock"))
        assertTrue(enhancedEn.contains("photorealistic"))
    }

    @Test
    fun testSamplePromptsAvailability() {
        assertTrue(PromptRepository.samplePrompts.isNotEmpty())
        PromptRepository.samplePrompts.forEach { sample ->
            assertNotNull(sample.promptHi)
            assertNotNull(sample.promptEn)
            assertNotNull(sample.style)
        }
    }
}
