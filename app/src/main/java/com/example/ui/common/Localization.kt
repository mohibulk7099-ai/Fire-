package com.example.ui.common

object Strings {
    fun get(key: String, isHindi: Boolean): String {
        val entry = stringMap[key] ?: return key
        return if (isHindi) entry.first else entry.second
    }

    private val stringMap = mapOf(
        "app_name" to Pair("AI Video Maker", "AI Video Maker"),
        "app_subtitle" to Pair("फ्री AI वीडियो जनरेटर", "Free AI Video Generator"),
        "nav_home" to Pair("होम", "Home"),
        "nav_text_video" to Pair("टेक्स्ट से वीडियो", "Text to Video"),
        "nav_photo_video" to Pair("फोटो से वीडियो", "Photo to Video"),
        "nav_creations" to Pair("मेरी लाइब्रेरी", "Creations"),
        "nav_settings" to Pair("सेटिंग्स", "Settings"),

        // Home
        "balance_credits" to Pair("उपलब्ध क्रेडिट", "Available Credits"),
        "free_credits_badge" to Pair("फ्री क्रेडिट", "Free Credits"),
        "daily_bonus_title" to Pair("दैनिक फ्री क्रेडिट बोनस", "Daily Free Bonus"),
        "daily_bonus_desc" to Pair("हर 24 घंटे में +20 फ्री क्रेडिट प्राप्त करें", "Get +20 free credits every 24 hours"),
        "claim_now" to Pair("बोनस प्राप्त करें (+20)", "Claim Bonus (+20)"),
        "claimed_cooldown" to Pair("आज का बोनस लिया जा चुका है", "Claimed! Next in"),
        "quick_text_video_title" to Pair("टेक्स्ट से वीडियो बनाएं", "Text-to-Video Studio"),
        "quick_text_video_desc" to Pair("अपने शब्दों से शानदार 8K रील और शॉर्ट्स बनाएं", "Turn your ideas into cinematic Reels & Shorts"),
        "quick_photo_video_title" to Pair("फोटो से वीडियो बनाएं", "Photo-to-Video Animator"),
        "quick_photo_video_desc" to Pair("गैलरी की फोटो को मोशन और लाइफ दें", "Bring any still picture to life with motion"),
        "recent_creations_title" to Pair("हाल ही में बनाए गए वीडियो", "Recent Creations"),
        "view_all" to Pair("सभी देखें", "View All"),
        "no_recent_videos" to Pair("अभी कोई वीडियो नहीं बना है। नीचे बटन दबाकर बनाएं!", "No videos yet. Tap below to create your first video!"),
        "create_video_cta" to Pair("नया वीडियो बनाएं", "Create New Video"),
        "mode_demo" to Pair("डेमो मोड (सक्रिय)", "Demo Mode (Active)"),
        "mode_real" to Pair("रियल AI प्रदाता (सक्रिय)", "Real AI Provider (Active)"),

        // Text to Video
        "text_generator_title" to Pair("टेक्स्ट से वीडियो बनाएं", "Text to Video Generator"),
        "prompt_input_label" to Pair("अपना प्रॉम्प्ट लिखें", "Enter Prompt"),
        "prompt_input_hint" to Pair("उदाहरण: बादलों के ऊपर उड़ता हुआ सुनहरा ड्रैगन...", "e.g. Majestic golden dragon flying above cloud peaks at sunset..."),
        "enhance_prompt_btn" to Pair("AI से बेहतर बनाएं", "Enhance with AI"),
        "enhanced_success" to Pair("प्रॉम्प्ट को AI से समृद्ध किया गया!", "Prompt enhanced with cinematic details!"),
        "sample_prompts_title" to Pair("लोकप्रिय उदाहरण (टैप करें):", "Sample Ideas (Tap to use):"),
        "select_style_title" to Pair("वीडियो स्टाइल चुनें", "Choose Video Style"),
        "aspect_ratio_title" to Pair("आकार / रेश्यो चुनें", "Aspect Ratio"),
        "duration_title" to Pair("अवधि (सेकंड)", "Duration (Seconds)"),
        "quality_title" to Pair("गुणवत्ता (Resolution)", "Output Quality"),
        "cost_indicator" to Pair("लागत: 10 क्रेडिट", "Cost: 10 Credits"),
        "current_balance" to Pair("वर्तमान बैलेंस:", "Balance:"),
        "generate_video_btn" to Pair("वीडियो बनाएं (10 क्रेडिट)", "Generate Video (10 Credits)"),
        "not_enough_credits" to Pair("पर्याप्त क्रेडिट नहीं हैं! कृपया दैनिक बोनस लें।", "Not enough credits! Please claim daily bonus."),
        "empty_prompt_error" to Pair("कृपया पहले एक प्रॉम्प्ट लिखें।", "Please enter a prompt first."),

        // Photo to Video
        "photo_generator_title" to Pair("फोटो से वीडियो बनाएं", "Photo to Video Generator"),
        "select_photo_btn" to Pair("गैलरी से फोटो चुनें", "Select Photo from Gallery"),
        "change_photo_btn" to Pair("दूसरी फोटो चुनें", "Change Photo"),
        "photo_selected_badge" to Pair("फोटो सफलतापूर्वक चुनी गई", "Photo selected successfully"),
        "motion_prompt_label" to Pair("मोशन व कैमरा प्रभाव (वैकल्पिक)", "Motion & Camera Prompt (Optional)"),
        "motion_prompt_hint" to Pair("उदाहरण: कैमरा ज़ूम इन, हल्की हवा, बहता पानी...", "e.g. Camera slow zoom in, hair blowing gently in wind, glowing aura..."),
        "photo_disclaimer" to Pair("सूचना: AI फोटो को गतिशीलता देता है। वीडियो में मूल फोटो की तुलना में सूक्ष्म बदलाव हो सकते हैं।", "Notice: AI animates the image. Small artistic variations from the original photo may occur."),
        "animate_photo_btn" to Pair("एनिमेटेड वीडियो बनाएं (10 क्रेडिट)", "Animate into Video (10 Credits)"),
        "no_photo_error" to Pair("कृपया पहले एक फोटो चुनें।", "Please select a photo first."),

        // Progress Dialog & States
        "generating_title" to Pair("वीडियो तैयार हो रहा है...", "Generating Video..."),
        "step_prompt_analyzing" to Pair("प्रॉम्प्ट और शैली का विश्लेषण...", "Analyzing prompt and style..."),
        "step_model_queue" to Pair("AI मॉडल कतार में सुरक्षित आवंटन...", "Allocating neural model slot..."),
        "step_neural_render" to Pair("न्यूरल वीडियो फ्रेम्स रेंडर हो रहे हैं...", "Rendering neural video frames..."),
        "step_encoding_mp4" to Pair("उच्च गुणवत्ता MP4 एन्कोडिंग...", "Encoding high quality MP4..."),
        "step_finalizing" to Pair("तैयार! अंतिम फाइल व्यवस्थित की जा रही है...", "Finalizing video output..."),
        "cancel_and_refund" to Pair("रद्द करें और क्रेडिट वापस लें", "Cancel & Refund Credits"),
        "refund_notice" to Pair("10 क्रेडिट वापस आपके बैलेंस में जोड़ दिए गए।", "10 credits have been refunded to your balance."),
        "job_failed_title" to Pair("जनरेशन असफल रहा", "Generation Failed"),
        "retry_btn" to Pair("दोबारा कोशिश करें", "Retry"),

        // Library / Creations
        "creations_title" to Pair("मेरी वीडियो लाइब्रेरी", "My Video Library"),
        "filter_all" to Pair("सभी", "All"),
        "filter_completed" to Pair("तैयार", "Completed"),
        "filter_processing" to Pair("प्रक्रिया में", "Rendering"),
        "filter_failed" to Pair("असफल", "Failed"),
        "no_creations_msg" to Pair("आपके द्वारा बनाई गई वीडियो यहाँ दिखाई देंगी।", "Videos generated by you will appear here."),
        "watch_video" to Pair("वीडियो चलाएं", "Play Video"),
        "download_mp4" to Pair("डाउनलोड करें (MP4)", "Download MP4"),
        "downloading" to Pair("डाउनलोड हो रहा है...", "Downloading..."),
        "download_complete" to Pair("वीडियो सफलतापूर्वक डाउनलोड फ़ोल्डर में सहेजा गया!", "Video saved to Downloads folder!"),
        "share_video" to Pair("शेयर करें", "Share Video"),
        "delete_video" to Pair("डिलीट करें", "Delete Video"),
        "confirm_delete_title" to Pair("क्या आप यह वीडियो हटाना चाहते हैं?", "Delete this video?"),
        "confirm_delete_desc" to Pair("यह क्रिया वापस नहीं ली जा सकती।", "This action cannot be undone."),
        "confirm" to Pair("हाँ, हटाएं", "Yes, Delete"),
        "cancel" to Pair("रद्द करें", "Cancel"),

        // Settings
        "settings_title" to Pair("सेटिंग्स और सहायता", "Settings & Help"),
        "language_section" to Pair("भाषा चुनें / Language", "Select Language"),
        "language_hindi" to Pair("हिंदी (Hindi - डिफ़ॉल्ट)", "Hindi (Default)"),
        "language_english" to Pair("English (अंग्रेज़ी)", "English"),
        "credits_section" to Pair("क्रेडिट और खाता", "Credits & Account"),
        "user_id_label" to Pair("उपयोगकर्ता आईडी:", "User ID:"),
        "starter_pack_note" to Pair("नए यूज़र्स को 50 फ्री क्रेडिट्स और रोज़ 20 क्रेडिट्स मिलते हैं।", "New users get 50 starter credits + 20 free daily bonus."),
        "ai_provider_section" to Pair("AI वीडियो प्रदाता कॉन्फ़िगरेशन", "AI Video Provider Settings"),
        "demo_mode_switch" to Pair("डेमो मोड (सत्यापित नमूना वीडियो)", "Demo Mode (Verified MP4 Samples)"),
        "demo_mode_desc" to Pair("यदि आपके पास API Key नहीं है, तो सुरक्षित डेमो मोड सक्रिय रहता है।", "Active when no external API key is configured."),
        "custom_api_key_label" to Pair("कस्टम AI API Key (वैकल्पिक)", "Custom AI API Key (Optional)"),
        "custom_api_key_hint" to Pair("Luma / Runway / Replicate / Fal Key", "Enter provider secret key"),
        "custom_endpoint_label" to Pair("कस्टम सर्वर URL (वैकल्पिक)", "Custom Backend Endpoint (Optional)"),
        "save_settings_btn" to Pair("सेटिंग्स सहेजें", "Save Settings"),
        "settings_saved" to Pair("सेटिंग्स सुरक्षित रूप से सहेज ली गईं!", "Settings saved successfully!"),
        "privacy_terms_section" to Pair("गोपनीयता और नियम", "Privacy & Legal"),
        "privacy_policy" to Pair("गोपनीयता नीति (Privacy Policy)", "Privacy Policy"),
        "terms_of_use" to Pair("उपयोग की शर्तें (Terms of Use)", "Terms of Service"),
        "danger_zone" to Pair("डेटा प्रबंधन", "Data Management"),
        "reset_all_data" to Pair("सभी डेटा रीसेट करें (डिफ़ॉल्ट 50 क्रेडिट)", "Reset All Data (Restore 50 Credits)"),
        "reset_success" to Pair("डेटा सफलतापूर्वक रीसेट हो गया।", "All data reset successfully.")
    )
}
