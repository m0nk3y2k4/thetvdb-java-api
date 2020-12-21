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

package com.github.m0nk3y2k4.thetvdb.api.enumeration;

/**
 * Represents the different funding models offered by <i>TheTVDB.com</i>.
 * <p><br>
 * Each API key has been issued based on one of these funding models and each model has it's own specification on how
 * authentication with the remote API is conducted.
 */
public enum FundingModel {
    /** Negotiated contract */
    CONTRACT,

    /** End-user subscription */
    SUBSCRIPTION
}