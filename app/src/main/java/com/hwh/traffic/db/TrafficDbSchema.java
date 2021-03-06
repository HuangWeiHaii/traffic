package com.hwh.traffic.db;

public class TrafficDbSchema {


    public static final class RouteTable{
        public static final String NAME = "route";

        public static final class cols{
            public static final String ROUTE_ID = "route_id";
            public static final String ROUTE_NAME = "route_name";
            public static final String OPPOSITE_ID = "opposite_id";
        }
    }
    public static final class StopTable{
        public static final String NAME = "stop";

        public static final class cols{
            public static final String SID = "sid";
            public static final String STOP_ID = "stop_id";
            public static final String STOP_NAME = "stop_name";
        }
    }

    public static final class SearchTable{
        public static final String NAME = "search";

        public static final class cols{
            public static final String SEARCH_ID = "search_id";
            public static final String SEARCH_ROUTE_NAME = "search_route";
            public static final String SEARCH_END_NAME = "search_end";
        }
    }


}
