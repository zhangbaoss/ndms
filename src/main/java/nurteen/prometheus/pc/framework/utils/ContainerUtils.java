package nurteen.prometheus.pc.framework.utils;

import java.util.*;

public class ContainerUtils {
    public static <Key, Value> Map<Key, Value> from(Key key, Value value) {
        return make(key, value).get();
    }

    public static <Key, Value> MapMaker<Key, Value> make(Key key, Value value) {
        return new MapMaker<>(key, value);
    }

    public static <Value> ArrayListMaker<Value> makeArrayList(Value value) {
        return new ArrayListMaker<>(value);
    }

    public static <Value> LinkedListMaker<Value> makeLinkedList(Value value) {
        return new LinkedListMaker<>(value);
    }

    public static <Value> ArrayListMaker<Value> makeArrayList(Collection<? extends Value> values) {
        return new ArrayListMaker<>(values);
    }

    public static <Value> LinkedListMaker<Value> makeLinkedList(Collection<? extends Value> values) {
        return new LinkedListMaker<>(values);
    }

    public static <Key1, Value> Value get(Map<Key1, Value> map, Key1 key1) {
        return map.get(key1);
    }

    public static <Key1, Key2, Value> Value get(Map<Key1, Map<Key2, Value>> map, Key1 key1, Key2 key2) {
        Map<Key2, Value> map2 = map.get(key1);
        if (map2 != null) {
            return get(map2, key2);
        }
        return null;
    }

    public static <Key1, Key2, Key3, Value> Value get(Map<Key1, Map<Key2, Map<Key3, Value>>> map, Key1 key1, Key2 key2, Key3 key3) {
        Map<Key2, Map<Key3, Value>> map2 = map.get(key1);
        if (map2 != null) {
            return get(map2, key2, key3);
        }
        return null;
    }

    public static <Key1, Key2, Key3, Key4, Value> Value get(Map<Key1, Map<Key2, Map<Key3, Map<Key4, Value>>>> map, Key1 key1, Key2 key2, Key3 key3, Key4 key4) {
        Map<Key2, Map<Key3, Map<Key4, Value>>> map2 = map.get(key1);
        if (map2 != null) {
            return get(map2, key2, key3, key4);
        }
        return null;
    }

    public static <Key1, Value> void put(Map<Key1, Value> map, Key1 key1, Value value) {
        map.put(key1, value);
    }

    public static <Key1, Key2, Value> void put(Map<Key1, Map<Key2, Value>> map, Key1 key1, Key2 key2, Value value) {
        Map<Key2, Value> map2 = map.get(key1);
        if (map2 != null) {
            put(map2, key2, value);
        } else {
            map.put(key1, from(key2, value));
        }
    }

    public static <Key1, Key2, Key3, Value> void put(Map<Key1, Map<Key2, Map<Key3, Value>>> map, Key1 key1, Key2 key2, Key3 key3, Value value) {
        Map<Key2, Map<Key3, Value>> map2 = map.get(key1);
        if (map2 != null) {
            put(map2, key2, key3, value);
        } else {
            map.put(key1, from(key2, from(key3, value)));
        }
    }

    public static <Key1, Key2, Key3, Key4, Value> void put(Map<Key1, Map<Key2, Map<Key3, Map<Key4, Value>>>> map, Key1 key1, Key2 key2, Key3 key3, Key4 key4, Value value) {
        Map<Key2, Map<Key3, Map<Key4, Value>>> map2 = map.get(key1);
        if (map2 != null) {
            put(map2, key2, key3, key4, value);
        } else {
            map.put(key1, from(key2, from(key3, from(key4, value))));
        }
    }

    public static <Key1, Value> void remove(Map<Key1, Value> map, Key1 key1) {
        map.remove(key1);
    }

    public static <Key1, Key2, Value> void remove(Map<Key1, Map<Key2, Value>> map, Key1 key1, Key2 key2) {
        Map<Key2, Value> map2 = map.get(key1);
        if (map2 != null) {
            remove(map2, key2);
            if (map2.isEmpty()) {
                remove(map, key1);
            }
        }
    }

