package com.github.m0nk3y2k4.thetvdb.api;

import static com.github.m0nk3y2k4.thetvdb.testutils.assertj.IntegrationTestAssertions.assertThat;

import com.github.m0nk3y2k4.thetvdb.testutils.annotation.IntegrationTestSuite;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

@IntegrationTestSuite("Languages")
class LanguagesIT {

    @Test @Order(1)
    void getAvailableLanguages(TheTVDBApi api) {
        assertThat(api::getAvailableLanguages).as("/languages").doesNotThrowAnyException();
    }

    @Test @Order(2)
    void getLanguage(TheTVDBApi api) {
        assertThat(() -> api.getLanguage(14)).as("/languages/14").doesNotThrowAnyException();
    }
}
