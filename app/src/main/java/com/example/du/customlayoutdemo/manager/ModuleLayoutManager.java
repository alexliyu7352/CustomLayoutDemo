package com.example.du.customlayoutdemo.manager;

import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.example.du.customlayoutdemo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleLayoutManager {
    private static ModuleLayoutManager INSTANCE;
    private Map<Integer, Integer> mViewTypeAndLayoutResMap;
    private Map<String, List<String>> mModulesFirstLine;
    private String[] layoutIds = new String[] {"layout_1", "layout_2"};
    private int[] layoutResIds = new int[] {R.layout.layout_module_1, R.layout.layout_module_2};

    private ModuleLayoutManager() {
        if (mViewTypeAndLayoutResMap == null) {
            mViewTypeAndLayoutResMap = new HashMap<>();
        }
        if (mModulesFirstLine == null) {
            mModulesFirstLine = new HashMap<>();
        }
        registerAllLayoutRes();
        setModulesFirstLine();
    }

    public static ModuleLayoutManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ModuleLayoutManager();
        }
        return INSTANCE;
    }

    public int getViewType(String layoutCode) {
        int viewType;
        //自定义布局的viewType取负值
        if (layoutCode.split("_")[1].equals("custom")) {
            int customId = Integer.parseInt(layoutCode.split("_")[2]);
            viewType = 0 - customId;
        } else {
            viewType = Integer.parseInt(layoutCode.substring(layoutCode.indexOf("_") + 1));
        }
        return viewType;
    }

    public int getLayoutResByViewType(int viewType) {
        int result = layoutResIds[0];
        if (mViewTypeAndLayoutResMap != null) {
            if (mViewTypeAndLayoutResMap.containsKey(viewType) && mViewTypeAndLayoutResMap.get(viewType) != null) {
                result = mViewTypeAndLayoutResMap.get(viewType);
            }
        }
        return result;
    }

    public void setModuleFirstLine(String layoutId, List<String> firstLineCellCodes) {
        mModulesFirstLine.put(layoutId, firstLineCellCodes);
    }

    public void setModuleFirstLineByViews(String layoutId, List<View> childViews) {
        List<String> firstLineCellCodes = new ArrayList<>();
        for (View childView : childViews) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childView.getLayoutParams();
            if (layoutParams.topMargin == 0) {
                firstLineCellCodes.add((String) childView.getTag());
            }
        }
        mModulesFirstLine.put(layoutId, firstLineCellCodes);
    }

    public boolean isNeedInterceptKeyEvent(String layoutId, String cellCode) {
        if (mModulesFirstLine == null) {
            return false;
        }
        List<String> firstLineCells = mModulesFirstLine.get(layoutId);
        if (firstLineCells == null || firstLineCells.size() == 0) {
            return false;
        }
        for (String firstLineCellCode : firstLineCells) {
            if (TextUtils.equals(firstLineCellCode, cellCode)) {
                return true;
            }
        }
        return false;
    }

    private void registerAllLayoutRes() {
        for (int i = 0; i < layoutIds.length; i++) {
            int viewType = getViewType(layoutIds[i]);
            registerLayoutResByViewType(viewType, layoutResIds[i]);
        }
    }

    private void registerLayoutResByViewType(int viewType, int layoutResId) {
        mViewTypeAndLayoutResMap.put(viewType, layoutResId);
    }

    private void setModulesFirstLine() {
        List<String> firstLineCellCodes;
        for (String layoutId : layoutIds) {
            switch (layoutId) {
                case "layout_1":
                    firstLineCellCodes = new ArrayList<>();
                    firstLineCellCodes.add("cell_1_1");
                    firstLineCellCodes.add("cell_1_2");
                    mModulesFirstLine.put(layoutId, firstLineCellCodes);
                    break;
                case "layout_2":
                    firstLineCellCodes = new ArrayList<>();
                    firstLineCellCodes.add("cell_2_1");
                    firstLineCellCodes.add("cell_2_2");
                    firstLineCellCodes.add("cell_2_3");
                    firstLineCellCodes.add("cell_2_4");
                    mModulesFirstLine.put(layoutId, firstLineCellCodes);
                default:
                    break;
            }
        }
    }
}
