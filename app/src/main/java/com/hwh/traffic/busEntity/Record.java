package com.hwh.traffic.busEntity;

public class Record {
    private Integer record_id;
    private String search_route;
    private String search_end;

    public Integer getRecord_id() {
        return record_id;
    }

    public void setRecord_id(Integer record_id) {
        this.record_id = record_id;
    }

    public String getSearch_route() {
        return search_route;
    }

    public void setSearch_route(String search_route) {
        this.search_route = search_route;
    }

    public String getSearch_end() {
        return search_end;
    }

    public void setSearch_end(String search_end) {
        this.search_end = search_end;
    }

    @Override
    public String toString() {
        return "Record{" +
                "record_id=" + record_id +
                ", search_route='" + search_route + '\'' +
                ", search_end='" + search_end + '\'' +
                '}';
    }
}
