package edu.ucsb.cs.cs184.group9.billsplitter.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import edu.ucsb.cs.cs184.group9.billsplitter.R

enum class ExpandableState { VISIBLE, HIDDEN }

// https://thecommonwise.com/blogs/6224b2820e1a380016cfd18e
@Composable
fun ExpandableCard(
    modifier: Modifier = Modifier,
    defaultState: ExpandableState = ExpandableState.HIDDEN,
    title: @Composable ColumnScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {

    //State
    var isContentVisible by rememberSaveable { mutableStateOf(defaultState) }
    var cardArrowDegrees by rememberSaveable { mutableStateOf(180f) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                isContentVisible =
                    if (isContentVisible == ExpandableState.VISIBLE) ExpandableState.HIDDEN else ExpandableState.VISIBLE
            }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(0.85f)) {
                    title()
                }
                Column(modifier = Modifier.weight(0.15f)) {
                    CardArrow(
                        degrees = cardArrowDegrees,
                        onClick = {
                            if (isContentVisible == ExpandableState.VISIBLE) {
                                isContentVisible = ExpandableState.HIDDEN
                                cardArrowDegrees = 180f
                            } else {
                                isContentVisible = ExpandableState.VISIBLE
                                cardArrowDegrees = 0f
                            }
                        }
                    )
                }
            }
            AnimatedVisibility(visible = isContentVisible == ExpandableState.VISIBLE) {
                Column( modifier = Modifier.fillMaxWidth() ) {
                    content()
                }
            }
        }
    }
}

@Composable
private fun CardArrow(
    degrees: Float,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        content = {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_expand_less_24),
                contentDescription = "Expand card",
                modifier = Modifier.rotate(degrees)
            )
        }
    )
}