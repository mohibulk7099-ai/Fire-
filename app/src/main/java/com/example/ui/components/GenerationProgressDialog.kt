package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.VideoEntity
import com.example.ui.common.Strings
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun GenerationProgressDialog(
    job: VideoEntity,
    isHindi: Boolean,
    onCancel: () -> Unit,
    onWatch: (VideoEntity) -> Unit,
    onDismiss: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin"
    )

    Dialog(onDismissRequest = {
        if (job.status == "COMPLETED" || job.status == "FAILED") {
            onDismiss()
        }
    }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(DarkSurface)
                .border(
                    1.5.dp,
                    Brush.linearGradient(
                        listOf(NeonPurple, NeonCyan, Color(0xFFEC4899))
                    ),
                    RoundedCornerShape(24.dp)
                )
                .padding(24.dp)
                .testTag("generation_progress_dialog")
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Top animation / status icon
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(DarkSurfaceVariant)
                        .border(2.dp, Brush.sweepGradient(listOf(NeonPurple, NeonCyan, NeonPurple)), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    when (job.status) {
                        "COMPLETED" -> {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Completed",
                                tint = SuccessGreen,
                                modifier = Modifier.size(44.dp)
                            )
                        }
                        "FAILED" -> {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = "Failed",
                                tint = ErrorRed,
                                modifier = Modifier.size(44.dp)
                            )
                        }
                        else -> {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Generating",
                                tint = NeonCyan,
                                modifier = Modifier
                                    .size(40.dp)
                                    .rotate(rotation)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Title
                Text(
                    text = when (job.status) {
                        "COMPLETED" -> if (isHindi) "वीडियो तैयार है!" else "Video Ready!"
                        "FAILED" -> Strings.get("job_failed_title", isHindi)
                        else -> Strings.get("generating_title", isHindi)
                    },
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Status message
                Text(
                    text = if (job.status == "FAILED") {
                        job.errorMessage ?: Strings.get("refund_notice", isHindi)
                    } else {
                        job.statusMessage.ifEmpty { Strings.get("step_neural_render", isHindi) }
                    },
                    color = if (job.status == "FAILED") ErrorRed else TextSecondary,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Progress Bar and Percentage
                if (job.status == "PROCESSING" || job.status == "QUEUED") {
                    val percent = (job.progress * 100).toInt().coerceIn(5, 98)
                    Text(
                        text = "$percent%",
                        color = NeonCyan,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { job.progress.coerceIn(0.05f, 0.98f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = NeonPurple,
                        trackColor = DarkSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Notice
                    Text(
                        text = if (job.isDemo) {
                            if (isHindi) "✨ डेमो मोड: सुरक्षित नमूना रेंडरिंग (10 क्रेडिट लागू)" else "✨ Demo Mode: Real-time rendering workflow"
                        } else {
                            if (isHindi) "⚡ रियल AI क्लस्टर कनेक्टेड" else "⚡ Real AI Cluster Active"
                        },
                        color = Color(0xFFA5F3FC),
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Actions
                when (job.status) {
                    "COMPLETED" -> {
                        Button(
                            onClick = { onWatch(job) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("watch_video_btn"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonPurple
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = Strings.get("watch_video", isHindi),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    "FAILED" -> {
                        Button(
                            onClick = { onDismiss() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkSurfaceVariant
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = Strings.get("cancel", isHindi),
                                fontSize = 14.sp,
                                color = TextPrimary
                            )
                        }
                    }
                    else -> {
                        // Cancel button with refund guarantee
                        OutlinedButton(
                            onClick = { onCancel() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("cancel_job_btn"),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed.copy(alpha = 0.6f)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = ErrorRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = Strings.get("cancel_and_refund", isHindi),
                                color = ErrorRed,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
