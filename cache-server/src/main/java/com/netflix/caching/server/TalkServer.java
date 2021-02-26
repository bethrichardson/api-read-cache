package com.netflix.caching.server;

import com.netflix.caching.common.Conversation;
import com.netflix.caching.common.Sentence;

import javax.ws.rs.GET;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/talk")
public class TalkServer implements Conversation {

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Sentence greeting() {
        return new Sentence("Hello");
    }

    @DELETE
    @Produces(MediaType.APPLICATION_XML)
    public Sentence farewell() {
        return new Sentence("Goodbye");
    }
}