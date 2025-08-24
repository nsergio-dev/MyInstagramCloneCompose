package com.nsergio.dev.myinstagramcompose.features.create_post.ui

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.nsergio.dev.myinstagramcompose.features.create_post.presentation.CreatePostState
import com.nsergio.dev.myinstagramcompose.features.create_post.presentation.CreatePostViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    onBack: () -> Unit,
    viewModel: CreatePostViewModel = hiltViewModel()
) {
    val ctx = LocalContext.current
    val state by viewModel.state.collectAsState()

    var pendingUri by remember { mutableStateOf<Uri?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            viewModel.setPreview(pendingUri)
        } else {
            pendingUri?.let { uri -> runCatching { ctx.contentResolver.delete(uri, null, null) } }
            pendingUri = null
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) launchCamera(ctx, takePictureLauncher) { uri -> pendingUri = uri }
    }

    Scaffold(
        // 👇 fondo general negro
        containerColor = Color.Black,
        // 👇 no apliques insets automáticos: así el contenido va "debajo" del topBar
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(), // separa de la status bar
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,          // 👈 fondo negro sólido sobre la imagen
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                title = { Text("Nueva publicación") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (state.previewUri != null) {
                        IconButton(
                            onClick = {
                                val ok = viewModel.publish()
                                if (ok) onBack()
                            }
                        ) {
                            Icon(Icons.Filled.Check, contentDescription = "Publicar")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(bottom = 56.dp),
                onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }
            ) {
                Icon(Icons.Filled.CameraAlt, contentDescription = "Tomar foto")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        BodyCreatePost(
            state = state,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding()),
            onRetake = {
                viewModel.clearPreview()
                permissionLauncher.launch(Manifest.permission.CAMERA)
            },
            onDiscard = {
                state.previewUri?.let { uri ->
                    runCatching {
                        ctx.contentResolver.delete(
                            uri,
                            null,
                            null
                        )
                    }
                }
                viewModel.clearPreview()
            }
        )
    }
}

@Composable
private fun BodyCreatePost(
    state: CreatePostState,
    modifier: Modifier = Modifier,
    onRetake: () -> Unit,
    onDiscard: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        if (state.previewUri == null) {
            Text(
                text = "Toca la cámara para tomar una foto",
                color = Color.White
            )
        } else {
            val painter = rememberAsyncImagePainter(model = state.previewUri)
            Image(
                painter = painter,
                contentDescription = "Preview",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(onClick = onDiscard) {
                    Icon(Icons.Filled.Delete, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Descartar")
                }
                Button(onClick = onRetake) {
                    Icon(Icons.Filled.CameraAlt, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Reintentar")
                }
            }
        }
    }
}


private fun launchCamera(
    context: Context,
    takePictureLauncher: androidx.activity.result.ActivityResultLauncher<Uri>,
    onUriReady: (Uri) -> Unit
) {
    val time = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val imagesDir = File(context.cacheDir, "images").apply { mkdirs() }
    val file = File(imagesDir, "IMG_$time.jpg")
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    onUriReady(uri)
    takePictureLauncher.launch(uri)
}