package ru.andrewkir.hse_mooc.flows.courses.search.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import ru.andrewkir.hse_mooc.R
import ru.andrewkir.hse_mooc.network.responses.CoursesSearch.Course
import java.text.DecimalFormat
import java.util.*

class SearchCoursesRecyclerAdapter(
    private val context: Context,
    private val onCourseClick: ((Course) -> Unit)? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var isLoadingState = false
    var loadingOffset = 0

    companion object {
        const val COURSE_VIEW = 0
        const val LOADING_VIEW = 1
    }

    var data: List<Course> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            COURSE_VIEW -> CourseViewHolder(
                LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.recycler_search_course_item, viewGroup, false)
            )
            else -> LoadingViewHolder(
                LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.recycler_search_loading_item, viewGroup, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoadingState) {
            if (position == data.size) LOADING_VIEW else COURSE_VIEW
        } else {
            COURSE_VIEW
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder.itemViewType) {
            COURSE_VIEW -> {
                (viewHolder as CourseViewHolder).courseTitle?.text = data[position].courseName

                viewHolder.courseAuthor?.text = data[position].author.name

                viewHolder.courseDescription?.text =
                    if (data[position].shortDescription.isNotBlank()) data[position].shortDescription else data[position].description

                if (data[position].price.amount != 0.0) {
                    val currency = Currency.getInstance(data[position].price.currency)
                    val symbol: String = currency.getSymbol(Locale("ru", "RU"))
                    val format = DecimalFormat("0.#")
                    viewHolder.courseCost?.text =
                        format.format(data[position].price.amount) + " $symbol"
                } else {
                    viewHolder.courseCost?.text = "Бесплатно"
                }
                Glide.with(context)
                    .load(data[position].previewImageLink)
                    //.apply(RequestOptions().override(88.px, 88.px))
                    //.transform(CenterCrop(), RoundedCorners(25)) TODO убрать
                    .transform(CenterCrop(), GranularRoundedCorners(25f, 25f, 0f, 0f))
                    .into(viewHolder.courseImage!!)

                viewHolder.cardView?.setOnClickListener { onCourseClick?.invoke(data[position]) }
            }
            LOADING_VIEW -> {

            }
        }
    }

    fun addLoading() {
        isLoadingState = true
        loadingOffset = 1
        notifyItemInserted(data.size)
    }

    fun removeLoading() {
        if (isLoadingState) {
            isLoadingState = false
            loadingOffset = 0
            notifyItemRemoved(data.size)
        }
        loadingOffset = 0
    }

    inner class CourseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var courseTitle: TextView? = null
        var courseDescription: TextView? = null
        var courseAuthor: TextView? = null

        var courseRank: TextView? = null
        var courseCost: TextView? = null

        var courseImage: ImageView? = null

        var cardView: CardView? = null

        init {
            courseTitle = view.findViewById(R.id.rawCourseSearchTitle)
            courseDescription = view.findViewById(R.id.rawCourseSearchDescription)
            courseAuthor = view.findViewById(R.id.rawCourseAuthorName)

            courseRank = view.findViewById(R.id.rawCourseSearchRating)
            courseCost = view.findViewById(R.id.rawCourseSearchCost)

            courseImage = view.findViewById(R.id.rawCourseSearchImageView)

            cardView = view.findViewById(R.id.cardViewCoursesSearch)
        }
    }

    inner class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var progressBar: ProgressBar? = null

        init {
            progressBar = view.findViewById(R.id.searchLoadingItem)
        }
    }

    override fun getItemCount() = data.size + loadingOffset
}