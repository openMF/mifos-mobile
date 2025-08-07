//package org.mifos.mobile.feature.settings.about
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.aspectRatio
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.navigationBarsPadding
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.mexico.financiera.bienestar.core.designsystem.component.MbsCard
//import com.mexico.financiera.bienestar.core.designsystem.component.MbsScaffold
//import com.mexico.financiera.bienestar.core.designsystem.theme.AppColors
//import com.mexico.financiera.bienestar.core.designsystem.theme.AppTypography
//import com.mexico.financiera.bienestar.core.designsystem.theme.DesignToken
//import com.mexico.financiera.bienestar.core.designsystem.theme.MbsTheme
//import core.ui.generated.resources.ic_icon_logo_name_2
//import core.ui.generated.resources.ic_icon_money_transfer
//import feature.settings.generated.resources.Res
//import feature.settings.generated.resources.feature_settings_about_logo_content_description
//import feature.settings.generated.resources.feature_settings_about_money_transfer_content_description
//import feature.settings.generated.resources.feature_settings_about_point_1
//import feature.settings.generated.resources.feature_settings_about_point_2
//import feature.settings.generated.resources.feature_settings_about_point_3
//import feature.settings.generated.resources.feature_settings_about_topbar_title
//import feature.settings.generated.resources.feature_settings_about_what_is_financiera_bienestar
//import feature.settings.generated.resources.feature_settings_about_who_are_we
//import feature.settings.generated.resources.feature_settings_about_who_are_we_desc
//import mifos_mobile.core.ui.generated.resources.Res
//import mifos_mobile.feature.settings.generated.resources.feature_settings_about_logo_content_description
//import mifos_mobile.feature.settings.generated.resources.feature_settings_about_topbar_title
//import mifos_mobile.feature.settings.generated.resources.feature_settings_about_what_does_mifos_do
//import mifos_mobile.feature.settings.generated.resources.feature_settings_about_who_are_we
//import mifos_mobile.feature.settings.generated.resources.feature_settings_about_who_are_we_desc
//import org.jetbrains.compose.resources.painterResource
//import org.jetbrains.compose.resources.stringResource
//import org.jetbrains.compose.ui.tooling.preview.Preview
//import org.mifos.mobile.core.designsystem.component.MifosCard
//import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
//import org.mifos.mobile.core.designsystem.theme.AppColors
//import org.mifos.mobile.core.designsystem.theme.DesignToken
//import org.mifos.mobile.core.designsystem.theme.MifosTypography
//import core.ui.generated.resources.Res as UIRes
//
//@Composable
//fun AboutScreen(
//    modifier: Modifier = Modifier,
//    onBackClick: () -> Unit,
//) {
//    AboutScreenContent(
//        modifier = modifier,
//        onBackClick = onBackClick,
//    )
//}
//
//@Composable
//internal fun AboutScreenContent(
//    modifier: Modifier = Modifier,
//    onBackClick: () -> Unit,
//) {
//    MifosElevatedScaffold(
//        onNavigateBack = onBackClick,
//        topBarTitle = stringResource(Res.string.feature_settings_about_topbar_title),
//        modifier = modifier,
//    ) {
//        Box(
//            Modifier
//                .fillMaxSize()
//                .navigationBarsPadding()
//                .padding(
//                    horizontal = DesignToken.padding.large,
//                    vertical = DesignToken.padding.small,
//                ),
//        ) {
//            MifosCard(
//                Modifier
//                    .fillMaxSize().align(Alignment.Center),
//                colors = CardDefaults.cardColors(
//                    containerColor = AppColors.lightGreen,
//                ),
//            ) {
//                Box(
//                    Modifier.fillMaxSize(),
//                ) {
//                    Column(
//                        Modifier
//                            .fillMaxWidth()
//                            .padding(DesignToken.padding.large),
//                        verticalArrangement = Arrangement.spacedBy(DesignToken.padding.medium),
//                    ) {
//                        Image(
//                            painter = painterResource(UIRes.drawable.ic_icon_logo_name_2),
//                            contentDescription =
//                                stringResource(Res.string.feature_settings_about_logo_content_description),
//                            modifier = Modifier
//                                .padding(vertical = DesignToken.padding.medium)
//                                .fillMaxWidth(0.7f)
//                                .aspectRatio(5.59f),
//                        )
//
//                        Text(
//                            text = stringResource(Res.string.feature_settings_about_what_does_mifos_do),
//                            style = MifosTypography.headingThreeSemiBold,
//                        )
//                        Text(
//                            text = stringResource(Res.string.feature_settings_about_who_are_we_desc),
//                            style = AppTypography.subtitleMedium,
//                        )
//
//                        Text(
//                            text = stringResource(Res.string.feature_settings_about_what_is_financiera_bienestar),
//                            style = AppTypography.headingThreeSemiBold,
//                            modifier = Modifier.padding(top = DesignToken.padding.small),
//                        )
//
//                        Column {
//                            listOf(
//                                stringResource(Res.string.feature_settings_about_point_1),
//                                stringResource(Res.string.feature_settings_about_point_2),
//                                stringResource(Res.string.feature_settings_about_point_3),
//                            ).forEach { point ->
//                                Text(
//                                    text = "• $point",
//                                    style = AppTypography.subtitleMedium,
//                                    modifier = Modifier.padding(vertical = 2.dp),
//                                )
//                            }
//                        }
//                    }
//                    Image(
//                        painter =
//                            painterResource(UIRes.drawable.ic_icon_money_transfer),
//                        contentDescription =
//                            stringResource(Res.string.feature_settings_about_money_transfer_content_description),
//                        Modifier.fillMaxWidth().align(Alignment.BottomCenter),
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Preview
//@Composable
//internal fun AboutScreenContentPreview() {
//    MbsTheme {
//        AboutScreenContent(onBackClick = {})
//    }
//}