    public static <Key1, Key2, Key3, Value> void remove(Map<Key1, Map<Key2, Map<Key3, Value>>> map, Key1 key1, Key2 key2, Key3 key3) {
        Map<Key2, Map<Key3, Value>> map2 = map.get(key1);
        if (map2 != null) {
            remove(map2, key2, key3);
            if (map2.isEmpty()) {
                remove(map, key1);
            }
        }
    }

    public static <Key1, Key2, Key3, Key4, Value> void remove(Map<Key1, Map<Key2, Map<Key3, Map<Key4, Value>>>> map, Key1 key1, Key2 key2, Key3 key3, Key4 key4) {
        Map<Key2, Map<Key3, Map<Key4, Value>>> map2 = map.get(key1);
        if (map2 != null) {
            remove(map2, key2, key3, key4);
            if (map2.isEmpty()) {
                remove(map, key1);
            }
        }
    }


    public static <Value> List<Value> add(List<Value> list, Value value) {
        list.add(value);
        return list;
    }

    public static <Key1, Value> void addArrayList(Map<Key1, List<Value>> map, Key1 key1, Value value) {
        List<Value> list = map.get(key1);
        if (list != null) {
            list.add(value);
        } else {
            put(map, key1, makeArrayList(value).get());
        }
    }
    public static <Key1, Value> void deleteArrayList(Map<Key1, List<Value>> map, Key1 key1, Value value) {
        List<Value> list = map.get(key1);
        if (list != null) {
            list.remove(value);
            if (list.isEmpty()) {
                remove(map, key1);
            }
        }
    }
    public static <Key1, Value> void addLinkedList(Map<Key1, List<Value>> map, Key1 key1, Value value) {
        List<Value> list = map.get(key1);
        if (list != null) {
            list.add(value);
        } else {
            put(map, key1, makeLinkedList(value).get());
        }
    }
    public static <Key1, Value> void deleteLinkedList(Map<Key1, List<Value>> map, Key1 key1, Value value) {
        List<Value> list = map.get(key1);
        if (list != null) {
            list.remove(value);
            if (list.isEmpty()) {
                remove(map, key1);
            }
        }
    }

    public static <Value> boolean notNull(List<Value> list, int size) {
        if ((list != null) && (list.size() == size)) {
            for (Value value : list) {
                if (value == null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static class MapMaker<Key, Value> {
        Map<Key, Value> map = new HashMap<>();

        MapMaker(Key key, Value value) {
            this.map.put(key, value);
        }

        public Map<Key, Value> get() {
            return this.map;
        }

        public MapMaker<Key, Value> put(Key key, Value value) {
            this.map.put(key, value);
            return this;
        }
    }

    public static class ArrayListMaker<Value> {
        ArrayList<Value> list = new ArrayList<>();

        ArrayListMaker(Value value) {
            this.list.add(value);
        }

        ArrayListMaker(Collection<? extends Value> values) {
            this.list.addAll(values);
        }

        public ArrayList<Value> get() {
            return this.list;
        }

        public ArrayListMaker<Value> add(Value value) {
            this.list.add(value);
            return this;
        }

        public ArrayListMaker<Value> add(Collection<? extends Value> values) {
            this.list.addAll(values);
            return this;
        }
    }

    public static class LinkedListMaker<Value> {
        LinkedList<Value> list = new LinkedList<>();

        LinkedListMaker(Value value) {
            this.list.add(value);
        }
        LinkedListMaker(Collection<? extends Value> values) {
            this.list.addAll(values);
        }

        public LinkedList<Value> get() {
            return this.list;
        }

        public LinkedListMaker<Value> add(Value value) {
            this.list.add(value);
            return this;
        }

        public LinkedListMaker<Value> add(Collection<? extends Value> values) {
            this.list.addAll(values);
            return this;
        }
    }
}
