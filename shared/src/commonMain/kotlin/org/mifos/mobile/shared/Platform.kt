package org.mifos.mobile.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform