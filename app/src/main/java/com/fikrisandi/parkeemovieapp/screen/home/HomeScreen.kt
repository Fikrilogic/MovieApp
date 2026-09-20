package com.fikrisandi.parkeemovieapp.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.fikrisandi.parkeemovieapp.BuildConfig
import com.fikrisandi.parkeemovieapp.domain.model.Movie
import com.fikrisandi.parkeemovieapp.ui.component.ShimmerCardItem
import com.fikrisandi.parkeemovieapp.ui.component.ShimmerImageItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    val listRowPopularMovie = rememberLazyListState()
    val listRowTopRatedMovie = rememberLazyListState()
    val listRowNowPlayingMovie = rememberLazyListState()

    val isLoadMoreMoviePopular = remember {
        derivedStateOf {
            val lastVisibleItemIndex =
                listRowPopularMovie.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItemsCount = listRowPopularMovie.layoutInfo.totalItemsCount
            lastVisibleItemIndex >= totalItemsCount - 5 && totalItemsCount > 0 && !uiState.loadingPopularMovie && uiState.moviesPopular.movies.isNotEmpty() && uiState.errorPopularMovie == null
        }
    }

    val isLoadMoreMovieTopRated = remember {
        derivedStateOf {
            val lastVisibleItemIndex =
                listRowTopRatedMovie.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItemsCount = listRowTopRatedMovie.layoutInfo.totalItemsCount
            lastVisibleItemIndex >= totalItemsCount - 5 && totalItemsCount > 0 && !uiState.loadingTopRatedMovie && uiState.moviesTopRated.movies.isNotEmpty() && uiState.errorTopRatedMovie == null
        }
    }
    val isLoadMoreMovieNowPlaying = remember {
        derivedStateOf {
            val lastVisibleItemIndex =
                listRowNowPlayingMovie.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItemsCount = listRowNowPlayingMovie.layoutInfo.totalItemsCount
            lastVisibleItemIndex >= totalItemsCount - 5 && totalItemsCount > 0 && !uiState.loadingNowPlayingMovie && uiState.moviesNowPlaying.movies.isNotEmpty() && uiState.errorNowPlayingMovie == null
        }
    }


    LaunchedEffect(
        isLoadMoreMoviePopular.value,
        isLoadMoreMovieTopRated.value,
        isLoadMoreMovieNowPlaying.value
    ) {
        if (isLoadMoreMoviePopular.value) {
            viewModel.loadMoviesPopular()
        }

        if (isLoadMoreMovieTopRated.value) {
            viewModel.loadMoviesTopRated()
        }

        if (isLoadMoreMovieNowPlaying.value) {
            viewModel.loadMoviesNowPlaying()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadMoviesPopular()
        viewModel.loadMoviesTopRated()
        viewModel.loadMoviesNowPlaying()
    }

    HomeContent(
        uiState,
        listRowPopularMovie,
        listRowTopRatedMovie,
        listRowNowPlayingMovie,
        onMovieClicked = { movie ->
            navController.navigate("movie_detail/${movie.id}")
        },
        onActionToolbar = {
            navController.navigate("favorite")
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    uiState: HomeUiState,
    listMoviePopularRowState: LazyListState,
    listRowTopRatedMovie: LazyListState,
    listRowNowPlayingMovie: LazyListState,
    onMovieClicked: (Movie) -> Unit = {},
    onActionToolbar: () -> Unit = {}
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = { onActionToolbar() }) {
                        Icon(Icons.Default.Favorite, contentDescription = "Favorite")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                ) {
                    Text(
                        "Popular Movie",
                        modifier = Modifier.padding(horizontal = 20.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    when {
                        !uiState.loadingPopularMovie && uiState.moviesPopular.movies.isEmpty() -> {
                            EmptyMovie(modifier = Modifier)
                        }

                        else -> {
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                state = listMoviePopularRowState
                            ) {
                                when {
                                    uiState.loadingPopularMovie && uiState.moviesPopular.movies.isEmpty() -> {
                                        items(3) {
                                            ShimmerImageItem()
                                        }
                                    }

                                    !uiState.loadingPopularMovie && uiState.moviesPopular.movies.isEmpty() -> {
                                        item {
                                            Box(
                                                modifier = Modifier
                                                    .fillParentMaxWidth()
                                                    .height(250.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "No popular movies found.",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.outline
                                                )
                                            }
                                        }
                                    }

                                    else -> {
                                        items(uiState.moviesPopular.movies.size) { item ->
                                            val movie = uiState.moviesPopular.movies[item]
                                            MovieImageItem(
                                                movie = movie,
                                                onClick = {
                                                    onMovieClicked(movie)
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                ) {
                    Text(
                        "Top Rated",
                        modifier = Modifier.padding(horizontal = 20.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    when {
                        !uiState.loadingTopRatedMovie && uiState.moviesTopRated.movies.isEmpty() -> {
                            EmptyMovie(modifier = Modifier)
                        }

                        else -> {
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                state = listRowTopRatedMovie
                            ) {

                                when {
                                    uiState.loadingTopRatedMovie && uiState.moviesTopRated.movies.isEmpty() -> {
                                        items(4) {
                                            ShimmerCardItem()
                                        }
                                    }

                                    !uiState.loadingTopRatedMovie && uiState.moviesTopRated.movies.isEmpty() -> {
                                        item {
                                            Box(
                                                modifier = Modifier
                                                    .fillParentMaxWidth()
                                                    .height(200.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "No top rated movies found.",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.outline
                                                )
                                            }
                                        }
                                    }

                                    else -> {
                                        items(uiState.moviesTopRated.movies.size) { item ->
                                            val movie = uiState.moviesTopRated.movies[item]
                                            MovieItem(
                                                movie = movie,
                                                onClick = {
                                                    onMovieClicked(movie)
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                ) {
                    Text(
                        "Now Playing",
                        modifier = Modifier.padding(horizontal = 20.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    when {
                        !uiState.loadingNowPlayingMovie && uiState.moviesNowPlaying.movies.isEmpty() -> {
                            EmptyMovie(modifier = Modifier)
                        }

                        else -> {
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                state = listRowNowPlayingMovie
                            ) {

                                when {
                                    uiState.loadingNowPlayingMovie && uiState.moviesNowPlaying.movies.isEmpty() -> {
                                        items(4) {
                                            ShimmerCardItem()
                                        }
                                    }

                                    !uiState.loadingNowPlayingMovie && uiState.moviesNowPlaying.movies.isEmpty() -> {
                                        item {
                                            Box(
                                                modifier = Modifier
                                                    .fillParentMaxWidth()
                                                    .height(200.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "No movies playing now found.",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.outline
                                                )
                                            }
                                        }
                                    }

                                    else -> {
                                        items(uiState.moviesNowPlaying.movies.size) { item ->
                                            val movie = uiState.moviesNowPlaying.movies[item]
                                            MovieItem(
                                                movie = movie,
                                                onClick = {
                                                    onMovieClicked(movie)
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun MovieItem(
    movie: Movie,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val widthScreen = LocalWindowInfo.current.containerDpSize.width * 0.4f

    Card(
        modifier = Modifier
            .width(widthScreen)
            .fillMaxHeight(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.7f)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data("${BuildConfig.IMAGE_URL}${movie.posterPath}")
                        .size(Size.ORIGINAL)
                        .crossfade(true).build(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            }

            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "★ ${movie.rating}",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFFFFB300)
                )
            }
        }
    }
}

@Composable
fun MovieImageItem(
    movie: Movie,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val widthScreen = LocalWindowInfo.current.containerDpSize.width * 0.8f

    Card(
        modifier = Modifier
            .width(widthScreen)
            .height(250.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data("${BuildConfig.IMAGE_URL}${movie.posterPath}")
                        .size(Size.ORIGINAL)
                        .crossfade(true).build(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }
}

@Composable
fun EmptyMovie(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No movies available.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
