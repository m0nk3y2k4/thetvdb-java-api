/*
 * Copyright (C) 2019 - 2020 thetvdb-java-api Authors and Contributors
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

package com.github.m0nk3y2k4.thetvdb.testutils.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.m0nk3y2k4.thetvdb.testutils.junit.jupiter.IntegrationTestExtension;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Meta-annotation used to run a test with the JUnit {@link IntegrationTestExtension}
 * <p><br>
 * For test classes annotated with this meta-annotation a preconfigured API instance will be provided for the time of
 * the test execution. This instance refers to the real <i>TheTVDB.com</i> remote API and will use the authorization
 * settings provided in the <em>{@code thetvdbapi.properties}</em> file.
 * <p><br>
 * The API can be accessed via injected {@link com.github.m0nk3y2k4.thetvdb.api.TheTVDBApi} parameter. Such objects may
 * be injected into the constructor, lifecycle methods and of course into the actual tests. As the provided API uses the
 * actual <i>TheTVDB.com</i> RESTful API, a connection to the Internet is required in order to execute these kind of tests.
 * <pre>{@code
 * package com.github.m0nk3y2k4.thetvdb.foobar.it;
 *
 * import static com.github.m0nk3y2k4.thetvdb.testutils.annotation.IntegrationTestSuite;
 *
 * // Imports...
 *
 * @IntegrationTestSuite
 * class SomeAPITestClass {
 *
 *     @Test
 *     void testSomething(TheTVDBApi api) {
 *         assertThat(api.getLanguage(14).getName()).isEqualTo("Deutsch");
 *     }
 * }
 * }</pre>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(IntegrationTestExtension.class)
@Target({ElementType.TYPE})
public @interface IntegrationTestSuite {

    /** Descriptive name of the test suite. Will be printed to console-out when the suite is executed (before the first tests) */
    String value();
}
