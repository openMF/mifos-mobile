/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.qr

import platform.AVFoundation.AVMetadataObjectTypeAztecCode
import platform.AVFoundation.AVMetadataObjectTypeCodabarCode
import platform.AVFoundation.AVMetadataObjectTypeCode128Code
import platform.AVFoundation.AVMetadataObjectTypeCode39Code
import platform.AVFoundation.AVMetadataObjectTypeCode93Code
import platform.AVFoundation.AVMetadataObjectTypeDataMatrixCode
import platform.AVFoundation.AVMetadataObjectTypeEAN13Code
import platform.AVFoundation.AVMetadataObjectTypeEAN8Code
import platform.AVFoundation.AVMetadataObjectTypeITF14Code
import platform.AVFoundation.AVMetadataObjectTypePDF417Code
import platform.AVFoundation.AVMetadataObjectTypeQRCode
import platform.AVFoundation.AVMetadataObjectTypeUPCECode

fun List<CodeType>.toFormat(): List<platform.AVFoundation.AVMetadataObjectType> = map {
    when (it) {
        CodeType.Codabar -> AVMetadataObjectTypeCodabarCode
        CodeType.Code39 -> AVMetadataObjectTypeCode39Code
        CodeType.Code93 -> AVMetadataObjectTypeCode93Code
        CodeType.Code128 -> AVMetadataObjectTypeCode128Code
        CodeType.EAN8 -> AVMetadataObjectTypeEAN8Code
        CodeType.EAN13 -> AVMetadataObjectTypeEAN13Code
        CodeType.ITF -> AVMetadataObjectTypeITF14Code
        CodeType.UPCE -> AVMetadataObjectTypeUPCECode
        CodeType.Aztec -> AVMetadataObjectTypeAztecCode
        CodeType.DataMatrix -> AVMetadataObjectTypeDataMatrixCode
        CodeType.PDF417 -> AVMetadataObjectTypePDF417Code
        CodeType.QR -> AVMetadataObjectTypeQRCode
    }
}
