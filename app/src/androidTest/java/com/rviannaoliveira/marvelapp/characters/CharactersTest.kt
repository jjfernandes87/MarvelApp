package com.rviannaoliveira.marvelapp.characters

import android.content.Intent
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.rviannaoliveira.marvelapp.initMockServer
import com.rviannaoliveira.marvelapp.main.MainActivity
import com.rviannaoliveira.marvelapp.readFileFromAssets
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.net.HttpURLConnection


/**
 * Criado por rodrigo on 01/07/17.
 */
@RunWith(AndroidJUnit4::class)
class CharactersTest {
    @Rule @JvmField
    val activityRule = ActivityTestRule(MainActivity::class.java, true, false)
    private lateinit var robo: RoboCharacters
    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        robo = RoboCharacters()
        server.initMockServer()
        activityRule.launchActivity(Intent())
        server.enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(server.readFileFromAssets(activityRule.activity, "characters.json")))
    }

    @Test
    @Throws(Exception::class)
    fun search_for_abomination_character() {
        robo.clickSearchButton()
                .findCharacters("Abom")
                .checkCharacterScreen("Abomination (Emil")
                .closeSearchButton()
                .checkCharacterScreenDoesntExist("Abomination (Emil")
    }

    @Test
    @Throws(Exception::class)
    fun filter_for_letter_d() {
        server.enqueue(MockResponse().setBody(server.readFileFromAssets(activityRule.activity, "d_characters.json")))

        robo.clickFilterButton()
                .chooseLetterFilter("D")
                .clickButtonOkDialogFilter()
                .checkCharacterScreen("Dagger")
    }

    @Test
    @Throws(Exception::class)
    fun filter_for_all() {
        robo.clickFilterButton()
                .chooseLetterFilter("ALL")
                .clickButtonOkDialogFilter()
                .checkCharacterScreen("3-D Man")
    }

    @Test
    @Throws(Exception::class)
    fun click_item_list_and_back() {
        server.enqueue(MockResponse().setBody(server.readFileFromAssets(activityRule.activity, "aim.json")))

        robo.clickItem(2)
                .backToList()
    }

    @Test
    @Throws(Exception::class)
    fun click_favorite() {
        robo.clickOnFavoriteItem(2)
                .clickOffFavoriteItem(2)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        server.shutdown()
    }

}