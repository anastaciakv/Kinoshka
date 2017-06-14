package de.proximity.kinoshka.movielist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.proximity.kinoshka.MyApplication;
import de.proximity.kinoshka.R;
import de.proximity.kinoshka.entity.Movie;
import de.proximity.kinoshka.moviedetails.ScrollingActivity;

import static dagger.internal.Preconditions.checkNotNull;

public class MovieListActivity extends AppCompatActivity implements MovieListContract.View {
    @Inject
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
        DaggerMovieListComponent.builder()
                .movieListModule(new MovieListModule(this))
                .appComponent(MyApplication.getAppComponent())
                .build().inject(this);
        adapter = new MovieGridAdapter(new MovieGridAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(int clickedItemIndex) {
                presenter.onMovieClicked(clickedItemIndex);
            }
        });
        initGrid();
    }

    private void initGrid() {
        final GridLayoutManager layoutManager = new GridLayoutManager(this, getNumberOfColumns());

        rvMovieList.setLayoutManager(layoutManager);
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

    private int getNumberOfColumns() {
        //todo put it to res and adopt for tablets
        return 2;
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
    public void navigateToMovieDetails(Movie movie) {
//        Intent intent = new Intent(MovieListActivity.this, MovieDetailsActivity.class);
        Intent intent = new Intent(MovieListActivity.this, ScrollingActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(Movie.ITEM_KEY, movie);
        intent.putExtras(extras);
        startActivity(intent);
    }
}
