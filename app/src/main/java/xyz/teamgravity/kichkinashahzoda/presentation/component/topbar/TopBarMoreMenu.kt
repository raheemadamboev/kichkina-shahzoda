package xyz.teamgravity.kichkinashahzoda.presentation.component.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import xyz.teamgravity.coresdkcompose.button.IconButtonPlain
import xyz.teamgravity.coresdkcompose.menu.GDropdownMenuItem
import xyz.teamgravity.kichkinashahzoda.R

@Composable
fun TopBarMoreMenu(
    expanded: Boolean,
    onExpand: () -> Unit,
    onDismiss: () -> Unit,
    onSupport: () -> Unit,
    onShare: () -> Unit,
    onRate: () -> Unit,
    onSourceCode: () -> Unit,
    onAbout: () -> Unit
) {
    IconButtonPlain(
        onClick = onExpand,
        icon = Icons.Rounded.MoreVert,
        contentDescription = R.string.cd_more_vertical
    )
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        GDropdownMenuItem(
            onDismiss = onDismiss,
            onClick = onSupport,
            icon = R.drawable.ic_customer_service,
            label = R.string.support
        )
        GDropdownMenuItem(
            onDismiss = onDismiss,
            onClick = onShare,
            icon = R.drawable.ic_share,
            label = R.string.share
        )
        GDropdownMenuItem(
            onDismiss = onDismiss,
            onClick = onRate,
            icon = R.drawable.ic_star,
            label = R.string.rate
        )
        GDropdownMenuItem(
            onDismiss = onDismiss,
            onClick = onSourceCode,
            icon = R.drawable.ic_github,
            label = R.string.source_code
        )
        GDropdownMenuItem(
            onDismiss = onDismiss,
            onClick = onAbout,
            icon = R.drawable.ic_info,
            label = R.string.about_me
        )
    }
}