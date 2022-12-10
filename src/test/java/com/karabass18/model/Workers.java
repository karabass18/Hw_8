package com.karabass18.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Workers {
    public String title;
    public int num;
    public ArrayList<InfoWorker> info;


    private static class InfoWorker {
        public String firstname;
        public String lastname;
        public int age;
        public Children children;

        private static class Children {
            public int num;
            @JsonProperty("child-info")
            public ArrayList<ChildInfo> childinfo;

            private static class ChildInfo {
                public String name;
                public int age;
            }
        }
    }
}
