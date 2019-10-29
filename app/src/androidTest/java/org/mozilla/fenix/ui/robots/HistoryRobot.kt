/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.ui.robots

import android.net.Uri
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.mozilla.fenix.R
import org.mozilla.fenix.helpers.click

/**
 * Implementation of Robot Pattern for the history menu.
 */
class HistoryRobot {

    fun verifyHistoryMenuView() = assertHistoryMenuView()

    fun verifyEmptyHistoryView() = assertEmptyHistoryView()

    fun verifyVisitedTimeTitle() = assertVisitedTimeTitle()

    fun verifyFirstTestPageTitle(title: String) = assertTestPageTitle(title)

    fun verifyTestPageUrl(expectedUrl: Uri) = assertPageUrl(expectedUrl)

    fun verifyDeleteConfirmationMessage() = assertDeleteConfirmationMessage()

    fun openOverflowMenu() {
        overflowMenu().click()
    }

    fun clickThreeDotMenuDelete() {
        threeDotMenuDeleteButton().click()
    }

    fun clickDeleteHistoryButton() {
        deleteAllHistoryButton().click()
    }

    fun confirmDeleteAllHistory() {
        onView(withText("Delete"))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .click()
    }

    class Transition {
        fun goBack(interact: LibraryRobot.() -> Unit): LibraryRobot.Transition {
            goBackButton().click()

            LibraryRobot().interact()
            return LibraryRobot.Transition()
        }

        fun closeMenu(interact: HomeScreenRobot.() -> Unit): HomeScreenRobot.Transition {
            closeButton().click()

            HomeScreenRobot().interact()
            return HomeScreenRobot.Transition()
        }
    }
}

private fun goBackButton() = onView(withContentDescription("Navigate up"))

private fun testPageTitle() = onView(allOf(withId(R.id.title), withText("Test_Page_1")))

private fun pageUrl() = onView(withId(R.id.url))

private fun overflowMenu() = onView(withId(R.id.overflow_menu))

private fun threeDotMenuDeleteButton() = onView(withId(R.id.simple_text))

private fun deleteAllHistoryButton() = onView(withId(R.id.delete_button))

private fun assertHistoryMenuView() {
    onView(
        allOf(withText("History"), withParent(withId(R.id.navigationToolbar)))
    )
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
}

private fun assertEmptyHistoryView() =
    onView(
        allOf(
            withId(R.id.history_empty_view),
            withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
        )
    )
        .check(matches(withText("No history here")))

private fun assertVisitedTimeTitle() =
    onView(withId(R.id.header_title)).check(matches(withText("Last 24 hours")))

private fun assertTestPageTitle(title: String) = testPageTitle()
    .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    .check(matches(withText(title)))

private fun assertPageUrl(expectedUrl: Uri) = pageUrl()
    .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    .check(matches(withText(Matchers.containsString(expectedUrl.toString()))))

private fun assertDeleteConfirmationMessage() =
    onView(withText("This will delete all of your browsing data."))
        .inRoot(isDialog())
        .check(matches(isDisplayed()))

private fun closeButton() = onView(withId(R.id.libraryClose))
