package com.chuckbot.heromvi.View

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.chuckbot.heromvi.Model.MainView
import com.chuckbot.heromvi.R
import com.chuckbot.heromvi.Utils.DataSource
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding2.view.RxView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.time.OffsetDateTime
import java.util.*

class MainActivity : MviActivity<MainView,MainPresenter>() ,MainView {

    internal lateinit var imageList:List<String>

    override fun createPresenter(): MainPresenter {
        return MainPresenter(DataSource(imageList))
    }

    override val imageIntent: Observable<Int>
        get() = RxView.clicks(btn_get_data as View)
            .map {
                _ -> getRandomNumberInRange(0, imageList.size-1)
            }

    private fun getRandomNumberInRange(min: Int, max: Int): Int? {
        if (min > max)
            throw IllegalArgumentException("Max must be greater than Min")
        val r = Random()
        return r.nextInt(max-min+1)+min
    }

    override fun render(viewState: MainViewState) {
        if (viewState.isLoading){
            progress_bar.visibility = View.VISIBLE
            image_view.visibility = View.GONE
            btn_get_data.isEnabled = false
        } else if (viewState.error != null) {
            progress_bar.visibility = View.GONE
            image_view.visibility = View.GONE
            btn_get_data.isEnabled = true
            Toast.makeText(this@MainActivity,""+viewState.error!!.message,Toast.LENGTH_LONG).show()
        } else if (viewState.isImageViewShow) {

            btn_get_data.isEnabled = true

            Picasso.get().load(viewState.imageLink)
                .fetch(object:Callback{
                    override fun onSuccess() {
                        image_view.alpha = 0f
                        Picasso.get().load(viewState.imageLink).into(image_view)
                        image_view.animate().setDuration(300).alpha(1f).start()

                        progress_bar.visibility = View.GONE
                        image_view.visibility = View.VISIBLE
                    }

                    override fun onError(e: Exception?) {
                        progress_bar.visibility = View.GONE
                    }

                })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageList = createImageList()
    }

    private fun createImageList(): List<String> {
        return Arrays.asList("https://2.bp.blogspot.com/-MGYP9ocbJoM/Vp3MK_Bog0I/AAAAAAAAFEs/wDLEAwrX_Oc/s400/Marvel-Heroes-2015-Free-Download.jpg",
            "https://vignette1.wikia.nocookie.net/marvelheroes/images/a/a0/NormalCostumePreview_Rare_Thor.jpg/revision/latest?cb=20141114200737",
            "https://www.newsstand.co.uk/i2550710/Zoom/MARVEL-HEROES_NO-115.jpg",
            "https://vignette3.wikia.nocookie.net/marvel-war-of-heroes/images/5/51/ScientificAdventurerGiantMan5.jpg/revision/latest?cb=20130917052025")
    }
}
