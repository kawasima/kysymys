package net.unit8.kysymys.user.application;

import lombok.Value;

import java.io.Serializable;

@Value
public class ListFollowersQuery implements Serializable {
    String userId;
    int page;
    int size;
}