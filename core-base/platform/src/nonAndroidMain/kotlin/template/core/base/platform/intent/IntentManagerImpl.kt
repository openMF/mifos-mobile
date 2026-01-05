/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.platform.intent

import androidx.compose.ui.graphics.ImageBitmap
import template.core.base.platform.model.MimeType

class IntentManagerImpl : IntentManager {
    override fun startActivity(intent: Any) {
        // TODO("Not yet implemented")
    }

    override fun launchUri(uri: String) {
        // TODO("Not yet implemented")
    }

    override fun shareText(text: String) {
        // TODO("Not yet implemented")
    }

    override fun shareFile(fileUri: String, mimeType: MimeType) {
        // TODO("Not yet implemented")
    }

    override fun shareFile(fileUri: String, mimeType: MimeType, extraText: String) {
        TODO("Not yet implemented")
    }

    override suspend fun shareImage(title: String, image: ImageBitmap) {
        TODO("Not yet implemented")
    }

    override fun createDocumentIntent(fileName: String): Any {
        // TODO("Not yet implemented")
        return Any()
    }

    override fun startApplicationDetailsSettingsActivity() {
        // TODO("Not yet implemented")
    }
}
