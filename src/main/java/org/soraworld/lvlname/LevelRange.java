package org.soraworld.lvlname;

import static org.soraworld.lvlname.LevelManager.LEVEL_RANGE;

public class LevelRange {

    private int min = 0, max = 0;

    public LevelRange(String range) {
        if (LEVEL_RANGE.matcher(range).matches()) {
            String[] ss = range.split("-");
            min = Integer.parseInt(ss[0]);
            max = Integer.parseInt(ss[1]);
            if (min > max) {
                int t = min;
                min = max;
                max = t;
            }
        }
    }

    public LevelRange(int min, int max) {
        if (min > max) {
            this.min = max;
            this.max = min;
        } else {
            this.min = min;
            this.max = max;
        }
    }

    public boolean match(int level) {
        return level >= min && level <= max;
    }

    public int hashCode() {
        return min + max;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof LevelRange) return min == ((LevelRange) obj).min && max == ((LevelRange) obj).max;
        return false;
    }

    public String toString() {
        return min + "-" + max;
    }
}
