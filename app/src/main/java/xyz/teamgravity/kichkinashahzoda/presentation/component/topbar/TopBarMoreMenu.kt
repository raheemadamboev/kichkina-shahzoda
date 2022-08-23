package xyz.teamgravity.kichkinashahzoda.presentation.component.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import xyz.teamgravity.kichkinashahzoda.R
import xyz.teamgravity.kichkinashahzoda.presentation.component.button.IconButtonPlain
import xyz.teamgravity.kichkinashahzoda.presentation.component.image.IconPlain
import xyz.teamgravity.kichkinashahzoda.presentation.component.text.TextPlain

@Composable
fun TopBarMoreMenu(
    expanded: Boolean,
    onExpand: () -> Unit,
    onDismiss: () -> Unit,
    onSupportClick: () -> Unit,
    onShareClick: () -> Unit,
    onRateClick: () -> Unit,
    onSourceCodeClick: () -> Unit,
    onAboutClick: () -> Unit,
) {
    IconButtonPlain(
        onClick = onExpand,
        icon = Icons.Default.MoreVert,
        contentDescription = R.string.cd_more_vertical
    )
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        DropdownMenuItem(
            text = { TextPlain(id = R.string.support) },
            onClick = onSupportClick,
            leadingIcon = {
                IconPlain(
                    icon = R.drawable.ic_customer_service,
                    contentDescription = R.string.support
                )
            }
        )
        DropdownMenuItem(
            text = { TextPlain(id = R.string.share) },
            onClick = onShareClick,
            leadingIcon = {
                IconPlain(
                    icon = R.drawable.ic_share,
                    contentDescription = R.string.share
                )
            }
        )
        DropdownMenuItem(
            text = { TextPlain(id = R.string.rate) },
            onClick = onRateClick,
            leadingIcon = {
                IconPlain(
                    icon = R.drawable.ic_star,
                    contentDescription = R.string.share
                )
            }
        )
        DropdownMenuItem(
            text = { TextPlain(id = R.string.source_code) },
            onClick = onSourceCodeClick,
            leadingIcon = {
                IconPlain(
                    icon = R.drawable.ic_github,
                    contentDescription = R.string.source_code,
                )
            }
        )
        DropdownMenuItem(
            text = { TextPlain(id = R.string.about_me) },
            onClick = onAboutClick,
            leadingIcon = {
                IconPlain(
                    icon = R.drawable.ic_info,
                    contentDescription = R.string.about_me
                )
            }
        )
    }
}