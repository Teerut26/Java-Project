package cs211.project.utils;

public class ReplaceComma {
    private NewLineClear newLineClear;

    public ReplaceComma() {
        newLineClear = new NewLineClear();
    }

    public String replace(String string) {
        return newLineClear.clean(string.replaceAll(",", "_+_"));
    }

    public String replaceBack(String string) {
        return string.replaceAll("_\\+_", ",");
    }
}
