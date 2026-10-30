public class DnsResolver {

    static String getNext(String[] from, String[] to, String host) {
        if (host == null) {
            return null;
        }

        for (int i = 0; i < from.length; i++) {

            if (from[i].equals(host)) {
                return to[i];
            }
        }

        return null;
    }
    static boolean hasCycle(String[] from, String[] to, String start) {
        String slow = start;
        String fast = start;

        while (fast!=null) {
            slow = getNext(from, to, slow);

            fast = getNext(from, to, fast);
            fast = getNext(from, to, fast);

            if (fast == null) {
                return false;
            }

            if (slow.equals(fast)) {
                return true;
            }
        }
        return false;
    }

    static String findFinalHost(String[] from, String[] to, String start) {
        String current = start;

        while (getNext(from, to, current) != null) {
            current = getNext(from, to, current);
        }
        return current;
    }

    public static String resolve(String[] from, String[] to, String start){
        if (hasCycle(from, to, start)) {
            return null;
        }

        return findFinalHost(from, to, start);
    }
}