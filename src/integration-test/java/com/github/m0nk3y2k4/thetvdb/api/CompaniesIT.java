/*
 * Copyright (C) 2019 - 2022 thetvdb-java-api Authors and Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.m0nk3y2k4.thetvdb.api;

import static com.github.m0nk3y2k4.thetvdb.TheTVDBApiFactory.createQueryParameters;
import static com.github.m0nk3y2k4.thetvdb.testutils.assertj.IntegrationTestAssertions.assertThat;

import com.github.m0nk3y2k4.thetvdb.testutils.annotation.IntegrationTestSuite;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

@IntegrationTestSuite("Companies")
class CompaniesIT {

    @Test
    @Order(1)
    void getAllCompanies(TheTVDBApi api) {
        assertThat(() -> api.getAllCompanies(createQueryParameters())).as("/companies").doesNotThrowAnyException();
    }

    @Test
    @Order(2)
    void getCompanyTypes(TheTVDBApi api) {
        assertThat(api::getCompanyTypes).as("/companies/types").doesNotThrowAnyException();
    }

    @Test
    @Order(3)
    void getCompany(TheTVDBApi api) {
        assertThat(() -> api.getCompany(1085)).as("/companies/1085").doesNotThrowAnyException();
    }
}
