package com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.m0nk3y2k4.thetvdb.api.model.data.User;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;

@Immutable
@Style(visibility = Style.ImplementationVisibility.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = com.github.m0nk3y2k4.thetvdb.internal.api.impl.model.data.UserDTOBuilder.class)
public abstract class UserDTO implements User {

    @Override
    public String toString() {
        return getUserName();
    }
}
