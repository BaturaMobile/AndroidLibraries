package com.baturamobile.utils.list;

import java.util.List;

/**
 * Created by vssnake on 05/06/2017.
 */

public interface BaseRecycleView<M> {

    void showCards(List<M> receiptModelList);

    void showEmpty();
}
