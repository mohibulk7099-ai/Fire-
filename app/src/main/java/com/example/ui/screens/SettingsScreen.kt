package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppPreferences
import com.example.ui.common.Strings
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceHighlight
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SettingsScreen(
    isHindi: Boolean,
    preferences: AppPreferences,
    onLanguageChange: (String) -> Unit,
    onSaveSettings: (demoMode: Boolean, apiKey: String, backendUrl: String) -> Unit,
    onResetData: () -> Unit,
    onShowToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var demoMode by remember { mutableStateOf(preferences.isDemoMode()) }
    var apiKey by remember { mutableStateOf(preferences.getCustomApiKey()) }
    var backendUrl by remember { mutableStateOf(preferences.getCustomBackendUrl()) }

    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }
    var showResetConfirm by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = 90.dp)
    ) {
        // Title
        Text(
            text = Strings.get("settings_title", isHindi),
            color = TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 1. Language Section
        SettingSectionHeader(title = Strings.get("language_section", isHindi), icon = Icons.Default.Language)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(DarkSurface)
                .border(1.dp, DarkOutline, RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Hindi Option
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isHindi) NeonPurple.copy(alpha = 0.25f) else DarkSurfaceVariant)
                        .border(
                            1.5.dp,
                            if (isHindi) NeonPurple else DarkOutline,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onLanguageChange("hi") }
                        .padding(vertical = 12.dp, horizontal = 10.dp)
                        .testTag("lang_hi_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isHindi) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            text = Strings.get("language_hindi", isHindi),
                            color = if (isHindi) Color.White else TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = if (isHindi) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }

                // English Option
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (!isHindi) NeonPurple.copy(alpha = 0.25f) else DarkSurfaceVariant)
                        .border(
                            1.5.dp,
                            if (!isHindi) NeonPurple else DarkOutline,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onLanguageChange("en") }
                        .padding(vertical = 12.dp, horizontal = 10.dp)
                        .testTag("lang_en_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (!isHindi) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            text = Strings.get("language_english", isHindi),
                            color = if (!isHindi) Color.White else TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = if (!isHindi) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 2. Credits & Account Section
        SettingSectionHeader(title = Strings.get("credits_section", isHindi), icon = Icons.Default.Bolt)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(DarkSurface)
                .border(1.dp, DarkOutline, RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(DarkSurfaceHighlight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = preferences.getUserName(), color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text(text = "${Strings.get("user_id_label", isHindi)} ${preferences.getUserId()}", color = TextSecondary, fontSize = 11.sp)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(GoldYellow.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = "${preferences.getCredits()} ⚡", color = GoldYellow, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = Strings.get("starter_pack_note", isHindi),
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3. AI Video Provider Settings
        SettingSectionHeader(title = Strings.get("ai_provider_section", isHindi), icon = Icons.Default.Dns)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(DarkSurface)
                .border(1.dp, DarkOutline, RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column {
                // Demo Mode Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = Strings.get("demo_mode_switch", isHindi),
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = Strings.get("demo_mode_desc", isHindi),
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }

                    Switch(
                        checked = demoMode,
                        onCheckedChange = { demoMode = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = NeonPurple,
                            uncheckedThumbColor = TextSecondary,
                            uncheckedTrackColor = DarkSurfaceHighlight
                        ),
                        modifier = Modifier.testTag("demo_mode_switch")
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Custom API Key Input
                Text(
                    text = Strings.get("custom_api_key_label", isHindi),
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = apiKey,
                    onValueChange = { apiKey = it },
                    placeholder = {
                        Text(
                            text = Strings.get("custom_api_key_hint", isHindi),
                            color = TextSecondary.copy(alpha = 0.5f),
                            fontSize = 12.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_api_key_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonPurple,
                        unfocusedBorderColor = DarkOutline,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(10.dp),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Key, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Custom Backend URL Input
                Text(
                    text = Strings.get("custom_endpoint_label", isHindi),
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = backendUrl,
                    onValueChange = { backendUrl = it },
                    placeholder = {
                        Text(
                            text = "https://your-api.com/v1/generate",
                            color = TextSecondary.copy(alpha = 0.5f),
                            fontSize = 12.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_endpoint_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonPurple,
                        unfocusedBorderColor = DarkOutline,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(10.dp),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Dns, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Save Provider Button
                Button(
                    onClick = {
                        onSaveSettings(demoMode, apiKey, backendUrl)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("save_settings_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = Strings.get("save_settings_btn", isHindi),
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 4. Privacy & Legal Section
        SettingSectionHeader(title = Strings.get("privacy_terms_section", isHindi), icon = Icons.Default.Policy)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(DarkSurface)
                .border(1.dp, DarkOutline, RoundedCornerShape(16.dp))
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showPrivacyDialog = true }
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = Strings.get("privacy_policy", isHindi), color = TextPrimary, fontSize = 14.sp)
                    Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                }

                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(DarkOutline))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showTermsDialog = true }
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = Strings.get("terms_of_use", isHindi), color = TextPrimary, fontSize = 14.sp)
                    Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 5. Danger Zone / Reset
        SettingSectionHeader(title = Strings.get("danger_zone", isHindi), icon = Icons.Default.Lock)

        OutlinedButton(
            onClick = { showResetConfirm = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .testTag("reset_all_data_btn"),
            border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed.copy(alpha = 0.6f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.Default.Restore, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = Strings.get("reset_all_data", isHindi),
                color = ErrorRed,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

    // Privacy Policy Dialog
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = {
                Text(
                    text = Strings.get("privacy_policy", isHindi),
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = if (isHindi) {
                        "AI Video Maker आपकी गोपनीयता का सम्मान करता है। हम उपयोगकर्ता की अनुमति के बिना किसी भी व्यक्तिगत फोटो या वीडियो को तीसरे पक्ष के साथ साझा नहीं करते हैं। आपके द्वारा चुनी गई तस्वीरें केवल वीडियो निर्माण के लिए संसाधित की जाती हैं। सभी क्रेडेंशियल्स और एपीआई कुंजियाँ केवल आपके डिवाइस पर स्थानीय रूप से सुरक्षित रखी जाती हैं।"
                    } else {
                        "AI Video Maker respects your privacy. We do not share your private photos or generated videos with third parties without consent. Photos you choose are processed exclusively for generating your animated video. API keys and account data remain stored safely on your device."
                    },
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) {
                    Text(text = if (isHindi) "समझ गया" else "Got it", color = NeonCyan)
                }
            },
            containerColor = DarkSurface
        )
    }

    // Terms of Use Dialog
    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { showTermsDialog = false },
            title = {
                Text(
                    text = Strings.get("terms_of_use", isHindi),
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = if (isHindi) {
                        "1. AI Video Maker को केवल वैध, नैतिक और रचनात्मक उद्देश्यों के लिए उपयोग करें।\n2. हानिकारक, अवैध या कॉपीराइट सामग्री बनाना सख्त वर्जित है।\n3. नए यूज़र्स को 50 स्टार्टर क्रेडिट और रोज़ 20 दैनिक क्रेडिट मिलते हैं। विफल जनरेशन के क्रेडिट अपने आप रिफंड हो जाते हैं।\n4. AI जनित वीडियो में मूल छवि की तुलना में रचनात्मक विभिन्नता हो सकती है।"
                    } else {
                        "1. Use AI Video Maker only for lawful, creative purposes.\n2. Generating harmful, illegal, or infringing content is strictly prohibited.\n3. Free tier includes 50 starter credits and 20 daily bonus credits. Eligible failed generation credits are refunded automatically.\n4. AI outputs are artistic animations and may naturally diverge from static inputs."
                    },
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { showTermsDialog = false }) {
                    Text(text = if (isHindi) "स्वीकार है" else "Accept", color = NeonPurple)
                }
            },
            containerColor = DarkSurface
        )
    }

    // Reset Data Confirmation
    if (showResetConfirm) {
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            title = {
                Text(text = if (isHindi) "डेटा रीसेट की पुष्टि करें" else "Confirm Data Reset", color = TextPrimary)
            },
            text = {
                Text(
                    text = if (isHindi) "यह सभी जनरेट किए गए वीडियो को हटा देगा और बैलेंस को 50 क्रेडिट पर रीसेट कर देगा।" else "This will clear all generated creations and reset your balance to 50 starter credits.",
                    color = TextSecondary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showResetConfirm = false
                        onResetData()
                    }
                ) {
                    Text(text = if (isHindi) "रीसेट करें" else "Reset", color = ErrorRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirm = false }) {
                    Text(text = Strings.get("cancel", isHindi), color = TextSecondary)
                }
            },
            containerColor = DarkSurface
        )
    }
}

@Composable
fun SettingSectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = title, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}
