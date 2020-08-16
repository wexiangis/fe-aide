package fans.develop.fe;

/*
    /assets/event 文件夹资源管理器
 */
public class FeAssetsEvent {

    //----- file -----

    public Event event = new Event("/event/", "event.txt", ";");
    public Target target = new Target("/event/", "target.txt", ";");
    public Trend trend = new Trend("/event/", "trend.txt", ";");

    //----- api -----

    //----- class -----

    public class Event extends FeReaderFile {

        public int getOrder() {
            return getInt(0, 0);
        }

        public int getSummary() {
            return getInt(0, 1);
        }

        public int total() {
            return line();
        }

        public Event(String folder, String name, String split) {
            super(folder, name, split);
        }
    }

    public class Target extends FeReaderFile {

        public int getOrder() {
            return getInt(0, 0);
        }

        public int getSummary() {
            return getInt(0, 1);
        }

        public int total() {
            return line();
        }

        public Target(String folder, String name, String split) {
            super(folder, name, split);
        }
    }

    public class Trend extends FeReaderFile {

        public int getOrder() {
            return getInt(0, 0);
        }

        public int getSummary() {
            return getInt(0, 1);
        }

        public int total() {
            return line();
        }

        public Trend(String folder, String name, String split) {
            super(folder, name, split);
        }
    }
}