package main.java.logic;

import main.java.path.MapUtils;

import java.util.concurrent.ConcurrentHashMap;

public class RefUtils {

    public static ConcurrentHashMap<Integer, Airplane> planes;
    public static int current_index_planes = 0;

    public static void init () {

        planes = new ConcurrentHashMap<>();

        planes.put(current_index_planes ++, new Airplane(MapUtils._rwy35.get(0), MapUtils._rwy35.get(0)));
//        planes.put(current_index_planes ++, new Airplane(MapUtils._rwy35.get(0), MapUtils._rwy35.get(0)));
//        planes.put(current_index_planes ++, new Airplane(MapUtils._rwy3L.get(0), MapUtils._rwy3L.get(0)));
//        planes.put(current_index_planes ++, new Airplane(MapUtils._rwy3R.get(0), MapUtils._rwy3R.get(0)));
//
//        planes.put(current_index_planes ++, new Airplane(MapUtils._taxiE.get(0), MapUtils._taxiE.get(0)));
//        planes.put(current_index_planes ++, new Airplane(MapUtils._taxiD.get(0), MapUtils._taxiD.get(0)));
//
//        planes.put(current_index_planes++, new Airplane(MapUtils._taxiA.get(0), MapUtils._taxiA.get(0)));
//        planes.put(current_index_planes++, new Airplane(MapUtils._taxiA1.get(0), MapUtils._taxiA1.get(0)));
//        planes.put(current_index_planes ++, new Airplane(MapUtils._taxiA2.get(0), MapUtils._taxiA2.get(0)));
//        planes.put(current_index_planes ++, new Airplane(MapUtils._taxiA3.get(0), MapUtils._taxiA3.get(0)));

        System.out.println(planes.size());

        planes.forEach((i,p) -> {
            p.incremental_update.start();
        });

    }

}
