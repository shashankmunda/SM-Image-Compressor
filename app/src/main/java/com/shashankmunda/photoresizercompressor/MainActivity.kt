package com.shashankmunda.photoresizercompressor

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil3.compose.AsyncImage
import com.shashankmunda.photoresizercompressor.ui.theme.PhotoResizerCompressorTheme
import com.shashankmunda.photoresizercompressor.utils.Utils
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhotoResizerCompressorTheme {
                Scaffold(modifier = Modifier.fillMaxSize().safeDrawingPadding()) { innerPadding ->
                    MyApp()
                }
            }
        }
    }
}

@Serializable
object Home

@Serializable
data class CompressPhoto(val uri: String)


@Composable
fun MyApp(){
    val navController = rememberNavController()
    NavHost(navController, startDestination = Home){
        composable<Home>{
            HomePage(
                onNavigateToCompressPhoto = {
                    navController.navigate(route = CompressPhoto(it))
                }
            )
        }
        composable<CompressPhoto> { backstackEntry ->
            val compressPhoto : CompressPhoto = backstackEntry.toRoute()
            CompressPhotoPage(
                compressPhoto = compressPhoto
            )
        }
    }

}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun CompressPhotoPage(
    compressPhoto: CompressPhoto
){
    val context = LocalContext.current
    val imageDimensions = Utils.getImageDimensions(context, Uri.parse(compressPhoto.uri))
    val imageSize = Utils.getFileSize(context, Uri.parse(compressPhoto.uri))
    val fileFormat = Utils.getFileMimeType(context, Uri.parse(compressPhoto.uri))
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        AsyncImage(
            model = Uri.parse(compressPhoto.uri),
            contentDescription = null
        )
        Column {
            Text(text = "Size")
            Text(text = Utils.format(imageSize.toDouble(), 2), fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.padding(4.dp))
            Text(text = "Resolution")
            Text(text = "${imageDimensions.second}x${imageDimensions.first}", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.padding(4.dp))
            Text(text = "Format")
            Text(text = fileFormat, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun HomePage(onNavigateToCompressPhoto: (String) -> Unit){
    Row(modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center){
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            ){
            ActionButton(text = "Compress Photo", onClick = onNavigateToCompressPhoto)
            ActionButton(text = "Resize Photo", onClick = onNavigateToCompressPhoto)
        }
    }

}

@Composable
private fun ActionButton(text: String, onClick: (String) -> Unit) {
    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            onClick(uri.toString())
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }
    Button(
        onClick = {
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        },
        colors = ButtonColors(
            containerColor = Color.Black,
            contentColor = Color.White,
            disabledContainerColor = Color.Black,
            disabledContentColor = Color.White,
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
        )
    }
}

// @Preview(showBackground = true)
// @Composable
// fun GreetingPreview() {
//     HomePage()
// }