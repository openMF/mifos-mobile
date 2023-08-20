package org.mifos.mobile.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import org.mifos.mobile.R
import org.mifos.mobile.theme.AppTheme
import org.mifos.mobile.ui.activities.PrivacyPolicyActivity
import org.mifos.mobile.utils.Constants.licenseLink
import org.mifos.mobile.utils.Constants.sourceCodeLink
import org.mifos.mobile.utils.Constants.websiteLink
import java.util.*


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@Preview
fun AboutView() {
    
    val context = LocalContext.current
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val copyrightText =
        context.getString(R.string.copy_right_mifos).replace("%1\$s", currentYear.toString())


    AppTheme(isSystemInDarkTheme()) {
        Scaffold {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
                    .padding(15.dp, 56.dp, 15.dp, 15.dp)
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.mifos_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                )

                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.h4,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                Text(
                    text = stringResource(id = R.string.about_app_description),
                    style = MaterialTheme.typography.subtitle2.copy(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                FilledCard(
                    shape = RoundedCornerShape(8.dp),
                    elevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.app_version_text),
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.padding(0.dp, 0.dp, 10.dp, 0.dp)
                        )
                        Text(
                            text = "1.0",
                            style = MaterialTheme.typography.body1,
                        )
                    }
                }

                FilledCard(
                    shape = RoundedCornerShape(8.dp),
                    elevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .clickable(onClick = {
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(websiteLink)
                                )
                            )
                        })
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_website),
                            contentDescription = null,
                            modifier = Modifier.padding(0.dp, 0.dp, 10.dp, 0.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.official_website),
                            style = MaterialTheme.typography.body1,
                        )
                    }
                }

                FilledCard(
                    shape = RoundedCornerShape(8.dp),
                    elevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .clickable(onClick = {
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(licenseLink)
                                )
                            )
                        })
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_law_icon),
                            contentDescription = null,
                            modifier = Modifier.padding(0.dp, 0.dp, 10.dp, 0.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.licenses),
                            style = MaterialTheme.typography.body1,
                        )
                    }
                }

                FilledCard(
                    shape = RoundedCornerShape(8.dp),
                    elevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .clickable(onClick = {
                            context.startActivity(
                                Intent(
                                    context,
                                    PrivacyPolicyActivity::class.java
                                )
                            )
                        })
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_privacy_policy),
                            contentDescription = null,
                            modifier = Modifier.padding(0.dp, 0.dp, 10.dp, 0.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.privacy_policy),
                            style = MaterialTheme.typography.body1,
                        )
                    }
                }

                FilledCard(
                    shape = RoundedCornerShape(8.dp),
                    elevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .clickable(onClick = {
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(sourceCodeLink)
                                )
                            )
                        })
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_source_code),
                            contentDescription = null,
                            modifier = Modifier.padding(0.dp, 0.dp, 10.dp, 0.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.sources),
                            style = MaterialTheme.typography.body1,
                        )

                    }
                }

                FilledCard(
                    shape = RoundedCornerShape(8.dp),
                    elevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .clickable(onClick = {
                            context.startActivity(
                                Intent(
                                    context,
                                    OssLicensesMenuActivity::class.java
                                )
                            )
                        })
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = copyrightText,
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = stringResource(id = R.string.license_string_with_value),
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilledCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    elevation: Dp = 1.dp,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = shape,
        elevation = elevation,
        content = content
    )
}


