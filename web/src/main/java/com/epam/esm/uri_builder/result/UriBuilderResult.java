package com.epam.esm.uri_builder.result;

import com.epam.esm.constants.WebLayerConstants;

public class UriBuilderResult {

    private int limit;
    private int offset;
    private String paramString;


    public UriBuilderResult() {
        this.limit = WebLayerConstants.DEFAULT_LIMIT;
        this.offset = WebLayerConstants.DEFAULT_OFFSET;
        this.paramString = "";
    }

    public UriBuilderResult(int limit, int offset, String paramString) {
        this.limit = limit;
        this.offset = offset;
        this.paramString = paramString;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getParamString() {
        return paramString;
    }

    public void setParamString(String paramString) {
        this.paramString = paramString;
    }
}
