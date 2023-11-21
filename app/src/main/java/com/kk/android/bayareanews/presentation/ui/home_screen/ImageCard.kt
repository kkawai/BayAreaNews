package com.kk.android.bayareanews.presentation.ui.home_screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.twotone.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode
import com.kk.android.bayareanews.R

@ExperimentalMaterial3Api
@Composable
fun ImageCard(
    imageUrl: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {

    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    Card(
        modifier = modifier.animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                //model = "https://picsum.photos/seed/${Random.nextInt()}/300/200"
                model = imageUrl
            ),
            contentDescription = null,
            modifier = Modifier
                .clip(MaterialTheme.shapes.large)
                .fillMaxWidth()
                .aspectRatio(3f / 2f)
        )
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )

                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Outlined.ExpandLess else Icons.TwoTone.ExpandMore,
                        contentDescription = if (isExpanded) stringResource(R.string.expanded) else stringResource(
                            R.string.collapsed
                        )
                    )
                }
            }
            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    mainAxisSpacing = 8.dp,
                    mainAxisSize = SizeMode.Wrap
                ) {
                    AssistChip(
                        onClick = { },
                        colors = AssistChipDefaults.assistChipColors(
                            leadingIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.FavoriteBorder,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(text = "Mark as favorite")
                        }
                    )
                    AssistChip(
                        onClick = { },
                        colors = AssistChipDefaults.assistChipColors(
                            leadingIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Share,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(text = "Share with others")
                        }
                    )
                }
            }
        }
    }
}