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

package com.github.m0nk3y2k4.thetvdb.testutils.parameterized;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.m0nk3y2k4.thetvdb.testutils.ResponseData;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.platform.commons.util.Preconditions;

/**
 * JUnit data provider for {@link ResponseData ResponseData&lt;T&gt;} test objects
 * <p><br>
 * This class is typically used in combination with the {@link ResponseDataSource} annotation
 */
public class ResponseDataProvider implements ArgumentsProvider, AnnotationConsumer<ResponseDataSource> {

    private ResponseDataSource jsonSource;

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        Collection<Field> constants = Arrays.stream(ResponseData.class.getDeclaredFields())
                .filter(field -> Modifier.isStatic(field.getModifiers()))
                .filter(field -> ResponseData.class.isAssignableFrom(field.getType()))
                .collect(Collectors.toSet());

        String[] declaredConstantNames = jsonSource.names();
        if (declaredConstantNames.length > 0) {
            Set<String> uniqueNames = Set.of(declaredConstantNames);
            Preconditions.condition(uniqueNames.size() == declaredConstantNames.length,
                    () -> "Duplicate enum constant name(s) found in " + jsonSource);

            jsonSource.mode().validate(jsonSource, constants, uniqueNames);
            constants.removeIf((constant) -> !jsonSource.mode().isSelected(constant, uniqueNames));
        }

        Collection<Arguments> arguments = new ArrayList<>();
        for (Field constant : constants) {
            arguments.add(Arguments.of(constant.get(null)));    // Use "null" for static fields
        }

        return arguments.stream();
    }

    @Override
    public void accept(ResponseDataSource source) {
        this.jsonSource = source;
    }
}
