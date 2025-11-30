package com.kelompok4.serena.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation. background
import androidx.compose.foundation. clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose. foundation.shape.RoundedCornerShape
import androidx.compose. material. icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime. Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose. ui.graphics.Color
import androidx.compose. ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx. compose.ui.unit.dp
import com.kelompok4.serena.data.Counseling
import com. kelompok4.serena. ui.theme.*

@Composable
fun CounselingScheduleCard(
    counseling: Counseling,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            . fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Primary500
        ),
        elevation = CardDefaults. cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                . fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header dengan foto konselor dan info
            Row(
                modifier = Modifier. fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Foto konselor
                    Image(
                        painter = painterResource(id = counseling.counselorImage),
                        contentDescription = "Counselor Photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier. width(12.dp))

                    // Info konselor
                    Column {
                        Text(
                            text = counseling.counselorSpecialty,
                            style = AppTypography. Subtitle2.medium,
                            color = Color.White. copy(alpha = 0.9f)
                        )
                        Text(
                            text = counseling.counselorName,
                            style = AppTypography. Body1.bold,
                            color = Color. White
                        )
                    }
                }

                // Arrow icon
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Detail",
                    tint = Color.White,
                    modifier = Modifier. size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tanggal dan waktu
            Row(
                modifier = Modifier. fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tanggal
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            color = Color.White. copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8. dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Date",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier. width(6.dp))
                    Text(
                        text = counseling. date,
                        style = AppTypography. Subtitle2.medium,
                        color = Color.White
                    )
                }

                // Waktu
                Row(
                    verticalAlignment = Alignment. CenterVertically,
                    modifier = Modifier
                        . background(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default. Schedule,
                        contentDescription = "Time",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = counseling.time,
                        style = AppTypography.Subtitle2.medium,
                        color = Color.White
                    )
                }
            }
        }
    }
}