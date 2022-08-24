package xyz.teamgravity.kichkinashahzoda.presentation.screen.support

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import xyz.teamgravity.kichkinashahzoda.R
import xyz.teamgravity.kichkinashahzoda.core.util.Helper
import xyz.teamgravity.kichkinashahzoda.presentation.component.button.IconButtonPlain
import xyz.teamgravity.kichkinashahzoda.presentation.theme.White

@Composable
fun SupportPortraitScreen(
    onBackButtonClick: () -> Unit,
) {

    val context = LocalContext.current

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (gradientC, backB, headerT, headerI, bodyC) = createRefs()
        val (telegramB, mailB) = createRefs()
        val oneG = createGuidelineFromTop(0.3F)
        val twoG = createGuidelineFromTop(0.5F)
        val threeG = createGuidelineFromTop(0.75F)

        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.primary)
                .constrainAs(gradientC) {
                    width = Dimension.matchParent
                    height = Dimension.fillToConstraints
                    linkTo(top = parent.top, bottom = oneG)
                }
        )
        IconButtonPlain(
            onClick = onBackButtonClick,
            icon = Icons.Default.ArrowBack,
            contentDescription = R.string.cd_back_button,
            tint = White,
            modifier = Modifier.constrainAs(backB) {
                start.linkTo(anchor = parent.start, margin = 12.dp)
                top.linkTo(anchor = parent.top, margin = 12.dp)
            }
        )
        Text(
            text = stringResource(id = R.string.need_help),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            color = White,
            modifier = Modifier.constrainAs(headerT) {
                width = Dimension.fillToConstraints
                linkTo(start = backB.end, end = parent.end, endMargin = 38.dp)
                linkTo(top = backB.top, bottom = backB.bottom)
            }
        )
        Image(
            painter = painterResource(id = R.drawable.sticker_customer_service),
            contentDescription = stringResource(id = R.string.cd_user_support),
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center,
            modifier = Modifier.constrainAs(headerI) {
                width = Dimension.matchParent
                height = Dimension.fillToConstraints
                linkTo(top = headerT.bottom, bottom = oneG, topMargin = 20.dp)
            }
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.constrainAs(bodyC) {
                width = Dimension.matchParent
                height = Dimension.fillToConstraints
                linkTo(start = parent.start, end = parent.end, startMargin = 30.dp, endMargin = 30.dp)
                linkTo(top = oneG, bottom = twoG)
            }
        ) {
            Text(
                text = stringResource(id = R.string.connect_us),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = stringResource(id = R.string.connect_us_body),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        ElevatedCard(
            onClick = { Helper.connectViaTelegram(context) },
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.constrainAs(telegramB) {
                width = Dimension.matchParent
                height = Dimension.fillToConstraints
                linkTo(start = parent.start, end = parent.end, startMargin = 16.dp, endMargin = 16.dp)
                linkTo(top = twoG, bottom = threeG, topMargin = 20.dp, bottomMargin = 10.dp)
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_telegram_round),
                    contentDescription = stringResource(id = R.string.cd_via_telegram),
                    modifier = Modifier.weight(0.2F)
                )
                Text(
                    text = stringResource(id = R.string.via_telegram),
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.8F)
                )
            }
        }
        ElevatedCard(
            onClick = { Helper.connectViaEmail(context) },
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.constrainAs(mailB) {
                width = Dimension.matchParent
                height = Dimension.fillToConstraints
                linkTo(start = parent.start, end = parent.end, startMargin = 16.dp, endMargin = 16.dp)
                linkTo(top = threeG, bottom = parent.bottom, topMargin = 10.dp, bottomMargin = 20.dp)
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_mail_round),
                    contentDescription = stringResource(id = R.string.cd_via_email),
                    modifier = Modifier.weight(0.2F)
                )
                Text(
                    text = stringResource(id = R.string.via_email),
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.8F)
                )
            }
        }
    }
}