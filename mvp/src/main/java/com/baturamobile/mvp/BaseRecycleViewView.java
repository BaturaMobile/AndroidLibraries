package com.baturamobile.mvp;

import java.util.List;

/**
 * Created by vssnake on 05/06/2017.
 */

public interface BaseRecycleViewView<M> {

    void showCards(List<M> receiptModelList);

    void showEmpty();
}
