package com.device42.client.parser;

import org.codehaus.jettison.json.JSONException;

public interface JsonParser<INPUT, OUTPUT> {
    OUTPUT parse(INPUT json) throws JSONException;
}
