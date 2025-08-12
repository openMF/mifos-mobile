/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.application.uploadDocs.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.niyajali.compose.sign.ComposeSign
import com.niyajali.compose.sign.exportSignature
import com.niyajali.compose.sign.rememberSignatureState
import io.github.vinceglb.filekit.dialogs.compose.util.encodeToByteArray
import kotlinx.coroutines.launch
import mifos_mobile.feature.loan_application.generated.resources.Res
import mifos_mobile.feature.loan_application.generated.resources.attach
import mifos_mobile.feature.loan_application.generated.resources.capture
import mifos_mobile.feature.loan_application.generated.resources.reset
import mifos_mobile.feature.loan_application.generated.resources.save_and_submit
import mifos_mobile.feature.loan_application.generated.resources.sign
import mifos_mobile.feature.loan_application.generated.resources.sign_here
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.common.toBase64DataUri
import org.mifos.mobile.core.designsystem.component.CardVariant
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosCustomCard
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.feature.loan.application.component.SignatureUploadType
import org.mifos.mobile.feature.loan.application.uploadDocs.UploadDocsAction

@Composable
internal fun BottomSheetContent(
    onAction: (UploadDocsAction) -> Unit,
    modifier: Modifier = Modifier,
    isSignatureMode: Boolean = false,
) {
    AnimatedContent(
        targetState = isSignatureMode,
        transitionSpec = {
            slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
            ) + fadeIn(
                animationSpec = tween(durationMillis = 300, delayMillis = 100),
            ) togetherWith slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
            ) + fadeOut(
                animationSpec = tween(durationMillis = 200),
            )
        },
        modifier = modifier,
    ) { isSignature ->
        if (isSignature) {
            SignatureContent(
                onAction = onAction,
            )
        } else {
            Column(
                modifier = Modifier.padding(horizontal = DesignToken.padding.large),
            ) {
                Row {
                    BottomSheetIconContainer(
                        text = Res.string.sign,
                        icon = MifosIcons.Signature,
                        onClick = {
                            onAction(UploadDocsAction.UploadSignature(SignatureUploadType.SIGN))
                        },
                    )

                    BottomSheetIconContainer(
                        text = Res.string.capture,
                        icon = MifosIcons.Camera,
                        onClick = {
                            onAction(UploadDocsAction.UploadSignature(SignatureUploadType.CAPTURE))
                        },
                    )

                    BottomSheetIconContainer(
                        text = Res.string.attach,
                        icon = MifosIcons.Attach,
                        onClick = {
                            onAction(UploadDocsAction.UploadSignature(SignatureUploadType.GALLERY))
                        },
                    )
                }
            }
        }
    }
}

@Composable
internal fun BottomSheetIconContainer(
    text: StringResource,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = DesignToken.padding.large),
    ) {
        Box(
            modifier = Modifier.size(DesignToken.sizes.inputHeight)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.secondaryContainer,
                    DesignToken.shapes.medium,
                )
                .clickable { onClick() },
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.size(DesignToken.sizes.iconMedium),
                imageVector = icon,
                contentDescription = null,
            )
        }

        Spacer(modifier = Modifier.height(DesignToken.spacing.small))

        Text(
            text = stringResource(text),
            style = MifosTypography.bodySmallEmphasized,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
private fun SignatureContent(
    modifier: Modifier = Modifier,
    onAction: (UploadDocsAction) -> Unit,
) {
    MifosElevatedScaffold(
        topBarTitle = stringResource(Res.string.sign_here),
        onNavigateBack = {
            onAction(UploadDocsAction.DismissDialog)
        },
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                )
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(DesignToken.padding.large),
            verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.large),
        ) {
            val signatureState = rememberSignatureState()
            var size = remember { Size.Zero }

            AnimatedVisibility(
                visible = true,
                enter = scaleIn(
                    animationSpec = tween(durationMillis = 500, delayMillis = 200),
                    initialScale = 0.8f,
                ) + fadeIn(
                    animationSpec = tween(durationMillis = 400, delayMillis = 200),
                ),
                modifier = Modifier.weight(1f),
            ) {
                MifosCustomCard(
                    variant = CardVariant.OUTLINED,
                    modifier = modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.secondaryContainer,
                            DesignToken.shapes.medium,
                        ),
                    shape = DesignToken.shapes.medium,
                ) {
                    ComposeSign(
                        modifier = Modifier
                            .fillMaxSize()
                            .onGloballyPositioned {
                                size = it.size.toSize()
                            },
                        state = signatureState,
                        backgroundColor = Color.Transparent,
                        showGrid = true,
                        onSignatureUpdate = {},
                    )
                }
            }

            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 400, delayMillis = 300),
                ) + fadeIn(
                    animationSpec = tween(durationMillis = 300, delayMillis = 300),
                ),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
                ) {
                    MifosButton(
                        modifier = Modifier.weight(0.4f),
                        shape = DesignToken.shapes.medium,
                        onClick = signatureState::clear,
                    ) {
                        Text(
                            text = stringResource(Res.string.reset),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }

                    val scope = rememberCoroutineScope()
                    MifosButton(
                        modifier = Modifier.weight(0.4f),
                        shape = DesignToken.shapes.medium,
                        onClick = {
                            scope.launch {
                                val data = signatureState.exportSignature(
                                    width = size.width.toInt(),
                                    height = size.height.toInt(),
                                    backgroundColor = Color.White,
                                )
                                if (data != null) {
                                    val data = data.encodeToByteArray().toBase64DataUri()
                                    onAction(UploadDocsAction.UploadSign(data))
                                }
                            }
                        },
                    ) {
                        Text(
                            text = stringResource(Res.string.save_and_submit),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }
            }
        }
    }
}
