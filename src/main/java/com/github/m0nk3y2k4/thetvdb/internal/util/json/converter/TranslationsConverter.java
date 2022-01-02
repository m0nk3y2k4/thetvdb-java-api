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

package com.github.m0nk3y2k4.thetvdb.internal.util.json.converter;

import java.util.List;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Translated;
import com.github.m0nk3y2k4.thetvdb.api.model.data.Translations;
import com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.TranslationsDTO;

/**
 * Simple JSON converter used in conjunction with deserialization of translation information.
 * <p><br>
 * May be used to convert data from Jackson-bound intermediate types into actual property types. This is basically a
 * two-step deserialization; Jackson binds data into a suitable intermediate type like a list for example and the
 * converter then builds actual property type.
 */
public class TranslationsConverter<T extends Translated> extends StdConverter<List<T>, Translations<T>> {

    @Override
    public Translations<T> convert(List<T> translations) {
        return new TranslationsDTO.Builder<T>().allTranslations(translations).build();
    }
}
