import androidx.activity.viewModels
import com.yourorg.aidwarehouse.viewmodel.ProductViewModel
import com.yourorg.aidwarehouse.ui.MainScreen

class MainActivity : ComponentActivity() {
    private val viewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }
}
