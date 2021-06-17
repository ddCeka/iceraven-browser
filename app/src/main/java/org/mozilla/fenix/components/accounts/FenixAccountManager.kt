/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.components.accounts

import android.content.Context
import mozilla.components.service.fxa.manager.FxaAccountManager
import org.mozilla.fenix.ext.components

/**
 * Component which holds a reference to [FxaAccountManager]. Manages account authentication,
 * profiles, and profile state observers.
 */
open class FenixAccountManager(context: Context) {
    val accountManager = context.components.backgroundServices.accountManager

    val authenticatedAccount
        get() = accountManager.authenticatedAccount() != null

    /**
     * Returns the Firefox Account email if authenticated in the app, `null` otherwise.
     */
    val accountProfileEmail: String?
        get() = if (accountState == AccountState.AUTHENTICATED) {
            accountManager.accountProfile()?.email
        } else {
            null
        }

    /**
     * The current state of the Firefox Account. See [AccountState].
     */
    val accountState: AccountState
        get() = if (accountManager.authenticatedAccount() == null) {
            AccountState.NO_ACCOUNT
        } else {
            if (accountManager.accountNeedsReauth()) {
                AccountState.NEEDS_REAUTHENTICATION
            } else {
                AccountState.AUTHENTICATED
            }
        }
}

/**
 * General states as an overview of the current Firefox Account.
 */
enum class AccountState {
    /**
     * There is no known Firefox Account.
     */
    NO_ACCOUNT,

    /**
     * A Firefox Account exists but needs to be re-authenticated.
     */
    NEEDS_REAUTHENTICATION,

    /**
     * A Firefox Account exists and the user is currently signed into it.
     */
    AUTHENTICATED,
}
