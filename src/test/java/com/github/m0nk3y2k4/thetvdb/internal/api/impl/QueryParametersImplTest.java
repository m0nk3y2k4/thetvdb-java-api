/*
 * Copyright (C) 2019 - 2021 thetvdb-java-api Authors and Contributors
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

package com.github.m0nk3y2k4.thetvdb.internal.api.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.github.m0nk3y2k4.thetvdb.api.QueryParameters;
import org.junit.jupiter.api.Test;

@SuppressWarnings("ConstantConditions")
class QueryParametersImplTest {

    @Test
    void createQueryParameters_fromMapWithInvalidValues_verifyParameterValidation() {
        Map<String, String> withNullKey = new HashMap<>();
        withNullKey.put(null, "Value");
        Map<String, String> withNullValue = new HashMap<>();
        withNullValue.put("Key", null);
        assertThatIllegalArgumentException().isThrownBy(() -> new QueryParametersImpl(null));
        assertThatIllegalArgumentException().isThrownBy(() -> new QueryParametersImpl(withNullKey));
        assertThatIllegalArgumentException().isThrownBy(() -> new QueryParametersImpl(withNullValue));
    }

    @Test
    void createQueryParameters_fromMapWithValidValues_verifyKeyAndValue() {
        QueryParameters params = new QueryParametersImpl(Map.of("name", "Elwood ", "age", "41"));
        assertThat(params.getParameterValue("name")).contains("Elwood ");
        assertThat(params.getParameterValue("age")).contains("41");
    }

    @Test
    void addParameter_withInvalidKeyAndValue_verifyParameterValidation() {
        assertThatIllegalArgumentException().isThrownBy(() -> new QueryParametersImpl().addParameter(null, "Value"));
        assertThatIllegalArgumentException().isThrownBy(() -> new QueryParametersImpl().addParameter("Key", null));
    }

    @Test
    void addParameter_withValidKeyAndValue_verifyKeyAndValue() {
        QueryParameters params = new QueryParametersImpl().addParameter("name", "Jake").addParameter("age", "40");
        assertThat(params.getParameterValue("age")).contains("40");
        assertThat(params.getParameterValue("name")).contains("Jake");
    }

    @Test
    void addParameter_withOverwrittenKey_verifyKeyAndValue() {
        QueryParameters params = new QueryParametersImpl().addParameter("name", "Marty").addParameter("name", "Emmett")
                .addParameter("weight", "91");
        assertThat(params.getParameterValue("weight")).contains("91");
        assertThat(params.getParameterValue("name")).contains("Emmett");
    }

    @Test
    void containsParameter_withVariousParameters_verifyOutput() {
        assertThat(new QueryParametersImpl().addParameter("name", "Bill").containsParameter("name")).isTrue();
        assertThat(new QueryParametersImpl().addParameter("name", "Ted").containsParameter("age")).isFalse();
    }

    @Test
    void size_withVariousParameters_verifySize() {
        assertThat(new QueryParametersImpl(Map.of("color", "red")).size()).isEqualTo(1);
        assertThat(new QueryParametersImpl().addParameter("color", "blue").addParameter("color", "green")
                .size()).isEqualTo(1);
        assertThat(new QueryParametersImpl().addParameter("width", "14").addParameter("height", "7")
                .size()).isEqualTo(2);
    }

    @Test
    void parameters_withSameKeyAndValue_shouldBeEqualAndHaveTheSameHashCode() {
        QueryParameters.Parameter bruceBanner = new QueryParametersImpl().addParameter("Bruce", "Banner")
                .iterator().next();
        QueryParameters.Parameter theHulk = new QueryParametersImpl().addParameter("Bruce", "Banner").iterator().next();
        assertThat(bruceBanner).isEqualTo(theHulk).hasSameHashCodeAs(theHulk);
    }

    @SuppressWarnings("java:S5838")
    @Test
    void parameters_withDifferentKeyAndValue_shouldNotBeEqualAndHaveDifferentHashCode() {
        QueryParameters.Parameter captainAmerica = new QueryParametersImpl().addParameter("Steve", "Rogers")
                .iterator().next();
        QueryParameters.Parameter captainCool = new QueryParametersImpl().addParameter("Steve", "McQueen")
                .iterator().next();
        QueryParameters.Parameter captainMarvel = new QueryParametersImpl().addParameter("Carol", "Danvers")
                .iterator().next();
        assertThat(captainAmerica).isNotEqualTo(captainCool).isNotEqualTo(captainMarvel).isNotEqualTo("Captain Marvel");
        assertThat(captainAmerica.equals(null)).isFalse();
        assertThat(captainAmerica.hashCode()).isNotEqualTo(captainMarvel.hashCode());
    }

    @Test
    void stream_withTwoParameters_verifyStreamContainsAllParameters() {
        Predicate<QueryParameters.Parameter> isIronMan = p -> p.getKey().equals("Tony") && p.getValue().equals("Stark");
        Predicate<QueryParameters.Parameter> isIronPatriot = p -> p.getKey().equals("James")
                && p.getValue().equals("Rhodes");
        Stream<QueryParameters.Parameter> parameterStream = new QueryParametersImpl(Map
                .of("Tony", "Stark", "James", "Rhodes")).stream();
        assertThat(parameterStream).isNotEmpty().allMatch(isIronMan.or(isIronPatriot));
    }

    @Test
    void iterator_withTwoParameters_verifyIteratorContainsAllParameters() {
        Predicate<QueryParameters.Parameter> isBatman = p -> p.getKey().equals("Bruce") && p.getValue().equals("Wayne");
        Predicate<QueryParameters.Parameter> isTwoFace = p -> p.getKey().equals("Harvey")
                && p.getValue().equals("Dent");
        Iterator<QueryParameters.Parameter> iterator = new QueryParametersImpl(Map
                .of("Bruce", "Wayne", "Harvey", "Dent")).iterator();
        assertThat(iterator).toIterable().isNotEmpty().allMatch(isBatman.or(isTwoFace));

    }

    @Test
    void toString_withMultipleParameters_verifyStringRepresentation() {
        QueryParameters params = new QueryParametersImpl().addParameter("year", "1955");
        // Flaky with more than one parameter as toString uses entrySet without guaranteed order of elements
        assertThat(params).asString().isEqualTo("[year=1955]");
    }
}
