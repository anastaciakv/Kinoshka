package de.proximity.kinoshka.movielist;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.proximity.kinoshka.R;
import de.proximity.kinoshka.entity.Movie;
import de.proximity.kinoshka.moviedetails.MovieDetailsActivity;

import static dagger.internal.Preconditions.checkNotNull;

public class MovieListActivity extends AppCompatActivity implements MovieListContract.View {

    MovieListContract.Presenter presenter;
    MovieGridAdapter adapter;
    @BindView(R.id.rvMovieList)
    RecyclerView rvMovieList;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);
//        DaggerMovieListComponent.builder()
//                .movieListModule(new MovieListModule(this))
//                .appComponent(MyApplication.getAppComponent())
//                .build().inject(this);
        adapter = new MovieGridAdapter(new MovieGridAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(int clickedItemIndex, View v) {
                presenter.onMovieClicked(clickedItemIndex, v);
            }
        });
        initGrid();
    }

    private void initGrid() {
        final GridLayoutManager layoutManager = new GridLayoutManager(this, getNumberOfColumns());
        rvMovieList.setLayoutManager(layoutManager);
        rvMovieList.setHasFixedSize(true);
        rvMovieList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();

                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                presenter.onListScrolled(visibleItemCount, totalItemCount, firstVisibleItemPosition);
            }
        });

        rvMovieList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sort_by_most_popular:
                presenter.onSortByMostPopular();
                item.setChecked(true);
                return true;
            case R.id.menu_sort_by_top_rated:
                presenter.onSortByTopRated();
                item.setChecked(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private int getNumberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    @Override
    public void setPresenter(MovieListContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void showProgress(boolean show) {
        progress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showEmptyView(boolean show) {
        tvEmpty.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showList(boolean show) {
        rvMovieList.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void updateMovieList(List<Movie> movies) {
        adapter.update(movies);
    }

    @Override
    public void navigateToMovieDetails(Movie movie, View v) {
        Intent intent = new Intent(MovieListActivity.this, MovieDetailsActivity.class);

        Bundle extras = new Bundle();
        extras.putParcelable(Movie.ITEM_KEY, Parcels.wrap(movie));
        intent.putExtras(extras);

        Bundle bundle = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            bundle = ActivityOptions.makeSceneTransitionAnimation(this, v, v.getTransitionName()).toBundle();
        }
        startActivity(intent, bundle);
    }

    @Override
    public void clearMovieList() {
        adapter.clearMovieList();
    }
}
