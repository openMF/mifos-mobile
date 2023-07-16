package org.mifos.mobile.repositories

interface ClientRepository {

    fun updateAuthenticationToken(password: String)
}