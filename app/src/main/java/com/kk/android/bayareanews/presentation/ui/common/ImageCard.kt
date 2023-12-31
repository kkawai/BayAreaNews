package com.kk.android.bayareanews.presentation.ui.common

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.twotone.ExpandMore
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.kk.android.bayareanews.domain.model.Rss

@ExperimentalMaterial3Api
@Composable
fun ImageCard(
    rss: Rss,
    expandedByDefault: Boolean,
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
    onArticleShared: () -> Unit,
    onSaveFavorite: () -> Unit,
    onDeleteFavorite: () -> Unit
) {

    var isExpanded by rememberSaveable {
        mutableStateOf(expandedByDefault)
    }

    var isFavorited by rememberSaveable {
        mutableStateOf(isFavorite)
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
                model = rss.imageUrl
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

            Text(
                text = "${rss.publisher} · ${rss.timeAgo} · ${rss.author}",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(0.dp),
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = rss.title,
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
                    text = rss.descr,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    mainAxisSpacing = 8.dp,
                    mainAxisSize = SizeMode.Wrap
                ) {
                    AssistChip(
                        onClick = {
                            if (isFavorited)
                                onDeleteFavorite()
                            else
                                onSaveFavorite()
                            isFavorited = !isFavorited
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            leadingIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = if (isFavorited) Icons.Outlined.Favorite
                                else Icons.Outlined.FavoriteBorder,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(text = stringResource(R.string.mark_as_favorite))
                        }
                    )
                    AssistChip(
                        onClick = {onArticleShared()},
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
                            Text(text = stringResource(R.string.share_with_others))
                        }
                    )
                }
            } //isExpanded
        }
    }
}