package com.fullLearn.beans;

import lombok.Data;

import java.util.Map;

@Data
public class ContactJson {
    boolean ok;
    Map<String,Object> data;
}
