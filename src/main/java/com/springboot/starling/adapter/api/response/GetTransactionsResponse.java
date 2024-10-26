package com.springboot.starling.adapter.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.starling.core.domain.model.Transaction;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class GetTransactionsResponse {

    @JsonProperty("feedItems")
    private List<Transaction> feedItems;

    public List<Transaction> getFeedItems() {
        // Initialize feedItems to an empty list if it is null
        if (feedItems == null) {
            feedItems = Collections.emptyList();
        }
        return feedItems;
    }

    public void setFeedItems(List<Transaction> feedItems) {
        this.feedItems = feedItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetTransactionsResponse that = (GetTransactionsResponse) o;
        return Objects.equals(feedItems, that.feedItems); // value comparison of feedItems rather than reference
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedItems);
    }
}
