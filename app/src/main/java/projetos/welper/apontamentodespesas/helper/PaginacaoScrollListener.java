package projetos.welper.apontamentodespesas.helper;

import android.widget.AbsListView;

/**
 * Created by welper on 27/06/2015.
 */
public class PaginacaoScrollListener implements AbsListView.OnScrollListener {

    private int visibleThreshold = 3;
    private int currentPage = 0;
    private int currentTotalItems = 0;
    private int firstItemPageIndex = 0;
    private boolean loading = false;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount < currentTotalItems) {
            this.currentPage = this.firstItemPageIndex;
            this.currentTotalItems = totalItemCount;
            if (totalItemCount == 0) { this.loading = true; }
        }

        if (loading && (totalItemCount > currentTotalItems)) {
            loading = false;
            currentTotalItems = totalItemCount;
            currentPage++;
        }

        if (!loading && (totalItemCount - visibleItemCount)<=(firstVisibleItem + visibleThreshold)) {
           // loadMoreListener.onLoadMore(currentPage + 1, totalItemCount);
            loading = true;
        }
    }
}
