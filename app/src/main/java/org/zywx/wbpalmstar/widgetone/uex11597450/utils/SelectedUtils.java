package org.zywx.wbpalmstar.widgetone.uex11597450.utils;

import java.util.ArrayList;
import java.util.List;

import static org.zywx.wbpalmstar.widgetone.uex11597450.utils.HtmlUtil.replaceSpace;

public class SelectedUtils {

    // selectedString 需要拆分的总字符串
    // selectedCount 一共会被拆分成几个选项

    public static List<String> selectedArray(String selectedString, int selectedCount) {
        selectedString = replaceSpace(selectedString);
        List<String> selectedArray = new ArrayList<String>();
        List<String> titleArray = new ArrayList<>();
        titleArray.add("A");
        titleArray.add("B");
        titleArray.add("C");
        titleArray.add("D");
        titleArray.add("E");
        titleArray.add("F");
        titleArray.add("G");
        titleArray.add("H");
        Utils.logh("========", selectedString);
        for (int index = 0; index < selectedCount; index++) {
            String selectedSingleString = "";
            String startTitle = titleArray.get(index);
            List<Integer> startIndexList = findStartIndex(selectedString, startTitle);
            int startTitleIndex = startIndexList.get(0);
            int selectedEndIndex = 0;
            int selectedStartIndex = 0;
            if (startTitleIndex != -1) {
                selectedStartIndex = startTitleIndex + startIndexList.get(1);
                if (index < selectedCount - 1) {
                    String endTitle = titleArray.get(index + 1);
                    int endTitleIndex = findStartIndex(selectedString, endTitle).get(0);
                    if (endTitleIndex != -1) {
                        selectedEndIndex = endTitleIndex;
                        selectedSingleString = selectedString.substring(selectedStartIndex, selectedEndIndex);
                    }
                } else {
                    selectedSingleString = selectedString.substring(selectedStartIndex);
                }
            } else {
                selectedEndIndex = selectedString.length();
                selectedSingleString = selectedString.substring(selectedStartIndex, selectedEndIndex);
            }
            selectedSingleString = selectedSingleString.replace("</p>", "");
            selectedSingleString = selectedSingleString.replace("<p>", "");
            selectedSingleString = selectedSingleString.replace("<br/>", "");
            selectedSingleString = selectedSingleString.replace("<br />", "");
            selectedSingleString = selectedSingleString.replace("</div>", "");
            selectedSingleString = selectedSingleString.replace("<div>", "");
            selectedSingleString = selectedSingleString.replace("<span>", "");
            selectedSingleString = selectedSingleString.replace("</span>", "");
            selectedSingleString = selectedSingleString.replace("\\r\\n", "");
            selectedSingleString = selectedSingleString.replace("\\r", "");
            if (selectedSingleString.length() > 0) {
                selectedArray.add(selectedSingleString);
            }
            Utils.logh("selected========", selectedSingleString);
        }
        return selectedArray;
    }

    private static List<Integer> findStartIndex(String selectedString, String headerString) {
        List<Integer> objectList = new ArrayList<>();
        int index = -1;
        int size = 1;
        List<String> findRegularArray = new ArrayList<>();
        findRegularArray.add(String.format(">(%s)", headerString));
        findRegularArray.add(String.format(">%s.", headerString));
        findRegularArray.add(String.format("> %s ", headerString));
        findRegularArray.add(String.format("> %s", headerString));
        findRegularArray.add(String.format(">(%s) ", headerString));
        findRegularArray.add(String.format(">(%s)。", headerString));
        findRegularArray.add(String.format(">%s", headerString));
        findRegularArray.add(String.format("(%s)", headerString));
        findRegularArray.add(String.format("%s.", headerString));
        findRegularArray.add(String.format(" %s ", headerString));
        findRegularArray.add(String.format(" %s", headerString));
        findRegularArray.add(String.format("(%s) ", headerString));
        findRegularArray.add(String.format("(%s)。", headerString));
        findRegularArray.add(String.format("%s", headerString));
        for (String findRegular : findRegularArray) {
            index = selectedString.indexOf(findRegular);
            if (index != -1) {
                size = findRegular.length();
                break;
            }
        }
        objectList.add(index);
        objectList.add(size);
        return objectList;
    }

}
