package cs211.project.utils;

public class ReplaceComma {
    public String replace(String string) {
        return string.replaceAll(",", "_+_");
    }
    public String replaceBack(String string) {
        return string.replaceAll("_\\+_", ",");
    }
}
