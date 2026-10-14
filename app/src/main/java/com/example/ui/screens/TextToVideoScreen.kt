package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AspectRatio
import com.example.model.PromptRepository
import com.example.model.VideoStyle
import com.example.ui.common.Strings
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceHighlight
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TextToVideoScreen(
    isHindi: Boolean,
    userCredits: Int,
    onGenerate: (prompt: String, style: VideoStyle, aspect: AspectRatio, duration: Int, quality: String) -> Unit,
    onShowToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var promptText by remember { mutableStateOf("") }
    var selectedStyle by remember { mutableStateOf(VideoStyle.CINEMATIC) }
    var selectedAspect by remember { mutableStateOf(AspectRatio.PORTRAIT_9_16) }
    var selectedDuration by remember { mutableIntStateOf(5) }
    var selectedQuality by remember { mutableStateOf("1080p FHD") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = 90.dp)
    ) {
        // Title
        Text(
            text = Strings.get("text_generator_title", isHindi),
            color = TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Prompt Input Card
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
                    Text(
                        text = Strings.get("prompt_input_label", isHindi),
                        color = NeonCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Enhance Prompt Button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(NeonPurple.copy(alpha = 0.2f))
                            .border(1.dp, NeonPurple.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .clickable {
                                if (promptText.isNotBlank()) {
                                    promptText = PromptRepository.enhancePrompt(promptText, selectedStyle, isHindi)
                                    onShowToast(Strings.get("enhanced_success", isHindi))
                                } else {
                                    onShowToast(Strings.get("empty_prompt_error", isHindi))
                                }
                            }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .testTag("enhance_prompt_btn")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color(0xFFC084FC),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = Strings.get("enhance_prompt_btn", isHindi),
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = promptText,
                    onValueChange = { promptText = it },
                    placeholder = {
                        Text(
                            text = Strings.get("prompt_input_hint", isHindi),
                            color = TextSecondary.copy(alpha = 0.6f),
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .testTag("prompt_input_field"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonPurple,
                        unfocusedBorderColor = DarkOutline,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = NeonCyan
                    ),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        if (promptText.isNotEmpty()) {
                            IconButton(onClick = { promptText = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Example Prompts Chips
        Text(
            text = Strings.get("sample_prompts_title", isHindi),
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(PromptRepository.samplePrompts) { item ->
                val sampleText = if (isHindi) item.promptHi else item.promptEn
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(DarkSurfaceVariant)
                        .border(1.dp, DarkOutline, RoundedCornerShape(14.dp))
                        .clickable {
                            promptText = sampleText
                            selectedStyle = item.style
                        }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = sampleText,
                        color = Color.White,
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Video Style Selector
        Text(
            text = Strings.get("select_style_title", isHindi),
            color = TextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VideoStyle.entries.forEach { style ->
                val isSelected = style == selectedStyle
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) NeonPurple.copy(alpha = 0.25f) else DarkSurface)
                        .border(
                            1.5.dp,
                            if (isSelected) NeonPurple else DarkOutline,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { selectedStyle = style }
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                        .testTag("style_${style.id}")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            text = style.getDisplayName(isHindi),
                            color = if (isSelected) Color.White else TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Aspect Ratio Selector
        Text(
            text = Strings.get("aspect_ratio_title", isHindi),
            color = TextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AspectRatio.entries.forEach { aspect ->
                val isSelected = aspect == selectedAspect
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSelected) NeonCyan.copy(alpha = 0.18f) else DarkSurface)
                        .border(
                            1.5.dp,
                            if (isSelected) NeonCyan else DarkOutline,
                            RoundedCornerShape(14.dp)
                        )
                        .clickable { selectedAspect = aspect }
                        .padding(vertical = 12.dp, horizontal = 8.dp)
                        .testTag("aspect_${aspect.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = aspect.ratioText,
                            color = if (isSelected) NeonCyan else TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = aspect.getDisplayName(isHindi),
                            color = TextSecondary,
                            fontSize = 10.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Duration & Quality Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Duration
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = Strings.get("duration_title", isHindi),
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(5, 10).forEach { dur ->
                        val isSelected = dur == selectedDuration
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) NeonPurple.copy(alpha = 0.3f) else DarkSurface)
                                .border(1.dp, if (isSelected) NeonPurple else DarkOutline, RoundedCornerShape(10.dp))
                                .clickable { selectedDuration = dur }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${dur}s",
                                color = if (isSelected) Color.White else TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Quality
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = Strings.get("quality_title", isHindi),
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("720p HD", "1080p FHD").forEach { q ->
                        val isSelected = q == selectedQuality
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) NeonCyan.copy(alpha = 0.25f) else DarkSurface)
                                .border(1.dp, if (isSelected) NeonCyan else DarkOutline, RoundedCornerShape(10.dp))
                                .clickable { selectedQuality = q }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = q,
                                color = if (isSelected) Color.White else TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Cost & Balance Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(DarkSurfaceHighlight)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    tint = GoldYellow,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = Strings.get("cost_indicator", isHindi),
                    color = GoldYellow,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "${Strings.get("current_balance", isHindi)} $userCredits",
                color = TextSecondary,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Big Generate Button
        Button(
            onClick = {
                if (promptText.isBlank()) {
                    onShowToast(Strings.get("empty_prompt_error", isHindi))
                } else if (userCredits < 10) {
                    onShowToast(Strings.get("not_enough_credits", isHindi))
                } else {
                    onGenerate(promptText, selectedStyle, selectedAspect, selectedDuration, selectedQuality)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("generate_video_submit_btn"),
            colors = ButtonDefaults.buttonColors(
                containerColor = NeonPurple
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = Strings.get("generate_video_btn", isHindi),
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}
